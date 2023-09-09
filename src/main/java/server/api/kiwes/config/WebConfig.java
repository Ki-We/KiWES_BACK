package server.api.kiwes.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import server.api.kiwes.global.converter.SocialLoginTypeConverter;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new SocialLoginTypeConverter());
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")      //패턴
                .allowedOriginPatterns("*")    //URL
                .allowedOrigins("https://kiwes.org") //URL
                .allowedHeaders("*")  //header
                .allowedMethods("GET", "POST");        //method
    }
}

