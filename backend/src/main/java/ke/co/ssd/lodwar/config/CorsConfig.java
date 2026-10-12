package ke.co.ssd.lodwar.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${cors.domain}")
    private String corsDomain;

    @Override
    public void addCorsMappings(final CorsRegistry registry) {

        registry.addMapping("/**")
            .allowedOrigins(corsDomain)
            .allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*");
    }
}
