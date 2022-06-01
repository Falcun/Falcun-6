package mapwriter.forge;

import org.json.JSONException;

import org.json.JSONObject;

import mapwriter.Mw;
import mapwriter.config.Config;
import mapwriter.map.Marker;
import mapwriter.util.Logging;
import mapwriter.util.Utils;
import net.mattbenson.events.types.entity.EntityCreateEvent;
import net.mattbenson.network.NetworkingClient;
import net.mattbenson.network.common.GroupData;
import net.mattbenson.network.common.WaypointMarker;
import net.mattbenson.network.network.packets.groups.GroupList;
import net.mattbenson.utils.NetworkUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.mattbenson.network.network.packets.waypoints.WaypointList;

public class EventHandler {
	Mw mw;

	public EventHandler(Mw mw) {
		this.mw = mw;
	}

	@SubscribeEvent
	public void eventChunkLoad(ChunkEvent.Load event) {
		if (event.world.isRemote) {
			this.mw.onChunkLoad(event.getChunk());
		}
	}

	@SubscribeEvent
	public void eventChunkUnload(ChunkEvent.Unload event) {
		if (event.world.isRemote) {
			this.mw.onChunkUnload(event.getChunk());
		}
	}

	@SubscribeEvent
	public void onClientChat(ClientChatReceivedEvent event) {
		if (this.mw.facInput.isFactionMap(event.message)) {
			if (!this.mw.facInput.showMap) {
				if(Minecraft.getMinecraft().thePlayer.prevPosX == Minecraft.getMinecraft().thePlayer.posX && Minecraft.getMinecraft().thePlayer.prevPosY == Minecraft.getMinecraft().thePlayer.posY && Minecraft.getMinecraft().thePlayer.prevPosZ == Minecraft.getMinecraft().thePlayer.posZ) return;
				event.setCanceled(true);
				return;
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void eventPlayerDeath(LivingDeathEvent event) {
		if (!event.isCanceled()) {
			if (event.entityLiving.getEntityId() == Minecraft.getMinecraft().thePlayer.getEntityId()) {
				this.mw.onPlayerDeath((EntityPlayerMP) event.entityLiving);
			}
		}
	}

	@SubscribeEvent
	public void renderMap(RenderGameOverlayEvent.Post event) {
		if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
			Mw.getInstance().onTick();
		}
	}

	@SubscribeEvent
	public void onTextureStitchEventPost(TextureStitchEvent.Post event) {
		if (Config.reloadColours) {
			Logging.logInfo("Skipping the first generation of blockcolours, models are not loaded yet", (Object[]) null);
		} else {
			this.mw.reloadBlockColours();
		}
	}

	@SubscribeEvent
	public void renderWorldLastEvent(RenderWorldLastEvent event) {
		if (Mw.getInstance().ready) {
			Mw.getInstance().markerManager.drawMarkersWorld(event.partialTicks);
			Mw.getInstance().markerManagerGroup.drawMarkersWorld(event.partialTicks);
		}
	}

	// a bit odd way to reload the blockcolours. if the models are not loaded
	// yet then the uv values and icons will be wrong.
	// this only happens if fml.skipFirstTextureLoad is enabled.
	@SubscribeEvent
	public void onGuiOpenEvent(GuiOpenEvent event) {
		if (event.gui instanceof GuiMainMenu && Config.reloadColours) {
			this.mw.reloadBlockColours();
			Config.reloadColours = false;
		}
	}
}
