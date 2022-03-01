package de.az.ware.lobby.controller;

import de.az.ware.common.model.LobbyUser;
import de.az.ware.common.model.MatchInformation;
import de.az.ware.common.model.MatchType;
import de.az.ware.common.packets.LobbyQueue;
import de.az.ware.common.packets.LobbyQueuePoll;
import de.az.ware.lobby.controller.service.LobbyQueueService;
import de.az.ware.lobby.controller.service.MatchingService;
import de.az.ware.lobby.model.LobbySession;
import de.az.ware.lobby.model.exception.LobbyFullException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/lobby")
public class LobbyController {

    public final static int MAX_QUEUE_TIME = 5000; //in millis

    @Autowired private LobbySession session;
    @Autowired private LobbyQueueService queueService;
    @Autowired private MatchingService matchMakingService;

    private Map<MatchType, Integer> queueLengths;

    @GetMapping("/queue/poll")
    public LobbyQueuePoll.Response poll(){
        return new LobbyQueuePoll.Response(queueLengths);
    }

    @PostMapping("/queue")
    public LobbyQueue.Response queue(@RequestBody @Valid LobbyQueue.Request request) {
        LobbyUser user = session.getUser();
        if(queueService.isInQueue(user, request.getQueue())) {
            queueService.updateLastQueueTime(user);

            MatchInformation match = matchMakingService.getMatchInfo(user);
            if(match != null) return new LobbyQueue.Response(match);
            return new LobbyQueue.Response(LobbyQueue.Status.OK);
        }

        try {
            queueService.addToQueue(user, request.getQueue());
        } catch (LobbyFullException e) {
            return new LobbyQueue.Response(LobbyQueue.Status.QUEUE_IS_FULL);
        }

        return new LobbyQueue.Response(LobbyQueue.Status.OK);
    }

    @PostMapping("/unqueue")
    public void unqueue(){
        LobbyUser user = session.getUser();
        queueService.removeFromQueue(user);
    }


    @Scheduled(fixedRate = 1000)
    private void check(){
        queueService.removeOldUsersInQueue();
        queueLengths = queueService.calculateAllQueueLengths();

        matchMakingService.tryMatchmaking();
    }

}
