package com.hutu.shortlinklink;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hutu.shortlinklink.mapper")
public class ShortLinkLinkApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShortLinkLinkApplication.class, args);
    }
}
