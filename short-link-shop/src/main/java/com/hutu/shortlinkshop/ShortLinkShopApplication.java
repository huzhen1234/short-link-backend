package com.hutu.shortlinkshop;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hutu.shortlinkshop.mapper")
public class ShortLinkShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShortLinkShopApplication.class, args);
    }

}
