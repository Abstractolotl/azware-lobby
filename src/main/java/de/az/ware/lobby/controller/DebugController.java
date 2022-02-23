package de.az.ware.lobby.controller;

import de.az.ware.lobby.model.GoogleOAuth2User;
import de.az.ware.lobby.model.LobbySession;
import de.az.ware.lobby.controller.service.LobbyUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lobby")
public class DebugController {

    @Autowired private LobbySession session;
    @Autowired private LobbyUserService service;

    @GetMapping("/clear")
    public void clear(){
        session.setUser(null);
        service.deleteUserFromGoogleOAuth(new GoogleOAuth2User((OAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
    }

    @GetMapping("/hello")
    public String hello(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

}
