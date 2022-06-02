package falcun.net.api.guidan.elements;

import falcun.net.api.guidragon.GuiUtils;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

public class FalcunHorizontalGradient extends BasicElement<FalcunHorizontalGradient> {

	public static FalcunHorizontalGradient makeGradientFast(BasicElement<?> parent, int l, int r) {
		return new FalcunHorizontalGradient().setBothColors(l, r);
	}

	public int leftColor, rightColor;

	public FalcunHorizontalGradient setLeftColor(int color) {
		this.leftColor = color;
		return this;
	}

	public FalcunHorizontalGradient setRightColor(int color) {
		this.rightColor = color;
		return this;
	}

	public FalcunHorizontalGradient setBothColors(int l, int r) {
		this.leftColor = l;
		this.rightColor = r;
		return this;
	}

	@Override
	public void render() {
		if (this.leftColor == 0 && this.rightColor == 0) {
			return;
		}
		Tessellator tess = Tessellator.instance;
		WorldRenderer wr = tess.worldRenderer;
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		GlStateManager.shadeModel(GL11.GL_SMOOTH);
		GlStateManager.disableCull();
		wr.begin(GL11.GL_QUAD_STRIP, DefaultVertexFormats.POSITION_COLOR);
		wr.pos(this.x, this.y, 0);
		GuiUtils.color(wr, leftColor);
		wr.endVertex();
		wr.pos(this.x, this.y + this.height, 0);
		GuiUtils.color(wr, leftColor);
		wr.endVertex();
		wr.pos(this.x + this.width, this.y + this.height, 0);
		GuiUtils.color(wr, rightColor);
		wr.endVertex();
		wr.pos(this.x + this.width, this.y, 0);
		GuiUtils.color(wr, rightColor);
		wr.endVertex();
		tess.draw();
		GlStateManager.enableCull();
		GlStateManager.shadeModel(GL11.GL_FLAT);
		GlStateManager.disableBlend();
		GlStateManager.enableTexture2D();
	}
}
