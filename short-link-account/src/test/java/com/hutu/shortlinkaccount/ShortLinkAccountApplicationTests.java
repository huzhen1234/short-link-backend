package com.hutu.shortlinkaccount;

import com.hutu.shortlinkaccount.domain.pojo.Traffic;
import com.hutu.shortlinkaccount.service.TrafficService;
import com.hutu.shortlinkaccount.utils.MailUtil;
import com.hutu.shortlinkcommon.config.MinioUtil;
import io.minio.messages.Item;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.List;
import java.util.Random;

@SpringBootTest
class ShortLinkAccountApplicationTests {

	@Resource
	private MailUtil mailUtil;
	@Resource
	private MinioUtil minioUtil;
	@Resource
	private TrafficService trafficService;

	@Test
	void contextLoads() throws MessagingException {
//		mailUtil.sendSimpleMail("3780430102@qq.com", "测试", "测试");
		List<Item> items = minioUtil.listObjects("short-link");
		System.out.println("en");
	}

	@Test
	void testTraffic() {
		for (int i = 0; i < 10; i++) {
			// 随机生成四位数字
			Random random = new Random();
			int randomNumber = 1000 + random.nextInt(9000);
			Traffic traffic = new Traffic();
			traffic.setAccountNo((long) randomNumber);
			trafficService.save( traffic);
		}

	}

}
