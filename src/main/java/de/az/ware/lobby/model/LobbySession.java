package de.az.ware.lobby.model;

import de.az.ware.common.model.LobbyUser;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LobbySession {

    private LobbyUser user;

    public LobbyUser getUser() {
        return user;
    }

    public void setUser(LobbyUser user) {
        this.user = user;
    }

}
