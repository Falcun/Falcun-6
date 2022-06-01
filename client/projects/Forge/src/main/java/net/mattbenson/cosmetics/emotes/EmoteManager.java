package net.mattbenson.cosmetics.emotes;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;

import com.google.common.base.Throwables;

import mchorse.emoticons.Emoticons;
import mchorse.emoticons.capabilities.CapabilitiesHandler;
import mchorse.emoticons.capabilities.cosmetic.Cosmetic;
import mchorse.emoticons.capabilities.cosmetic.CosmeticStorage;
import mchorse.emoticons.capabilities.cosmetic.ICosmetic;
import mchorse.emoticons.client.EmoteKeys;
import mchorse.emoticons.client.EntityModelHandler;
import mchorse.emoticons.common.emotes.Emote;
import mchorse.emoticons.common.emotes.Emotes;
import mchorse.emoticons.skin_n_bones.api.animation.Animation;
import mchorse.emoticons.skin_n_bones.api.animation.AnimationManager;
import mchorse.emoticons.skin_n_bones.api.animation.AnimationManager.AnimationEntry;
import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorConfig;
import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorConfig.AnimatorConfigEntry;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJLoader;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJLoader.BOBJData;
import net.mattbenson.Falcun;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.commands.CommandRegisterEvent;
import net.mattbenson.render.RenderLightmap;
import net.mattbenson.requests.ContentType;
import net.mattbenson.requests.WebRequest;
import net.mattbenson.requests.WebRequestResult;
import net.mattbenson.utils.AssetUtils;
import net.mattbenson.utils.ForgeUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.capabilities.Capability;

public class EmoteManager {
	public List<Emote> ownedEmotes;
	public Emoticons emoticons;
	public static Capability<ICosmetic> capability = new Capability<ICosmetic>(Cosmetic.class.getName().intern(), new CosmeticStorage(), new Callable<Cosmetic>()
    {
        @Override
        public Cosmetic call() throws Exception
        {
            try {
                return (Cosmetic) Cosmetic.class.newInstance();
            } catch (InstantiationException e) {
                Throwables.propagate(e);
            } catch (IllegalAccessException e) {
                Throwables.propagate(e);
            }
            return null;
        }
    });
	
	public EmoteManager() {
		ownedEmotes = new CopyOnWriteArrayList<>();
		
		Falcun.getInstance().languageUtils.registerLanguageResource("emoticons", "en_US");
		
		RenderLightmap.create();
		Emotes.register();
		
		Falcun.getInstance().EVENT_BUS.register(new CapabilitiesHandler());
		Falcun.getInstance().EVENT_BUS.register(new EntityModelHandler());
		
		emoticons = new Emoticons();
		Emoticons.proxy.configFolder = ForgeUtils.getPreferredFolder("emotes");
		Emoticons.proxy.configFolder.mkdir();
		
		File file = new File(Emoticons.proxy.configFolder, "keys.json");
		
		Emoticons.proxy.keys = EmoteKeys.fromFile(file);
		
		if(Emoticons.proxy.keys == null) {
			Emoticons.proxy.keys = new EmoteKeys();
			EmoteKeys.toFile(Emoticons.proxy.keys, file);
		}
		
		try {
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

			Emoticons.proxy.reloadActions();
	
			steveData.actions = Emoticons.proxy.actionMap;
			steve3DData.actions = Emoticons.proxy.actionMap;
			alexData.actions = Emoticons.proxy.actionMap;
			alex3DData.actions = Emoticons.proxy.actionMap;
			steveSimpleData.actions = Emoticons.proxy.actionMap;
			alexSimpleData.actions = Emoticons.proxy.actionMap;
			
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Falcun.getInstance().emoteSettings.loadEmoteSettings();
		
		updateOwnedEmotes();
	}
	
	public void updateOwnedEmotes() {
		new Thread(() -> {
			try {
				 WebRequest request = new WebRequest("http://falcun.net/old/getemotes.php?mcname=" + Minecraft.getMinecraft().getSession().getUsername(), "GET", ContentType.NONE, false);
				 WebRequestResult result = request.connect();
				
				 String useremotes = result.getData();
				
				
				// URLConnection connection = new URL("http://falcun.net/old/getemotes.php?mcname=FalcunDev").openConnection();
				// connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
				// connection.connect();

				// BufferedReader r  = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));

				// StringBuilder sb = new StringBuilder();
				// String line;
				// while ((line = r.readLine()) != null) {
				//     sb.append(line);
				// }
				// System.out.println("Cape DATA:");
				// System.out.println(sb.toString());

//				String result;
//				try {
//					String url = "http://falcun.net/old/getemotes.php?mcname=" + Minecraft.getMinecraft().getSession().getUsername();
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
				
				this.ownedEmotes = emotes;
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}).start();
	}
	
	
	@SubscribeEvent
	public void CommandRegisterEvent(CommandRegisterEvent event) {
		event.getCommands().add(new RKOCommand());
	}
}

