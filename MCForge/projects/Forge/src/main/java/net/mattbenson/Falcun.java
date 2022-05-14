package net.mattbenson;

import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.nio.charset.Charset;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;

import mchorse.emoticons.Emoticons;
import mchorse.emoticons.capabilities.CapabilitiesHandler;
import mchorse.emoticons.capabilities.cosmetic.Cosmetic;
import mchorse.emoticons.capabilities.cosmetic.CosmeticStorage;
import mchorse.emoticons.capabilities.cosmetic.ICosmetic;
import mchorse.emoticons.client.EmoteKeys;
import mchorse.emoticons.client.EntityModelHandler;
import mchorse.emoticons.commands.CommandEmote;
import mchorse.emoticons.common.emotes.Emote;
import mchorse.emoticons.common.emotes.Emotes;
import mchorse.emoticons.skin_n_bones.api.animation.Animation;
import mchorse.emoticons.skin_n_bones.api.animation.AnimationManager;
import mchorse.emoticons.skin_n_bones.api.animation.AnimationManager.AnimationEntry;
import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorConfig;
import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorConfig.AnimatorConfigEntry;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJLoader;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJLoader.BOBJData;
import net.mattbenson.config.ConfigManager;
import net.mattbenson.console.ConsoleManager;
import net.mattbenson.cosmetics.bandana.BandanaManager;
import net.mattbenson.cosmetics.cape.CapeManager;
import net.mattbenson.cosmetics.crates.CrateManager;
import net.mattbenson.cosmetics.emotes.EmoteSettings;
import net.mattbenson.cosmetics.emotes.RKOCommand;
import net.mattbenson.discord.DiscordRPC;
import net.mattbenson.events.EventBus;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.MinecraftInitEvent;
import net.mattbenson.events.types.forge.FMLInitializationEvent;
import net.mattbenson.events.types.forge.FMLPostInitializationEvent;
import net.mattbenson.events.types.forge.FMLPreInitializationEvent;
import net.mattbenson.events.types.world.WorldLoadEvent;
import net.mattbenson.file.FileHandler;
import net.mattbenson.fonts.Fonts;
import net.mattbenson.gui.CustomSplashScreen;
import net.mattbenson.gui.hud.HUDManager;
import net.mattbenson.input.KeybindManager;
import net.mattbenson.macros.MacroManager;
import net.mattbenson.modules.ModuleManager;
import net.mattbenson.modules.types.fpssettings.cruches.EntityCulling;
import net.mattbenson.network.NetworkingClient;
import net.mattbenson.notifications.NotificationManager;
import net.mattbenson.partners.PartnerManager;
import net.mattbenson.playerlocations.LocationManager;
import net.mattbenson.render.RenderLightmap;
import net.mattbenson.requests.ContentType;
import net.mattbenson.requests.WebRequest;
import net.mattbenson.requests.WebRequestResult;
import net.mattbenson.schemshare.SchemShareManager;
import net.mattbenson.utils.AssetUtils;
import net.mattbenson.utils.LanguageUtils;
import net.mattbenson.utils.NetworkUtils;
import net.mattbenson.utils.legacy.LessArrayLists;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandHandler;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.optifine.CustomSky;

public class Falcun {
	public static final boolean DOWNLOADER_ENABLE = true;
	
	public static final String VERSION = "5.3.2";
	
	public static final boolean OVERRIDE_WITH_MIXINS = true;
	public static final String MC_VERSION = "1.8.9";
	
	public static final File MAIN_DIR = Paths.get(Minecraft.getMinecraft().mcDataDir.getAbsolutePath(), "Falcun").toFile();
	public static final File ASSETS_DIR = Paths.get(Minecraft.getMinecraft().mcDataDir.getAbsolutePath(), "falcunassets").toFile();
	public static final File CONFIGS_DIR = Paths.get(MAIN_DIR.getAbsolutePath(), "configs").toFile();
	public static final File ACCOUNTS_DIR = Paths.get(MAIN_DIR.getAbsolutePath(), "accounts").toFile();
	
	public static final String PROFILES_URL = "https://falcun.net/v5/share";
	public static final String PARTNER_URL = "https://falcun.net/launcher/partners.json";
	public static final String MICROSOFT_LOGIN_CLIENT_ID = "477fe4e3-c799-418f-9865-633f611668d8";
	
	private static Falcun instance;
	public static EventBus EVENT_BUS;
	public static DiscordRPC rpc;
	
