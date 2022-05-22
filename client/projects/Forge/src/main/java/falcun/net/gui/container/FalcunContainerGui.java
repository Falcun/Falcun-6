package falcun.net.gui.container;

import falcun.net.api.gui.inheritance.ScalingGui;
import falcun.net.api.gui.menu.FalcunMenu;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public abstract class FalcunContainerGui extends FalcunMenu implements ScalingGui {
	protected static final ResourceLocation inventoryBackground = new ResourceLocation("textures/gui/container/inventory.png");
	public Container inventorySlots;

	protected int guiLeft;
	protected int guiTop;

	protected int xSize = 176;
	protected int ySize = 166;

	public FalcunContainerGui(Container inventorySlots) {
		this.inventorySlots = inventorySlots;
	}

	@Override
	protected void init() {
		super.initGui();
		this.mc.thePlayer.openContainer = this.inventorySlots;
		this.guiLeft = (this.width - this.xSize) / 2;
		this.guiTop = (this.height - this.ySize) / 2;
	}
}
