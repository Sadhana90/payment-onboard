package com.paymentonboard.config;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.paymentonboard.entity.JwtTokenInfo;
import com.paymentonboard.helper.JwtTokenUtil;
import com.paymentonboard.repository.JwtTokenInfoRepository;
import com.paymentonboard.service.CustomUserDetailsService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    JwtTokenInfoRepository jwtTokenInfoRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String jwt = parseJwt(request);
        JwtTokenInfo jwtTokenInfo = jwtTokenInfoRepository.findByTokenAndIsActive(jwt, Boolean.TRUE).orElse(null);
        try {
            if (jwt != null && jwtTokenInfo != null) {
                String username = jwtTokenUtil.getUsernameFromToken(jwt);
                UserDetails userDetails = customUserDetailsService.loadUserByUsernameCustom(username);
                if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, "", userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (ExpiredJwtException jwtException) {
            jwtTokenInfo.setIsActive(Boolean.FALSE);
            jwtTokenInfoRepository.save(jwtTokenInfo);
            logger.error("JWT token expired");
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e);
        }

        chain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }

}
