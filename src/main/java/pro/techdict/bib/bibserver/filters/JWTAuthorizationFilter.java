package pro.techdict.bib.bibserver.filters;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pro.techdict.bib.bibserver.configs.JWTProperties;
import pro.techdict.bib.bibserver.utils.JWTUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
    JWTProperties jwtProperties;

    final String TOKEN_PREFIX = "Bearer ";

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTProperties jwtProperties) {
        super(authenticationManager);
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain
    ) throws IOException, ServletException {

        String tokenBearer = request.getHeader(jwtProperties.getRequestHeader());
        // 如果请求头中没有 Authorization 信息则直接放行了
        if (tokenBearer == null || !tokenBearer.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        // 如果请求头中有 token，则进行解析，并且设置认证信息
        String token = tokenBearer.replace(TOKEN_PREFIX, "");
        if (!JWTUtils.isTokenExpired(token, jwtProperties) && JWTUtils.parserToken(token, jwtProperties) != null) {
            Claims claims = JWTUtils.parserToken(token, jwtProperties);
            SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(
                    Objects.requireNonNull(claims).getSubject(),
                    null,
                    Collections.singleton(
                        new SimpleGrantedAuthority(claims.get("role", String.class))
                    )
                )
            );
            super.doFilterInternal(request, response, chain);
        }
    }
}
