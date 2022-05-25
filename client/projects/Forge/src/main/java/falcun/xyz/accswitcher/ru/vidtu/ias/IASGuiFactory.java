package falcun.xyz.accswitcher.ru.vidtu.ias;

import java.util.Collections;
import java.util.Set;

import falcun.xyz.accswitcher.ru.vidtu.ias.gui.IASConfigScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

public class IASGuiFactory implements IModGuiFactory {
	@Override
	public void initialize(Minecraft mc) {}

	@Override
	public Class<? extends GuiScreen> mainConfigGuiClass() {
		return IASConfigScreen.class;
	}

	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
		return Collections.emptySet();
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
		return null;
	}

}
