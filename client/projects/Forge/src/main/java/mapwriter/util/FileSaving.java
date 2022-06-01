package mapwriter.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import mapwriter.Mw;
import mapwriter.fac.Faction;
import mapwriter.fac.Faction.Claim;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FileSaving {
	public File factionsDir = null;
	
	public Gson gson = new GsonBuilder().setPrettyPrinting().create();
	
	public FileSaving() {}
	
	public void load() {
		this.factionsDir = new File(Mw.getInstance().worldDir, "factions");
		if (!this.factionsDir.exists()) this.factionsDir.mkdir();
		else loadFactions();
	}
	
	private void loadFactions() {
		Mw.getInstance().facInput.facCoords.clear();
		Mw.getInstance().facInput.factions.clear();
		
		new Thread(new LoadThread(), "Factions Loading").start();
	}
	
	public void saveFactions() {
		new Thread(new SaveThread(), "Factions Saving").start();
	}
	
	class LoadThread implements Runnable {
		@Override
		public void run() {
			File[] facsFile = factionsDir.listFiles();
			if (facsFile == null) return;

			for (File fac : facsFile) {
				try {
					BufferedReader loader = new BufferedReader(new FileReader(fac));
					JsonObject json = (JsonObject)new JsonParser().parse(loader);
					loader.close();

					String name 	= json.get("Name").getAsString();
					String color 	= json.get("Color").getAsString();
					boolean cColor 	= json.has("CustomColor") ? json.get("CustomColor").getAsBoolean() : false;
					String image 	= json.has("Image") ? json.get("Image").getAsString() : "";
					
					// A check because I was retarded and used a json object instead of an array.
					if(json.get("Claims").isJsonObject()) {
						int amount 			= json.get("ClaimAmount").getAsInt();
						JsonObject claim 	= json.get("Claims").getAsJsonObject();

						for (int i = 0; i < amount; i++) {
							JsonObject c = claim.get("Claim " + i).getAsJsonObject();
							Mw.getInstance().facInput.addOrEditFaction(new Faction(name, color, cColor, image), c.get("x").getAsInt(), c.get("z").getAsInt());
						}
					} else {
						JsonArray claims = json.get("Claims").getAsJsonArray();

						for (int i = 0; i < claims.size(); i++) {
							JsonObject claim = claims.get(i).getAsJsonObject();
							Mw.getInstance().facInput.addOrEditFaction(new Faction(name, color, cColor, image), claim.get("x").getAsInt(), claim.get("z").getAsInt());
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			for (Faction fac : Mw.getInstance().facInput.factions.values()) {
				for (Faction.Claim claim : fac.getClaims()) {
					claim.update();
				}
			}
		}
	}
	
	class SaveThread implements Runnable {
		@Override 
		public void run() {
			File config = new File(Mw.getInstance().worldDir, "factions.cfg");
			JsonObject jConfig = new JsonObject();

			jConfig.addProperty("Regex Faction Map", FactionRegex.getRegex("map"));
			jConfig.addProperty("Regex Faction Name", FactionRegex.getRegex("show"));

			try {
				if (!config.exists()) config.createNewFile();

				PrintWriter save = new PrintWriter(new FileWriter(config));
				save.println(gson.toJson(jConfig));
				save.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			Collection<Faction> factions = new ArrayList<Faction>(Mw.getInstance().facInput.factions.values());
			
			for (Faction fac : factions) {
				try {
					File facFile = new File(factionsDir, fac.getName() + ".txt");
					if (!facFile.exists()) facFile.createNewFile();

					JsonObject jFaction = new JsonObject();
					jFaction.addProperty("Name", fac.getName());
					jFaction.addProperty("Color", fac.getColor());
					jFaction.addProperty("CustomColor", fac.hasCustomColor());
					jFaction.addProperty("Image", fac.getTexture() == null ? "" : fac.getTexture().toString());

					JsonArray jClaims = new JsonArray();

					for (Faction.Claim claim : fac.getClaims()) {
						JsonObject jClaim = new JsonObject();
						jClaim.addProperty("x", claim.getX());
						jClaim.addProperty("z", claim.getZ());
						jClaims.add(jClaim);
					}

					jFaction.add("Claims", jClaims);
					
					PrintWriter save = new PrintWriter(new FileWriter(facFile));
					save.println(gson.toJson(jFaction));
					save.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			Mw.getInstance().facInput.facCoords.clear();
			Mw.getInstance().facInput.factions.clear();
		}
	}
}