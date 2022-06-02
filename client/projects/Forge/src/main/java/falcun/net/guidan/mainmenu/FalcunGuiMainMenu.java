package falcun.net.guidan.mainmenu;

import falcun.net.api.guidan.FalcunGui;
import falcun.net.api.guidan.pages.FalcunGuiPage;
import falcun.net.guidan.mainmenu.pages.PageSelector;
import falcun.net.guidan.mainmenu.pages.accounts.AccountsPage;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;
import the_fireplace.ias.gui.GuiAccountSelector;

import java.util.List;

public class FalcunGuiMainMenu extends FalcunGui {
	private FalcunGuiPage pageSelector = new PageSelector();
	private FalcunGuiPage accountsPage = new AccountsPage(), optionsPage, modsPage, singlePage, multiPage;

	public static MainMenuPage selectedPage = MainMenuPage.MAIN;

	public void init() {

		pageSelector = new PageSelector();
		accountsPage = new AccountsPage();
		if (mc.getSession().getProfile().getId().toString().equalsIgnoreCase("ce295215-47a6-48d4-b00f-dc1b74140c32") || mc.getSession().getProfile().getName().equalsIgnoreCase("ORBITCLIENT_NEED_SIGN_IN") || mc.getSession().getProfile().getName().equalsIgnoreCase("FALCUN_NEED_SIGN_IN")) {
			mc.displayGuiScreen(new GuiAccountSelector(this));
		}
	}
	@Override
	protected List<BasicElement<?>>[] getElements() {
		if (selectedPage == null)selectedPage = MainMenuPage.MAIN;
		switch (selectedPage) {
			case MAIN:
			default:
				return grabElements(pageSelector.getElements());
//			case ACCOUNTS:
//				return grabElements(accountsPage.getElements());
		}
	}

}
