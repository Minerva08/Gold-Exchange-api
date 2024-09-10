package com.gold.api.gold_api.security.filter;

import com.gold.api.gold_api.global.error.ErrorCode;
import com.gold.api.gold_api.global.exception.JwtAuthenticationException;
import com.gold.api.gold_api.security.custom.CustomUserDetails;
import com.gold.api.gold_api.security.jwt.JwtUtill;
import com.gold.api.gold_api.user.entity.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtill jwtUtil;

    public JwtFilter(JwtUtill jwtUtil) {

        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        try {

            //request에서 Authorization 헤더를 찾음
            String authorization= request.getHeader("Authorization");

            //Authorization 헤더 검증
            if (authorization == null || !authorization.startsWith("Bearer ")) {

                log.error("[{}] token null",Thread.currentThread().getStackTrace()[1].getMethodName());
                filterChain.doFilter(request, response);

                //조건이 해당되면 메소드 종료 (필수)
                return;
            }

            log.info("[{}] Start to split authorization now",Thread.currentThread().getStackTrace()[1].getMethodName());
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
            log.error("JWT 인증 실패: {}", e.getMessage());
            handleException(request, response, e, filterChain);
        }catch (ExpiredJwtException e){
            log.info("[{}] Token Expired: {}",Thread.currentThread().getStackTrace()[1].getMethodName(),e.getMessage());
            handleException(request, response, new JwtAuthenticationException(ErrorCode.TOKEN_EXPIRED, e), filterChain);
        }catch (JwtException e ){
            log.error("기타 인증 오류 발생: {}", e.getMessage());
            handleException(request, response, new JwtAuthenticationException("기타 인증 에러 발생", e), filterChain);
        }
        catch (Exception e) {
            log.error("[{}] Internal ServerError : {}",Thread.currentThread().getStackTrace()[1].getMethodName(),e.getMessage());
            handleException(request, response, new JwtAuthenticationException("기타 서버 내부 에러 발생", e), filterChain);
        }

    }


    private void handleException(HttpServletRequest request, HttpServletResponse response, JwtAuthenticationException e, FilterChain filterChain) throws IOException, ServletException {
        request.setAttribute("exception", e);
        filterChain.doFilter(request, response);
    }
}
