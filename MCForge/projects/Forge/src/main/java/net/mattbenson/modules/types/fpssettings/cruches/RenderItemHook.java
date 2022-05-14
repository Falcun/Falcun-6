package net.mattbenson.modules.types.fpssettings.cruches;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.mattbenson.Falcun;
import net.mattbenson.modules.types.fpssettings.FPSSettings;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;

public class RenderItemHook {
	private final EnhancedItemRenderer itemRenderer = (EnhancedItemRenderer) EnhancementManager.getInstance()
			.getEnhancement(EnhancedItemRenderer.class);

	private ItemHash itemHash;

	private int glList;

	private static FPSSettings settings;
	
	//Code at RenderItem.java
	public boolean renderModelStart(IBakedModel model, int color, ItemStack stack) {
		if(settings == null) {
			settings = Falcun.getInstance().moduleManager.getModule(FPSSettings.class);
		}
		
		this.itemHash = null;
		this.glList = 0;
		if (settings.OPTIMISED_ITEM_RENDERER) {
			List<Object> itemInformation = new ArrayList<>();
			itemInformation.add(Boolean.valueOf(model.isGui3d()));
			itemInformation.add(Boolean.valueOf(model.isBuiltInRenderer()));
			itemInformation.add(Boolean.valueOf(model.isAmbientOcclusion()));
			itemInformation.add(model.getTexture());
			itemInformation.addAll(model.getGeneralQuads());
			itemInformation.add(Integer.valueOf(color));
			itemInformation.add((stack == null) ? "" : stack.getUnlocalizedName());
			itemInformation.add((stack == null) ? "" : Integer.valueOf(stack.getItemDamage()));
			itemInformation.add((stack == null) ? "" : Integer.valueOf(stack.getMetadata()));
			itemInformation.add((stack == null) ? "" : stack.getTagCompound());
			this.itemHash = new ItemHash(itemInformation.toArray());
			Integer integer = (Integer) itemRenderer.getItemCache().getIfPresent(this.itemHash);
			if (integer != null) {
				GlStateManager.callList(integer.intValue());
				GlStateManager.resetColor();
				return true;
			}
			this.glList = itemRenderer.getGlList();
			GL11.glNewList(this.glList, 4865);
		}
		return false;
	}

	//Code at RenderItem.java
	public void renderModelEnd() {
		if(settings == null) {
			settings = Falcun.getInstance().moduleManager.getModule(FPSSettings.class);
		}
		
		if (settings.OPTIMISED_ITEM_RENDERER) {
			GL11.glEndList();
			itemRenderer.getItemCache().put(this.itemHash, Integer.valueOf(this.glList));
		}
	}
}
