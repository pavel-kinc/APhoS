package com.example.astroapp.config;


import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/home", "/", "/about").permitAll()
                    //TODO needs to be restricted
                    .antMatchers("/js/**", "/user").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .oauth2Login().permitAll()
                    .and()
                .logout()
                    .logoutSuccessUrl("/");
    }

}

