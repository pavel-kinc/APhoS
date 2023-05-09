package cz.muni.aphos.config;


import cz.muni.aphos.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;

/**
 * The Security configuration of the app.
 * Redirect after login: -username setting for new user
 * -profile page for existing user
 */
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
//                .csrf(c -> c
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(
                                "/search", "/", "/about", "/reference/**", "/reference/js/**",
                                "/object/**", "/object/download", "/object/aperture",
                                "/js/**", "/css/**", "/js/**", "/images/**", "/webjars/**",
                                "/openapi","/openapi.yaml", "/api/**", "/swagger-ui/**", "/swagger-ui.html",
                                "/swagger-resources/**", "/swagger-ui"
                        ).permitAll()
                        .anyRequest().authenticated()
                )
                .logout(x -> x.logoutSuccessUrl("/"))
                .oauth2Login(x -> x
                        .successHandler((request, response, authentication) -> {
                            OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
                            boolean newUser = userService.processOAuthPostLogin(
                                    oauthUser.getAttribute("sub"));
                            if (newUser) {
                                response.sendRedirect("/profile/username");
                            } else {
                                response.sendRedirect("/profile/?id=" + oauthUser.getAttribute("sub"));
                            }
                        })
                );
        return http.build();
    }
}

