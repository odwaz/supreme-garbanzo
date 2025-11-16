package za.blkmarket.gateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        
        // Require auth for /api/v1/auth/** (except login/register) and /api/v1/private/** (except login)
        boolean isLoginOrRegister = path.contains("/login") || path.contains("/register");
        boolean requiresAuth = (path.contains("/api/v1/auth/") && !isLoginOrRegister) 
                            || (path.contains("/api/v1/private/") && !isLoginOrRegister);
        
        if (requiresAuth) {
            String auth = exchange.getRequest().getHeaders().getFirst("Authorization");
            
            if (auth == null || !auth.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        }
        
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
