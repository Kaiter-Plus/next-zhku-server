package com.example.nextzhkuserver;

import com.example.nextzhkuserver.utils.AjaxResult;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.util.DigestUtils;

@Slf4j
public class AppUtilTest {

    @Test
    public void test() {
        log.info("{}", DigestUtils.md5DigestAsHex("123456".getBytes()));
    }

    @Test
    public void jsonTest() {
        log.info(AjaxResult.error("验证失败了").toString());
    }
}
