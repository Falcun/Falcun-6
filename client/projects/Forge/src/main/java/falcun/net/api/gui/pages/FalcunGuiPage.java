package falcun.net.api.gui.pages;

import falcun.net.Falcun;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;

import java.util.List;

public interface FalcunGuiPage {
	int getWidth();

	int getHeight();

	List<BasicElement<?>> getElements();

	default int getFullWidth(){
		return Falcun.minecraft.displayWidth;
	}

	default int getFullHeight(){
		return Falcun.minecraft.displayHeight;
	}
}
