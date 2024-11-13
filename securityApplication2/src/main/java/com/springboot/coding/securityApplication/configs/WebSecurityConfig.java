package com.springboot.coding.securityApplication.configs;

import com.springboot.coding.securityApplication.advice.CustomAccessDeniedHandler;
import com.springboot.coding.securityApplication.entities.enums.Role;
import com.springboot.coding.securityApplication.filter.JwtAuthFilter;
import com.springboot.coding.securityApplication.filter.LoggingFilter;
import com.springboot.coding.securityApplication.handlers.Oauth2SuccessHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
@EnableMethodSecurity
@RequiredArgsConstructor
@Slf4j
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final LoggingFilter loggingFilter;
    private final CorsConfigurationSource corsConfigurationSource;
    private final Oauth2SuccessHandler oauth2SuccessHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;


    private static final String[] publicRoutes = {
            "/auth/**",
            "/error",
            "/home.html"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF as we are using JWT
                /*  .cors(Customizer.withDefaults()) // Enable CORS with the defined CorsConfigurationSource
                  .cors(corsConfig -> {
                      corsConfig.configurationSource(corsConfigurationSource);
                      log.info("CorsConfigurationSource bean injected");
                  })*/
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless sessions with JWT
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicRoutes)
                        .permitAll()
                        .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())  // Restrict all `/admin/**` routes to users with ADMIN role
                        .requestMatchers("/users/**").hasAnyRole(Role.USER.name(), Role.ADMIN.name(), Role.CREATOR.name())
                        .requestMatchers(HttpMethod.GET, "/posts/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/posts/**").hasAnyRole(Role.ADMIN.name(), Role.CREATOR.name())
                        .anyRequest().authenticated())

                // no need to explicitly add the authentication provider as we create a bean in AppConfig
//                .authenticationProvider(authenticationProvider)
                .addFilterBefore(loggingFilter, UsernamePasswordAuthenticationFilter.class) // Add logging filter before JWT filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter before authentication filter
                .oauth2Login(oauth2Config -> oauth2Config
                        .failureUrl("/login?error=true") // login page on failure
                        .successHandler(oauth2SuccessHandler))  // Enable OAuth2 login
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .accessDeniedHandler(customAccessDeniedHandler)
                );
        return httpSecurity.build();
    }
}
