package com.cdcas;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j
@SpringBootApplication
//这个注解是开启过滤器(扫描@WebFiler)
@ServletComponentScan
public class JierApplication {
    public static void main(String[] args) {
        SpringApplication.run(JierApplication.class,args);
        log.info("项目启动成功。。。");
    }
}
