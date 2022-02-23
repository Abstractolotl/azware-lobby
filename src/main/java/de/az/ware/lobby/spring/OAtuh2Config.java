package de.az.ware.lobby.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

@Configuration
public class OAtuh2Config {

    public static final String OAUTH_REGISTRATION_ID_GOOGLE = "bebb";

    @Value("${GOOGLE_SECRET}")
    private String GOOGLE_SECRET;

    @Bean
    public ClientRegistrationRepository repository(){
        if(GOOGLE_SECRET == null) throw new RuntimeException("Google Secret is missing!");

        return new InMemoryClientRegistrationRepository(
                ClientRegistration.withRegistrationId(OAUTH_REGISTRATION_ID_GOOGLE)
                        .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                        .clientId("406396251393-lfdqrnlb75jkhankt9jtugeeih08ie3d.apps.googleusercontent.com")
                        .clientSecret(GOOGLE_SECRET)
                        .issuerUri("https://accounts.google.com")
                        .authorizationUri("https://accounts.google.com/o/oauth2/v2/auth")
                        .scope("openid", "profile", "email")
                        .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}")
                        .tokenUri("https://oauth2.googleapis.com/token")
                        .userInfoUri("https://openidconnect.googleapis.com/v1/userinfo")
                        .jwkSetUri("https://www.googleapis.com/oauth2/v3/certs")
                        .build()
        );
    }

}
