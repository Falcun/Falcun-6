package net.mattbenson.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.mattbenson.Falcun;
import net.mattbenson.accountmanager.Account;
import net.mattbenson.accountmanager.microsoft.AccountMicrosoft;
import net.mattbenson.accountmanager.microsoft.MicrosoftThread;
import net.mattbenson.accountmanager.mojang.AccountMojang;
import net.mattbenson.requests.ContentType;
import net.mattbenson.requests.WebRequest;
import net.mattbenson.requests.WebRequestResult;

public class AccountUtils {
	static Thread runThread = null;
	
	public static List<Account> getAccounts() throws IOException {		
		List<Account> accounts = new ArrayList<>();
		
		for(File file : Falcun.ACCOUNTS_DIR.listFiles()) {
			if(!file.isDirectory() && file.getName().endsWith(".json")) {
				accounts.add(getAccountData(file));
			}
		}
		
		return accounts;
	}

	private static Account getAccountData(File file) throws IOException {
		try(BufferedReader br = new BufferedReader(new FileReader(file))) {
			StringBuilder builder = new StringBuilder();
			
			int read = -1;
			
			while((read = br.read()) != -1) {
				builder.append((char)read);
			}
			
			String name = file.getName();
			
			if(name.contains(".")) {
				name = name.substring(0, name.lastIndexOf("."));
			}
			
			try {
				JSONObject obj = new JSONObject(builder.toString());
				String type = obj.getString("type");
				if(type.equalsIgnoreCase("mojang")) {
					return new AccountMojang(name, obj.getString("uuid"), obj.getString("accessToken"), obj.getString("clientToken"), file);
				} else if(type.equalsIgnoreCase("microsoft")) {
					return new AccountMicrosoft(name, obj.getString("uuid"), obj.getString("accessToken"), obj.getString("refreshToken"), file);
				}
			} catch(JSONException e) {
				Falcun.getInstance().log.error("Failed to load account " + name + ".", e);
			}
			
			return null;
		}
	}
	
	public static Object createNewUserMojang(String email, String password) {
		String status = "";
		
		try {
			JSONObject response = AuthUtils.authUser(email, password);
			
			if(response.has("errorMessage")) {
				status = response.getString("errorMessage");
			} else {
				JSONObject profile = response.getJSONObject("selectedProfile");
				
				if(response.has("selectedProfile")) {
					return new AccountMojang(profile.getString("name"), profile.getString("id"), response.getString("accessToken"), response.getString("clientToken"));
				} else {
					status = "Account doesn't own minecraft.";
				}
			}
		} catch (NoSuchElementException | JSONException | IOException e) {
			Falcun.getInstance().log.error("Failed to create account " + email + ".", e);
			status = "An unknown error occurred, contact an administrator.";
		}
	
		return status;
	}
	
	public static void startMicrosoftAuth() {
		boolean valid = true;
		
		if(runThread != null) {
			if(runThread.isAlive()) {
				valid = false;
			}
		}
		
		if(!valid) {
			((MicrosoftThread) runThread).visitURL();
			Falcun.getInstance().log.debug("Ignored user action for microsoft login since browser already open.");
		} else {
			runThread = new MicrosoftThread();
			runThread.start();
			
			((MicrosoftThread) runThread).visitURL();
		}
	}
	