	public static List<Emote> ownedEmotes;
	
	public Logger log = LogManager.getLogger("Falcun");
	
	public ConfigManager configManager;
	public NotificationManager notificationManager;
	public ModuleManager moduleManager;
	public HUDManager hudManager;
	public MacroManager macroManager;
	public ConsoleManager consoleManager;
	public net.mattbenson.hud.HUDManager newHudManager;
	public CrateManager crateManager;
	public LanguageUtils languageUtils;
	public CommandHandler commandHandler;
	
	/* Legacy */
	public BandanaManager bandanaManager;
	public static CapeManager capeManager;
	public LocationManager locationManager;
	public SchemShareManager schemShareManager;
	
	public static Emoticons emoticons;
	public static EmoteSettings emoteSettings;
	/* Classes containing edits:
	 * GuiButton.java, original: GuiButtonOriginal.java
	 * GuiOptionSlider.java, original: GuiOptionSliderOriginal.java
	 * GuiTextField.java, original: GuiTextFieldOriginal.java
	 * LayerCape.java
	 * AbstractClientPlayer.java
	 * TexturedQuad.java
	 * ModelRenderer.java
	 * RenderPlayer.java
	 * I18n.java
	 * Entity.java
	 * DynamicTexture.java
	 * GuiPlayerTabOverlay.java
	 */
	
	public static String partnerContent;
	
	public String lastServer = "";
	
	public Falcun() {
		instance = this;
		
		EVENT_BUS = new EventBus();
		EVENT_BUS.register(this);

		
		EVENT_BUS.register(new CustomSplashScreen());
		
		EVENT_BUS.register(new KeybindManager());
		
		if(!MAIN_DIR.exists()) {
			MAIN_DIR.mkdir();
		}
		
		if(!ACCOUNTS_DIR.exists()) {
			ACCOUNTS_DIR.mkdir();
		}
	}
	
	private void postInit() {
		Fonts.setupFonts();
		
		languageUtils = new LanguageUtils();
		configManager = new ConfigManager(CONFIGS_DIR);
		notificationManager = new NotificationManager();
		moduleManager = new ModuleManager();
		hudManager = new HUDManager();
		macroManager = new MacroManager();
		consoleManager = new ConsoleManager();
		newHudManager = new net.mattbenson.hud.HUDManager();
		crateManager = new CrateManager();
		
		capeManager = new CapeManager();
		capeManager.initCapes();
		
		bandanaManager = new BandanaManager();
		emoteSettings = new EmoteSettings();
		locationManager = new LocationManager();
		schemShareManager = new SchemShareManager();
		
		EVENT_BUS.register(hudManager);
		EVENT_BUS.register(macroManager);
		EVENT_BUS.register(consoleManager);
		EVENT_BUS.register(newHudManager);
		EVENT_BUS.register(crateManager);
		EVENT_BUS.register(locationManager);
		EVENT_BUS.register(schemShareManager);
		
		commandHandler = new ClientCommandHandler();
		
		configManager.postInit();
		new NetworkingClient();
		NetworkUtils.initUpdateThread();
		
		EVENT_BUS.post(new FMLPreInitializationEvent());
		EVENT_BUS.post(new FMLInitializationEvent());
		EVENT_BUS.post(new FMLPostInitializationEvent());
		
		partnerContent = getURL("https://falcun.net/launcher/partners.json").trim();
		
		rpc = new DiscordRPC(973938829209772082L);
		rpc.loadRPC();
	
		emoticons = new Emoticons();
		
		/* Load emote keys configuration */
		File file = new File(Falcun.emoticons.proxy.configFolder, "keys.json");
		Falcun.emoticons.proxy.keys = EmoteKeys.fromFile(file);

		if (Falcun.emoticons.proxy.keys == null) {
			Falcun.emoticons.proxy.keys = new EmoteKeys();
			EmoteKeys.toFile(Falcun.emoticons.proxy.keys, file);
		}
		
		File PATH = new File(Minecraft.getMinecraft().mcDataDir + "/hud/");
		
		if(!PATH.exists()) {
			PATH.mkdirs();
		}
	}
	
	@SubscribeEvent
	public void onSpawn(WorldLoadEvent event) {
		CustomSky.update();
		System.out.println("ran");
	}
	
