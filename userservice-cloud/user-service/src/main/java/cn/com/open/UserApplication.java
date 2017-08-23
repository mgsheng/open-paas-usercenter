package cn.com.open;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.WebMvcRegistrationsAdapter;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.Set;

@RestController
@RefreshScope
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class UserApplication {

	@Value("${api.base.request.url}")
	private String apiBaseRequestUrl;

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

	@RequestMapping("/dnotdelet/mom.html")
	public String home() {
		return "<html><head><meta http-equiv='content-type' content='text/html; charset=UTF-8'></head><body>helloworld!</body></html>";
	}

	@Bean
	public WebMvcRegistrationsAdapter webMvcRegistrationsHandlerMapping() {
		return new WebMvcRegistrationsAdapter() {
			@Override
			public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
				return new RequestMappingHandlerMapping() {
					private final static String EXCLUDE_PATH = "/dnotdelet/mom.html";
					@Override
					protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
						Set<String> patterns = mapping.getPatternsCondition().getPatterns();
						boolean flag = patterns.stream().anyMatch(e -> e.equals(EXCLUDE_PATH));
						if (!flag) {
							Class<?> beanType = method.getDeclaringClass();
							if (AnnotationUtils.findAnnotation(beanType, RestController.class) != null) {
								PatternsRequestCondition apiPattern = new PatternsRequestCondition(apiBaseRequestUrl).combine(mapping.getPatternsCondition());
								mapping = new RequestMappingInfo(mapping.getName(), apiPattern,
										mapping.getMethodsCondition(), mapping.getParamsCondition(),
										mapping.getHeadersCondition(), mapping.getConsumesCondition(),
										mapping.getProducesCondition(), mapping.getCustomCondition());
							}
						}
						super.registerHandlerMethod(handler, method, mapping);
					}
				};
			}
		};
	}
}

 
	
 
