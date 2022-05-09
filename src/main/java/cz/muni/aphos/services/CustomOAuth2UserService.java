package cz.muni.aphos.services;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;

/**
 * Need to define Custom OAuth2Service which just extends the default one,
 * so we can use it as a bean.
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
}
