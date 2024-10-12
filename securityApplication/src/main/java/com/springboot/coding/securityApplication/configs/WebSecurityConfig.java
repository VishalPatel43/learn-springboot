package com.springboot.coding.securityApplication.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity // Enable Spring Security and configure security filter chain
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    //    private final AuthenticationProvider authenticationProvider;
    private final PasswordEncoder passwordEncoder;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
//                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF as we are using JWT
                .cors(AbstractHttpConfigurer::disable) // Disable CORS at security level, managed in WebConfig

//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless sessions with JWT
//                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/posts").permitAll()
                        .requestMatchers("/posts/**").hasAnyRole("ADMIN")
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults()) // default login page

        /*
            We are not using this login form as we are using JWT if front-end is not in this project
            so basically we are stateless, and we are not using session
            When we are using the front-end in this project, we can use this form login form
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .formLogin(formLogin -> formLogin.loginPage("/newLogin.html").permitAll())
        */

//                .authenticationProvider(authenticationProvider)
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter before authentication filter
        ;
        return httpSecurity.build();
    }

    @Bean
    UserDetailsService myInMemoryUserDetailsManager(PasswordEncoder passwordEncoder) {
        UserDetails normalUser = User
                .withUsername("user")
//                .password("{bcrypt}$2a$10$sTH.vPQFLXD1CS7NGdR6W.LBBFy7fbu5nCXXb/.FOIH0mgJ3mq5YG")
                .password(passwordEncoder.encode("1234"))
                .roles("USER")
                .build();

        UserDetails adminUser = User
                .withUsername("ADMIN")
//                .password("{bcrypt}$2a$10$0beBvHgkgZ2lt1S9tTqAZ.Vta.iuth22t3hKPwrb7GYagocPDJbKu")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN")
                .build();


        return new InMemoryUserDetailsManager(normalUser, adminUser);
    }
}
