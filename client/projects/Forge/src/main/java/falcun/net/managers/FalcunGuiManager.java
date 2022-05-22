package falcun.net.managers;

import falcun.net.Falcun;
import falcun.net.gui.container.playerinventory.FalcunGuiInventory;
import falcun.net.gui.mainmenu.FalcunMainMenu;
import falcun.net.gui.ingame.FalcunInGameMenu;
//import falcun.net.gui.mainmenu.FalcunModListMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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

