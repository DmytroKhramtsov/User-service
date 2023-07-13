package com.company.userservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket adminApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("adminApi")
                .select()
                .paths(PathSelectors.ant("/admin/users/**"))
                .apis(RequestHandlerSelectors.basePackage("com.company.userservice.api"))
                .build()
                .apiInfo(apiInfo());
    }

    @Bean
    public Docket publicApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("publicApi")
                .select()
                .paths(PathSelectors.ant("/login"))
                .apis(RequestHandlerSelectors.basePackage("com.company.userservice.api"))
                .build()
                .apiInfo(apiInfo());
    }

    @Bean
    public Docket internalApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("internalApi")
                .select()
                .paths(PathSelectors.ant("/auth/**"))
                .apis(RequestHandlerSelectors.basePackage("com.company.userservice.api"))
                .build()
                .apiInfo(apiInfo());
    }


    private ApiInfo apiInfo(){
        return ApiInfo.DEFAULT;
    }
}