	@SubscribeEvent
	public void onMinecraftInit(MinecraftInitEvent event) {
		
        MinecraftForge.EVENT_BUS.register(EntityCulling.INSTANCE);
		
		String hwid = "";
		String email = "";
		
		try {
			hwid = getHWID();
		} catch (NoSuchAlgorithmException | IOException e) {
			log.error("Failed to grab HWID.", e);
			showPopup("We failed to grab your computer identifier, try again later or contact an developer");
			event.setCancelled(true);
			return;
		}
		
		try {
			email = getEmail();
			
			if(email.isEmpty()) {
				showPopup("Please enter email in folder: .minecraft/Falcun/email.txt.");
				event.setCancelled(true);
				return;
			}
		} catch (IOException e) {
			log.error("Failed to grab email.", e);
			showPopup("We failed to grab your email, try again later or contact an developer");
			event.setCancelled(true);
			return;
		}
		
		try {
			email = URLEncoder.encode(email, "UTF-8");
			hwid = URLEncoder.encode(hwid, "UTF-8");
			
			WebRequest request = new WebRequest("https://falcun.xyz/verify.php?email=" + email + "&hwid=" + hwid, "GET", ContentType.NONE, false);
			WebRequestResult result = request.connect();
			
			//Matt
			/*/
			if(!result.getData().trim().equalsIgnoreCase("true")) {
				showPopup("HWID mismatch, please reset your hwid in #ticket-create and try again.");
				event.setCancelled(true);
				return;
			}
			/*/
			
			request.setURL("https://falcun.xyz/version.php");
			result = request.connect();
			
			if(!result.getData().trim().equalsIgnoreCase(VERSION)) {
				if(!hwid.equalsIgnoreCase(URLEncoder.encode("305-35d-3de-3aa-336-39e-3d0-33e-31d-308-35c-39e-365-359-3b2-302", "UTF-8"))) {
					showPopup("Version mismatch, please update your client.");
					event.setCancelled(true);
					return;
				}
			}
		} catch (JSONException | NoSuchElementException | IOException e) {
			log.error("Failed to reach site for authentication & version checking.", e);
			showPopup("Failed to contact our servers, servers down? Try again later or contact an developer.");
			event.setCancelled(true);
			return;
		}
		
		postInit();
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception ignored) {
		}

		emoticons.init(event);

		Emotes.register();

		/* Register the capability */
		CapabilityManager.INSTANCE.register(ICosmetic.class, new CosmeticStorage(), Cosmetic.class);
		
        MinecraftForge.EVENT_BUS.register((Object)new LessArrayLists());

		/* Register event handlers */
		MinecraftForge.EVENT_BUS.register(new CapabilitiesHandler());

		/* Register event handlers */
		// MinecraftForge.EVENT_BUS.register(new KeyboardHandler());
		MinecraftForge.EVENT_BUS.register(new EntityModelHandler());
		
		ClientCommandHandler.instance.registerCommand(new CommandEmote());
		ClientCommandHandler.instance.registerCommand(new RKOCommand());
		
		RenderLightmap.create();

