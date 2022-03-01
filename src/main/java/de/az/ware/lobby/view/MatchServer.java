package de.az.ware.lobby.view;

import de.az.ware.common.model.MatchType;
import de.az.ware.common.packets.MasterAuthenticate;
import de.az.ware.common.packets.MatchCreation;
import de.az.ware.connection.Connection;
import de.az.ware.connection.packet.*;
import de.az.ware.connection.websocket.WebSocketClient;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class MatchServer implements PacketHandler {

    private final String address;
    private final WebSocketClient client;
    private final RequestMapper requestMapper;

    private final Map<UUID, Consumer<MatchCreation.Status>> callbacks;

    private boolean ready = false;

    public MatchServer(String address, RequestMapper requestMapper) {
        this.address = address;
        this.requestMapper = requestMapper;
        callbacks = new HashMap<>();
        client = new WebSocketClient(address);

        PacketParser parser = new PacketParser();
        client.setConnectionListener(new ConnectionPacketListenerAdapter(parser, new DelegatedPacketListener<Connection>(Connection.class, parser, this)));
        client.connect();
    }

    public void sendRequest(RequestPacket request, Consumer<ResponsePacket> callback) {
        requestMapper.sendAndRegisterCallback(client, request, callback);
    }

    private void sendPacket(Packet packet){
        client.sendMessage(PacketParser.SerializePacket(packet));
    }

    public void authenticate(String authToken){
        if(ready) throw new IllegalStateException("Server already authenticated");

        var req = new MasterAuthenticate.Request(authToken);
        sendPacket(req);
    }

    public void onAuthResponse(Connection connection, MasterAuthenticate.Response packet){
        if(ready) throw new IllegalStateException("Server already authenticated");

        if(packet.getStatus() == MasterAuthenticate.Status.OK) {
            ready = true;
        } else {
            throw new RuntimeException("Error while authenticating: " + packet.getStatus());
        }
    }

    public boolean isReady() {
        return ready;
    }

    public String getConnectionAdress(){
        return address;
    }

}
