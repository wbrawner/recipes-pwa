package com.wbrawner.recipes.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class SinglePageApplicationFilter implements WebFilter {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final List<String> staticRoutes = List.of("/", "/login", "/register");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var path = exchange.getRequest().getURI().getPath();
        if (staticRoutes.contains(path) || path.startsWith("/recipes")) {
            return chain.filter(exchange.mutate().request(exchange.getRequest().mutate().path("/index.html").build()).build());
        }
        return chain.filter(exchange);
    }
}
