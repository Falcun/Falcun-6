package net.mattbenson.accountmanager.microsoft;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.mattbenson.Falcun;
import net.mattbenson.accountmanager.Account;
import net.mattbenson.file.FileHandler;
import net.mattbenson.network.NetworkingClient;
import net.mattbenson.requests.ContentType;
import net.mattbenson.requests.WebRequest;
import net.mattbenson.requests.WebRequestResult;
import net.mattbenson.utils.AccountUtils;
import net.mattbenson.utils.AuthUtils;
import net.minecraft.client.Minecraft;

public class AccountMicrosoft extends Account {
	private String username;
	private String uuid; //UUIDs arent stored in proper format (mojang's end, not ours), however they are uuids.
	private String accessToken;
	private String refreshToken;
	
	private FileHandler handler;
	
	public AccountMicrosoft(String username, String uuid, String accessToken, String clientToken) {
		this(username, uuid, accessToken, clientToken, null);
	}
	
	public AccountMicrosoft(String username, String uuid, String accessToken, String refreshToken, File file) {
		this.username = username;
		this.uuid = uuid;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		
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
		obj.put("type", "microsoft");
		obj.put("accessToken", accessToken);
		obj.put("refreshToken", refreshToken);
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
			boolean attempt = !uuid.isEmpty() && !username.isEmpty() && !accessToken.isEmpty() && !refreshToken.isEmpty();
			WebRequest request = new WebRequest("https://api.minecraftservices.com/minecraft/profile", "GET", ContentType.JSON, false);
			request.setHeader("Authorization", "Bearer " + accessToken);
			WebRequestResult result = null;
			
			if(attempt) {
				result = request.connect();
			}
			
			if(result == null || result.getResponse() != 200) {
				request.clearHeaders();
				request.setContentType(ContentType.FORM);
				
				try {
					request.setURL("https://falcun.net/launcher/microsoft_redirect.php?client_id=" + Falcun.MICROSOFT_LOGIN_CLIENT_ID + "&refresh_token=" + URLEncoder.encode(refreshToken, "UTF-8") + "&grant_type=refresh_token&type=refresh&redirect_uri=" + MicrosoftThread.REDIRECT_URL);
				} catch (MalformedURLException e) {
					Falcun.getInstance().log.error("Failed to get microsoft redirect url.", e);
					throw new IOException("Failed to construct refresh url.");
				}
				
				String accessTokenInternal = "";
				String refreshToken = "";
				
				try {
					result = request.connect();
					String dataString = result.getData();
					System.out.println(dataString);
					JSONObject obj = new JSONObject(result.getData());
					JSONObject req = obj.getJSONObject("request");
					accessTokenInternal = req.getString("access_token");
					refreshToken = req.getString("refresh_token");
				} catch (JSONException | NoSuchElementException | IOException e) {
					Falcun.getInstance().log.error("Failed to do refresh url.", e);
					return "Account expired, try again later or try removing the account and try again.";
				}
				
				String resp = AccountUtils.getMicrosoftAuth(request, accessTokenInternal);
				
				if(resp == null) {
					return "An unknown error occurred, contact an administrator.";
				}
				
				JSONObject obj;
				
				try {
					obj = new JSONObject(resp);
				} catch(JSONException e) {
					return resp;
				}
				
				String uuid = obj.getString("id");
				String username = obj.getString("name");
				String accessToken = obj.getString("accessToken");
				
				this.uuid = uuid;
				this.username = username;
				this.accessToken = accessToken;
				this.refreshToken = refreshToken;
			}
			
			if(status.isEmpty()) {
				Minecraft.getMinecraft().session = AuthUtils.getSession(username, uuid.toString(), accessToken);
				NetworkingClient.sendLine("SwitchAccount", accessToken);
				status = "You have successfully logged in.";
			}
		} catch (NoSuchElementException | JSONException | IOException e) {
			Falcun.getInstance().log.error("Failed to load microsoft user for account " + username + ".", e);
			status = "An unknown error occurred (M$), contact an administrator.";
		}
		
		return status;
	}
}
