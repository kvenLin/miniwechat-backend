package com.clf.cloud.gatewayserver.config;

import com.clf.cloud.gatewayserver.filters.AuthFilter;
import com.clf.cloud.gatewayserver.filters.CrossFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: clf
 * @Date: 2020-03-02
 * @Description: TODO
 */
@Configuration
public class ZuulConfig {

    @Bean
    public CrossFilter initialCrossFilter() {
        return new CrossFilter();
    }

    @Bean
    public AuthFilter initialAuthFilter(){
        return new AuthFilter();
    }
}
