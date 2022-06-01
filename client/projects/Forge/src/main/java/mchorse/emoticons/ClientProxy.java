package mchorse.emoticons;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import mchorse.emoticons.capabilities.CapabilitiesHandler;
import mchorse.emoticons.capabilities.cosmetic.Cosmetic;
import mchorse.emoticons.capabilities.cosmetic.CosmeticMode;
import mchorse.emoticons.capabilities.cosmetic.CosmeticStorage;
import mchorse.emoticons.capabilities.cosmetic.ICosmetic;
import mchorse.emoticons.client.EmoteKeys;
import mchorse.emoticons.client.EntityModelHandler;
import mchorse.emoticons.client.KeyboardHandler;
import mchorse.emoticons.commands.CommandEmote;
import mchorse.emoticons.common.emotes.Emote;
import mchorse.emoticons.common.emotes.Emotes;
import mchorse.emoticons.skin_n_bones.api.animation.Animation;
import mchorse.emoticons.skin_n_bones.api.animation.AnimationManager;
import mchorse.emoticons.skin_n_bones.api.animation.AnimationManager.AnimationEntry;
import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorConfig;
import mchorse.emoticons.skin_n_bones.api.animation.model.AnimatorConfig.AnimatorConfigEntry;
import mchorse.emoticons.skin_n_bones.api.asm.InjectStringObfuscation;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJAction;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJLoader;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJLoader.BOBJData;
import net.mattbenson.events.types.MinecraftInitEvent;
import net.mattbenson.utils.AssetUtils;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy
{
    /**
     * Client folder where saved selectors and animations are getting
     * stored. 
     */
    public static File configFolder;

    /**
     * Emote keys configuration 
     */
    public static EmoteKeys keys;

    /**
     * Cosmetic mode determines how emote packet will be sent from the client
     */
    public static CosmeticMode mode = CosmeticMode.CLIENT;

    /**
     * Action map
     */
    public static Map<String, BOBJAction> actionMap = new HashMap<String, BOBJAction>();

    public static void reloadActions()
    {
    	try
	    {
		    BOBJData actions = BOBJLoader.readData(AssetUtils.getResourceAsStream("/emoticons/models/entity/actions.bobj"));

		    try
		    {
			    /* Try loading user animations and emote data */
			    loadUserEmotes(actions);
		    }
		    catch (Exception e)
		    {
			    System.err.println("Failed to load user animation or emote data...");
		    }

		    actionMap.clear();
		    actionMap.putAll(actions.actions);
	    }
    	catch (Exception e)
	    {
	    	e.printStackTrace();
	    }
    }

    @InjectStringObfuscation
    private static void loadUserEmotes(BOBJData actions) throws Exception
    {
        File user = new File(configFolder, "emoticons");

        user.mkdirs();

        File[] files = user.listFiles();

        if (files == null)
        {
            return;
        }

        for (File file : files)
        {
            if (!file.getName().endsWith(".bobj"))
            {
                continue;
            }

            File json = new File(file.getParentFile(), FilenameUtils.getBaseName(file.getName()) + ".json");
            BOBJData data = BOBJLoader.readData(new FileInputStream(file));

            actions.actions.putAll(data.actions);

            if (json.exists())
            {
                registerEmotes(json, data.actions);
            }
        }
    }

    @InjectStringObfuscation
    private static void registerEmotes(File file, Map<String, BOBJAction> actions)
    {
        try
        {
            JsonObject element = new JsonParser().parse(new FileReader(file)).getAsJsonObject();

            for (Map.Entry<String, JsonElement> entry : element.entrySet())
            {
                String key = entry.getKey();
                BOBJAction action = actions.get("emote_" + key);
                JsonElement value = entry.getValue();

                if (action == null || !value.isJsonObject())
                {
                    continue;
                }

                JsonObject object = value.getAsJsonObject();
                boolean looping = object.has("looping") && object.get("looping").getAsBoolean();
                Emote emote = new Emote(key, action.getDuration(), looping);

                if (object.has("title"))
                {
                    emote.customTitle = object.get("title").getAsString();
                }

                if (object.has("description"))
                {
                    emote.customDescription = object.get("description").getAsString();
                }

                Emotes.register(emote);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    public void preInit(FMLPreInitializationEvent event)
    {
       

    }

    public void init(MinecraftInitEvent event)
    {
    	
    }
}