package net.mattbenson.partners;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.mattbenson.Falcun;
import net.mattbenson.requests.ContentType;
import net.mattbenson.requests.WebRequest;
import net.mattbenson.requests.WebRequestResult;
import net.minecraft.client.main.Main;

public class PartnerManager {
	private List<Partner> partners;
	
	public PartnerManager() {
		this.partners = new ArrayList<>();
		init();
	}
	
	private void init() {
		WebRequest request;
		
		try {
			request = new WebRequest(Falcun.PARTNER_URL, "GET", ContentType.NONE, false);
		} catch (MalformedURLException e) {
			Falcun.getInstance().log.error("Failed to make url for partner url.", e);
			return;
		}
		
		WebRequestResult result;
		
		try {
			result = request.connect();
		} catch (NoSuchElementException | IOException | JSONException e) {
			Falcun.getInstance().log.error("Failed to reach partner page.", e);
			return;
		}
		
		JSONObject json;
		
		try {
			json = new JSONObject(result.getData());
		} catch (JSONException e) {
			return;
		}
		
		JSONArray keys = json.names();
		int max = keys.length();
		
		for(int i = 0; i < max; i++) {
			try {
				String name = keys.getString(i);
				JSONObject entry = json.getJSONObject(name);
				partners.add(new Partner(name, entry.getString("ip")));
			} catch (JSONException e) {
				Falcun.getInstance().log.error("Failed to parse partner entry.", e);
			}
		}
	}
	
	public List<Partner> getPartners() {
		return partners;
	}
	
	public boolean isPartner(String ip) {
		for(Partner partner : partners) {
			if(partner.getIp().equalsIgnoreCase(ip)) {
				return true;
			}
		}
		
		return false;
	}
}
