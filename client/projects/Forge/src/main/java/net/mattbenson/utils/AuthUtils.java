package net.mattbenson.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import net.mattbenson.requests.ContentType;
import net.mattbenson.requests.WebRequest;
import net.mattbenson.requests.WebRequestResult;
import net.minecraft.util.Session;

public class AuthUtils {
	public AuthUtils() {}
	
	/**
	 * Gets the authentication result from login.
	 * @param email The minecraft account email.
	 * @param password The minecraft account password.
	 * @return JSONObject, containing login result.
	 */
	public static JSONObject authUser(String email, String password) throws JSONException, NoSuchElementException, IOException {
		WebRequest request;
		
		try {
			request = new WebRequest("https://authserver.mojang.com/authenticate", "POST", ContentType.JSON, false);
		} catch (MalformedURLException e) {
			return new JSONObject().put("errorMessage", "Invalid url.");
		}
		
		JSONObject agent = new JSONObject();
		try {
			agent.put("name", "Minecraft");
			agent.put("version", 1);
		} catch (JSONException e) {
			return new JSONObject().put("errorMessage", "Invalid agent.");
		}
		
		request.setArguement("agent", agent);
		request.setArguement("username", email);
		request.setArguement("password", password);
		request.setArguement("clientToken", UUID.randomUUID().toString().replace("-", ""));
		
		WebRequestResult result = request.connect();
		
		return new JSONObject(result.getData());
	}
	
	/**
	 * Refreshes a valid access token.
	 * @param accessToken The access token received when logging in.
	 * @param clientToken The client token received when logging in.
	 * @return JSONObject, containing refresh result.
	 */
	public static JSONObject refreshUser(String accessToken, String clientToken) throws JSONException, NoSuchElementException, IOException {
		WebRequest request;
		
		try {
			request = new WebRequest("https://authserver.mojang.com/refresh", "POST", ContentType.JSON, false);
		} catch (MalformedURLException e) {
			return new JSONObject().put("errorMessage", "Invalid url.");
		}
		
		request.setArguement("accessToken", accessToken);
		request.setArguement("clientToken", clientToken);
		
		WebRequestResult result = request.connect();
		
		return new JSONObject(result.getData());
	}
	
	/**
	 * Checks if a access token is valid.
	 * @param accessToken The access token received when logging in.
	 * @param clientToken The client token received when logging in.
	 * @return JSONObject, should be empty if its valid.
	 */
	public static JSONObject isValidAccessToken(String accessToken, String clientToken) throws JSONException, NoSuchElementException, IOException {
		WebRequest request;
		
		try {
			request = new WebRequest("https://authserver.mojang.com/validate", "POST", ContentType.JSON, false);
		} catch (MalformedURLException e) {
			return new JSONObject().put("errorMessage", "Invalid url.");
		}
		
		request.setArguement("accessToken", accessToken);
		request.setArguement("clientToken", clientToken);
		
		WebRequestResult result = request.connect();
		
		if(result.getResponse() == 204) {
			return new JSONObject();
		} else if(result.getResponse() == 403) {
			return new JSONObject().put("errorMessage", "Access token is invalid.");
		}
		
		return new JSONObject(result.getData());
	}
	
	/**
	 * Invalidates access tokens generated using the account details. Result is null if the request was denied.
	 * @param email The minecraft account email.
	 * @param password The minecraft account password.
	 * @return JSONObject, should be empty if its valid.
	 */
	public static JSONObject signOutUser(String email, String password) throws JSONException, NoSuchElementException, IOException {
		WebRequest request;
		
		try {
			request = new WebRequest("https://authserver.mojang.com/signout", "POST", ContentType.JSON, false);
		} catch (MalformedURLException e) {
			return new JSONObject().put("errorMessage", "Invalid url.");
		}
		
		request.setArguement("username", email);
		request.setArguement("password", password);
		
		WebRequestResult result = request.connect();
		
		if(result.getResponse() == 204) {
			return new JSONObject();
		}
		
		return new JSONObject(result.getData());
	}
	
	/**
	 * Invalidates a access token.
	 * @param accessToken The access token received when logging in.
	 * @param clientToken The client token received when logging in.
	 * @return JSONObject, should be empty if its valid.
	 */
	public static JSONObject invalidateToken(String accessToken, String clientToken) throws JSONException, NoSuchElementException, IOException {
		WebRequest request;
		
		try {
			request = new WebRequest("https://authserver.mojang.com/invalidate", "POST", ContentType.JSON, false);
		} catch (MalformedURLException e) {
			return new JSONObject().put("errorMessage", "Invalid url.");
		}
		
		request.setArguement("accessToken", accessToken);
		request.setArguement("clientToken", clientToken);
		
		WebRequestResult result = request.connect();
		
		if(result.getResponse() == 204) {
			return new JSONObject();
		}
		
		return new JSONObject(result.getData());
	}
	
	/**
	 * Retrieves a session object, prepared using the details provided.
	 * @param username The username of the account to generate a session for.
	 * @param id The id of the account to generate a session for.
	 * @param accessToken A valid access token for the account.
	 * @return Session containing the user data provided.
	 */
	public static Session getSession(String username, String id, String accessToken) {
		return new Session(username, id, accessToken, Session.Type.MOJANG.toString());
	}
}
