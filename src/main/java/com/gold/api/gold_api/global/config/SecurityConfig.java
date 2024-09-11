package com.gold.api.gold_api.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gold.api.gold_api.global.error.ErrorCode;
import com.gold.api.gold_api.global.error.ErrorResponse;
import com.gold.api.gold_api.global.exception.JwtAuthenticationException;
import com.gold.api.gold_api.grpc.JwtServiceCaller;
import com.gold.api.gold_api.security.filter.JwtFilter;
import com.gold.api.gold_api.security.filter.LoginFilter;
import com.gold.api.gold_api.security.jwt.JwtUtill;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtill jwtUtill;
    private final JwtServiceCaller jwtServiceCaller;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    new AntPathRequestMatcher("/join"),
                    new AntPathRequestMatcher("/login"),
                    new AntPathRequestMatcher("/reissue"),
                    new AntPathRequestMatcher("/v3/api-docs/**"),
                    new AntPathRequestMatcher("/swagger-ui/**"),
                    new AntPathRequestMatcher("/reissue-token"),
                    new AntPathRequestMatcher("/error")
                ).permitAll()
                .anyRequest().authenticated()
            )
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS)
            )
            .formLogin(AbstractHttpConfigurer::disable)
            .logout(AbstractHttpConfigurer::disable)
            .exceptionHandling(exceptionHandling -> exceptionHandling
                .authenticationEntryPoint(authenticationEntryPoint)
                .accessDeniedHandler(accessDeniedHandler))
            .addFilterBefore(new JwtFilter(jwtUtill), UsernamePasswordAuthenticationFilter.class)
            .addFilterAt(new LoginFilter(objectMapper, authenticationManager(authenticationConfiguration),jwtServiceCaller), UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception {
        return configuration.getAuthenticationManager();
    }

    private final AuthenticationEntryPoint authenticationEntryPoint =
        (request, response, authException) -> {
            log.error("인증 실패: {}", authException.getMessage());
            log.error("Authentication failed: {}", authException.getClass().getSimpleName());
            log.error("Request URI: {}", request.getRequestURI());
            log.error("Error Message: {}", authException.getMessage());
            ErrorCode errorCode = ErrorCode.AUTHENTICATION_FAILED;
            if (authException instanceof JwtAuthenticationException) {
                errorCode = ((JwtAuthenticationException) authException).getErrorCode();
            }
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, errorCode);
        };

    private final AccessDeniedHandler accessDeniedHandler =
        (request, response, accessDeniedException) ->{
            log.error("Access Denied: {}", accessDeniedException.getMessage());
            sendErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, ErrorCode.ACCESS_DENIED);
        };

    private static void sendErrorResponse(HttpServletResponse response, int status, ErrorCode errorCode) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(new ObjectMapper().writeValueAsString(new ErrorResponse(errorCode, errorCode.getMessage())));
    }
}
