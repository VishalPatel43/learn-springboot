package com.springboot.coding.securityApplication.filter;

import com.springboot.coding.securityApplication.services.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final String CLASS_NAME = "JwtAuthFilter";
    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    //    @Autowired
//    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;
/*
    --> This is the constructor that is used to inject the dependencies of the class.
    @Autowired
    public JwtAuthFilter(
            JWTUtil jwtUtil,
            UserDetailsService userDetailsService,
            @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }
    */

    // Setter for HandlerExceptionResolver
    @Autowired
    @Qualifier("handlerExceptionResolver")
    public void setHandlerExceptionResolver(HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) {

        try {
            final String requestTokenHeader = request.getHeader("Authorization");

            if (requestTokenHeader == null || !requestTokenHeader.startsWith("Bearer")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String token = requestTokenHeader.split("Bearer ")[1];
            final String username = jwtUtil.extractUsername(token);

            // If username is extracted and SecurityContext is not yet set
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (jwtUtil.validateToken(token, username, userDetails)) {
                    // Log user details for debugging
                    System.out.println("Authenticated user: " + userDetails.getUsername());
                    System.out.println("Authorities: " + userDetails.getAuthorities());

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, // principal
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
                logRequest(request);
                filterChain.doFilter(request, response);
                logResponse(response);
            }
        } catch (Exception ex) {
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }

    private void logRequest(HttpServletRequest request) {
        log.info("Incoming Request: {} {} from {} inside {}", request.getMethod(), request.getRequestURI(), request.getRemoteAddr(),CLASS_NAME);
    }

    private void logResponse(HttpServletResponse response) {
        log.info("Outgoing Response: status={} inside {}", response.getStatus(),CLASS_NAME );
    }
}
