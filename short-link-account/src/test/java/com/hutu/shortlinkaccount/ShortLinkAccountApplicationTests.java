package com.hutu.shortlinkaccount;

import com.hutu.shortlinkaccount.utils.MailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.mail.MessagingException;

@SpringBootTest
class ShortLinkAccountApplicationTests {

	@Resource
	private MailUtil mailUtil;

	@Test
	void contextLoads() throws MessagingException {
//		mailUtil.sendSimpleMail("3780430102@qq.com", "测试", "测试");
	}

}
