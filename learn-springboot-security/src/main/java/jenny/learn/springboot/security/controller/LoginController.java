package jenny.learn.springboot.security.controller;

import com.google.common.collect.Maps;
import jenny.learn.springboot.security.util.TokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

@Slf4j
@RestController
public class LoginController {

    @Resource
    private AuthenticationManager authenticationManager;

    @RequestMapping("/login")
    public Object login(String userCode, String password) {
        log.info("## user login[ code={}, password={} ] ##", userCode, password);
        // 权限验证，如果验证成功，返回Authentication对象（含授予的权限）；验证失败时抛AuthenticationException异常
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userCode, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 生成token
        String token = TokenUtils.generateToken(userCode);
        log.debug("token: {}", token);
        User user = (User) authentication.getPrincipal();
        // 返回结果
        Map<String, Object> result = Maps.newHashMap();
        result.put("token", token);
        return result;
    }
}
