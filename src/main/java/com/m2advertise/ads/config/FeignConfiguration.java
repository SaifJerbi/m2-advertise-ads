package com.m2advertise.ads.config;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.m2advertise.ads")
public class FeignConfiguration {

}
