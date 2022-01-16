package com.example.astroapp.config;


import com.example.astroapp.services.CustomOAuth2UserService;
import com.example.astroapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomOAuth2UserService oAuth2UserService;

    @Autowired
    private UserService userService;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(c -> c
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .authorizeRequests()
                    .antMatchers("/home", "/", "/about").permitAll()
                    .antMatchers("/js/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .logout()
                    .logoutSuccessUrl("/")
                    .and()
                .oauth2Login().permitAll()
                    .userInfoEndpoint()
                        .userService(oAuth2UserService)
                    .and()
                .successHandler((request, response, authentication) -> {

                    OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
                    userService.processOAuthPostLogin(
                            oauthUser.getAttribute("sub"),
                            oauthUser.getAttribute("name"),
                            oauthUser.getAttribute("email"),
                            oauthUser.getAttribute("picture"));
                    response.sendRedirect("/profile/?id="+oauthUser.getAttribute("sub"));
                });
    }



}

