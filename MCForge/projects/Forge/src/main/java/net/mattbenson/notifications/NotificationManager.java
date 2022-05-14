package net.mattbenson.notifications;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.mattbenson.Falcun;
import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.render.RenderEvent;
import net.mattbenson.events.types.render.RenderType;
import net.mattbenson.utils.AssetUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;

public class NotificationManager {
    public static final ResourceLocation LOGO = AssetUtils.getResource("/gui/falcunwings.png");
	private List<Notification> notifications;
	private Thread removalThread;
	
	public NotificationManager() {
		notifications = new ArrayList<>();
		removalThread = new Thread(new NotiRemovalThread(notifications));
		removalThread.start();
		
		Falcun.EVENT_BUS.register(this);
	}
	
	@SubscribeEvent
	public void drawOverlay(RenderEvent event) {
		if(event.getRenderType() != RenderType.INGAME_OVERLAY) {
			return;
		}
		
		int y = new ScaledResolution(Minecraft.getMinecraft()).getScaledHeight();
		
		for(Notification noti : notifications) {
			y = noti.draw(y);
		}
	}

	public void showNotification(String text, Color color) {
		boolean contains = false;
		
		for(Notification noti : notifications) {
			if(noti.getText().equalsIgnoreCase(text) && !noti.isDead()) {
				contains = true;
				break;
			}
		}
		
		if(!contains) {
			notifications.add(new Notification(text, color));
		}
	}
}
