package cn.com.open.user.app.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.com.open.user.app.interceptor.VerifySignatureInterceptor;
import cn.com.open.user.app.log.LogFilter;

/**
 * 系统配置
 * Created by guxuyang on 05/07/2017.
 */
@Configuration
@EnableTransactionManagement
@MapperScan("cn.com.open.user.app.mapper")
public class SystemConfig extends WebMvcConfigurerAdapter {

    @Bean
    public RestTemplate getRestTemplate() {
        return new RestTemplate();
    }

    @Bean
    public VerifySignatureInterceptor verifySignatureInterceptor() {
        return new VerifySignatureInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(verifySignatureInterceptor()).excludePathPatterns("/dnotdelet/mom.html");
        super.addInterceptors(registry);
    }

    @Bean
    public LogFilter getLogFilter() {
        return new LogFilter();
    }

    @Bean
    public FilterRegistrationBean filterRegistration() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(getLogFilter());
        registration.addUrlPatterns("/api/usercloudservice/*");
        registration.setName("LogFilter");
        registration.setOrder(1);
        return registration;
    }

}


