package com.starbug.rabbitmq.springbootrabbitmq.config;

import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 *  @Author Starbug
 *  @Date 2021/6/20 22:14
 */
public class SwaggerConfig {

    @Bean
    public Docket webApiConfig() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                .build();
    }

    private ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                .title("RabbitMQ接口文档")
                .description("描述RabbitMQ微服务接口定义")
                .version("1.0")
                .contact(new Contact("starbug", "https://www.starbug.vip", "1285226024@qq.com"))
                .build();
    }


}
