package com.springboot.coding.securityApplication.configs;

import com.springboot.coding.securityApplication.filter.JwtAuthFilter;
import com.springboot.coding.securityApplication.filter.LoggingFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity // Enable Spring Security and configure security filter chain
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final LoggingFilter loggingFilter;


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF as we are using JWT
                .cors(Customizer.withDefaults()) // Enable CORS with the defined CorsConfigurationSource
//                .cors(corsConfig -> corsConfig.configurationSource(corsConfigurationSource()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless sessions with JWT
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/posts", "/error", "/public/**")
                        .permitAll()
//                        .requestMatchers("/user/profile").hasAnyRole("USER", "ADMIN") // Adjust this as necessary
                        .anyRequest().authenticated())
//                .formLogin(Customizer.withDefaults()) // default login page

        /*
            We are not using this login form as we are using JWT if front-end is not in this project
            so basically we are stateless, and we are not using session
            When we are using the front-end in this project, we can use this form login form
                .formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
                .formLogin(formLogin -> formLogin.loginPage("/newLogin.html").permitAll())
        */

        // no need to explicitly add the authentication provider as we create a bean in AppConfig
//                .authenticationProvider(authenticationProvider)
                .addFilterBefore(loggingFilter, UsernamePasswordAuthenticationFilter.class) // Add logging filter before JWT filter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter before authentication filter
        return httpSecurity.build();
    }

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().requestMatchers("/auth/**");  // Optional: Ignore security for these paths
//    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:4200"
        ));
//        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowedHeaders(List.of(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT
        ));
//        config.setAllowedMethods(Collections.singletonList("*")); // allow all methods => GET, POST, PUT, DELETE
        config.setAllowedMethods(List.of(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name()
        ));
//        config.setExposedHeaders(List.of("Authorization"));
        config.setExposedHeaders(List.of(HttpHeaders.AUTHORIZATION));
        config.setMaxAge(3600L); // 1 hour

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    /*
        @Bean
        UserDetailsService myInMemoryUserDetailsManager() {
            UserDetails normalUser = User
                    .withUsername("user")
                    .password(passwordEncoder.encode("1234"))
                    .roles("USER")
                    .build();

            UserDetails adminUser = User
                    .withUsername("ADMIN")
                    .password(passwordEncoder.encode("admin"))
                    .roles("ADMIN")
                    .build();


            return new InMemoryUserDetailsManager(normalUser, adminUser);
        }
    */
}
