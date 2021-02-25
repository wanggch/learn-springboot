package jenny.learn.springboot.security.util;

import com.google.common.collect.Maps;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class TokenUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(TokenUtils.class);

    /**
     * 签名密钥
     */
    private static final String SECRET = "mySecret";
    /**
     * 签发者
     */
    private static final String ISSUER = "jenny";
    /**
     * 过期时间：5分钟
     */
    private static Long expiration = new Long(300000);

    /**
     * 生成 Token
     */
    public static String generateToken(String username) {
        return generateToken(username, Maps.newHashMap());
    }

    /**
     * 生成 Token
     */
    public static String generateToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
                // 受众
                .setAudience(username)
                // 面向的用户
                .setSubject(username)
                // 签发者
                .setIssuer(ISSUER)
                .setClaims(claims)
                // 签发时间
                .setIssuedAt(new Date())
                // 过期时间
                .setExpiration(expiration())
                // 签名算法、签名密钥
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
    }

    /**
     * 从 Token 中获取 username
     */
    public static String getUserCodeFromToken(String token) {
        String userCode = "";
        try {
            final Claims claims = getClaimsFromToken(token);
            if (Objects.isNull(claims) || StringUtils.isEmpty(claims.getSubject())) {
                return userCode;
            }
            userCode = claims.getSubject();
        } catch (Exception e) {
            LOGGER.error("ger username from token error. ", e);
        }
        return userCode;
    }

    /**
     * 从 Token 中获取创建时间
     */
    public static Date getIssuedAt(String token) {
        Date issuedAt;
        try {
            final Claims claims = getClaimsFromToken(token);
            if (Objects.isNull(claims) || Objects.isNull(claims.getIssuedAt())) {
                return null;
            }
            issuedAt = claims.getIssuedAt();
        } catch (Exception e) {
            // TODO
            issuedAt = null;
        }
        return issuedAt;
    }

    /**
     * 从 Token 中获取过时时间
     */
    public static Date getExpiration(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            if (Objects.isNull(claims) || Objects.isNull(claims.getExpiration())) {
                return null;
            }
            expiration = claims.getExpiration();
        } catch (Exception e) {
            // TODO
            expiration = null;
        }
        return expiration;
    }

    private static Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            if (e instanceof ExpiredJwtException) {
                throw new RuntimeException("TOKEN超时，请重新登录。");
            } else {
                LOGGER.error("解析Token信息时发生异常：", e);
                claims = null;
            }
        }
        return claims;
    }

    /**
     * 生成过期时间
     */
    private static Date expiration() {
        return new Date(System.currentTimeMillis() + expiration);
    }

    /**
     * 验证 Token 是否过期
     */
    private static Boolean isTokenExpired(String token) {
        final Date expiration = getExpiration(token);
        if (Objects.isNull(expiration)) {
            return true;
        }
        return expiration.before(new Date());
    }

//    private static Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
//        return (lastPasswordReset != null && created.before(lastPasswordReset));
//    }

//    public static Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
//        final Date created = getIssuedAt(token);
//        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
//                && !isTokenExpired(token);
//    }
}
