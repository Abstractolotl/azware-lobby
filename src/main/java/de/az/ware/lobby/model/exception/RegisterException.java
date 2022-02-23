package de.az.ware.lobby.model.exception;

import de.az.ware.common.packets.LobbyRegister;

public class RegisterException extends RuntimeException{

    private final LobbyRegister.Status status;

    public RegisterException(LobbyRegister.Status status) {
        this.status = status;
    }

    public LobbyRegister.Status getStatus() {
        return status;
    }
}
