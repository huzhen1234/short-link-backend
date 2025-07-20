package com.hutu.shortlinkaccount;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hutu.shortlinkaccount.mapper")
public class ShortLinkAccountApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShortLinkAccountApplication.class, args);
	}

}
