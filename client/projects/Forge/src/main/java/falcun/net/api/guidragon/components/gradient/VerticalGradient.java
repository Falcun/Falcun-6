package falcun.net.api.guidragon.components.gradient;

import falcun.net.api.guidragon.GuiUtils;
import falcun.net.api.guidragon.components.Component;
import falcun.net.api.guidragon.region.GuiRegion;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.util.function.Supplier;

public class VerticalGradient extends Component {
	public Supplier<Integer> color1, color2;

	public VerticalGradient(GuiRegion region, Supplier<Integer> color, Supplier<Integer> color2) {
		super(region);
		this.color1 = color;
		this.color2 = color2;
	}

	@Override
	public void draw(int mX, int mY) {
		GuiUtils.drawNonTextured((tess, wr) -> {
			GlStateManager.disableAlpha();
			GlStateManager.shadeModel(7425);
			wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
			int top = color1.get(), bottom = color2.get();
//			{
//				wr.pos(region.x, region.y, 0);
//				GuiUtils.color(wr, left);
//				wr.endVertex();

//				wr.pos(region.x, region.y + region.height, 0);
//				GuiUtils.color(wr, left);
//				wr.endVertex();
//
//				wr.pos(region.x + region.width, region.y + region.height, 0);
//				GuiUtils.color(wr, right);
//				wr.endVertex();
//
//				wr.pos(region.x + region.width, region.y, 0);
//				GuiUtils.color(wr, right);
//				wr.endVertex();
//			}
			{
				wr.pos(region.x + region.width, region.y, 0);
				GuiUtils.color(wr, top);
				wr.endVertex();

				wr.pos(region.x, region.y, 0);
				GuiUtils.color(wr, top);
				wr.endVertex();

				wr.pos(region.x, region.y + region.height, 0);
				GuiUtils.color(wr, bottom);
				wr.endVertex();

				wr.pos(region.x + region.width, region.y + region.height, 0);
				GuiUtils.color(wr, bottom);
				wr.endVertex();
			}
			tess.draw();
			GuiUtils.setColor(0xFFFFFFFF);
			GL11.glLineWidth(2f);
			wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);
			tess.draw();
			GL11.glLineWidth(1f);
			GlStateManager.shadeModel(7424);
			GlStateManager.enableAlpha();
		});
	}

}