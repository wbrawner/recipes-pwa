package com.wbrawner.recipes.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.session.config.annotation.web.server.EnableSpringWebSession;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@EnableWebFluxSecurity
@EnableWebFlux
@Configuration
public class Config implements WebFluxConfigurer {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final SecurityContextRepository securityContextRepository;

    @Autowired
    public Config(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, SecurityContextRepository securityContextRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.securityContextRepository = securityContextRepository;
    }

    @Bean
    RouterFunction<ServerResponse> staticResourceRouter() {
        logger.info("created staticResourceRouter");
        return RouterFunctions.resources("/**", new ClassPathResource("static/"));
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.authorizeExchange()
                .pathMatchers("/api/users/login", "/api/users/register")
                .permitAll()
                .pathMatchers("/api/**")
                .authenticated()
                .anyExchange()
                .permitAll()
                .and()
                .csrf()
                .disable()
                .authenticationManager(authenticationManager)
                .securityContextRepository(securityContextRepository)
                .build();
    }
}
