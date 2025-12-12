package za.blkmarket.gateway;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {
    
    private static final int JWT_FILTER_ORDER = -100;
    private static final Pattern LOGIN_PATTERN = Pattern.compile(".*/login$");
    private static final Pattern REGISTER_PATTERN = Pattern.compile(".*/register$");
    private static final Pattern PASSWORD_RESET_PATTERN = Pattern.compile(".*/password/reset/.*");
    
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        
        boolean isPublicEndpoint = LOGIN_PATTERN.matcher(path).matches() || 
                                   REGISTER_PATTERN.matcher(path).matches() ||
                                   path.startsWith("/api/v1/products") ||
                                   PASSWORD_RESET_PATTERN.matcher(path).matches();
        
        boolean requiresAuth = (path.startsWith("/api/v1/auth/") || 
                               path.startsWith("/api/v1/private/")) && 
                               !isPublicEndpoint;
        
        if (requiresAuth) {
            String auth = exchange.getRequest().getHeaders().getFirst("Authorization");
            
            if (auth == null || !auth.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            
            String token = auth.substring(7);
            if (!validateToken(token)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }
        
        return chain.filter(exchange);
    }
    
    private boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int getOrder() {
        return JWT_FILTER_ORDER;
    }
}
