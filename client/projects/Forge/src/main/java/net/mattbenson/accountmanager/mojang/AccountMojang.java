package net.mattbenson.accountmanager.mojang;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import org.json.JSONException;
import org.json.JSONObject;

import net.mattbenson.Falcun;
import net.mattbenson.accountmanager.Account;
import net.mattbenson.file.FileHandler;
import net.mattbenson.network.NetworkingClient;
import net.mattbenson.utils.AuthUtils;
import net.minecraft.client.Minecraft;

public class AccountMojang extends Account {
	private String username;
	private String uuid; //UUIDs arent stored in proper format (mojang's end, not ours), however they are uuids.
	private String accessToken;
	private String clientToken;
	
	private FileHandler handler;
	
	public AccountMojang(String username, String uuid, String accessToken, String clientToken) {
		this(username, uuid, accessToken, clientToken, null);
	}
	
	public AccountMojang(String username, String uuid, String accessToken, String clientToken, File file) {
		this.username = username;
		this.uuid = uuid;
		this.accessToken = accessToken;
		this.clientToken = clientToken;
		
		boolean save = false;
		
		if(file == null) {
			file = Paths.get(Falcun.ACCOUNTS_DIR.getAbsolutePath(), username + ".json").toFile();
			save = true;
		}
		
		handler = new FileHandler(file);
		
		try {
			handler.init();
		} catch (IOException e) {
			Falcun.getInstance().log.error("Failed to init file handler for account " + username + ".", e);
		}
		
		if(save) {
			saveAccount();
		}
	}
	
	@Override
	public String getUsername() {
		return username;
	}
	
	@Override
	public void removeAccount() {
		handler.getFile().delete();
	}
	
	@Override
	public void saveAccount() {
		JSONObject obj = new JSONObject();
		obj.put("type", "mojang");
		obj.put("accessToken", accessToken);
		obj.put("clientToken", clientToken);
		obj.put("uuid", uuid);
		
		try {
			handler.writeToFile(obj.toString(4), false);
		} catch (JSONException | IOException e) {
			Falcun.getInstance().log.error("Failed to write to file handler for account " + username + ".", e);
		}
	}
	
	@Override
	public String loadUser() {
		String status = "";
		
		try {
			JSONObject response = AuthUtils.isValidAccessToken(accessToken, clientToken);
			
			if(response.has("errorMessage")) {
				response = AuthUtils.refreshUser(accessToken, clientToken);
				
				if(response.has("errorMessage")) {
					status = response.getString("errorMessage");
					
					if(status.equalsIgnoreCase("Invalid token.")) {
						status = "Invalid account, try re-adding the account.";
					}
				} else {
					JSONObject profile = response.getJSONObject("selectedProfile");
					
					if(response.has("selectedProfile")) {
						accessToken = response.getString("accessToken");
						uuid = profile.getString("id");
						saveAccount();
					}
				}
			}
			
			if(status.isEmpty()) {
				Minecraft.getMinecraft().session = AuthUtils.getSession(username, uuid.toString(), accessToken);
				NetworkingClient.sendLine("SwitchAccount", accessToken);
				status = "You have successfully logged in.";
			}
		} catch (NoSuchElementException | JSONException | IOException e) {
			Falcun.getInstance().log.error("Failed to load user for account " + username + ".", e);
			status = "An unknown error occurred, contact an administrator.";
		}
		
		return status;
	}
}
