package cz.muni.aphos.config;


import cz.muni.aphos.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * The Security configuration of the app.
 * Redirect after login: -username setting for new user
 *                       -profile page for existing user
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DefaultOAuth2UserService oAuth2UserService;

    @Autowired
    private UserService userService;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(c -> c
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .authorizeRequests()
                    .antMatchers("/search" ,"/", "/about", "/reference/**", "/reference/js/**").permitAll()
                    .antMatchers("/object/**", "/object/download", "/object/aperture").permitAll()
                    .antMatchers("/js/**", "/css/**", "/js/**","/images/**", "/webjars/**").permitAll()
                    .antMatchers("/api-docs").permitAll()
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
                    boolean newUser = userService.processOAuthPostLogin(
                            oauthUser.getAttribute("sub"));
                    if (newUser) {
                        response.sendRedirect("/profile/username");
                    } else {
                        response.sendRedirect("/profile/?id="+oauthUser.getAttribute("sub"));
                    }
                });
    }
}

