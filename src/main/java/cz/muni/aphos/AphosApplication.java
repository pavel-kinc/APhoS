package cz.muni.aphos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * The entry point of the application.
 */
@EnableRetry
@SpringBootApplication
@EnableOpenApi
public class AphosApplication {

    public static void main(String[] args) {
        SpringApplication.run(AphosApplication.class, args);
    }

}
