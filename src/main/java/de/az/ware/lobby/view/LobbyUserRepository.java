package de.az.ware.lobby.view;

import de.az.ware.common.model.LobbyUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;


public interface LobbyUserRepository extends CrudRepository<LobbyUser, UUID> {

    LobbyUser findByGoogleId(String googleId);

    //List<LobbyUser> findAllByUsername(String gooogleId, String username);

    //boolean existsByGoogleId(String googleId);
    boolean existsByUsername(String username);

    @Transactional
    void deleteByGoogleId(String googleId);

}
