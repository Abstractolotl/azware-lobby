package de.az.ware.lobby.controller;

import de.az.ware.common.model.LobbyUser;
import de.az.ware.common.packets.LobbyLogin;
import de.az.ware.common.packets.LobbyRegister;
import de.az.ware.lobby.model.GoogleOAuth2User;
import de.az.ware.lobby.model.LobbySession;
import de.az.ware.lobby.model.exception.RegisterException;
import de.az.ware.lobby.controller.service.LobbyUserService;
import de.az.ware.lobby.spring.OAtuh2Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class LobbyAuthController {

    @Autowired private LobbyUserService userService;
    @Autowired private LobbySession session;

    @GetMapping("/login")
    public LobbyLogin.Response login(LobbyLogin.Request request){
        LobbyUser user;

        if(session.getUser() == null) {
            user = getUserFromService();
            if(user == null) return new LobbyLogin.Response(LobbyLogin.Status.NOT_REGISTERED, null);
            session.setUser(user);
        } else {
            user = session.getUser();
        }

        return new LobbyLogin.Response(LobbyLogin.Status.OK, user);
    }

    @PostMapping("/register")
    public LobbyRegister.Response register(@Valid @RequestBody LobbyRegister.Request request){
        LobbyUser user;
        if((user = session.getUser()) != null || (user = getUserFromService()) != null){
            if(session.getUser() == null) session.setUser(user);
            return new LobbyRegister.Response(LobbyRegister.Status.ALREADY_REGISTERED, user);
        }

        try {
            user = createUser(request);
        } catch (RegisterException e) {
            return new LobbyRegister.Response(e.getStatus(), null);
        }

        session.setUser(user);
        return new LobbyRegister.Response(LobbyRegister.Status.OK, user);
    }

    private LobbyUser createUser(LobbyRegister.Request request) {
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.isAuthenticated()) throw new RuntimeException("Session is not authenticated. Should not be happening!");

        switch (authentication.getAuthorizedClientRegistrationId()) {
            case OAtuh2Config.OAUTH_REGISTRATION_ID_GOOGLE:
                return userService.createUserFromGoogleOAuth(new GoogleOAuth2User(authentication.getPrincipal()), request);
        }
        throw new RuntimeException("Unknown OAuth Registration ID. Should not be happening!");
    }

    private LobbyUser getUserFromService(){
        OAuth2AuthenticationToken authentication = (OAuth2AuthenticationToken)SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.isAuthenticated()) throw new RuntimeException("Session is not authenticated. Should not be happening!");

        switch (authentication.getAuthorizedClientRegistrationId()) {
            case OAtuh2Config.OAUTH_REGISTRATION_ID_GOOGLE:
                return userService.getUserFromGoogleOAuth(new GoogleOAuth2User(authentication.getPrincipal()));
        }
        throw new RuntimeException("Unknown OAuth Registration ID. Should not be happening!");
    }

}
