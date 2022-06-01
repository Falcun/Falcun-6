package net.mattbenson.network.network.packets.account;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import net.mattbenson.Falcun;
import net.mattbenson.accountmanager.Account;
import net.mattbenson.network.NetworkingClient;
import net.mattbenson.network.network.Packet;
import net.mattbenson.utils.AccountUtils;
import net.minecraft.client.Minecraft;

public class Handshake implements Packet {

	@Override
	public String getPacketName() {
		return "Handshake";
	}

	@Override
	public int getArgsWanted() {
		return 0;
	}

	@Override
	public void onReceive(String[] args) {
		try {
			if(Minecraft.getMinecraft().getSession().getToken().equalsIgnoreCase("0")) {
				List<Account> accounts = AccountUtils.getAccounts();
				
				for(Account acc : accounts) {
					acc.loadUser();
					
					if(!Minecraft.getMinecraft().getSession().getToken().equalsIgnoreCase("0")) {
						break;
					}
				}
			}
			
			if(!NetworkingClient.reconnecting) {
				// uncomment this line under if you're on MCP and haven't loaded any accounts yet, then load some account(s) into the account manager and restart.
				NetworkingClient.networkManager.sendLine("Login", Falcun.getInstance().getHWID(), Falcun.getInstance().getEmail(), Minecraft.getMinecraft().getSession().getToken());
			} else {
				NetworkingClient.networkManager.sendLine("ResumeSession", Falcun.getInstance().getHWID(), Falcun.getInstance().getEmail());
			}
		} catch (NoSuchAlgorithmException | IOException e) {
			Falcun.getInstance().log.error("Failed to respond to handshake.", e);
			//Matt
			Runtime.getRuntime().halt(0);
		}
	}
}
