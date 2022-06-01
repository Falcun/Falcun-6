package ru.vidtu.ias.account;

import java.util.UUID;
import java.util.function.Consumer;

import net.minecraft.client.Minecraft;

public interface Account {
	public String alias();
	public void login(Minecraft mc, Consumer<Throwable> handler);
	public boolean editable();
	public boolean online();
	public void use();
	public int uses();
	public long lastUse();
	
	public default UUID uuid() {
		return null;
	}
}
