import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class E2ETest {
    private static final String GATEWAY = "http://localhost:8080";
    private static String storeId;
    private static String storeCode;
    private static String manufacturerId;
    private static String categoryId;
    private static String productId;
    private static String taxRateId;
    private static String customerToken;
    private static String adminToken;
    private static String cartCode;
    private static String orderId;

    public static void main(String[] args) throws Exception {
        System.out.println("=== Comprehensive E2E Test via API Gateway ===\n");
        
        System.out.println("1. Admin login...");
        String adminLoginJson = "{\"username\":\"admin@spaza.com\",\"password\":\"admin123\"}";
        String adminLoginResp = post(GATEWAY + "/api/auth/login", adminLoginJson);
        adminToken = extractToken(adminLoginResp);
        System.out.println("✓ Admin Token: " + adminToken.substring(0, 20) + "...\n");
        
        System.out.println("2. Creating store...");
        storeCode = "STORE" + System.currentTimeMillis();
        String storeJson = "{\"code\":\"" + storeCode + "\",\"name\":\"Test Store\",\"email\":\"test@store.com\",\"phone\":\"+27123456789\",\"address\":{\"address\":\"123 Test St\",\"city\":\"Johannesburg\",\"stateProvince\":\"Gauteng\",\"postalCode\":\"2000\",\"country\":\"ZA\"}}";
        String storeResp = postWithAuth(GATEWAY + "/api/v1/private/stores", storeJson, adminToken);
        storeId = extractId(storeResp);
        System.out.println("✓ Store ID: " + storeId + " Code: " + storeCode + "\n");
        
        System.out.println("3. Creating manufacturer...");
        String mfgJson = "{\"merchantId\":" + storeId + ",\"code\":\"DELL\",\"visible\":true,\"featured\":true,\"status\":true,\"sortOrder\":1}";
        String mfgResp = postWithAuth(GATEWAY + "/api/v1/private/manufacturers", mfgJson, adminToken);
        manufacturerId = extractId(mfgResp);
        System.out.println("✓ Manufacturer ID: " + manufacturerId + "\n");
        
        System.out.println("4. Creating category...");
        String catJson = "{\"merchantId\":" + storeId + ",\"code\":\"ELECTRONICS\",\"visible\":true,\"featured\":true,\"status\":true,\"sortOrder\":1}";
        String catResp = postWithAuth(GATEWAY + "/api/v1/private/category", catJson, adminToken);
        categoryId = catResp.trim();
        System.out.println("✓ Category ID: " + categoryId + "\n");
        
        System.out.println("5. Creating product...");
        String prodJson = "{\"merchantId\":" + storeId + ",\"categoryId\":" + categoryId + ",\"sku\":\"LAPTOP-001\",\"name\":\"Test Laptop\",\"description\":\"High performance\",\"price\":15999.99,\"quantity\":50,\"available\":true}";
        String prodResp = postWithAuth(GATEWAY + "/api/v1/private/product", prodJson, adminToken);
        productId = prodResp.trim();
        System.out.println("✓ Product ID: " + productId + "\n");
        
        System.out.println("6. Updating product...");
        String updateJson = "{\"merchantId\":" + storeId + ",\"categoryId\":" + categoryId + ",\"sku\":\"LAPTOP-001\",\"name\":\"Updated Laptop\",\"description\":\"Ultra performance\",\"price\":17999.99,\"quantity\":45,\"available\":true}";
        putWithAuth(GATEWAY + "/api/v1/private/product/" + productId, updateJson, adminToken);
        System.out.println("✓ Product updated\n");
        
        System.out.println("7. Listing products...");
        String products = get(GATEWAY + "/api/v1/products?merchantId=" + storeId);
        System.out.println("✓ Products: " + products.substring(0, Math.min(100, products.length())) + "...\n");
        
        System.out.println("8. Getting product by ID...");
        String product = get(GATEWAY + "/api/v1/product/" + productId);
        System.out.println("✓ Product: " + product.substring(0, Math.min(100, product.length())) + "...\n");
        
        System.out.println("9. Listing manufacturers...");
        String manufacturers = getWithAuth(GATEWAY + "/api/v1/private/manufacturers?merchantId=" + storeId, adminToken);
        System.out.println("✓ Manufacturers: " + manufacturers.substring(0, Math.min(100, manufacturers.length())) + "...\n");
        
        System.out.println("10. Customer registration...");
        String email = "customer" + System.currentTimeMillis() + "@test.com";
        String custJson = "{\"email\":\"" + email + "\",\"password\":\"Test123!\",\"firstName\":\"Test\",\"lastName\":\"Customer\",\"phone\":\"+27987654321\",\"address\":\"456 St\",\"city\":\"Cape Town\",\"postalCode\":\"8001\",\"country\":\"ZA\",\"billingAddress\":\"456 St\",\"deliveryAddress\":\"456 St\"}";
        post(GATEWAY + "/api/v1/auth/register", custJson);
        System.out.println("✓ Customer registered\n");
        
        System.out.println("11. Customer login...");
        String loginJson = "{\"username\":\"" + email + "\",\"password\":\"Test123!\"}";
        String loginResp = post(GATEWAY + "/api/v1/auth/login", loginJson);
        customerToken = extractToken(loginResp);
        System.out.println("✓ Token: " + customerToken.substring(0, 20) + "...\n");
        
        System.out.println("12. Getting public store info...");
        String storeInfo = get(GATEWAY + "/api/v1/store/" + storeCode);
        System.out.println("✓ Store: " + storeInfo.substring(0, Math.min(100, storeInfo.length())) + "...\n");
        
        System.out.println("13. Adding to cart...");
        String cartJson = "{\"product\":" + productId + ",\"quantity\":2}";
        String cartResp = postWithAuth(GATEWAY + "/api/v1/cart", cartJson, customerToken);
        cartCode = extractCartCode(cartResp);
        System.out.println("✓ Cart Code: " + cartCode + "\n");
        
        System.out.println("14. Getting cart...");
        String cart = getWithAuth(GATEWAY + "/api/v1/cart/" + cartCode, customerToken);
        System.out.println("✓ Cart: " + cart.substring(0, Math.min(100, cart.length())) + "...\n");
        
        System.out.println("15. Checkout...");
        String orderResp = postWithAuth(GATEWAY + "/api/v1/auth/cart/" + cartCode + "/checkout", "{}", customerToken);
        System.out.println("DEBUG: Checkout response: " + orderResp);
        orderId = extractOrderId(orderResp);
        System.out.println("✓ Order ID: " + orderId + "\n");
        
        System.out.println("16. Getting customer orders...");
        String orders = getWithAuth(GATEWAY + "/api/v1/auth/orders", customerToken);
        System.out.println("✓ Orders: " + orders.substring(0, Math.min(100, orders.length())) + "...\n");
        
        System.out.println("17. Getting order by ID...");
        String order = getWithAuth(GATEWAY + "/api/v1/auth/orders/" + orderId, customerToken);
        System.out.println("✓ Order: " + order.substring(0, Math.min(100, order.length())) + "...\n");
        
        System.out.println("18. Admin listing all orders...");
        String allOrders = getWithAuth(GATEWAY + "/api/v1/private/orders?merchantId=" + storeId, adminToken);
        System.out.println("✓ All Orders: " + allOrders.substring(0, Math.min(100, allOrders.length())) + "...\n");
        
        System.out.println("19. Admin updating order status...");
        putWithAuth(GATEWAY + "/api/v1/private/orders/" + orderId + "/status/PROCESSING", "", adminToken);
        System.out.println("✓ Order status updated to PROCESSING\n");
        
        System.out.println("20. Admin getting order by ID...");
        String adminOrder = getWithAuth(GATEWAY + "/api/v1/private/orders/" + orderId, adminToken);
        System.out.println("✓ Admin Order: " + adminOrder.substring(0, Math.min(100, adminOrder.length())) + "...\n");
        
        System.out.println("21. Admin listing customers...");
        String customers = getWithAuth(GATEWAY + "/api/v1/private/customers", adminToken);
        System.out.println("✓ Customers: " + customers.substring(0, Math.min(100, customers.length())) + "...\n");
        
        System.out.println("=== ✓ ALL 21 TESTS PASSED ===\n");
        System.out.println("Summary:");
        System.out.println("- Store: " + storeId);
        System.out.println("- Manufacturer: " + manufacturerId);
        System.out.println("- Category: " + categoryId);
        System.out.println("- Product: " + productId);
        System.out.println("- Customer: " + email);
        System.out.println("- Order: " + orderId);
    }

    private static String get(String url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        return readResponse(conn);
    }

    private static String getWithAuth(String url, String token) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        return readResponse(conn);
    }

    private static String post(String url, String json) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }
        return readResponse(conn);
    }

    private static String postWithAuth(String url, String json, String token) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }
        return readResponse(conn);
    }

    private static String put(String url, String json) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }
        return readResponse(conn);
    }

    private static String putWithAuth(String url, String json, String token) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer " + token);
        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }
        return readResponse(conn);
    }

    private static String readResponse(HttpURLConnection conn) throws Exception {
        int code = conn.getResponseCode();
        if (code >= 400) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                String line;
                while ((line = br.readLine()) != null) System.err.println(line);
            }
            throw new RuntimeException("HTTP " + code);
        }
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
        }
        return sb.toString();
    }

    private static String extractId(String json) {
        int idx = json.indexOf("\"id\":");
        if (idx == -1) return json;
        int start = idx + 5;
        int end = start;
        while (end < json.length() && Character.isDigit(json.charAt(end))) end++;
        return json.substring(start, end);
    }

    private static String extractToken(String json) {
        int idx = json.indexOf("\"token\":\"");
        if (idx == -1) return "";
        int start = idx + 9;
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    private static String extractCartCode(String json) {
        int idx = json.indexOf("\"code\":\"");
        if (idx == -1) return "";
        int start = idx + 8;
        int end = json.indexOf("\"", start);
        return json.substring(start, end);
    }

    private static String extractOrderId(String json) {
        // Handle both regular quotes and HTML entities
        int idx = json.indexOf("orderId");
        if (idx == -1) return "";
        // Find the colon after orderId
        int colonIdx = json.indexOf(":", idx);
        if (colonIdx == -1) return "";
        int start = colonIdx + 1;
        // Skip any non-digit characters
        while (start < json.length() && !Character.isDigit(json.charAt(start))) start++;
        int end = start;
        while (end < json.length() && Character.isDigit(json.charAt(end))) end++;
        return json.substring(start, end);
    }
}