	public static String getMicrosoftAuth(WebRequest request, String accessTokenInternal) {
		try {
			request.setURL("https://user.auth.xboxlive.com/user/authenticate");
		} catch (MalformedURLException e) {
			Falcun.getInstance().log.error("Failed to set new xbl url.", e);
			return null;
		}
		
		request.clearArguments();
		request.setContentType(ContentType.JSON);
		request.setRequestMethod("POST");
		
		JSONObject properties = new JSONObject();
		properties.put("AuthMethod", "RPS");
		properties.put("SiteName", "user.auth.xboxlive.com");
		properties.put("RpsTicket", "d=" + accessTokenInternal);
		
		request.setArguement("Properties", properties);
		request.setArguement("RelyingParty", "http://auth.xboxlive.com");
		request.setArguement("TokenType", "JWT");
		
		request.setHeader("Accept", "application/json");
		String uhs = "";
		String token = "";
		
		try {
			WebRequestResult result = request.connect();
			JSONObject obj = new JSONObject(result.getData());
			JSONObject display = obj.getJSONObject("DisplayClaims");
			
			JSONArray array = display.getJSONArray("xui");
			
			for(int i = 0; i < array.length(); i++) {
				JSONObject uhsObj = array.getJSONObject(i);
				
				if(uhsObj.has("uhs")) {
					uhs = uhsObj.getString("uhs");
					break;
				}
			}
			
			token = obj.getString("Token");
			
			if(uhs == null || uhs.length() == 0) {
				throw new JSONException("Missing uhs.");
			}
		} catch (JSONException | NoSuchElementException | IOException e) {
			Falcun.getInstance().log.error("Failed to do xbox sign in.", e);
			return null;
		}
		
		request.clearArguments();
		
		try {
			request.setURL("https://xsts.auth.xboxlive.com/xsts/authorize");
		} catch (MalformedURLException e) {
			Falcun.getInstance().log.error("Failed to set new xsts url.", e);
			return null;
		}
		
		properties = new JSONObject();
		JSONArray arr = new JSONArray();
		arr.put(token);
		
		properties.put("SandboxId", "RETAIL");
		properties.put("UserTokens", arr);
		
		request.setArguement("Properties", properties);
		request.setArguement("RelyingParty", "rp://api.minecraftservices.com/");
		request.setArguement("TokenType", "JWT");
		
		try {
			WebRequestResult result = request.connect();
			JSONObject obj = new JSONObject(result.getData());
			
			if(obj.has("XErr")) {
				long err = obj.getLong("XErr");
				return "Failed to log you in with microsoft. Error code: " + err + ", please contact an developer.";
			}
			
			token = obj.getString("Token");
		} catch (JSONException | NoSuchElementException | IOException e) {
			Falcun.getInstance().log.error("Failed to do xbox sign in.", e);
			return null;
		}
		
		request.clearArguments();
		request.setArguement("identityToken", "XBL3.0 x=" + uhs + ";" + token);
		String accessToken = "";
		
		try {
			request.setURL("https://api.minecraftservices.com/authentication/login_with_xbox");
		} catch (MalformedURLException e) {
			Falcun.getInstance().log.error("Failed to set new minecraft login url.", e);
			return null;
		}
		
		try {
			WebRequestResult result = request.connect();
			JSONObject obj = new JSONObject(result.getData());
			accessToken = obj.getString("access_token");
		} catch (JSONException | NoSuchElementException | IOException e) {
			Falcun.getInstance().log.error("Failed to do minecraft sign in.", e);
			return null;
		}
		
		request.clearArguments();
		request.clearHeaders();
		
		request.setHeader("Authorization", "Bearer " + accessToken);
		request.setRequestMethod("GET");
		
		try {
			request.setURL("https://api.minecraftservices.com/entitlements/mcstore");
		} catch (MalformedURLException e) {
			Falcun.getInstance().log.error("Failed to set new minecraft purchase check url.", e);
			return null;
		}
		
		try {
			WebRequestResult result = request.connect();
			JSONObject obj = new JSONObject(result.getData());
			
			if(obj.getJSONArray("items").length() == 0) {
				return "You don't own minecraft on the attempted account.";
			}
		} catch (JSONException | NoSuchElementException | IOException e) {
			Falcun.getInstance().log.error("Failed to do minecraft purchase check.", e);
			return null;
		}
		
		try {
			request.setURL("https://api.minecraftservices.com/minecraft/profile");
		} catch (MalformedURLException e) {
			Falcun.getInstance().log.error("Failed to set profile url.", e);
			return null;
		}
		
		try {
			WebRequestResult result = request.connect();
			JSONObject obj = new JSONObject(result.getData());
			obj.put("accessToken", accessToken);
			return obj.toString();
		} catch (JSONException | NoSuchElementException | IOException e) {
			Falcun.getInstance().log.error("Failed to do minecraft purchase check.", e);
			return null;
		}

	}
}
