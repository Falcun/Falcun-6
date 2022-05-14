package net.mattbenson.utils;

import java.util.LinkedList;
import java.util.Queue;

public class ClickCounter {
    private final Queue<Long> clicks = new LinkedList<Long>();
    
    public void onClick() {
        clicks.add(System.currentTimeMillis() + 1000L);
    }
    
    public int getCps() {
        long time = System.currentTimeMillis();

        while (!clicks.isEmpty() && clicks.peek() < time) {
            clicks.remove();
        }

        return clicks.size();
    }

}
