package com.abel.wallet.api.config;

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
public class SwaggerConfig {
	//create a docket bean i.e a method that returns a Docket bean
	@Bean
	public Docket api() {
				
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(getApiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.abel.wallet.api"))//specify controller classes(in a package) u wanna expose
				.paths(PathSelectors.any())//specify the controller  that you wanna expose, using their endpoint pattern(antpattern)
				.build();//build the docket
}
	@SuppressWarnings("deprecation")
	private ApiInfo getApiInfo() {
		return new ApiInfo("My Wallet Service", "This page lists all the APIs in My wallet Service",
				"1.0", "our terms are client friendly", "call Abel on 08067950474", "licence 1.0", "http://abellicence.com");
	}
}
