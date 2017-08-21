package cn.com.open.user.app;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OAUserModelControllerTest {

	private static final Logger log = LoggerFactory.getLogger(OAUserModelControllerTest.class);

	@LocalServerPort
	private int port;
	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void getOAUserModel() {
		String url = "http://localhost:" + port + "usercenter/GetOAUserModel";
		MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
		param.add("idNo", "");
		param.add("username", "wangshuaia");
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(param, null);
		ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
		log.info(responseEntity.getBody());
	}
}
