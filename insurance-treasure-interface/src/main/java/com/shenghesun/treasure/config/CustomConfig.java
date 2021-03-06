package com.shenghesun.treasure.config;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.shenghesun.treasure.filter.CheckTokenFilter;
import com.shenghesun.treasure.filter.CrossFilter;


@Configuration
public class CustomConfig {
	
	public static final Long EXPIRE_TIME_SECOND = 1800L; //秒值
	public static final Long SMSCODE_TIME_SECOND = 180L; //秒值
	public static final String REDIS_TOKEN_PREFIX = "login_token:";
	public static final String REDIS_USER_ID = "user_id:";
	public static final String URL_PATTERNS = "/api/*";
	public static final String URL_ALL = "/*";
	public static final String MODEL = "company/";
//	public static final String HASH_ALGORITHM_NAME = "md5";// 散列算法:这里使用MD5算法;;
	public static final int HASH_ITERATIONS = 10;// 散列的次数，比如散列两次，相当于 md5(md5(""));;

	
	public static final long EXPIRATION_TIME_MILLISECOND = 1800_000L; // 30 分钟 过期时间 毫秒值
	public static final String SECRET = "s459gb5s1o88p5vewbgwrwvcqrfmhyf9"; // 加密 secret ，一般不对外公布，写死在配置文件或者配置类中
//	static final String TOKEN_PREFIX = "Bearer"; // token 的前缀
//	static final String HEADER_STRING = "Authorization"; // 头部请求的 key 值

	@Bean
    public Filter kevinCrossFilter() {
    	System.out.println("----------------check cross filter-----------------");
    	return new CrossFilter();
    }
 
	
	@Bean
    public Filter kevinTokenFilter() {
    	System.out.println("----------------check token filter-----------------");
    	return new CheckTokenFilter();
    }
	 @Bean
	    public FilterRegistrationBean crossFilter() {
	    	FilterRegistrationBean registration = new FilterRegistrationBean();
	    	registration.setFilter(kevinCrossFilter());
	    	registration.addUrlPatterns(URL_ALL);
//	    	registration.addInitParameter("", "");
	    	registration.setName("kevinCrossFilter");
	    	
	    	return registration;
	    }
    @Bean
    public FilterRegistrationBean tokenFilter() {
    	FilterRegistrationBean registration = new FilterRegistrationBean();
    	registration.setFilter(kevinTokenFilter());
    	registration.addUrlPatterns(URL_PATTERNS);
//    	registration.addInitParameter("", "");
    	registration.setName("checkTokenFilter");
    	
    	return registration;
    }
}
