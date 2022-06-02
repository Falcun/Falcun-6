package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.Listener;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class ElementRenderer {
	public static <T extends BasicElement<?>> void renderElement(BasicElement<T> element) {
		if (element.shouldRender()) {
			element.listeners.forEach(elementListener -> elementListener.render((T) element, Listener.Phase.PRE));
			element.dimensionsCalculator.computeChildBounds(element);
			ElementRenderer.renderDebug(element);
			element.render();
			if (element.outlineElement != null) {
				ElementRenderer.renderElement(element.outlineElement);
			}
			element.listeners.forEach(elementListener -> elementListener.render((T) element, Listener.Phase.PRECHILD));
			ElementRenderer.renderElements(element.children);
			element.listeners.forEach(elementListener -> elementListener.render((T) element, Listener.Phase.POST));
		}
	}

	public static void renderElements(List<BasicElement<?>> elementList) {
		elementList.forEach(ElementRenderer::renderElement);
	}

	public static void renderDebug(BasicElement<?> element) {
		Tessellator tess = Tessellator.getInstance();
		WorldRenderer wr = tess.getWorldRenderer();
		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		// blue for the element area
		GlStateManager.color(0.0f, 0.0f, 1.0f, 0.5f);
		GL11.glLineWidth(2f);
		wr.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
		wr.pos(element.x, element.y, 0).endVertex();
		wr.pos(element.x, element.y + element.height, 0).endVertex();
		wr.pos(element.x + element.width, element.y + element.height, 0).endVertex();
		wr.pos(element.x + element.width, element.y, 0).endVertex();
		tess.draw();
		// green for padding
		GlStateManager.color(0.0f, 1.0f, 0.0f, 0.5f);
		if (element.paddingLeft != 0) {
			wr.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
			wr.pos(element.x, element.y, 0).endVertex();
			wr.pos(element.x, element.y + element.height, 0).endVertex();
			wr.pos(element.x + element.paddingLeft, element.y + element.height, 0).endVertex();
			wr.pos(element.x + element.paddingLeft, element.y, 0).endVertex();
			tess.draw();
		}
		if (element.paddingRight != 0) {
			wr.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
			wr.pos(element.x + element.width - element.paddingRight, element.y, 0).endVertex();
			wr.pos(element.x + element.width - element.paddingRight, element.y + element.height, 0).endVertex();
			wr.pos(element.x + element.width + element.paddingRight - element.paddingLeft, element.y + element.height, 0).endVertex();
			wr.pos(element.x + element.width + element.paddingRight - element.paddingLeft, element.y, 0).endVertex();
			tess.draw();
		}
		if (element.paddingTop != 0) {
			wr.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
			wr.pos(element.x + element.paddingLeft, element.y, 0).endVertex();
			wr.pos(element.x + element.paddingLeft, element.y + element.paddingTop, 0).endVertex();
			wr.pos(element.x + element.width - element.paddingLeft, element.y + element.paddingTop, 0).endVertex();
			wr.pos(element.x + element.width - element.paddingLeft, element.y, 0).endVertex();
			tess.draw();
		}
		if (element.paddingBottom != 0) {
			wr.begin(GL11.GL_LINE_LOOP, DefaultVertexFormats.POSITION);
			wr.pos(element.x + element.paddingLeft, element.y + element.height - element.paddingBottom, 0).endVertex();
			wr.pos(element.x + element.paddingLeft, element.y + element.height, 0).endVertex();
			wr.pos(element.x + element.width - element.paddingLeft, element.y + element.height, 0).endVertex();
			wr.pos(element.x + element.width - element.paddingLeft, element.y + element.height - element.paddingBottom, 0).endVertex();
			tess.draw();
		}
		GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
		GlStateManager.enableTexture2D();
		GL11.glLineWidth(1f);
	}
}