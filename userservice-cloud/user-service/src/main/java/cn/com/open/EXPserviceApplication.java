package cn.com.open;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RefreshScope
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class EXPserviceApplication {
	public static void main(String[] args) {
		SpringApplication.run(EXPserviceApplication.class, args);
	}
	@RequestMapping("/dnotdelet/mom.html")
	public String home() {
		return "<html><head><meta http-equiv='content-type' content='text/html; charset=UTF-8'></head><body>helloworld!</body></html>";
	}
}
