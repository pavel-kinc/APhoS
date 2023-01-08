package cz.muni.aphos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerDocumentationConfig {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
            .info(new Info()
                .title("Aphos")
                .description("This is Amateur Photometric Survey (APhoS)  Application Programming Interface. ")
                .termsOfService("")
                .version("1.0.0")
                .license(new License()
                    .name("")
                    .url("http://unlicense.org"))
                .contact(new io.swagger.v3.oas.models.info.Contact()
                    .email("pavelkinc@gmail.com")));
    }

}
