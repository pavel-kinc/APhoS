package com.example.astroapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The entry point of the application.
 */
@EnableRetry
@SpringBootApplication
@RestController
public class AstroAppApplication {

//    @GetMapping("/user")
//    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
//        Map<String, Object> userAttributes =
//                new HashMap<>(Collections.singletonMap("name", principal.getAttribute("name")));
//        userAttributes.put("email", principal.getAttribute("email"));
//        return userAttributes;
//    }

    public static void main(String[] args) {
        SpringApplication.run(AstroAppApplication.class, args);
    }

}
