package de.az.ware.lobby.controller.service;

import de.az.ware.common.model.LobbyUser;
import de.az.ware.common.model.MatchInformation;
import de.az.ware.common.model.MatchType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Service
public class MatchService {

    @Autowired private LobbyQueueService queueService;

    public MatchInformation getMatchInfo(LobbyUser user) {
        return null;
    }

    private void startMatch(MatchType type, Set<LobbyUser> users) {

    }

    /* TODO: Games with more than 2 players
     * TODO: More than one match per call
     */
    public void tryMatchmaking(){
        Arrays.stream(MatchType.values()).forEach(type -> {
            var queue = queueService.getQueue(type);

            Set<LobbyUser> users = new HashSet<>();
            synchronized (queue) {
                if(queue.size() >= 2) {
                    var itr = queue.iterator();
                    users.add(itr.next());
                    itr.remove();
                    users.add(itr.next());
                    itr.remove();
                }
            }
            if(users.size() != 0) startMatch(type, users);
        });
    }

}
