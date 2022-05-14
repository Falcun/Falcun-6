package net.mattbenson.accountmanager.microsoft;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.NoSuchElementException;

import org.json.JSONException;
import org.json.JSONObject;

import net.mattbenson.Falcun;
import net.mattbenson.accountmanager.Account;
import net.mattbenson.gui.old.alt.GuiAccountManager;
import net.mattbenson.requests.ContentType;
import net.mattbenson.requests.WebRequest;
import net.mattbenson.requests.WebRequestResult;
import net.mattbenson.utils.AccountUtils;
import net.mattbenson.web.Servlet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class MicrosoftServlet extends Servlet {
	public MicrosoftServlet() {
		super("handler:microsoftlogin");
	}
	
	@Override
	public String onRequest(String requestMethod, Map<String, String> data, String pageContent) {
		if(!data.containsKey("code")) {
			return pageContent;
		}
		
		String code = data.get("code");
		
		WebRequest request;
		
		try {
			request = new WebRequest("https://falcun.net/launcher/microsoft_redirect.php?client_id=" + Falcun.MICROSOFT_LOGIN_CLIENT_ID + "&code=" + code + "&grant_type=authorization_code&type=auth&redirect_uri=" + MicrosoftThread.REDIRECT_URL, "GET", ContentType.FORM, false);
		} catch (MalformedURLException e) {
			Falcun.getInstance().log.error("Failed to get microsoft redirect url.", e);
			return pageContent;
		}
		
		WebRequestResult result;
		
		String accessTokenInternal = "";
		String refreshToken = "";
		
		try {
			result = request.connect();
			JSONObject obj = new JSONObject(result.getData());
			JSONObject req = obj.getJSONObject("request");
			accessTokenInternal = req.getString("access_token");
			refreshToken = req.getString("refresh_token");
		} catch (JSONException | NoSuchElementException | IOException e) {
			Falcun.getInstance().log.error("Failed to do internal microsoft redirect, invalid account?", e);
			return pageContent;
		}
		
		String resp = AccountUtils.getMicrosoftAuth(request, accessTokenInternal);
		
		if(resp == null) {
			return pageContent;
		}
		
		JSONObject obj;
		
		try {
			obj = new JSONObject(resp);
		} catch(JSONException e) {
			showError(resp);
			return "A problem occurred, check the client for more information.";
		}
		
		String uuid = obj.getString("id");
		String username = obj.getString("name");
		String accessToken = obj.getString("accessToken");
		
		Account acc = new AccountMicrosoft(username, uuid, accessToken, refreshToken);
		GuiScreen screen = Minecraft.getMinecraft().currentScreen;
		
		if(screen instanceof GuiAccountManager) {
			GuiAccountManager mgr = (GuiAccountManager) screen;
			mgr.altList.add(acc);
			mgr.displayErrorMessagePermanent(acc.loadUser());
		}
		
		return "You can now close this window.";
	}
	
	private void showError(String error) {
		GuiScreen screen = Minecraft.getMinecraft().currentScreen;
		
		if(screen instanceof GuiAccountManager) {
			GuiAccountManager mgr = (GuiAccountManager) screen;
			mgr.displayErrorMessagePermanent(error);
		}
	}
}