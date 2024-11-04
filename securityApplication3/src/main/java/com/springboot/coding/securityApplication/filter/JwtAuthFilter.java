package com.springboot.coding.securityApplication.filter;

import com.springboot.coding.securityApplication.entities.User;
import com.springboot.coding.securityApplication.services.JWTService;
import com.springboot.coding.securityApplication.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserService userService;

    private HandlerExceptionResolver handlerExceptionResolver;


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
            final Long userId = jwtService.getUserIdFromToken(token);

//            final String username = jwtService.extractUsername(token);

            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                User user1 = (User) userDetailsService.loadUserByUsername(userId.toString());
                User user = userService.getUserById(userId);
//                User user2 = userService.getUserByEmail(username);
                if (jwtService.validateToken(token, user)) {
                    log.info("Authenticated user: {}", user.getUsername());
                    log.info("Authorities: {}", user.getAuthorities());

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    user, // principal
                                    null,
                                    user.getAuthorities()
                            );
                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
                filterChain.doFilter(request, response);
            }
        } catch (Exception ex) {
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}
