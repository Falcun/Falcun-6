package mchorse.emoticons.common.emotes;

import java.util.HashMap;
import java.util.Map;

import mchorse.emoticons.skin_n_bones.api.asm.InjectStringObfuscation;
import mchorse.emoticons.utils.Time;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;

/**
 * Emotes registry
 * 
 * This class is responsible for registering and storing emote entries 
 * for usage with Emoticon's cosmetic API.
 */

@Mod(modid="emoticons", name="emoticons", version="0")
public class Emotes
{
    public static final Map<String, Emote> EMOTES = new HashMap<String, Emote>();

    public static boolean has(String emote)
    {
        if (emote.contains(":"))
        {
            emote = emote.split(":")[0];
        }

        return EMOTES.containsKey(emote);
    }

    public static Emote get(String emote)
    {
        if (emote.contains(":"))
        {
            String[] splits = emote.split(":");
        	
      
            Emote meme = EMOTES.get(splits[0]);
        	
            return meme == null ? null : meme.getDynamicEmote(splits[1]);
        }

        Emote meme = EMOTES.get(emote);

        return meme == null ? null : meme.getDynamicEmote();
    }

    public static Emote getDefault(String emote)
    {
        Emote emo = get(emote);

        return emo == null ? get("default") : emo;
    }

    @InjectStringObfuscation
    public static void register()
    {
        EMOTES.clear();
        
        loadSounds();
        
        ResourceLocation bestMates 		= new ResourceLocation("emoticons", "best_mates");
        ResourceLocation boneless 		= new ResourceLocation("emoticons", "boneless");
        ResourceLocation defaultEmote	= new ResourceLocation("emoticons", "default");
        ResourceLocation discoFever 	= new ResourceLocation("emoticons", "disco_fever");
        ResourceLocation electroShuffle	= new ResourceLocation("emoticons", "electro_shuffle");
        ResourceLocation floss 			= new ResourceLocation("emoticons", "floss");
        ResourceLocation fresh 			= new ResourceLocation("emoticons", "fresh");
        ResourceLocation gangnamStyle	= new ResourceLocation("emoticons", "gangnam_style");
        ResourceLocation hype 			= new ResourceLocation("emoticons", "hype");
        //ResourceLocation infiniteDab 	= new ResourceLocation("emoticons", "infinite_dab");
        ResourceLocation orangeJustice 	= new ResourceLocation("emoticons", "orange_justice");
        ResourceLocation skibidi 		= new ResourceLocation("emoticons", "skibidi");
        ResourceLocation squatKick 		= new ResourceLocation("emoticons", "squat_kick");
        ResourceLocation starPower 		= new ResourceLocation("emoticons", "star_power");
        ResourceLocation takeTheL 		= new ResourceLocation("emoticons", "take_the_l");
        ResourceLocation tidy 			= new ResourceLocation("emoticons", "tidy");
        ResourceLocation freeFlow 		= new ResourceLocation("emoticons", "free_flow");
        ResourceLocation shimmer 		= new ResourceLocation("emoticons", "shimmer");
        ResourceLocation getFunky 		= new ResourceLocation("emoticons", "getFunky");
        ResourceLocation woo_dance 		= new ResourceLocation("emoticons", "woo_dance");
        ResourceLocation wap 		= new ResourceLocation("emoticons", "wap");
        ResourceLocation toosie_slide 		= new ResourceLocation("emoticons", "toosie_slide");
        ResourceLocation woo_walk 		= new ResourceLocation("emoticons", "woo_walk");
        
        ResourceLocation oh_na_na_na 		= new ResourceLocation("emoticons", "oh_na_na_na");
        ResourceLocation shuffle 		= new ResourceLocation("emoticons", "shuffle");
        ResourceLocation renegade 		= new ResourceLocation("emoticons", "renegade");
        ResourceLocation say_so 		= new ResourceLocation("emoticons", "say_so");
        ResourceLocation savage 		= new ResourceLocation("emoticons", "savage");
        ResourceLocation hit_it_fergie  = new ResourceLocation("emoticons", "hit_it_fergie");
        ResourceLocation arm_pump 		= new ResourceLocation("emoticons", "arm_pump");
        ResourceLocation rockstar 		= new ResourceLocation("emoticons", "rockstar");
        ResourceLocation smoke 		    = new ResourceLocation("emoticons", "smoke");
        ResourceLocation party 		    = new ResourceLocation("emoticons", "party_girl");
        ResourceLocation ronaldo 		= new ResourceLocation("emoticons", "ronaldo");
        ResourceLocation rko 			= new ResourceLocation("emoticons", "rko");
        ResourceLocation big_bank 		= new ResourceLocation("emoticons", "big_bank");
        /*/ Matt
        /* Dance emotes */
        register(new Emote("best_mates", 11, true, bestMates, resource("best_mates")));
        register(new Emote("boneless", 40, true, boneless, resource("boneless")));
        register(new Emote("default", 139, false, defaultEmote, resource("default")));
        register(new Emote("disco_fever", 175, true, discoFever));
        register(new Emote("electro_shuffle", 169, true, electroShuffle));
        register(new Emote("floss", 32, true, floss, resource("floss")));
        register(new Emote("fresh", 101, true, fresh));
        register(new Emote("gangnam_style", Time.toTicks(200), false, gangnamStyle, resource("gangnam_style")));
        register(new Emote("hype", 68, true, hype, resource("hype")));
        register(new Emote("infinite_dab", 19, true, resource("infinite_dab")));
        register(new Emote("orange_justice", 130, true, orangeJustice, resource("orange_justice")));
        register(new Emote("skibidi", 16, true, skibidi));
        register(new Emote("squat_kick", 232, true, squatKick));
        register(new StarPowerEmote("star_power", 160, true, starPower, null));
        register(new Emote("take_the_l", 16, true, takeTheL, resource("l_dance")));
        register(new Emote("tidy", 104, true, tidy));
        register(new Emote("free_flow", 158, true, freeFlow));
        register(new Emote("shimmer", 156, true, shimmer));
        register(new Emote("get_funky", 172, true, getFunky));

        /* Just emotes */
        register(new Emote("boy", 29, false));
        register(new Emote("bow", 43, false, null, resource("bow")));
        register(new Emote("calculated", 33, false));
        register(new Emote("chicken", 19, true));
        register(new Emote("clapping", 15, true, null, resource("clap")));
        register(new Emote("club", 20, true));
        register(new Emote("confused", 140, false));
        register(new CryingEmote("crying", 27, true, null, resource("crying")));
        register(new Emote("dab", 23, false, null, resource("dab")));
        register(new Emote("facepalm", 104, false, null, resource("facepalm")));
        register(new Emote("fist", 53, false));
        register(new Emote("laughing", 15, true));
        register(new Emote("no", 30, false));
        register(new Emote("pointing", 33, false, null, resource("point")));
        register(new PopcornEmote("popcorn", 102, true, null, resource("popcorn")));
        register(new PureSaltEmote("pure_salt", 104, false, null, resource("salt")));
        register(new RockPaperScissorsEmote("rock_paper_scissors", 60, false)); 
        register(new Emote("salute", 50, false, null, resource("salute")));
        register(new Emote("shrug", 50, false, null, resource("shrug")));
        register(new Emote("t_pose", 80, true, null, resource("tpose")));
        register(new Emote("thinking", 100, true, null, resource("think")));
        register(new Emote("twerk", 14, true, null, resource("twerk")));
        register(new Emote("wave", 40, false, null, resource("wave")));
        register(new Emote("yes", 23, false));

        /* Emotes 2020 */
        register(new Emote("bitchslap", Time.toTicks(100), false));
        register(new Emote("bongo_cat", Time.toTicks(238), false, null, resource("bongo_cat")));
        register(new Emote("breathtaking", Time.toTicks(154), false));
        register(new DisgustedEmote("disgusted", Time.toTicks(200), false));
        register(new Emote("exhausted", Time.toTicks(330), true));
        register(new Emote("punch", Time.toTicks(58), false));
        register(new SneezeEmote("sneeze", Time.toTicks(200), false));
        register(new Emote("threatening", Time.toTicks(70), false, null, resource("threatening")));
        register(new Emote("woah", Time.toTicks(66), false, null, resource("woah")));

        register(new Emote("stick_bug", Time.toTicks(25), true));
        register(new Emote("am_stuff", Time.toTicks(80), false));
        register(new Emote("slow_clap", Time.toTicks(200), false, null, resource("clap")));
        register(new Emote("hell_yeah", Time.toTicks(70), false));
        register(new Emote("paranoid", Time.toTicks(315), false));
        register(new Emote("scared", Time.toTicks(50), true));
        register(new Emote("wap", Time.toTicks(344), false, wap));
        
        register(new Emote("toosie_slide", Time.toTicks(230), false, toosie_slide));
        register(new Emote("party_girl", Time.toTicks(410), false));
        
        register(new Emote("backflip", Time.toTicks(65), false));
        
        register(new Emote("jumping_jacks", Time.toTicks(100), false));
        
        register(new Emote("worm", Time.toTicks(220), false));
        
        register(new Emote("wiggle", Time.toTicks(113), false));
        
        register(new Emote("woo_dance", Time.toTicks(265), false, woo_dance));
        
        register(new Emote("woo_walk", Time.toTicks(247), false, woo_walk));

        register(new PureSaltEmote("pure_salt", 104, false, null, resource("salt")));
        
        register(new Emote("party_girl", Time.toTicks(410), false, party));
        
        register(new Emote("oh_na_na_na", Time.toTicks(430), false, oh_na_na_na));
        register(new Emote("shuffle", Time.toTicks(380), false, shuffle));
        register(new Emote("renegade", Time.toTicks(455), false, renegade));
        register(new Emote("say_so", Time.toTicks(340), false, say_so));
        register(new Emote("savage", Time.toTicks(420), false, savage));
        register(new Emote("hit_it_fergie", Time.toTicks(450), false, hit_it_fergie));
        register(new Emote("arm_pump", Time.toTicks(260), false, arm_pump));
        register(new Emote("rockstar", Time.toTicks(420), false, rockstar));
        
        register(new SmokeEmote("smoke", 170, false, smoke, null));
        
        register(new KickUpsEmote("kick_up", 102, true, null, null));
        
        register(new Emote("head_spin", Time.toTicks(320), false, null, null));
        
        register(new Emote("ronaldo", Time.toTicks(135), false, ronaldo, null));
        
        register(new RKOEmote("rko", 100, false, rko, null));
        
        register(new Emote("big_bank", 110, false, big_bank, null));


    }

    private static void loadSounds() {
		//Automatically added since its a "mod".
    	/*
    	try {
			for (IResource iresource : Minecraft.getMinecraft().getResourceManager()
					.getAllResources(new ResourceLocation("emoticons", "sounds.json"))) {
				try {
					Map<String, SoundList> map = Minecraft.getMinecraft().getSoundHandler()
							.getSoundMap(iresource.getInputStream());

					for (Entry<String, SoundList> entry : map.entrySet()) {
						Minecraft.getMinecraft().getSoundHandler().loadSoundResource(
								new ResourceLocation("emoticons", (String) entry.getKey()),
								(SoundList) entry.getValue());
					}
				} catch (RuntimeException runtimeexception) {
					Falcun.Log("Invalid sounds.json");
					runtimeexception.printStackTrace();
				}
			}
		} catch (IOException e) {
			Falcun.Log("Invalid sounds.json");
			e.printStackTrace();
		}
		*/
	}

	public static void register(Emote emote)
    {
        EMOTES.put(emote.name, emote);
    }
    
    public static ResourceLocation resource(String name) {
    	return new ResourceLocation("textures/gui/title/emotes/" + name +".png");
    }
}