package com.example.nextzhkuserver.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.nextzhkuserver.config.GlobalConfiguration;
import com.example.nextzhkuserver.entity.User;
import com.example.nextzhkuserver.service.UserService;
import com.example.nextzhkuserver.utils.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author kaiter
 * @since 2021-04-23
 */
@RestController
@Slf4j
public class UserController implements InitializingBean {

    private Algorithm algorithm;

    @Autowired
    private GlobalConfiguration configuration;
    @Autowired
    private UserService userService;

    @Override
    public void afterPropertiesSet() throws Exception {
        algorithm = Algorithm.HMAC256(configuration.getJwtKey());
    }

    @PostMapping("user/login")
    public AjaxResult login(String username, String password) {
        User user = userService.getOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUserName, username)
                .last("limit 1")
        );
        if (user == null) {
            return AjaxResult.fail("用户不存在");
        }
        if (DigestUtils.md5DigestAsHex(user.getUserPassword().getBytes()).equals(password)) {
            String token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("user", user.getUserName())
                    .sign(algorithm);
            return AjaxResult.success("登录成功", token);
        } else {
            return AjaxResult.fail("密码错误");
        }
    }
}
