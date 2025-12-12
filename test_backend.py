#!/usr/bin/env python3
import requests
import json
import sys
from datetime import datetime

BASE_URL = "http://localhost:8080"
ADMIN_EMAIL = "admin@spaza.com"
ADMIN_PASSWORD = "admin123"

def log(msg, status="INFO"):
    print(f"[{datetime.now().strftime('%H:%M:%S')}] {status}: {msg}")

def test_health():
    """Test all services health"""
    services = {
        "api-gateway": "http://localhost:8080/actuator/health",
        "catalog": "http://localhost:8081/actuator/health",
        "customer": "http://localhost:8085/actuator/health",
        "order": "http://localhost:8087/actuator/health",
        "user-auth": "http://localhost:8082/actuator/health",
    }
    
    for name, url in services.items():
        try:
            r = requests.get(url, timeout=5)
            if r.status_code == 200:
                log(f"{name} UP", "PASS")
            else:
                log(f"{name} DOWN (status {r.status_code})", "FAIL")
                return False
        except Exception as e:
            log(f"{name} UNREACHABLE: {e}", "FAIL")
            return False
    return True

def test_admin_login():
    """Test admin login"""
    url = f"{BASE_URL}/api/v1/private/login"
    payload = {"email": ADMIN_EMAIL, "password": ADMIN_PASSWORD}
    
    try:
        r = requests.post(url, json=payload, timeout=5)
        if r.status_code == 200:
            token = r.json().get("token")
            if token:
                log("Admin login successful", "PASS")
                return token
            else:
                log("No token in response", "FAIL")
                return None
        else:
            log(f"Login failed: {r.status_code} - {r.text}", "FAIL")
            return None
    except Exception as e:
        log(f"Login error: {e}", "FAIL")
        return None

def test_products(token):
    """Test product listing"""
    url = f"{BASE_URL}/api/v1/products"
    headers = {"Authorization": f"Bearer {token}"}
    
    try:
        r = requests.get(url, headers=headers, timeout=5)
        if r.status_code == 200:
            log(f"Products retrieved: {len(r.json())} items", "PASS")
            return True
        else:
            log(f"Products failed: {r.status_code}", "FAIL")
            return False
    except Exception as e:
        log(f"Products error: {e}", "FAIL")
        return False

def test_customer_register():
    """Test customer registration"""
    url = f"{BASE_URL}/api/v1/customer/register"
    payload = {
        "email": f"test{int(datetime.now().timestamp())}@test.com",
        "password": "Test123!",
        "firstName": "Test",
        "lastName": "User"
    }
    
    try:
        r = requests.post(url, json=payload, timeout=5)
        if r.status_code in [200, 201]:
            log("Customer registration successful", "PASS")
            return payload["email"]
        else:
            log(f"Registration failed: {r.status_code} - {r.text}", "FAIL")
            return None
    except Exception as e:
        log(f"Registration error: {e}", "FAIL")
        return None

def test_customer_login(email, password):
    """Test customer login"""
    url = f"{BASE_URL}/api/v1/customer/login"
    payload = {"email": email, "password": password}
    
    try:
        r = requests.post(url, json=payload, timeout=5)
        if r.status_code == 200:
            token = r.json().get("token")
            if token:
                log("Customer login successful", "PASS")
                return token
            else:
                log("No token in response", "FAIL")
                return None
        else:
            log(f"Customer login failed: {r.status_code}", "FAIL")
            return None
    except Exception as e:
        log(f"Customer login error: {e}", "FAIL")
        return None

def test_cart(customer_token):
    """Test cart operations"""
    url = f"{BASE_URL}/api/v1/customer/cart"
    headers = {"Authorization": f"Bearer {customer_token}"}
    
    try:
        r = requests.get(url, headers=headers, timeout=5)
        if r.status_code == 200:
            log("Cart retrieved", "PASS")
            return True
        else:
            log(f"Cart failed: {r.status_code}", "FAIL")
            return False
    except Exception as e:
        log(f"Cart error: {e}", "FAIL")
        return False

def test_orders(token):
    """Test order listing"""
    url = f"{BASE_URL}/api/v1/auth/orders"
    headers = {"Authorization": f"Bearer {token}"}
    
    try:
        r = requests.get(url, headers=headers, timeout=5)
        if r.status_code == 200:
            log(f"Orders retrieved: {len(r.json())} items", "PASS")
            return True
        else:
            log(f"Orders failed: {r.status_code}", "FAIL")
            return False
    except Exception as e:
        log(f"Orders error: {e}", "FAIL")
        return False

def test_jwt_validation(token):
    """Test JWT validation with invalid token"""
    url = f"{BASE_URL}/api/v1/auth/orders"
    headers = {"Authorization": "Bearer invalid_token"}
    
    try:
        r = requests.get(url, headers=headers, timeout=5)
        if r.status_code == 401:
            log("JWT validation working (rejected invalid token)", "PASS")
            return True
        else:
            log(f"JWT validation failed: {r.status_code}", "FAIL")
            return False
    except Exception as e:
        log(f"JWT validation error: {e}", "FAIL")
        return False

def main():
    log("Starting backend tests...", "INFO")
    print()
    
    results = []
    
    # Health checks
    log("=== HEALTH CHECKS ===", "INFO")
    results.append(("Health Checks", test_health()))
    print()
    
    # Admin login
    log("=== ADMIN LOGIN ===", "INFO")
    admin_token = test_admin_login()
    results.append(("Admin Login", admin_token is not None))
    print()
    
    if admin_token:
        # Products
        log("=== PRODUCTS ===", "INFO")
        results.append(("Products", test_products(admin_token)))
        print()
        
        # Orders
        log("=== ORDERS ===", "INFO")
        results.append(("Orders", test_orders(admin_token)))
        print()
    
    # Customer registration
    log("=== CUSTOMER REGISTRATION ===", "INFO")
    customer_email = test_customer_register()
    results.append(("Customer Registration", customer_email is not None))
    print()
    
    if customer_email:
        # Customer login
        log("=== CUSTOMER LOGIN ===", "INFO")
        customer_token = test_customer_login(customer_email, "Test123!")
        results.append(("Customer Login", customer_token is not None))
        print()
        
        if customer_token:
            # Cart
            log("=== CART ===", "INFO")
            results.append(("Cart", test_cart(customer_token)))
            print()
    
    # JWT validation
    log("=== JWT VALIDATION ===", "INFO")
    results.append(("JWT Validation", test_jwt_validation(admin_token)))
    print()
    
    # Summary
    log("=== TEST SUMMARY ===", "INFO")
    passed = sum(1 for _, result in results if result)
    total = len(results)
    
    for test_name, result in results:
        status = "PASS" if result else "FAIL"
        print(f"  {test_name}: {status}")
    
    print()
    log(f"Results: {passed}/{total} passed", "PASS" if passed == total else "FAIL")
    
    return 0 if passed == total else 1

if __name__ == "__main__":
    sys.exit(main())
