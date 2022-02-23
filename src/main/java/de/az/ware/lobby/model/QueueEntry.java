package de.az.ware.lobby.model;

import de.az.ware.common.model.MatchType;
import de.az.ware.lobby.controller.LobbyController;


public class QueueEntry {

    private final MatchType matchType;

    private long lastQueueUpdate;

    public QueueEntry(MatchType matchType) {
        this.matchType = matchType;
        lastQueueUpdate = System.currentTimeMillis();
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public void updateLastQueue(){
        lastQueueUpdate = System.currentTimeMillis();
    }

    public boolean isTooOld(){
        return System.currentTimeMillis() - lastQueueUpdate > LobbyController.MAX_QUEUE_TIME;
    }
}
