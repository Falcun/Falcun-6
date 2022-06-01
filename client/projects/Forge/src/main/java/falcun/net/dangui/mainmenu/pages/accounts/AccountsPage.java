package falcun.net.dangui.mainmenu.pages.accounts;

import falcun.net.api.dangui.pages.FalcunFullScreenMenuPage;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;

import java.util.LinkedList;
import java.util.List;

public class AccountsPage implements FalcunFullScreenMenuPage {
	private final List<BasicElement<?>> elements;

	public AccountsPage() {
		elements = new LinkedList<>();

	}

	@Override
	public List<BasicElement<?>> getElements() {
		return elements;
	}
}
