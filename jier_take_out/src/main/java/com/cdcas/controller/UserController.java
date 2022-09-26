package com.cdcas.controller;

import com.cdcas.common.R;
import com.cdcas.common.SMSUtils;
import com.cdcas.common.ValidateCodeUtils;
import com.cdcas.pojo.User;
import com.cdcas.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
//        获取手机号

        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
//        生成随机的4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info(code);
//        调用阿里云提供的短信服务api完成发送短信
//            SMSUtils.sendMessage("阿里云短信测试", "SMS_154950909", phone, code);
//        需要将生成的验证码保存到Session
            session.setAttribute(phone, code);
            return R.success("手机验证码已发送");
        }
        return R.error("手机验证码发送失败");
    }

    /**
     * 移动端用户登录
     *
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/login")
    public R<String> login(@RequestBody Map user, HttpSession session) {
        log.info(user.toString());
        return R.success("登陆成功");
    }
}
