package de.az.ware.lobby.controller.service;


import de.az.ware.connection.packet.RequestMapper;
import de.az.ware.lobby.view.MatchServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchServerService {

    @Autowired private RequestMapper requestMapper;

    private MatchServer server;

    private void initServer(){
        server = new MatchServer("ws://192.168.1.162:12001", requestMapper);
        server.authenticate("df2dc4fb-571b-4bff-bb77-84edcbfc3172");
    }

    public MatchServer getAvailableServer(){
        if(server == null) initServer();
        return server;
    }

}
