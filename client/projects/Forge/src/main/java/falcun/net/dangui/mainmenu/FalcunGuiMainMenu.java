package falcun.net.dangui.mainmenu;

import falcun.net.api.dangui.FalcunGui;
import falcun.net.api.dangui.pages.FalcunGuiPage;
import falcun.net.dangui.mainmenu.pages.PageSelector;
import falcun.net.dangui.mainmenu.pages.accounts.AccountsPage;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.ElementRenderer;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;

import java.util.Arrays;
import java.util.List;

public class FalcunGuiMainMenu extends FalcunGui {
	// 	MAIN("Main Menu"), ACCOUNTS("ACCOUNTS"), OPTIONS("OPTIONS"), MODS("MOD LIST"), SINGLE("SINGLE PLAYER"), MULTI("MULTI-PLAYER");
	private FalcunGuiPage pageSelector = new PageSelector();
	private FalcunGuiPage accountsPage = new AccountsPage(), optionsPage, modsPage, singlePage, multiPage;

	public static MainMenuPage selectedPage = MainMenuPage.MAIN;

	public void init() {
		pageSelector = new PageSelector();
		accountsPage = new AccountsPage();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);

	}

	@Override
	protected List<BasicElement<?>>[] getElements() {
		switch (selectedPage) {
			case MAIN:
			default:
				return grabElements(pageSelector.getElements());
//			case ACCOUNTS:
//				return grabElements(accountsPage.getElements());
		}
	}

}
