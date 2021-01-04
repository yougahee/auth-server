package com.gaga.apigateway;

import com.gaga.apigateway.filter.ErrorFilter;
import com.gaga.apigateway.filter.PostFilter;
import com.gaga.apigateway.filter.PreFilter;
import com.gaga.apigateway.filter.RouteFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy
@SpringBootApplication
public class ApigatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApigatewayApplication.class, args);
    }

    @Bean
    public PreFilter prefilter() {
        return new PreFilter();
    }

    @Bean
    public PostFilter postFilter(){
        return new PostFilter();
    }

    @Bean
    public ErrorFilter errorFilter(){
        return new ErrorFilter();
    }

    @Bean
    public RouteFilter routeFilter(){
        return new RouteFilter();
    }

}