		/* Load default models, manually... */
		try {
			Class loader = this.getClass();
			AnimationManager manager = AnimationManager.INSTANCE;

			BOBJData propData = BOBJLoader
					.readData(AssetUtils.getResourceAsStream("emoticons/models/entity/props.bobj"));
			BOBJData propSimpleData = BOBJLoader
					.readData(AssetUtils.getResourceAsStream("emoticons/models/entity/props_simple.bobj"));
			
			BOBJData steveData = BOBJLoader
					.readData(AssetUtils.getResourceAsStream("emoticons/models/entity/default.bobj"));
			BOBJData steve3DData = BOBJLoader
					.readData(AssetUtils.getResourceAsStream("emoticons/models/entity/default_3d.bobj"));
			BOBJData alexData = BOBJLoader
					.readData(AssetUtils.getResourceAsStream("emoticons/models/entity/slim.bobj"));
			BOBJData alex3DData = BOBJLoader
					.readData(AssetUtils.getResourceAsStream("emoticons/models/entity/slim_3d.bobj"));
			BOBJData steveSimpleData = BOBJLoader
					.readData(AssetUtils.getResourceAsStream("emoticons/models/entity/default_simple.bobj"));
			BOBJData alexSimpleData = BOBJLoader
					.readData(AssetUtils.getResourceAsStream("emoticons/models/entity/slim_simple.bobj"));
			BOBJData airKOData = BOBJLoader
			        .readData(AssetUtils.getResourceAsStream("emoticons/models/entity/Air_ko_3_body_2.bobj"));
			
			Falcun.emoticons.proxy.reloadActions();

			steveData.actions = Falcun.emoticons.proxy.actionMap;
			steve3DData.actions = Falcun.emoticons.proxy.actionMap;
			alexData.actions = Falcun.emoticons.proxy.actionMap;
			alex3DData.actions = Falcun.emoticons.proxy.actionMap;
			steveSimpleData.actions = Falcun.emoticons.proxy.actionMap;
			alexSimpleData.actions = Falcun.emoticons.proxy.actionMap;
			
			BOBJLoader.merge(propData, airKOData);
			BOBJLoader.merge(steveData, propData);
			BOBJLoader.merge(steve3DData, propData);
			BOBJLoader.merge(alexData, propData);
			BOBJLoader.merge(alex3DData, propData);
			BOBJLoader.merge(steveSimpleData, propSimpleData);
			BOBJLoader.merge(alexSimpleData, propSimpleData);

			Animation steve = new Animation("default", steveData);
			Animation steve3d = new Animation("default_3d", steve3DData);
			Animation alex = new Animation("slim", alexData);
			Animation alex3d = new Animation("slim_3d", alex3DData);
			Animation steveSimple = new Animation("default_simple", steveSimpleData);
			Animation alexSimple = new Animation("slim_simple", alexSimpleData);

			steve.init();
			steve3d.init();
			alex.init();
			alex3d.init();
			steveSimple.init();
			alexSimple.init();

			manager.animations.put("default", new AnimationEntry(steve, Falcun.emoticons.proxy.configFolder, 1));
			manager.animations.put("default_3d",
					new AnimationEntry(steve3d, Falcun.emoticons.proxy.configFolder, 1));
			manager.animations.put("slim", new AnimationEntry(alex, Falcun.emoticons.proxy.configFolder, 1));
			manager.animations.put("slim_3d", new AnimationEntry(alex3d, Falcun.emoticons.proxy.configFolder, 1));
			manager.animations.put("default_simple",
					new AnimationEntry(steveSimple, Falcun.emoticons.proxy.configFolder, 1));
			manager.animations.put("slim_simple",
					new AnimationEntry(alexSimple, Falcun.emoticons.proxy.configFolder, 1));

			manager.animations.put("default", new AnimationEntry(steve, Emoticons.proxy.configFolder, 1));
			manager.animations.put("default_3d",
					new AnimationEntry(steve3d, Emoticons.proxy.configFolder, 1));
			manager.animations.put("slim", new AnimationEntry(alex, Emoticons.proxy.configFolder, 1));
			manager.animations.put("slim_3d", new AnimationEntry(alex3d, Emoticons.proxy.configFolder, 1));
			manager.animations.put("default_simple",
					new AnimationEntry(steveSimple, Emoticons.proxy.configFolder, 1));
			manager.animations.put("slim_simple",
					new AnimationEntry(alexSimple, Emoticons.proxy.configFolder, 1));
	
			AnimatorConfig steveConfig = manager.gson.fromJson(
					IOUtils.toString(AssetUtils.getResourceAsStream("emoticons/models/entity/default.json"),
							Charset.defaultCharset()),
					AnimatorConfig.class);
			AnimatorConfig alexConfig = manager.gson
					.fromJson(IOUtils.toString(AssetUtils.getResourceAsStream("emoticons/models/entity/slim.json"),
							Charset.defaultCharset()), AnimatorConfig.class);
			AnimatorConfig steveSimpleConfig = manager.gson.fromJson(
					IOUtils.toString(AssetUtils.getResourceAsStream("emoticons/models/entity/default_simple.json"),
							Charset.defaultCharset()),
					AnimatorConfig.class);
			AnimatorConfig alexSimpleConfig = manager.gson.fromJson(
						IOUtils.toString(AssetUtils.getResourceAsStream("emoticons/models/entity/slim_simple.json"),
								Charset.defaultCharset()),
						AnimatorConfig.class);

			manager.configs.put("default", new AnimatorConfigEntry(steveConfig, 1));
			manager.configs.put("default_3d", new AnimatorConfigEntry(steveConfig, 1));
			manager.configs.put("slim", new AnimatorConfigEntry(alexConfig, 1));
			manager.configs.put("slim_3d", new AnimatorConfigEntry(alexConfig, 1));
			manager.configs.put("default_simple", new AnimatorConfigEntry(steveSimpleConfig, 1));
			manager.configs.put("slim_simple", new AnimatorConfigEntry(alexSimpleConfig, 1));

			
			Falcun.getInstance().emoteSettings.loadEmoteSettings();
			
			updateOwnedEmotes();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public static void updateOwnedEmotes() {
		new Thread(() -> {
			try {

//				String result;
//				try {
//					String url = "https://falcun.net/old/getemotes.php?mcname=" + Minecraft.getMinecraft().getSession().getUsername();
//					HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
//					conn.setRequestMethod("GET");
//					conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.135 Safari/537.36");
//					conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//					int resp = conn.getResponseCode();
//					System.out.println(resp);
//					BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//					String strCurrentLine;
//					while ((strCurrentLine = br.readLine()) != null) {
//						System.out.println(strCurrentLine);
//					}
//					result = strCurrentLine;
//				} catch (Throwable err) {
//					err.printStackTrace();
//					return;
//				}

				 WebRequest request = new WebRequest("https://falcun.net/old/getemotes.php?mcname=" + Minecraft.getMinecraft().getSession().getUsername(), "GET", ContentType.NONE, false);
				 WebRequestResult result = request.connect();
				
				String useremotes = result.getData();
				
				List<Emote> emotes = new ArrayList<>();
				List<String> list = Arrays.asList(useremotes.split("\\s*,\\s*"));
				for (int i = 0; i < list.size(); i++) {
					String emote = list.get(i);
					emote = emote.replace("emote_", "");
					Emote e = Emotes.get(emote);
					
					if (e != null && !emotes.contains(e)) {
						emotes.add(e);
					}
				}

				emotes.sort(new Comparator<Emote>() {

					@Override
					public int compare(Emote o1, Emote o2) {
						String name1 = o1.name.toUpperCase().replace("_", " ");
						String name2 = o2.name.toUpperCase().replace("_", " ");
						return name1.compareTo(name2);
					}
				});
				
			ownedEmotes = emotes;
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}).start();
	}
	

	public String getHWID() throws NoSuchAlgorithmException, IOException {
		StringBuilder builder = new StringBuilder();
		
		byte[] cleanHWID = (System.getenv("PROCESSOR_IDENTIFIER") + System.getenv("COMPUTERNAME") + System.getProperty("user.name")).trim().getBytes("UTF-8");
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		byte[] md5 = messageDigest.digest(cleanHWID);
		
		for(int i = 0; i < md5.length; i++) {
			byte b = md5[i];
			
			if(!builder.toString().isEmpty()) {
				builder.append("-");
			}
			
			builder.append(Integer.toHexString((b & 0xFF) | 0x300).substring(0, 3));
		}
		
		return builder.toString();
	}
	
	public String getEmail() throws IOException {
		if(!MAIN_DIR.exists()) {
			MAIN_DIR.mkdir();
		}
		
		File file = Paths.get(MAIN_DIR.getAbsolutePath(), "email.txt").toFile();
		FileHandler handler = new FileHandler(file);
		handler.init();
		
		return handler.getContent(false).trim();
	}

	private void showPopup(String string) {
		JFrame panel = new JFrame();
		
		panel.setAlwaysOnTop(true);
		panel.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		panel.setLocationRelativeTo(null);
		
		Toolkit.getDefaultToolkit().beep();
				
		JOptionPane.showMessageDialog(panel, string);
	}
	
	public static Falcun getInstance() {
		return instance;
	}
	

	public static String getURL(String url) {
		
		try {
			 WebRequest request = new WebRequest(url, "GET", ContentType.NONE, false);
			 WebRequestResult result = request.connect();
			 return result.getData();
		} catch (Throwable err) {
			err.printStackTrace();
			return "";
		}
		
//		try {
//			String result = "";
//			URL urlObj = new URL(url);
//			URLConnection con = urlObj.openConnection();
//			con.setDoOutput(true);
//			con.setRequestProperty("Cookie", "myCookie=test123");
//			con.connect();
//
//			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//
//			StringBuilder response = new StringBuilder();
//
//			String newLine = System.getProperty("line.separator");
//			String inputLine;
//			while ((inputLine = in.readLine()) != null) {
//				response.append(inputLine + newLine);
//			}
//			in.close();
//			result = response.toString();
//			response = null;
//			return result;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return "";
//		}
	}
}
