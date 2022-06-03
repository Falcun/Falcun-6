package falcun.net.modules.hud;

import falcun.net.Falcun;
import falcun.net.api.guidragon.GuiUtils;
import falcun.net.api.guidragon.region.GuiRegion;
import falcun.net.api.modules.config.FalcunBounds;
import falcun.net.api.modules.config.FalcunModuleInfo;
import falcun.net.api.modules.config.FalcunSetting;
import falcun.net.api.modules.config.FalcunValue;
import falcun.net.api.modules.hud.FalcunHudModule;
import falcun.net.modules.ModuleCategory;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util.Colors;
import net.minecraft.client.gui.MapItemRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec4b;
import net.minecraft.world.storage.MapData;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Map;

@FalcunModuleInfo(name = "Map View", version = "1.0", description = "Renders a map in your inventory as a hud module!", category = ModuleCategory.RENDER, fileName = "mapvieww1www")
public class MapView extends FalcunHudModule {
	public MapView() {
		this.width.setValue(300);
		this.height.setValue(300);
	}

	@FalcunSetting("Element Scale Multiplier")
	@FalcunBounds(min = 0.5f, max = 5f)
	public FalcunValue<Float> scaleMultiplier = new FalcunValue<>(1.0f);

	private static final ResourceLocation resource = new ResourceLocation("textures/map/map_icons.png");

	@Override
	public void render() {
		int x1 = getIntFromDouble(this.screenPosition.getValue().first), y1 = getIntFromDouble(this.screenPosition.getValue().second);
		if (Falcun.minecraft.thePlayer == null) return;
		for (ItemStack stack : Falcun.minecraft.thePlayer.inventory.mainInventory) {
			if (stack == null || stack.getItem() == null) {
				continue;
			}
			if (stack.getItem() instanceof ItemMap) {
				ItemMap im = (ItemMap) stack.getItem();
				MapData md = im.getMapData(stack, Falcun.minecraft.theWorld);
				MapItemRenderer mir = Falcun.minecraft.entityRenderer.getMapItemRenderer();
				try {
					MapItemRenderer.Instance obj = mir.loadedMaps.get(md.mapName);
					DynamicTexture dt = obj.mapTexture;
					GuiUtils.drawShape(new GuiRegion(x1, y1, width.getValue(), height.getValue()), 0xFFF1C27D, 0, true, 0);
					GlStateManager.color(1f, 1f, 1f, 1f);
					GlStateManager.bindTexture(dt.getGlTextureId());
					Tessellator tess = Tessellator.getInstance();
					WorldRenderer wr = tess.getWorldRenderer();
					GlStateManager.disableBlend();
					wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
					wr.pos(x1, y1, 0).tex(0, 0).endVertex();
					wr.pos(x1, y1 + height.getValue(), 0).tex(0, 1).endVertex();
					wr.pos(x1 + width.getValue(), y1 + height.getValue(), 0).tex(1, 1).endVertex();
					wr.pos(x1 + width.getValue(), y1, 0).tex(1, 0).endVertex();
					tess.draw();
					Falcun.minecraft.getTextureManager().bindTexture(resource);
					float c = width.getValue() / 128f;
					float d = height.getValue() / 128f;

					float scalexy = 4.0f * scaleMultiplier.getValue();
					float scalez = 3.0f * scaleMultiplier.getValue();

					for (Map.Entry<String, Vec4b> vec : md.mapDecorations.entrySet()) {
						int xOffset = vec.getValue().func_176112_b();
						int yOffset = vec.getValue().func_176113_c();
						int x = (int) (((xOffset + 128) / 255f) * 128);
						int y = (int) (((yOffset + 128) / 255f) * 128);
						x *= c;
						y *= d;
						int relX = 5 + x + x1;
						int relY = 5 + y + y1;
						GL11.glPushMatrix();
						GlStateManager.translate(relX, relY, 0);
						GlStateManager.scale(scalexy, scalexy, scalez);
						GlStateManager.rotate((float) (vec.getValue().func_176111_d() * 360) / 16.0F, 0.0F, 0.0F, 1.0F);
						byte b0 = vec.getValue().func_176110_a();
						float f1 = (float) (b0 % 4 + 0) / 4.0F;
						float f2 = (float) (b0 / 4 + 0) / 4.0F;
						float f3 = (float) (b0 % 4 + 1) / 4.0F;
						float f4 = (float) (b0 / 4 + 1) / 4.0F;
						wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
						wr.pos(-1.0D, 1.0D, 1).tex(f1, f2).endVertex();
						wr.pos(1.0D, 1.0D, 1).tex(f3, f2).endVertex();
						wr.pos(1.0D, -1.0D, 1).tex(f3, f4).endVertex();
						wr.pos(-1.0D, -1.0D, 1).tex(f1, f4).endVertex();
						tess.draw();
						GL11.glPopMatrix();
					}

				} catch (Throwable err) {
					err.printStackTrace();
				}
				return;
			}
		}
		

	}

	@Override
	public void renderPreview() {
		int x1 = getIntFromDouble(this.screenPosition.getValue().first), y1 = getIntFromDouble(this.screenPosition.getValue().second);
		GuiRegion gr = new GuiRegion(x1, y1, width.getValue(), height.getValue());
		render();
		int color = isEnabled() ? Color.GREEN.getRGB() : Color.RED.getRGB();
		GuiUtils.drawShape(gr, color, 0, false, 1f);
	}
}
