package com.springboot.coding.securityApplication.configs;

import com.springboot.coding.securityApplication.advice.CustomAccessDeniedHandler;
import com.springboot.coding.securityApplication.advice.CustomAuthenticationEntryPoint;
import com.springboot.coding.securityApplication.filter.JwtAuthFilter;
import com.springboot.coding.securityApplication.filter.LoggingFilter;
import com.springboot.coding.securityApplication.handlers.Oauth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;


@Configuration
@EnableWebSecurity // Enable Spring Security and configure security filter chain
@EnableMethodSecurity(securedEnabled = true) // Enable method level security (now @Secure annotation can be used)
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final LoggingFilter loggingFilter;
    private final CorsConfigurationSource corsConfigurationSource;
    private final Oauth2SuccessHandler oauth2SuccessHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private static final String[] publicRoutes = {
            "/auth/**",
            "/error",
            "/home.html"
//            "users/**",
//            "/posts/**"
    };

    private static final String[] authenticateRoutes = {
            "/users/**",
            "/posts/**",
            "/admin/**"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF as we are using JWT
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless sessions with JWT
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicRoutes).permitAll()
                        // Require only authentication
//                        .requestMatchers(authenticateRoutes).authenticated()
                        .anyRequest().authenticated())

                .addFilterBefore(loggingFilter, UsernamePasswordAuthenticationFilter.class) // Add logging filter before JWT filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter before authentication filter
                .oauth2Login(oauth2Config -> oauth2Config
                        .failureUrl("/login?error=true") // login page on failure
                        .successHandler(oauth2SuccessHandler)
                )  // Enable OAuth2 login

                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(customAccessDeniedHandler)
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                );

        return httpSecurity.build();
    }
}
