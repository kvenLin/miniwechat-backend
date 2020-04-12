package com.clf.cloud.nettyserver;

import com.clf.cloud.common.utils.SpringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@EnableDiscoveryClient
@SpringBootApplication
@MapperScan("com.clf.cloud.nettyserver.dao")
@ComponentScan(basePackages = {"com.clf.cloud", "org.n3r.idworker"})
public class NettyServerApplication {

    @Bean
    public SpringUtils getSpringUtils() {
        return new SpringUtils();
    }
    public static void main(String[] args) {
        SpringApplication.run(NettyServerApplication.class, args);
    }

}
