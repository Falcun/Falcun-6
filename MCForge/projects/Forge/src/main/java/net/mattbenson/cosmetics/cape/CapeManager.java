package net.mattbenson.cosmetics.cape;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

import org.json.JSONException;
import org.lwjgl.opengl.GL11;

import net.mattbenson.Falcun;
import net.mattbenson.gui.menu.pages.cosmetics.CapeType;
import net.mattbenson.requests.ContentType;
import net.mattbenson.requests.WebRequest;
import net.mattbenson.requests.WebRequestResult;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

public class CapeManager {
	public List<Cape> ownedCapes;
	public ModelRenderer bipedCloakShoulders;
	public ModelRenderer miniBipedCloak;
	public ModelRenderer miniBipedCloakShoulders;
	private ArrayList<Cape> capes;
	private CapeSettings settings;
	private HashMap<String, Integer> userCapes;
	private List<PlayerHandler> players;
	public final CapeLookupThread capeLookupThread;
	private final CapeRenderer capeRenderer;

	public CapeManager() {
		loadSettings();
		players = new ArrayList();
		userCapes = new HashMap<String,Integer>();
	
		capeLookupThread = new CapeLookupThread(this);
		this.capeLookupThread.start();
		
		this.capeRenderer = new CapeRenderer();
		
		ownedCapes = new CopyOnWriteArrayList<>();
		capes = new ArrayList();
		capes.add(new Cape(0,"None","none", "", false, 0));
		//1 STAFF

		capes.add(new Cape(2,"Red Beta","cape_falcun_redbeta", Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/FalcunBetaCape.png"));
		capes.add(new Cape(3,"Dark Beta","cape_falcun_darkbeta", Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/FalcunBetaCape2.png"));
		capes.add(new Cape(4,"The Hut","cape_falcun_hut", Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/hut.png"));
		capes.add(new Cape(5,"Purge","cape_falcun_purge",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/purge.png"));
		capes.add(new Cape(6,"Crystal","cape_falcun_crystal",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/crystal.png"));
		//7 BOOSTER
		capes.add(new Cape(8,"Fire", "cape_falcun_fire", Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/firecape/firecape-#.png", true, 23));
		capes.add(new Cape(9,"Lightning", "cape_falcun_lightning", Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/lightning/lightning-#.png", true, 33));
		capes.add(new Cape(10,"Sky", "cape_falcun_sky", Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/sky/sky-#.png", true, 89));
		capes.add(new Cape(11,"Wild", "cape_falcun_wild", Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/wild/wild-#.png", true, 29));
		capes.add(new Cape(12,"Nightmare","cape_falcun_nightmare",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/nightmare.png"));
		capes.add(new Cape(13,"ImNutty","cape_faction_imnutty",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/imnutty.png"));
		capes.add(new Cape(14,"Origin","cape_faction_origin",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/origin.png"));
		capes.add(new Cape(15,"RIP","cape_faction_rip",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/rip.png"));
		capes.add(new Cape(16,"Spook","cape_faction_spook",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/spook.png"));
		capes.add(new Cape(17,"Triad","cape_faction_triad",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/triad.png"));
		capes.add(new Cape(18,"Pirates","cape_faction_pirates",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/pirates.png"));
		capes.add(new Cape(19,"Coitus","cape_faction_coitus",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/coitus.png"));
		capes.add(new Cape(20,"Karma","cape_faction_karma",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/karma.png"));
		capes.add(new Cape(21,"Hydra","cape_faction_hydra",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/hydra.png"));
		capes.add(new Cape(22,"Burritos","cape_faction_burritos",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/burritos.png"));
		capes.add(new Cape(23,"Joker","cape_faction_joker",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/joker.png"));
		capes.add(new Cape(24,"Abzya","cape_faction_abzya",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/abzya.png"));
		capes.add(new Cape(25,"Acid","cape_faction_acid",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/acid.png"));
		capes.add(new Cape(26,"Turtles","cape_faction_turtles",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/turtles.png"));
		capes.add(new Cape(27,"Hentai","cape_faction_hentai",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/hentai.png"));
		capes.add(new Cape(28,"Gamers","cape_faction_gamers",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/gamers.png"));
		capes.add(new Cape(29,"Smog","cape_faction_smog",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/smog.png"));
		capes.add(new Cape(30,"Shield", "cape_faction_shield", Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/shield.png"));
		capes.add(new Cape(31,"Peppa","cape_faction_peppa",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/peppa.png"));
		capes.add(new Cape(32,"Rawri","cape_faction_rawri",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/rawri.png"));
		capes.add(new Cape(33,"Exotic","cape_server_exotic",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/partnerships/exotic.png"));
		capes.add(new Cape(34,"Infused", "cape_server_infused", Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/infused/infused-#.png", true, 32));
		capes.add(new Cape(35,"Wizzy","cape_media_wizzy",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/partnerships/wizzy.png"));
		capes.add(new Cape(36,"Hxroic","cape_media_hxroic",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/partnerships/hxroic.png"));
		capes.add(new Cape(37,"Astro", "cape_falcun_astro", Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/astro/astro-#.png", true, 21));
		capes.add(new Cape(39,"MineHeroes", "cape_server_mineheroes", Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/mineheroes/mineheroes-#.png", true, 10));
		capes.add(new Cape(40,"Kush","cape_faction_kush",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/kush.png"));
		capes.add(new Cape(41,"Bamboo","cape_faction_bamboo",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/bamboo.png"));
		capes.add(new Cape(42,"Visa","cape_faction_visa",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/visa.png"));
		capes.add(new Cape(43,"Ghosts","cape_faction_ghosts",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/ghosts.png"));
		capes.add(new Cape(44,"Moon","cape_falcun_moon",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/moon.png"));
		capes.add(new Cape(45,"Forest","cape_falcun_forest",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/forest.png"));
		capes.add(new Cape(46,"Rose","cape_falcun_rose",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/rose.png"));
		capes.add(new Cape(47,"FactionsHeaven", "cape_server_factionsheaven", Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factionsheaven/facheaven_#.png", true, 24));
		capes.add(new Cape(48,"Pollitos","cape_faction_pollitos",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/pollitos.png"));
		capes.add(new Cape(49,"Wookiees","cape_faction_wookiees",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/wookiees.png"));
		capes.add(new Cape(50,"Trauma","cape_faction_trauma",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/trauma.png"));
		capes.add(new Cape(51,"Rangas","cape_faction_rangas",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/rangas.png"));
		capes.add(new Cape(52,"ElektraMC","cape_server_elektramc",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/partnerships/elektramc.png"));
		capes.add(new Cape(53,"Top","cape_faction_top",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/top.png"));
		capes.add(new Cape(54,"999","cape_faction_999",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/999.png"));
		capes.add(new Cape(55,"FalloutPVP","cape_server_falloutpvp",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/partnerships/falloutpvp.png"));
		capes.add(new Cape(56,"VisePvP","cape_server_visepvp",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/partnerships/visepvp.png"));
		capes.add(new Cape(57,"Christmas", "cape_falcun_christmas", Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/christmas/christmas-#.png", true, 20));
		capes.add(new Cape(58,"Breeze","cape_faction_breeze",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/breeze.png"));
		capes.add(new Cape(59,"SlatePlays","cape_media_slateplays",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/partnerships/slateplays.png"));
		capes.add(new Cape(60,"DigitalDash","cape_faction_digitaldash",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/digitaldash.png"));
		capes.add(new Cape(61,"OlympusMC", "cape_server_olympusmc", Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/olympusmc/olympusmc-#.png", true, 28));
		capes.add(new Cape(63,"Supreme","cape_falcun_supreme",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/supreme.png"));
		capes.add(new Cape(64,"ReaperMC","cape_server_reaper",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/partnerships/reapermc.png"));
		capes.add(new Cape(65,"HOF","cape_faction_hof",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/HOF.png"));
		capes.add(new Cape(66,"FalcunStock","cape_falcun_falcunstock",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/falcunstock.png"));
		capes.add(new Cape(67,"Dexter113","cape_media_dexter",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/partnerships/dexter.png"));
		capes.add(new Cape(68,"BeepBeep","cape_faction_beepbeep",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/beepbeep.png"));
		capes.add(new Cape(69,"Milk","cape_faction_milk",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/milk.png"));
		capes.add(new Cape(70,"ViciousPvP","cape_faction_viciouspvp",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/partnerships/viciouspvp.png"));
		capes.add(new Cape(71,"Jasmine", "cape_server_jasmine", Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/jasmine/jasmine-#.png", true, 21));
		capes.add(new Cape(72,"Cloud","cape_falcun_cloud",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/cloud.png"));
		capes.add(new Cape(73,"Easter", "cape_falcun_easter", Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/easter/easter-#.png", true, 16));
		capes.add(new Cape(74,"Dragons","cape_faction_dragons",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/dragons.png"));
		capes.add(new Cape(75,"WheelChair","cape_faction_wheelchair",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/wheelchair.png"));
		capes.add(new Cape(76,"Ice","cape_faction_ice",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/ice.png"));
		capes.add(new Cape(77,"PPGang","cape_faction_ppgang",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/ppgang.png"));
		capes.add(new Cape(78,"Nightmare","cape_faction_nightmare",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/nightmare.png"));
		capes.add(new Cape(79,"Red","cape_falcun_red",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/red.png"));
		capes.add(new Cape(80,"Japanese","cape_falcun_japanese",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/japanese.png"));
		capes.add(new Cape(81,"Red","cape_falcun_red",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/red.png"));
		capes.add(new Cape(82,"Halloween", "cape_falcun_halloween", Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/halloween/halloween-#.png", true, 34));
		capes.add(new Cape(85,"Suicide","cape_faction_suicide",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/factions/suicide.png"));
		capes.add(new Cape(86,"ChaoticPVP","cape_server_chaoticpvp",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/chaoticpvp.png"));
		
		capes.add(new Cape(100,"Custom","cape_falcun_custom",Minecraft.getMinecraft().mcDataDir + "/falcunassets/capes/supreme.png"));
		
		updateOwnedCapes();
	}
	
	public static String readStringFromURL(String requestURL) throws IOException
	{
	    try (Scanner scanner = new Scanner(new URL(requestURL).openStream(),
	            StandardCharsets.UTF_8.toString()))
	    {
	        scanner.useDelimiter("\\A");
	        return scanner.hasNext() ? scanner.next() : "";
	    }
	}

	
    private static String downloadWebPage(String url) throws IOException {

        StringBuilder result = new StringBuilder();
        String line;

        // add user agent 
        URLConnection urlConnection = new URL(url).openConnection();
        urlConnection.addRequestProperty("User-Agent", "Mozilla");
        urlConnection.setReadTimeout(5000);
        urlConnection.setConnectTimeout(5000);

        try (InputStream is = new URL(url).openStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            while ((line = br.readLine()) != null) {
                result.append(line);
            }

        }

        return result.toString();

    }
    

	public void updateOwnedCapes() {
		new Thread(() -> {
			try {
				
				WebRequest request = new WebRequest("https://falcun.net/old/getcapes.php?mcname=" + Minecraft.getMinecraft().getSession().getUsername(), "GET", ContentType.JSON, false);
				WebRequestResult result = request.connect();
				for (int i = 0; i < 5; ++i) {
					System.out.println("Cape Data:\n" + result.getData());	
				}

				String[] list = result.getData().split(",");

				for (String str : list){
					Cape toAdd = getCape(str);
					if (toAdd != null && !ownedCapes.contains(toAdd)){
						ownedCapes.add(toAdd);
					}
				}
				

			
				ownedCapes.sort(new Comparator<Cape>() {
					@Override
					public int compare(Cape o1, Cape o2) {
						String name1 = o1.getName().toUpperCase().replace("_", " ");
						String name2 = o2.getName().toUpperCase().replace("_", " ");
					     return name1.compareTo(name2);
					}
		        	
		        });
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	public void loadSettings() {
		boolean isEnabled = true;
		CapeType type = CapeType.NORMAL;
		if(!Falcun.getInstance().MAIN_DIR.exists()) {
			Falcun.getInstance().MAIN_DIR.mkdir();
		}
		File capeFile = new File(Falcun.getInstance().MAIN_DIR + File.separator + "capesettings.txt");
		if(!capeFile.exists()) {
			try {
				capeFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				BufferedReader bf = new BufferedReader(new FileReader(capeFile));
				String line;
				while ((line = bf.readLine()) != null) {
					String[] args = line.split("::");
					String name = args[0];
					if(name.equalsIgnoreCase("enabled")) {
						isEnabled = Boolean.parseBoolean(args[1]);
					}
					if(name.equalsIgnoreCase("type")) {
						type = CapeType.valueOf(args[1]);
					}
				}
				bf.close();
			}
			catch(Exception ex) {

			}
		}
		this.settings = new CapeSettings(isEnabled, type == null ? CapeType.NORMAL : type); 
		saveSettings();
	}

	public void saveSettings() {
		if(!Falcun.getInstance().MAIN_DIR.exists()) {
			Falcun.getInstance().MAIN_DIR.mkdir();
		}
		File capeFile = new File(Falcun.getInstance().MAIN_DIR + File.separator + "capesettings.txt");
		if(!capeFile.exists()) {
			try {
				capeFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			capeFile.delete();
			try {
				capeFile.createNewFile();
			}
			catch(Exception ex) {

			}
		}

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(capeFile));
			StringBuilder builder1 = new StringBuilder();
			builder1.append("Enabled");
			builder1.append("::");
			builder1.append(getSettings().isEnabled);
			builder1.append("\n");
			builder1.append("Type");
			builder1.append("::");
			builder1.append(getSettings().getType() == null ? CapeType.NORMAL.toString() : getSettings().getType().toString());
			writer.write(builder1.toString());
			writer.close();
		}
		catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	public CapeSettings getSettings() {
		return this.settings;
	}

	public class CapeSettings {
		private boolean isEnabled;
		private CapeType capeType;
		public CapeSettings(boolean isEnabled, CapeType capeType) {
			this.isEnabled = isEnabled;
			this.capeType = capeType;
		}

		public boolean getEnabled() {
			return this.isEnabled;
		}

		public CapeType getType() {
			return this.capeType;
		}

		public void setEnabled(boolean enabled) {
			this.isEnabled = enabled;
		}

		public void setType(CapeType type) {
			this.capeType = type;
		}
	}

	public CapeRenderer getCapeRenderer() {
		return this.capeRenderer;
	}

	public ArrayList<Cape> getCapes() {
		return this.capes;
	}

	public List<PlayerHandler> getPlayerHandler() {
		return this.players;
	}

	public PlayerHandler getFromName(String name) {
		for(PlayerHandler p : players) {
			if(p.getName().equalsIgnoreCase(name)) {
				return p;
			}
		}
		return null;
	}

	public PlayerHandler getFromUUID(String uuid) {
		for(PlayerHandler p : players) {
			if(p.getPlayerUUID().equalsIgnoreCase(uuid)) {
				return p;
			}
		}
		return null;
	}

	public void removeByName(String name) {
		for(int i = 0; i < players.size(); i++) {
			if(players.get(i).getName().equalsIgnoreCase(name)) {
				players.remove(i);
			}
		}
	}

	public void removeByUUID(String uuid) {
		for(int i = 0; i < players.size(); i++) {
			if(players.get(i).getPlayerUUID().equalsIgnoreCase(uuid)) {
				players.remove(i);
			}
		}
	}

	public HashMap<String, Integer> getUserCapes() {
		return this.userCapes;
	}

	public Cape getCape(String name) {
		for(Cape cape : capes) {
			if(cape.getName().equalsIgnoreCase(name) || cape.getCapeName().equalsIgnoreCase(name)) {
				return cape;
			}
		}
		return null;
	}
	public Cape getCapeByID(int id) {
		for(Cape cape : capes) {
			if(cape.getID() == id) {
				return cape;
			}
		}
		return null;
	}

	public void initCapes() {
		if (bipedCloakShoulders != null && miniBipedCloakShoulders != null && miniBipedCloak != null) {
			return;
		}

		if (Minecraft.getMinecraft() == null || Minecraft.getMinecraft().getRenderManager() == null) {
			return;
		}

		ModelPlayer modelPlayer = (ModelPlayer) Minecraft.getMinecraft().getRenderManager().getSkinMap().get("default").getMainModel();

		this.bipedCloakShoulders = new ModelRenderer(modelPlayer, 0, 0);
		this.bipedCloakShoulders.addBox(-5.0F, -1.0F, -2.0F, 2, 1, 5, 0.0F);
		this.bipedCloakShoulders.addBox(3.0F, -1.0F, -2.0F, 2, 1, 5, 0.0F);
		this.miniBipedCloak = new ModelRenderer(modelPlayer, 0, 0);
		this.miniBipedCloak.addBox(-3.0F, 0.0F, -1.0F, 6, 10, 1, 0.0F);
		this.miniBipedCloakShoulders = new ModelRenderer(modelPlayer, 0, 0);
		this.miniBipedCloakShoulders.addBox(-3.0F, -1.0F, -2.0F, 1, 1, 5, 0.0F);
		this.miniBipedCloakShoulders.addBox(2.0F, -1.0F, -2.0F, 1, 1, 5, 0.0F);
	}
	
	public ResourceLocation getLocationCape(AbstractClientPlayer player)
	{
		if(Falcun.capeManager.getUserCapes().containsKey(player.getUniqueID().toString()) && Falcun.capeManager.getSettings().getEnabled()) {
			int nom = Falcun.capeManager.getUserCapes().get(player.getUniqueID().toString());
			if (nom == -2 || nom == 0) {
				player.locationOfCape = null;
			}
			else if (nom == 6 || nom == 12) {
				if(Falcun.capeManager.getFromUUID(player.getUniqueID() +"") == null) {
					Falcun.capeManager.getPlayerHandler().add(new PlayerHandler(player.getNameClear(),player.getUniqueID() +""));
				}
				PlayerHandler playerHandler = Falcun.capeManager.getFromUUID(player.getUniqueID() +"");
				if(playerHandler.getCapeLocation() == null) {
					DownloadCape.downloadCape(playerHandler);
				} else {
					player.locationOfCape = playerHandler.getCapeLocation();
				}
			} else if (nom == 100) {
				if(Falcun.capeManager.getFromUUID(player.getUniqueID() +"") == null) {
					Falcun.capeManager.getPlayerHandler().add(new PlayerHandler(player.getNameClear(),player.getUniqueID() +""));
				}
				PlayerHandler playerHandler = Falcun.capeManager.getFromUUID(player.getUniqueID() +"");
				if(playerHandler.getCapeLocation() == null) {
					DownloadCustomCape.downloadCape(playerHandler);
				} else {
					player.locationOfCape = playerHandler.getCapeLocation();
				}
			} 
			else {
				Cape cape = Falcun.capeManager.getCapeByID(nom);
				if(cape != null) {
					if(cape.isAnimated()) {
						if(Falcun.capeManager.getFromUUID(player.getUniqueID() +"") == null) {
							Falcun.capeManager.getPlayerHandler().add(new PlayerHandler(player.getNameClear(),player.getUniqueID() +""));
						}
						PlayerHandler playerHandler = Falcun.capeManager.getFromUUID(player.getUniqueID() +"");
						player.locationOfCape = Falcun.capeManager.getFrame(cape, playerHandler);
					} else {
						player.locationOfCape = cape.getFrame(0);
					}
				}
			}
		}
		
		if (player.locationOfCape != null)
		{
			return player.locationOfCape;
		}
		else
		{
			NetworkPlayerInfo networkplayerinfo = player.getPlayerInfo();
			return networkplayerinfo == null ? null : networkplayerinfo.getLocationCape();
		}
	}
	
	public ResourceLocation getFrame(Cape cape, PlayerHandler playerHandler) {
		final long time = System.currentTimeMillis();
		if (time > playerHandler.lastFrameTime + playerHandler.capeInterval ) {
			final int currentFrameNo = (playerHandler.lastFrame + 1 > cape.getTotal() - 1) ? 0 : (playerHandler.lastFrame + 1);
			playerHandler.lastFrame = currentFrameNo;
			playerHandler.lastFrameTime = time;
			return cape.getFrame(currentFrameNo);
		} 
		return cape.getFrame(playerHandler.lastFrame);
	}

	public static void renderCape(RenderPlayer playerRenderer, AbstractClientPlayer entitylivingbaseIn, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
	{
		boolean flag = entitylivingbaseIn.hasPlayerInfo() && !entitylivingbaseIn.isInvisible() && entitylivingbaseIn.isWearing(EnumPlayerModelParts.CAPE) && entitylivingbaseIn.getLocationCape() != null;
		ResourceLocation cape = entitylivingbaseIn.getLocationCape();
		if (flag)
		{
			if((Falcun.getInstance().capeManager.getSettings().getType() == CapeType.NORMAL || Falcun.getInstance().capeManager.getSettings().getType() == CapeType.SHOULDERS)) {
				if(cape == null) {
					return;
				}
				
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.pushMatrix();
				Minecraft.getMinecraft().getTextureManager().bindTexture(cape);
				GlStateManager.translate(0.0F, 0.0F, 0.125F);
				double d0 = entitylivingbaseIn.prevChasingPosX + (entitylivingbaseIn.chasingPosX - entitylivingbaseIn.prevChasingPosX) * (double)partialTicks - (entitylivingbaseIn.prevPosX + (entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX) * (double)partialTicks);
				double d1 = entitylivingbaseIn.prevChasingPosY + (entitylivingbaseIn.chasingPosY - entitylivingbaseIn.prevChasingPosY) * (double)partialTicks - (entitylivingbaseIn.prevPosY + (entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY) * (double)partialTicks);
				double d2 = entitylivingbaseIn.prevChasingPosZ + (entitylivingbaseIn.chasingPosZ - entitylivingbaseIn.prevChasingPosZ) * (double)partialTicks - (entitylivingbaseIn.prevPosZ + (entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ) * (double)partialTicks);
				float f = entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks;
				
				double max = 1.5;
				
				if(d0 > max) {	
					d0 = max;	
				} else if(d0 < -max) {	
					d0 = -max;	
				}	

				if(d1 > max) {	
					d1 = max;	
				} else if(d1 < -max) {	
					d1 = -max;	
				}	

				if(d2 > max) {	
					d2 = max;	
				} else if(d2 < -max) {	
					d2 = -max;	
				}
				
				double d3 = (double)MathHelper.sin(f * (float)Math.PI / 180.0F);
				double d4 = (double)(-MathHelper.cos(f * (float)Math.PI / 180.0F));
				float f1 = (float)d1 * 10.0F;
				f1 = MathHelper.clamp_float(f1, -6.0F, 32.0F);
				float f2 = (float)(d0 * d3 + d2 * d4) * 100.0F;
				float f3 = (float)(d0 * d4 - d2 * d3) * 100.0F;

				if (f2 < 0.0F)
				{
					f2 = 0.0F;
				}

				if (f2 > 165.0F)
				{
					f2 = 165.0F;
				}

				float f4 = entitylivingbaseIn.prevCameraYaw + (entitylivingbaseIn.cameraYaw - entitylivingbaseIn.prevCameraYaw) * partialTicks;
				f1 = f1 + MathHelper.sin((entitylivingbaseIn.prevDistanceWalkedModified + (entitylivingbaseIn.distanceWalkedModified - entitylivingbaseIn.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f4;

				if (entitylivingbaseIn.isSneaking())
				{
					f1 += 25.0F;
					GlStateManager.translate(0.0F, 0.05F, -0.0178F);
				}

				GlStateManager.rotate(6.0F + f2 / 2.0F + f1, 1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(f3 / 2.0F, 0.0F, 0.0F, 1.0F);
				GlStateManager.rotate(-f3 / 2.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				playerRenderer.getMainModel().renderCape(0.0625F);
				GlStateManager.popMatrix();
				if(Falcun.getInstance().capeManager.getSettings().getType() == CapeType.SHOULDERS) {
					GL11.glPushMatrix();

					if (entitylivingbaseIn.isSneaking())
					{
						GL11.glTranslatef(0.0F, 0.2F, 0.0F);
						GL11.glRotatef(10.0F, 1.0F, 0.0F, 0.0F);
					}

					GL11.glRotatef(-f3 / 2.0F, 0.0F, 1.0F, 0.0F);

					Falcun.getInstance().capeManager.renderCloakShoulders(0.0625F);
					GL11.glPopMatrix();
				}
			} else {
				if(cape == null) {
					return;
				}
				
				GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
				GlStateManager.pushMatrix();
				Minecraft.getMinecraft().getTextureManager().bindTexture(cape);
				GlStateManager.translate(0.0F, 0.0F, 0.125F);
				double d0 = entitylivingbaseIn.prevChasingPosX + (entitylivingbaseIn.chasingPosX - entitylivingbaseIn.prevChasingPosX) * (double)partialTicks - (entitylivingbaseIn.prevPosX + (entitylivingbaseIn.posX - entitylivingbaseIn.prevPosX) * (double)partialTicks);
				double d1 = entitylivingbaseIn.prevChasingPosY + (entitylivingbaseIn.chasingPosY - entitylivingbaseIn.prevChasingPosY) * (double)partialTicks - (entitylivingbaseIn.prevPosY + (entitylivingbaseIn.posY - entitylivingbaseIn.prevPosY) * (double)partialTicks);
				double d2 = entitylivingbaseIn.prevChasingPosZ + (entitylivingbaseIn.chasingPosZ - entitylivingbaseIn.prevChasingPosZ) * (double)partialTicks - (entitylivingbaseIn.prevPosZ + (entitylivingbaseIn.posZ - entitylivingbaseIn.prevPosZ) * (double)partialTicks);
				
				double max = 1.5;
				
				if(d0 > max) {	
					d0 = max;	
				} else if(d0 < -max) {	
					d0 = -max;	
				}	

				if(d1 > max) {	
					d1 = max;	
				} else if(d1 < -max) {	
					d1 = -max;	
				}	

				if(d2 > max) {	
					d2 = max;	
				} else if(d2 < -max) {	
					d2 = -max;	
				}

				float f = entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks;
				double d3 = (double)MathHelper.sin(f * (float)Math.PI / 180.0F);
				double d4 = (double)(-MathHelper.cos(f * (float)Math.PI / 180.0F));
				float f1 = (float)d1 * 10.0F;
				f1 = MathHelper.clamp_float(f1, -6.0F, 32.0F);
				float f2 = (float)(d0 * d3 + d2 * d4) * 100.0F;
				float f3 = (float)(d0 * d4 - d2 * d3) * 100.0F;

				float f4 = entitylivingbaseIn.prevCameraYaw + (entitylivingbaseIn.cameraYaw - entitylivingbaseIn.prevCameraYaw) * partialTicks;
				f1 = f1 + MathHelper.sin((entitylivingbaseIn.prevDistanceWalkedModified + (entitylivingbaseIn.distanceWalkedModified - entitylivingbaseIn.prevDistanceWalkedModified) * partialTicks) * 6.0F) * 32.0F * f4;

				if (entitylivingbaseIn.isSneaking())
				{
					f1 += 25.0F;
				}

				float med = 6.0F + f2 / 2.0F + f1;
				med = MathHelper.clamp_float(med, 6.0F, 60.0F);

				GlStateManager.rotate(med, 1.0F, 0.0F, 0.0F);
				//GlStateManager.rotate(f3 / 2.0F, 0.0F, 0.0F, 1.0F);
				//GlStateManager.rotate(-f3 / 2.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				GlStateManager.translate(0.0F, 0.0F, 0.225F);
				//this.playerRenderer.getMainModel().renderCape(0.0625F);          
				GlStateManager.translate(0.0F, 0.0F, -0.3F);
				if (entitylivingbaseIn.isSneaking()) {
					GlStateManager.translate(0.0F, 0.12F, 0.05F);
				}

				  max = Math.max(Math.min(0.0f, f1), -3.0f);
                  final float min = Math.min(f3 + f1, 90.0f);
				
				//Apply custom cape bend animation
				Falcun.getInstance().capeManager.getCapeRenderer().applyAnimation(entitylivingbaseIn);
				Falcun.getInstance().capeManager.getCapeRenderer().render(0.0625F);

				GlStateManager.popMatrix();
				if(Falcun.getInstance().capeManager.getSettings().getType() == CapeType.BEND_AND_SHOULDERS) {
					GL11.glPushMatrix();

					if (entitylivingbaseIn.isSneaking())
					{
						GL11.glTranslatef(0.0F, 0.2F, 0.0F);
						GL11.glRotatef(10.0F, 1.0F, 0.0F, 0.0F);
					}

					GL11.glRotatef(-f3 / 2.0F, 0.0F, 1.0F, 0.0F);

					Falcun.getInstance().capeManager.renderCloakShoulders(0.0625F);
					GL11.glPopMatrix();
				}
			}
		}
	}

	public void renderCloakShoulders(float p_78111_1_)
	{
		this.bipedCloakShoulders.render(p_78111_1_);
	}    	
}
