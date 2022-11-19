package ru.practicum.shareit.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
//@RestController
@EnableWebMvc
public class SwaggerConfig {

    public static final String REDIRECT_SWAGGER_UI_HTML = "redirect:/swagger-ui/";
    @Value("ShareIt WS")
    String name;

    @Value("ShareIt Webservice")
    String description;

    @Bean
    public Docket apiV1() {
        String version = "v1.0";
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName(version)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    @Bean
    public InternalResourceViewResolver defaultViewResolver() {
        return new InternalResourceViewResolver();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(name)
                .description(description)
                .version(AppConfig.INSTANCE_ID)
                .build();
    }
    @GetMapping(value = "/swagger-ui.html")
    public ModelAndView redirectLegacyUrl() {
        return new ModelAndView(REDIRECT_SWAGGER_UI_HTML);
    }

    @GetMapping(value = "/")
    public ModelAndView redirect() {
        return new ModelAndView(REDIRECT_SWAGGER_UI_HTML);
    }

    @GetMapping(value = "/api")
    public ModelAndView redirectApi() {
        return new ModelAndView(REDIRECT_SWAGGER_UI_HTML);
    }

    @GetMapping(value = "/doc")
    public ModelAndView redirectDoc() {
        return new ModelAndView(REDIRECT_SWAGGER_UI_HTML);
    }
}
