package jenny.learn.springboot.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(TokenUtils.class);

    /*
    Bearer
     */

    public static final String CLAIM_KEY_USERNAME = "sub";
    public static final String CLAIM_KEY_CREATED = "created";
    public static final String CLAIM_KEY_SURNAME = "Surname";

    private static String secret = "mySecret";

    private static Long expiration = new Long(720000);

    /**
     * 从 Token 中获取 username
     */
    public static String getUsernameFromToken(String token) {
        String username = "";
        try {
            final Claims claims = getClaimsFromToken(token);
            if (null == claims || StringUtils.isEmpty(claims.getSubject())) return username;
            username = claims.getSubject();
        } catch (Exception e) {
            LOGGER.error("ger username from token error. ", e);
            username = e.toString();
        }
        return username;
    }

    /**
     * 从 Token 中获取创建时间
     */
    public static Date getCreatedDateFromToken(String token) {
        Date created;
        try {
            final Claims claims = getClaimsFromToken(token);
            if (null == claims || null == claims.get(CLAIM_KEY_CREATED)) return null;
            created = new Date((Long) claims.get(CLAIM_KEY_CREATED));
        } catch (Exception e) {
            // TODO
            created = null;
        }
        return created;
    }

    /**
     * 从 Token 中获取过时时间
     */
    public static Date getExpirationDateFromToken(String token) {
        Date expiration;
        try {
            final Claims claims = getClaimsFromToken(token);
            if (null == claims || null == claims.getExpiration()) return null;
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
                    .setSigningKey(secret)
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
    private static Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    /**
     * 验证 Token 是否过期
     */
    private static Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        if (null == expiration) return true;
        return expiration.before(new Date());
    }

    private static Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }

    /**
     * 生成 Token
     */
    public static String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, username);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 生成 Token
     */
    public static String generateToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public static Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
        final Date created = getCreatedDateFromToken(token);
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
                && !isTokenExpired(token);
    }

    /**
     * 刷新 Token
     */
    public static String refreshToken(String token) {
        String refreshedToken;
        try {
            final Claims claims = getClaimsFromToken(token);
            if (null == claims) return null;
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }
}
