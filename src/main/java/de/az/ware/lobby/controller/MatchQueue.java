package de.az.ware.lobby.controller;

import de.az.ware.common.model.MatchType;
import de.az.ware.lobby.model.LobbySession;

import java.util.HashSet;
import java.util.Set;

public class MatchQueue {

    private final MatchType type;
    private final Set<LobbySession> users;

    public MatchQueue(MatchType type) {
        this.type = type;
        this.users = new HashSet<>();
    }

    public MatchType getType() {
        return type;
    }

    public Set<LobbySession> getUsers() {
        return users;
    }

    public int getInQueue() {
        return users.size();
    }

    public void enqueue(LobbySession user) {
        synchronized (users){
            users.add(user);
        }
    }

    public void dequeue(LobbySession user) {
        synchronized (users){
            users.remove(user);
        }
    }

}
