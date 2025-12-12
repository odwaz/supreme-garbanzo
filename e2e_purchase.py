import json
import os
import time
import hmac
import hashlib
import random
import string
from typing import Tuple, Dict, Any

import requests


BASE_URL = os.getenv("GATEWAY_URL", "http://localhost:8080")


def _post(path: str, body: Dict[str, Any], headers: Dict[str, str] = None) -> requests.Response:
    url = BASE_URL + path
    h = {"Content-Type": "application/json"}
    if headers:
        h.update(headers)
    return requests.post(url, headers=h, data=json.dumps(body), timeout=20)


def _get(path: str, headers: Dict[str, str] = None) -> requests.Response:
    url = BASE_URL + path
    return requests.get(url, headers=headers or {}, timeout=20)


def _bearer(token: str) -> Dict[str, str]:
    return {"Authorization": f"Bearer {token}"}


def register_and_login_customer() -> Tuple[str, str]:
    # Returns (email, jwt)
    email = f"e2e_{int(time.time())}_{random.randint(1000,9999)}@test.com"
    password = "Test123!"
    # Some projects expose register under customer-service public routes; keeping generic shape
    reg_paths = [
        "/api/v1/customer/register",  # customer-service (per gateway routes)
        "/api/v1/auth/register",      # fallback used by some modules
    ]
    payload = {
        "email": email,
        "password": password,
        "firstName": "E2E",
        "lastName": "User"
    }
    registered = False
    for p in reg_paths:
        r = _post(p, payload)
        if r.status_code in (200, 201, 202):
            registered = True
            break
    if not registered:
        # If already exists or endpoint differs, continue to login
        pass

    # Login
    login_paths = [
        "/api/v1/customer/login",   # per gateway routes (public)
        "/api/v1/auth/login",       # sometimes used by auth service
        "/api/v1/private/login",    # user-auth-service public login entry
    ]
    jwt = None
    for p in login_paths:
        r = _post(p, {"username": email, "password": password})
        if r.status_code == 200:
            try:
                data = r.json()
                # common fields: token, jwt, accessToken
                jwt = data.get("token") or data.get("jwt") or data.get("accessToken")
                if not jwt and isinstance(data, dict):
                    # sometimes nested
                    for k, v in data.items():
                        if isinstance(v, str) and len(v.split(".")) == 3:
                            jwt = v
                            break
            except Exception:
                pass
        if jwt:
            break
    if not jwt:
        raise RuntimeError(f"Login failed for {email}; last status: {r.status_code} body: {r.text[:200]}")

    return email, jwt


def pick_store() -> Dict[str, Any]:
    # Try to list stores; fall back to default if endpoint differs
    candidates = ["/api/v1/store", "/api/v1/store/list", "/api/v1/store/all"]
    for p in candidates:
        r = _get(p)
        if r.status_code == 200:
            try:
                data = r.json()
            except Exception:
                continue
            # Accept array or envelope
            if isinstance(data, list) and data:
                return data[0]
            if isinstance(data, dict):
                # look for common keys
                for k in ("content", "items", "data", "stores"):
                    if k in data and isinstance(data[k], list) and data[k]:
                        return data[k][0]
    # Fallback minimal default
    return {"code": "DEFAULT"}


def pick_product(store: Dict[str, Any]) -> Dict[str, Any]:
    # Query public products and choose first
    qs = ""
    # If store id/code known, try pass it
    if "id" in store:
        qs = f"?merchantId={store['id']}"
    elif "code" in store:
        qs = f"?store={store['code']}"
    r = _get(f"/api/v1/products{qs}")
    if r.status_code != 200:
        # try v2
        r = _get(f"/api/v2/products{qs}")
    r.raise_for_status()
    data = r.json()
    if isinstance(data, list) and data:
        return data[0]
    if isinstance(data, dict):
        for k in ("content", "items", "data", "products"):
            if k in data and isinstance(data[k], list) and data[k]:
                return data[k][0]
    raise RuntimeError("No products available to purchase")


def add_to_cart(product: Dict[str, Any], jwt: str) -> str:
    # Common shapes: { product: <id> or sku, quantity }
    product_id = product.get("id") or product.get("productId") or product.get("sku")
    body = {"product": product_id, "quantity": 1}
    r = _post("/api/v1/cart", body, headers=_bearer(jwt))
    if r.status_code not in (200, 201):
        # try without auth (gateway marks cart public)
        r = _post("/api/v1/cart", body)
    r.raise_for_status()
    try:
        data = r.json()
    except Exception:
        data = {}
    cart_code = (
        data.get("code")
        or data.get("cartCode")
        or data.get("id")
        or ''.join(random.choices(string.ascii_uppercase + string.digits, k=8))
    )
    return cart_code


