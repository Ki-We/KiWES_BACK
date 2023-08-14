package server.api.kiwes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@EnableWebMvc
public class SwaggerConfig {

    private static final String REFERENCE = "Authorization 헤더 값";

    private ApiInfo swaggerInfo() {
        return new ApiInfoBuilder()
                .title("KiWES API")
                .version("1.0.0")
                .description("KiWES API Docs\n")
                .build();
    }

    @Bean
    public Docket swaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
//        return new Docket(DocumentationType.OAS_30)
                .consumes(getConsumeContentTypes())
                .produces(getProduceContentTypes())
                .apiInfo(swaggerInfo()).select()
                .apis(RequestHandlerSelectors.basePackage("server.api.kiwes"))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false)
                .securityContexts(List.of(securityContext()))
                .securitySchemes(List.of(securityScheme()));

    }

    private Set<String> getConsumeContentTypes() {
        Set<String> consumes = new HashSet<>();
        consumes.add("application/json;charset=UTF-8");
        consumes.add("application/x-www-form-urlencoded");
        return consumes;
    }

    private Set<String> getProduceContentTypes() {
        Set<String> produces = new HashSet<>();
        produces.add("application/json;charset=UTF-8");
        return produces;
    }

    private SecurityContext securityContext() {
        return springfox.documentation
                .spi.service.contexts
                .SecurityContext.builder()
                .securityReferences(defaultAuth())
                .operationSelector(operationContext -> true)
//                .forPaths(PathSelectors.regex("/api/v1/auth.*"))  // 인증 적용할 URL 패턴 지정
                .build();
    }

    private List defaultAuth() {
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = new AuthorizationScope("global", "accessEverything");
        return List.of(new SecurityReference(REFERENCE, authorizationScopes));
    }

    private ApiKey securityScheme() {
        String targetHeader = "Authorization"; // 어떠한 헤더에 값을 대입할 것인가: Authorization 헤더 return new ApiKey(REFERENCE, targetHeader, "header"); }
        return new ApiKey(REFERENCE, targetHeader, "header");

    }

    private HttpAuthenticationScheme bearerAuthSecurityScheme() {
        return HttpAuthenticationScheme.JWT_BEARER_BUILDER.name(REFERENCE).build();
    }


}

