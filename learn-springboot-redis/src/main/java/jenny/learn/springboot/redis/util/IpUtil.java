package jenny.learn.springboot.redis.util;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

@Slf4j
public class IpUtil {
    private final static String UNKNOWN = "unknown";
    private final static int MAX_LENGTH = 15;
    private final static String LOCALHOST_IPV4 = "127.0.0.1";
    private final static String LOCALHOST_IPV6 = "0:0:0:0:0:0:0:1";

    /**
     * 获取客户端IP地址
     */
    public static String getIpAddr() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StrUtil.isEmpty(ip) || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StrUtil.isEmpty(ip) || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if (LOCALHOST_IPV4.equals(ip) || LOCALHOST_IPV6.equals(ip)) {
                    InetAddress inetAddress = InetAddress.getLocalHost();
                    ip = inetAddress.getHostAddress();
                }
            }
        } catch (Exception e) {
            log.error("IPUtils ERROR ", e);
        }
        if (!StrUtil.isEmpty(ip) && ip.length() > MAX_LENGTH && ip.indexOf(StrUtil.COMMA) > 0) {
            ip = ip.substring(0, ip.indexOf(StrUtil.COMMA));
        }
        return ip;
    }
}
