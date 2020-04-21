package cn.itcast.springboot;

import cn.itcast.springboot.controller.HelloController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

//引导类：springboot应用的入口

@EnableAutoConfiguration //启用springboot应用的自动配置
@ComponentScan //扫描该类所在的包以及它的子子孙孙包
@SpringBootApplication //使用组合注解，相当于@EnableAutoConfiguration ComponentScan SpringBootConfiguration

public class  TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class,args);
    }
}
