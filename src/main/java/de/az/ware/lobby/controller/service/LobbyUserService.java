package de.az.ware.lobby.controller.service;

import de.az.ware.common.model.LobbyUser;
import de.az.ware.common.packets.LobbyRegister;
import de.az.ware.lobby.model.GoogleOAuth2User;
import de.az.ware.lobby.model.exception.RegisterException;
import de.az.ware.lobby.view.LobbyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LobbyUserService {

    @Autowired private LobbyUserRepository repository;

    public LobbyUser createUserFromGoogleOAuth(GoogleOAuth2User oUser, LobbyRegister.Request request) throws RegisterException {
        if(repository.existsByUsername(request.getUsername())) throw new RegisterException(LobbyRegister.Status.USERNAME_ALREADY_USED);

        LobbyUser user = new LobbyUser(UUID.randomUUID(), request.getUsername(), oUser.getGoogleId());
        repository.save(user); //TODO: catch already exists: google id, username
        return user;
    }

    public LobbyUser getUserFromGoogleOAuth(GoogleOAuth2User oUser){
        return repository.findByGoogleId(oUser.getGoogleId());
    }

    public void deleteUserFromGoogleOAuth(GoogleOAuth2User oUser) {
        repository.deleteByGoogleId(oUser.getGoogleId());
    }

}
