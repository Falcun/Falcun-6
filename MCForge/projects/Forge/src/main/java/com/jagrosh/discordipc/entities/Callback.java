 

package com.jagrosh.discordipc.entities;

import java.util.function.Consumer;

public class Callback
{
    private final Consumer<Packet> success;
    private final Consumer<String> failure;
    
    public Callback() {
        this((Consumer<Packet>)null, null);
    }
    
    public Callback(final Consumer<Packet> success) {
        this(success, null);
    }
    
    public Callback(final Consumer<Packet> success, final Consumer<String> failure) {
        this.success = success;
        this.failure = failure;
    }
    
    public boolean isEmpty() {
        return this.success == null && this.failure == null;
    }
    
    public void succeed(final Packet packet) {
        if (this.success != null) {
            this.success.accept(packet);
        }
    }
    
    public void fail(final String message) {
        if (this.failure != null) {
            this.failure.accept(message);
        }
    }
}
