package com.example.jetcachedemo;

import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableMethodCache(basePackages = "com.example.jetcachedemo")
public class JetCacheDemoApplication {

	@Bean
	DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator autoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		autoProxyCreator.setProxyTargetClass(true);
		return autoProxyCreator;
	}

	public static void main(String[] args) {
		SpringApplication.run(JetCacheDemoApplication.class, args);
	}

}

@Service
@Transactional
class UserService {

	@Cached(name="UserService.queryUserInfo", expire = 3)
	@CachePenetrationProtect
	String loadUser(String account) {
		System.out.println("load user from db");
		return account;
	}
}

@RestController
class UserController {

	private UserService userService;

	UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/user/{account}")
	String user(@PathVariable String account) {
		return userService.loadUser(account);
	}
}
