package de.az.ware.lobby.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class GoogleOAuth2User implements OAuth2User {

    private final OAuth2User wrapped;

    public GoogleOAuth2User(OAuth2User wrapped) {
        this.wrapped = wrapped;
    }

    public String getGoogleId(){
        return getAttribute("sub");
    }

    public String getFullName(){
        return getAttribute("name");
    }

    public String getEmail(){
        return getAttribute("email");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return wrapped.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return wrapped.getAuthorities();
    }

    @Override
    public String getName() {
        return wrapped.getName();
    }
}
