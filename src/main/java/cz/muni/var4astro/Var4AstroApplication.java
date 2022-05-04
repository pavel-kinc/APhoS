package cz.muni.var4astro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.bind.annotation.RestController;

/**
 * The entry point of the application.
 */
@EnableRetry
@SpringBootApplication
@RestController
public class Var4AstroApplication {

    public static void main(String[] args) {
        SpringApplication.run(Var4AstroApplication.class, args);
    }

}
