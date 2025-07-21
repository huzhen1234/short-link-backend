package com.hutu.shortlinkaccount;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("com.hutu.shortlinkaccount.mapper")
@EnableAsync
@EnableAspectJAutoProxy(exposeProxy = true)
public class ShortLinkAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShortLinkAccountApplication.class, args);
	}

}
