package com.hutu.shortlinkaccount;

import com.hutu.shortlinkaccount.utils.MailUtil;
import com.hutu.shortlinkcommon.config.MinioUtil;
import io.minio.messages.Item;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.List;

@SpringBootTest
class ShortLinkAccountApplicationTests {

	@Resource
	private MailUtil mailUtil;
	@Resource
	private MinioUtil minioUtil;

	@Test
	void contextLoads() throws MessagingException {
//		mailUtil.sendSimpleMail("3780430102@qq.com", "测试", "测试");
		List<Item> items = minioUtil.listObjects("short-link");
		System.out.println("en");
	}

}
