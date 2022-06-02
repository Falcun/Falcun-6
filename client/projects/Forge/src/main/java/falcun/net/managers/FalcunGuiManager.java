package falcun.net.managers;

import falcun.net.Falcun;
import falcun.net.guidan.mainmenu.FalcunGuiMainMenu;
import falcun.net.guidragonclient.container.playerinventory.FalcunGuiInventory;
import falcun.net.guidragonclient.mainmenu.FalcunMainMenu;
import falcun.net.guidragonclient.ingame.FalcunInGameMenu;
import net.minecraft.client.gui.*;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraftforge.fml.common.ModContainer;
import the_fireplace.ias.gui.GuiAccountSelector;

public final class FalcunGuiManager {

	public static final FalcunInGameMenu modMenu = new FalcunInGameMenu();
//	private static final FalcunModListMenu modListMenu = new FalcunModListMenu();

	public static void openMainMenu() {
//		Falcun.minecraft.displayGuiScreen(new FalcunMainMenu());
		Falcun.minecraft.displayGuiScreen(new FalcunGuiMainMenu());
	}

	public static void openSinglePlayerMenu() {
		Falcun.minecraft.displayGuiScreen(new GuiSelectWorld(new FalcunGuiMainMenu()));
	}

	public static void openMultiPlayerMenu() {
		Falcun.minecraft.displayGuiScreen(new GuiMultiplayer(new FalcunGuiMainMenu()));
	}

	public static void openOptionsMenu(GuiScreen mainMenu) {
		Falcun.minecraft.displayGuiScreen(new GuiOptions(mainMenu, Falcun.minecraft.gameSettings));
//		Falcun.minecraft.displayGuiScreen(new GuiModList(mainMenu));
	}

	public static void openModListMenu() {
//		Falcun.minecraft.displayGuiScreen(new FalcunModListMenu());
//		Falcun.minecraft.displayGuiScreen(new DanFrame());
//		Falcun.minecraft.displayGuiScreen(new FalcunMainMenu());
		Falcun.minecraft.displayGuiScreen(new FalcunGuiMainMenu());
	}

	public static void openAccountSwitchMenu(GuiScreen guiScreen) {
		Falcun.minecraft.displayGuiScreen(new GuiAccountSelector(guiScreen));
	}

	public static void openModConfigMenu(ModContainer modContainer) {
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

