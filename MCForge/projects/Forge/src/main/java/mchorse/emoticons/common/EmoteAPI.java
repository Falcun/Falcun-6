package mchorse.emoticons.common;

import mchorse.emoticons.ClientProxy;

import mchorse.emoticons.capabilities.cosmetic.Cosmetic;
import mchorse.emoticons.capabilities.cosmetic.CosmeticMode;
import mchorse.emoticons.capabilities.cosmetic.ICosmetic;
import mchorse.emoticons.common.emotes.Emote;
import mchorse.emoticons.common.emotes.Emotes;
import net.mattbenson.network.NetworkingClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EmoteAPI
{
    public static void setEmote(String emote, EntityPlayer player)
    {
    	emote = emote.replace("emote_", "");
    	
        setEmote(Emotes.get(emote), player);
    }

    public static void setEmote(Emote emote, EntityPlayer player)
    {
        ICosmetic cap = Cosmetic.get(player);

        if (cap != null)
        {
            cap.setEmote(emote, player);
            
        }
    }

    @SideOnly(Side.CLIENT)
    public static void setEmoteClient(String emote, EntityPlayer player)
    {
        ICosmetic cap = Cosmetic.get(player);

        if (cap == null)
        {
            return;
        }
        
        if (!Emotes.has(emote))
        {
            emote = "";
        }
        
        Emote memote = Emotes.get(emote);
        CosmeticMode mode = ClientProxy.mode;
        
       	
        if (mode == CosmeticMode.CLIENT && memote != null)
        {   
        	NetworkingClient.sendLine("EmoticonStart", memote.name);
            //cap.setEmote(memote, player);
            
        }
        
     
        
    }
}