package com.example.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
@EnableSwagger2WebMvc
public class knife2jConfig {
    @Bean
    public Docket createApiDoc() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                // 文件 API 的信息
                .apiInfo(buildApiInfo())
                // 分组名称
                .groupName("后台接口")
                .select()
                // 这里指定 Controller 扫描包路径
                .apis(RequestHandlerSelectors.basePackage("com.atxbai.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    /**
     * 构建 API 信息
     * @return 返回 API 的信息
     */
    private ApiInfo buildApiInfo() {
        return new ApiInfoBuilder()
                .title("后台接口文档") // 标题
                .description("后台接口文档") // 描述
                .termsOfServiceUrl("") // 服务条款
                .contact(new Contact("","","")) // 作者信息
                .version("1.0") // 版本号
                .build();
    }
}