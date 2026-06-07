package com.library.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI配置（Knife4j）
 * 只定义SecurityScheme，让Knife4j右上角出现Authorize按钮
 * 不添加全局SecurityRequirement，避免所有接口默认要求鉴权
 */
@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(components());
    }

    private Info apiInfo() {
        return new Info()
                .title("智慧图书馆管理系统 API")
                .description("智慧图书馆管理系统接口文档")
                .version("1.0")
                .contact(new Contact().name("library-server"));
    }

    private Components components() {
        return new Components()
                .addSecuritySchemes(SECURITY_SCHEME_NAME, securityScheme());
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT");
    }
}
