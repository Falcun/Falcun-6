package falcun.net.managers;

import falcun.net.Falcun;
import falcun.net.oldgui.container.playerinventory.FalcunGuiInventory;
import falcun.net.oldgui.mainmenu.FalcunMainMenu;
import falcun.net.oldgui.ingame.FalcunInGameMenu;
//import falcun.net.gui.mainmenu.FalcunModListMenu;
import falcun.net.oldgui.test.DanFrame;
import falcun.xyz.accswitcher.the_fireplace.ias.gui.GuiAccountSelector;
import net.minecraft.client.gui.*;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraftforge.fml.common.ModContainer;

public final class FalcunGuiManager {

	public static final FalcunInGameMenu modMenu = new FalcunInGameMenu();
//	private static final FalcunModListMenu modListMenu = new FalcunModListMenu();

	public static void openMainMenu() {
		Falcun.minecraft.displayGuiScreen(new FalcunMainMenu());
	}

	public static void openSinglePlayerMenu() {
		Falcun.minecraft.displayGuiScreen(new GuiSelectWorld(new FalcunMainMenu()));
	}

	public static void openMultiPlayerMenu() {
		Falcun.minecraft.displayGuiScreen(new GuiMultiplayer(new FalcunMainMenu()));
	}

	public static void openOptionsMenu(FalcunMainMenu mainMenu) {
		Falcun.minecraft.displayGuiScreen(new GuiOptions(mainMenu, Falcun.minecraft.gameSettings));
//		Falcun.minecraft.displayGuiScreen(new GuiModList(mainMenu));
	}

	public static void openModListMenu() {
//		Falcun.minecraft.displayGuiScreen(new FalcunModListMenu());
		Falcun.minecraft.displayGuiScreen(new DanFrame());
	}

	public static void openAccountSwitchMenu(GuiScreen guiScreen) {
		Falcun.minecraft.displayGuiScreen(new GuiAccountSelector(guiScreen));
	}

	public static void openModConfigMenu(ModContainer modContainer) {
//		Falcun.minecraft.displayGuiScreen(new GuiMod);
	}

	public static void openModGui() {
		Falcun.minecraft.displayGuiScreen(modMenu);
	}

	public static void openInventoryGui() {
		if (Falcun.minecraft.playerController.isRidingHorse()) {
			Falcun.minecraft.thePlayer.sendHorseInventory();
		} else {
			Falcun.minecraft.getNetHandler().addToSendQueue(new C16PacketClientStatus(C16PacketClientStatus.EnumState.OPEN_INVENTORY_ACHIEVEMENT));
			Falcun.minecraft.displayGuiScreen(new FalcunGuiInventory(Falcun.minecraft.thePlayer));
		}
	}

}

