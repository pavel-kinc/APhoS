package com.example.astroapp.security;


import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/home", "/").permitAll()
                    .antMatchers("/about").permitAll()
                    .anyRequest().authenticated()
                    .and()
                .oauth2Login().permitAll()
                    .and()
                .logout()
                    .logoutSuccessUrl("/");
    }

}