def checkout(cart_code: str, jwt: str, product: Dict[str, Any]) -> str:
    price = product.get("price", 99.99)
    body = {
        "total": price,
        "paymentMethod": "COD",
        "shippingAddress": "123 Test St",
        "billingAddress": "123 Test St",
        "shippingCity": "Test City",
        "shippingPostalCode": "12345",
        "shippingCountry": "US",
        "billingCity": "Test City",
        "billingPostalCode": "12345",
        "billingCountry": "US",
        "phone": "1234567890",
        "email": "test@test.com"
    }
    r = _post(f"/api/v1/auth/cart/{cart_code}/checkout", body, headers=_bearer(jwt))
    r.raise_for_status()
    try:
        data = r.json()
        # Attempt common fields
        oid = data.get("orderId") or data.get("id") or data.get("code")
        if not oid and isinstance(data, dict):
            # find any string value that looks like an id
            for v in data.values():
                if isinstance(v, (str, int)):
                    oid = str(v)
                    break
    except Exception:
        # Fallback: extract digits
        text = r.text
        digits = ''.join([c for c in text if c.isdigit()])
        oid = digits or text.strip()[:36]
    if not oid:
        raise RuntimeError(f"Could not determine orderId from checkout response: {r.text[:200]}")
    return str(oid)


def stripe_signature(secret: str, payload: str, timestamp: int) -> str:
    signed_payload = f"{timestamp}.{payload}"
    mac = hmac.new(secret.encode("utf-8"), signed_payload.encode("utf-8"), hashlib.sha256).hexdigest()
    return f"t={timestamp},v1={mac}"


def simulate_payment_success(order_id: str) -> requests.Response:
    secret = os.getenv("STRIPE_WEBHOOK_SECRET")
    if not secret:
        raise RuntimeError("STRIPE_WEBHOOK_SECRET env var is required to simulate Stripe webhook")
    # Minimal Stripe event with PaymentIntent carrying metadata.order_id
    event = {
        "id": f"evt_{int(time.time())}",
        "type": "payment_intent.succeeded",
        "data": {
            "object": {
                "object": "payment_intent",
                "id": f"pi_{int(time.time())}",
                "metadata": {"order_id": str(order_id)}
            }
        }
    }
    payload = json.dumps(event, separators=(",", ":"))
    ts = int(time.time())
    sig = stripe_signature(secret, payload, ts)
    url = BASE_URL + "/api/v1/payment/stripe/webhook"
    headers = {"Stripe-Signature": sig, "Content-Type": "application/json"}
    r = requests.post(url, headers=headers, data=payload, timeout=20)
    return r


def verify_order_captured(order_id: str, jwt: str) -> bool:
    # Try explicit order fetch; fallback to listing
    paths = [f"/api/v1/auth/orders/{order_id}", "/api/v1/auth/orders"]
    for p in paths:
        r = _get(p, headers=_bearer(jwt))
        if r.status_code != 200:
            continue
        try:
            data = r.json()
        except Exception:
            text = r.text
            if "CAPTURED" in text or "PAID" in text or "COMPLETED" in text:
                return True
            continue
        # Accept object or array/envelope
        def has_captured(d: Any) -> bool:
            if isinstance(d, dict):
                status = (d.get("status") or d.get("paymentStatus") or d.get("orderStatus") or "").upper()
                return any(s in status for s in ("CAPTURED", "PAID", "COMPLETED"))
            return False
        if isinstance(data, dict):
            if has_captured(data):
                return True
            for k in ("content", "items", "data", "orders"):
                if k in data and isinstance(data[k], list):
                    if any(has_captured(x) for x in data[k]):
                        return True
        if isinstance(data, list) and any(has_captured(x) for x in data):
            return True
    return False


def run() -> Dict[str, Any]:
    results = {"ok": False}
    email, jwt = register_and_login_customer()
    store = pick_store()
    product = pick_product(store)
    cart_code = add_to_cart(product, jwt)
    order_id = checkout(cart_code, jwt, product)

    webhook_resp = simulate_payment_success(order_id)
    if webhook_resp.status_code != 200:
        results["error"] = f"Webhook failed: {webhook_resp.status_code} {webhook_resp.text[:200]}"
        return results

    time.sleep(2)  # Allow webhook processing
    captured = verify_order_captured(order_id, jwt)
    
    results["ok"] = captured
    results["email"] = email
    results["order_id"] = order_id
    results["product"] = product.get("name") or product.get("sku")
    results["status"] = "CAPTURED" if captured else "PENDING"
    return results


if __name__ == "__main__":
    print("Starting E2E purchase test...")
    result = run()
    print(json.dumps(result, indent=2))
    if result["ok"]:
        print("\n✅ E2E TEST PASSED")
        exit(0)
    else:
        print("\n❌ E2E TEST FAILED")
        exit(1)
