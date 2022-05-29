package com.xjh.library.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class Swagger3Config {

    // 是否启动swagger3文档
    private final static boolean swaggerEnabled = true;

    @Bean
    public Docket createRestApi() {
        boolean swaggerEnabled = true;
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                // 是否开启
                .enable(swaggerEnabled)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xjh.library"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("图书管理系统接口测试文档")
                //创建人
                .contact(new Contact("谢嘉豪","https://www.xiejiahao.top","2292927675@qq.com"))
                .version("1.0")
                .description("开发测试阶段使用")
                .build();
    }
}
