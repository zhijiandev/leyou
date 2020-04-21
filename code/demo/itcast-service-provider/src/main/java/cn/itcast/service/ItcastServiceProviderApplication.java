package cn.itcast.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("cn.itcast.service.mapper")
@EnableDiscoveryClient //启用eureka的客户端
public class ItcastServiceProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItcastServiceProviderApplication.class, args);
	}

}
