package com.jagrosh.discordipc;

import org.json.JSONObject;
import com.jagrosh.discordipc.entities.User;
import com.jagrosh.discordipc.entities.Packet;

public interface IPCListener
{
    void onPacketSent(final IPCClient client, final Packet packet);
    
     void onPacketReceived(final IPCClient client, final Packet packet);
    
     void onActivityJoin(final IPCClient client, final String secret);
    
     void onActivitySpectate(final IPCClient client, final String secret);
    
     void onActivityJoinRequest(final IPCClient client, final String secret, final User user);
    
     void onReady(final IPCClient client);
    
     void onClose(final IPCClient client, final JSONObject json);
    
     void onDisconnect(final IPCClient client, final Throwable t);
}
