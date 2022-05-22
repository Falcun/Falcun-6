package falcun.net.api.gui.components;


import falcun.net.api.gui.GuiUtils;
import falcun.net.api.gui.components.Component;
import falcun.net.api.gui.region.GuiRegion;

import java.util.function.Supplier;
import java.util.function.Consumer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;
import falcun.net.api.gui.effects.*;


public class CurvedComponent extends ColoredComponent {

	int curve;

	public CurvedComponent(GuiRegion region, Supplier<Integer> color, int curve) {
		super(region, color);
		this.curve = curve;
	}

}
