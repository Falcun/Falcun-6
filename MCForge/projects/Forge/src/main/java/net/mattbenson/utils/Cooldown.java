package net.mattbenson.utils;

import java.util.*;

public class Cooldown {
    public static ArrayList<String> listOfStrings = new ArrayList<String>();
    
    public Cooldown(){
    }

    public static void add(int itemID, Long maxSeconds, Long startime, String item) {
    	
    	if (Cooldown.listOfStrings.size() == 0) {
    		listOfStrings.add(itemID + "," + maxSeconds + "," + System.currentTimeMillis() + "," + item);
    	} else {
    	boolean contains = false;
    	for (int i = 0; i < Cooldown.listOfStrings.size(); i++) {
    		String[] data = Cooldown.listOfStrings.get(i).split(",");
    		if (Cooldown.listOfStrings.get(i).contains(item)) {
       		   	contains = true;
    		}
    	}
    	if (!contains) {
    		listOfStrings.add(itemID + "," + maxSeconds + "," + System.currentTimeMillis() + "," + item);
    	}
    	}
    	
    }
}