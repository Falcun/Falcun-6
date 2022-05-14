package net.mattbenson.utils.legacy;

import java.util.ArrayList;

import net.mattbenson.events.SubscribeEvent;
import net.mattbenson.events.types.world.OnTickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;

public class LessArrayLists {
	public static ArrayList<EntityTNTPrimed> currentTnT;

	@SubscribeEvent
	public void clienttickevent(OnTickEvent event) {
		final ArrayList<EntityTNTPrimed> tempTNT = new ArrayList<EntityTNTPrimed>();
		
		if (Minecraft.getMinecraft().thePlayer == null) {
			return;
		}
		
		for (final Entity ent : Minecraft.getMinecraft().theWorld.loadedEntityList) {
			if (ent instanceof EntityTNTPrimed) {
				tempTNT.add((EntityTNTPrimed) ent);
			}
		}
		LessArrayLists.currentTnT = tempTNT;
	}

	static {
		LessArrayLists.currentTnT = new ArrayList<EntityTNTPrimed>();
	}
}