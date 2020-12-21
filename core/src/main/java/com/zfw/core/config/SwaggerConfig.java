package com.zfw.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Value("${swagger.enable}")
	private Boolean enable;
	@Value("${spring.application.name}")
	private String swaggerTitle;
	@Bean
	public Docket createApi() {
		ParameterBuilder tokenPar = new ParameterBuilder();
		List<Parameter> pars = new ArrayList<>();
		tokenPar.name("Authorization").description("鉴权令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
		pars.add(tokenPar.build());
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).enable(enable).select()
				.apis(RequestHandlerSelectors.basePackage("com.zfw")).paths(PathSelectors.any()).build()
				.pathMapping("/")
//				.globalOperationParameters(pars)
				.securitySchemes(security());
	}
	private List<ApiKey> security() {
		return Arrays.asList(new ApiKey("token" , "token" , "header"));
	}
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(swaggerTitle).description("api文档服务").version("2.1").build();
	}

}
