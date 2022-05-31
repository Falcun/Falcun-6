package falcun.net.api.dangui.pages;

import falcun.net.Falcun;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;

import java.util.List;

public interface FalcunGuiPage {

//	List<BasicElement<?>> elements = new LinkedList<>();
//
//	default List<BasicElement<?>> getElements() {
//		return elements;
//	}

	int getWidth();

	int getHeight();


	default int getFullWidth() {
		return Falcun.minecraft.displayWidth;
	}

	default int getFullHeight() {
		return Falcun.minecraft.displayHeight;
	}

	List<BasicElement<?>> getElements();
}
