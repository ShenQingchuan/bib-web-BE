package pro.techdict.bib.bibserver.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import pro.techdict.bib.bibserver.configs.JWTProperties;

import java.util.Date;
import java.util.Objects;

public class JWTUtils {
    // 生成token
    public static String makeJsonWebToken(JWTUserDetails userDetails, boolean isRememberMe, JWTProperties jwtProperties) {
        Date now = new Date(System.currentTimeMillis());
        Date expires = new Date(now.getTime() + (isRememberMe ? jwtProperties.getRemember() : jwtProperties.getExpires()));
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret())
                .setSubject(userDetails.getUsername())
                .claim("uid", userDetails.getUid())
                .claim("role", userDetails.getRole())
                .claim("avatarURL", userDetails.getAvatarURL())
                .setExpiration(expires)
                .setIssuer(jwtProperties.getIssuer()) // 发行主体
                .setIssuedAt(now)// 获取当前时间为发行日期
                .compact();
    }

    // 解析 token
    public static Claims parserToken(String token, JWTProperties jwtProperties) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtProperties.getSecret())
                .parseClaimsJws(token)
                .getBody();
        if (!claims.isEmpty() && claims.getIssuer().equals(jwtProperties.getIssuer())) {
            return claims;
        }
        return null;
    }

    // 从 token 中获取用户 id
    public static String getUidFromToken(String token, JWTProperties jwtProperties){
        return Objects.requireNonNull(parserToken(token, jwtProperties)).get("uid", String.class);
    }
    // 从 token 中获取用户名
    public static String getUserNameFromToken(String token, JWTProperties jwtProperties){
        return Objects.requireNonNull(parserToken(token, jwtProperties)).getSubject();
    }

    // 验证 token 是否已过期
    public static boolean isTokenExpired(String token, JWTProperties jwtProperties){
        return Objects.requireNonNull(parserToken(token, jwtProperties)).getExpiration().before(new Date());
    }
}