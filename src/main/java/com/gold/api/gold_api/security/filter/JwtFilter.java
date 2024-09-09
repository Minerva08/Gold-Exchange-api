package com.gold.api.gold_api.security.filter;

import com.gold.api.gold_api.global.exception.JwtAuthenticationException;
import com.gold.api.gold_api.security.custom.CustomUserDetails;
import com.gold.api.gold_api.security.jwt.JwtUtill;
import com.gold.api.gold_api.user.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtill jwtUtil;

    private final String[] PERMIT_URL_ARRAY = {
        "/api/v3/api-docs/**", "/api/swagger-ui/**", "/api/v3/api-docs", "/api/swagger-ui.html",
        "/api/error", "/api/join", "/api/login", "/api/reissue"
    };

    public JwtFilter(JwtUtill jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    private static final AntPathMatcher pathMatcher = new AntPathMatcher();

    public boolean shouldNotFilter(HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        for (String pattern : PERMIT_URL_ARRAY) {
            if (pathMatcher.match(pattern, requestURI)) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        try {

            //request에서 Authorization 헤더를 찾음
            String authorization= request.getHeader("Authorization");

            //Authorization 헤더 검증
            if (authorization == null || !authorization.startsWith("Bearer ")) {

                System.out.println("token null");
                filterChain.doFilter(request, response);

                //조건이 해당되면 메소드 종료 (필수)
                return;
            }

            System.out.println("authorization now");
            //Bearer 부분 제거 후 순수 토큰만 획득
            String token = authorization.split(" ")[1];

            //토큰 소멸 시간 검증
            if (jwtUtil.isExpired(token)) {

                filterChain.doFilter(request, response);

                //조건이 해당되면 메소드 종료 (필수)
                return;
            }

            //토큰에서 userId
            String userId = jwtUtil.getUserId(token);

            //userEntity를 생성하여 값 set
            User user = User.builder()
                .userId(userId)
                .password("temp")
                .build();

            //UserDetails에 회원 정보 객체 담기
            CustomUserDetails customUserDetails = new CustomUserDetails(user);

            //스프링 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            //세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);

        } catch (JwtAuthenticationException e) {
            log.info("JWT 인증 실패: {}", e.getMessage());
            handleException(request, response, e, filterChain);
        } catch (Exception e) {
            log.info("기타 인증 오류 발생: {}", e.getMessage());
            handleException(request, response, new JwtAuthenticationException("기타 인증 에러 발생", e), filterChain);
        }


    }


    private void handleException(HttpServletRequest request, HttpServletResponse response, JwtAuthenticationException e, FilterChain filterChain) throws IOException, ServletException {
        request.setAttribute("exception", e);
        filterChain.doFilter(request, response);
    }
}
