package com.atguigu.gmall.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter(){
        //跨域配置对象
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 所有请求头信息 * 表示任意
        corsConfiguration.addAllowedHeader("*");
        //设置允许访问的网络
        corsConfiguration.addAllowedOrigin("*");
        // 设置请求方法 * 表示任意
        corsConfiguration.addAllowedMethod("*");
        // 设置是否从服务器获取cookie
        corsConfiguration.setAllowCredentials(true);

        //配置源对象
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();

        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
        //过滤器对象
        return new CorsWebFilter(urlBasedCorsConfigurationSource);
    }
}
