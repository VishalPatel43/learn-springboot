package com.springboot.coding.securityApplication.handlers;

import com.springboot.coding.securityApplication.auth.CustomUserDetails;
import com.springboot.coding.securityApplication.entities.User;
import com.springboot.coding.securityApplication.entities.enums.Role;
import com.springboot.coding.securityApplication.services.JWTService;
import com.springboot.coding.securityApplication.services.UserService;
import com.springboot.coding.securityApplication.utils.PasswordUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class Oauth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserService userService;
    private final JWTService jwtService;
    private final PasswordUtil passwordUtil;
    private final PasswordEncoder passwordEncoder;

    @Value("${deploy.env}")
    private String deployEnv;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        DefaultOAuth2User oAuth2User = (DefaultOAuth2User) token.getPrincipal();


        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");
//        String name = oAuth2User.getAttribute("given_name");
        log.info("OAuth2User: {}", oAuth2User);
        log.info("Email: {}", email);
        log.info("Name: {}", name);

        User user = userService.getUserByEmail(email);

        if (user == null) {
            User newUser = User.builder()
                    .name(name)
                    .email(email)
//                    .password(passwordEncoder.encode(passwordUtil.generateRandomPassword()))
                    .roles(Set.of(Role.USER))
                    .build();
            user = userService.save(newUser);
        }

        log.info("User: {}", user);
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        log.info("CustomUserDetails: {}", customUserDetails);

        String accessToken = jwtService.generateAccessToken(customUserDetails);
        String refreshToken = jwtService.generateRefreshToken(customUserDetails);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure("production".equals(deployEnv));
        response.addCookie(cookie);

//        response.sendRedirect("http://localhost:3000");
        String frontEndUrl = "http://localhost:8080/api/home.html?token=" + accessToken;
//        response.sendRedirect("http://localhost:3000?accessToken=" + accessToken + "&refreshToken=" + refreshToken);
        log.info("FrontEndUrl: {}", frontEndUrl);
        response.sendRedirect(frontEndUrl);
//        getRedirectStrategy().sendRedirect(request, response, frontEndUrl);
        log.info("Return from sendRedirect");
    }
}
