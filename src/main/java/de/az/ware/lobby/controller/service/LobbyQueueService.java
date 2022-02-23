package de.az.ware.lobby.controller.service;

import de.az.ware.common.model.LobbyUser;
import de.az.ware.common.model.MatchType;
import de.az.ware.lobby.model.QueueEntry;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LobbyQueueService {

    private final Map<LobbyUser, QueueEntry> userInQueue;
    private final Map<MatchType, Set<LobbyUser>> matchQueues;

    public LobbyQueueService() {
        userInQueue = new HashMap<>();
        matchQueues = new HashMap<>();
        Arrays.stream(MatchType.values()).forEach(type -> matchQueues.put(type, new HashSet<>()));
    }

    public Set<LobbyUser> getQueue(MatchType type){
        return matchQueues.get(type);
    }

    public boolean isInQueue(LobbyUser user, MatchType match){
        QueueEntry entry = userInQueue.get(user);
        return entry != null && entry.getMatchType() == match;
    }

    public void removeFromQueue(LobbyUser user){
        QueueEntry entry = userInQueue.get(user);
        if(entry == null) return;

        synchronized (userInQueue) {
            userInQueue.remove(user);
        }

        var queue = matchQueues.get(entry.getMatchType());
        synchronized (queue) {
            queue.remove(user);
        }
    }

    public void updateLastQueueTime(LobbyUser user) {
        QueueEntry entry = userInQueue.get(user);
        if(entry != null) entry.updateLastQueue();
    }

    public void addToQueue(LobbyUser user, MatchType match) {
        removeFromQueue(user);

        QueueEntry entry = new QueueEntry(match);
        synchronized (userInQueue) {
            userInQueue.put(user, entry);
        }

        var queue = matchQueues.get(match);
        synchronized (queue){
            queue.add(user);
        }
    }

    public Map<MatchType, Integer> calculateAllQueueLengths(){
        var map = new HashMap<MatchType, Integer>();
        synchronized (matchQueues){
            matchQueues.forEach((type, queue) -> map.put(type, queue.size()));
        }
        return map;
    }

    public void removeOldUsersInQueue(){
        List<LobbyUser> toRemove = new LinkedList<>();
        synchronized (userInQueue) {
            userInQueue.forEach((user, entry) -> {
                if(!entry.isTooOld()) return;
                var queue = matchQueues.get(entry.getMatchType());
                synchronized (queue) {
                    queue.remove(user);
                }
                toRemove.add(user);
            });
            toRemove.forEach(userInQueue::remove);
        }
    }

}
