package net.smart.rfid.tunnel.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.ResourceHandlerRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

public class SwaggerConfig implements WebFluxConfigurer {

	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(new ApiInfoBuilder().description("My Reactive API")
				.title("My Domain object API").version("1.0.0").build()).enable(true).select()
				.apis(RequestHandlerSelectors.basePackage("net.smart.rfid.tunnel.controller")).paths(PathSelectors.any()).build();

	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/swagger-ui/**").addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/").resourceChain(false);
	}
}