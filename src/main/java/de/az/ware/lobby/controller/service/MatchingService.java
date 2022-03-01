package de.az.ware.lobby.controller.service;

import de.az.ware.common.model.LobbyUser;
import de.az.ware.common.model.MatchInformation;
import de.az.ware.common.model.MatchType;
import de.az.ware.common.packets.MatchCreation;
import de.az.ware.lobby.view.MatchServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MatchingService {

    @Autowired private LobbyQueueService queueService;
    @Autowired private MatchServerService serverService;

    private final Map<LobbyUser, MatchInformation> pendingMatches;

    public MatchingService() {
        pendingMatches = new HashMap<>();

    }

    public MatchInformation getMatchInfo(LobbyUser user) {
        var info = pendingMatches.get(user);
        if(info != null) pendingMatches.remove(user);
        return info;
    }

    private void startMatch(MatchType type, LobbyUser[] users) {
        UUID[] playerMatchTokens = Arrays.stream(users).map(u -> UUID.randomUUID()).toArray(UUID[]::new);
        MatchCreation.Request request = new MatchCreation.Request(type, playerMatchTokens);

        MatchServer server = serverService.getAvailableServer();
        server.sendRequest(request, (rsp) -> {
            MatchCreation.Response response = (MatchCreation.Response) rsp;
            if(response.getStatus() != MatchCreation.Status.OK) {
                onMatchCreationFailure(type, users);
                return;
            }

            for(int i = 0; i < users.length; i++){
                MatchInformation info = new MatchInformation();
                info.setMatchType(type);
                info.setMatchToken(playerMatchTokens[i]);
                info.setMatchServerAdress(server.getConnectionAdress());
                pendingMatches.put(users[i], info);
            }
        });
    }

    private void onMatchCreationFailure(MatchType type, LobbyUser[] users){
        Arrays.stream(users).forEach(u -> {
            pendingMatches.remove(u);
            queueService.addToQueue(u, type);
        });
    }

     /* TODO: Games with more than 2 players
     ** TODO: More than one match per call */
    public void tryMatchmaking(){
        serverService.getAvailableServer();
        Arrays.stream(MatchType.values()).forEach(type -> {
            var queue = queueService.getQueue(type);

            LobbyUser[] users = new LobbyUser[2];
            synchronized (queue) {
                if(queue.size() >= 2) {
                    var itr = queue.iterator();
                    users[0] = itr.next();
                    itr.remove();
                    users[1] = itr.next();
                    itr.remove();
                }
            }
            if(users[0] != null && users[1] != null) startMatch(type, users);
        });
    }

}
