package the_fireplace.ias;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import ru.vidtu.ias.Config;
import ru.vidtu.ias.utils.Converter;
import ru.vidtu.ias.utils.Expression;
import ru.vidtu.ias.utils.SkinRenderer;
import the_fireplace.ias.gui.GuiAccountSelector;
import the_fireplace.ias.gui.GuiButtonWithImage;
/**
 * @author The_Fireplace
 */
@Mod(modid = "ias", clientSideOnly = true, guiFactory = "ru.vidtu.ias.IASGuiFactory")
public class IAS {
	public static final Gson GSON = new Gson();
	public static final Logger LOG = LogManager.getLogger("IAS");
	private static int textX, textY;
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		Minecraft mc = Minecraft.getMinecraft();
		Config.load(mc);
		Converter.convert(mc);
		mc.addScheduledTask(() -> {
			SkinRenderer.loadAllAsync(mc, false, () -> {});
		});
	}
	
	@SubscribeEvent
	public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event) {
		if (event.gui instanceof GuiMainMenu) {
			try {
				if (StringUtils.isNotBlank(Config.textX) && StringUtils.isNotBlank(Config.textY)) {
					textX = (int) new Expression(Config.textX.replace("%width%", Integer.toString(event.gui.width)).replace("%height%", Integer.toString(event.gui.height))).parse(0);
					textY = (int) new Expression(Config.textY.replace("%width%", Integer.toString(event.gui.width)).replace("%height%", Integer.toString(event.gui.height))).parse(0);
				} else {
					textX = event.gui.width / 2;
					textY = event.gui.height / 4 + 48 + 72 + 12 + 22;
				}
			} catch (Throwable t) {
				t.printStackTrace();
				textX = event.gui.width / 2;
				textY = event.gui.height / 4 + 48 + 72 + 12 + 22;
			}
			if (Config.showOnTitleScreen) {
				int btnX = event.gui.width / 2 + 104;
				int btnY = event.gui.height / 4 + 48 + 48;
				try {
					if (StringUtils.isNotBlank(Config.btnX) && StringUtils.isNotBlank(Config.btnY)) {
						btnX = (int) new Expression(Config.btnX.replace("%width%", Integer.toString(event.gui.width)).replace("%height%", Integer.toString(event.gui.height))).parse(0);
						btnY = (int) new Expression(Config.btnY.replace("%width%", Integer.toString(event.gui.width)).replace("%height%", Integer.toString(event.gui.height))).parse(0);
					}
				} catch (Throwable t) {
					t.printStackTrace();
					btnX = event.gui.width / 2 + 104;
					btnY = event.gui.height / 4 + 48 + 48;
				}
				event.buttonList.add(new GuiButtonWithImage(btnX, btnY, () -> event.gui.mc.displayGuiScreen(new GuiAccountSelector(event.gui))));
			}
		}
		if (event.gui instanceof GuiMultiplayer) {
			if (Config.showOnMPScreen) {
				event.buttonList.add(new GuiButtonWithImage(event.gui.width / 2 + 4 + 76 + 79, event.gui.height - 28, () -> {
					event.gui.mc.displayGuiScreen(new GuiAccountSelector(event.gui));
				}));
			}
		}
	}
	
	@SubscribeEvent
	public void onGuiAction(GuiScreenEvent.ActionPerformedEvent.Post event) {
		if (event.button instanceof GuiButtonWithImage) {
			((GuiButtonWithImage)event.button).click();
		}
	}
	
	@SubscribeEvent
	public void onGuiRender(GuiScreenEvent.DrawScreenEvent.Post event) {
		if (event.gui instanceof GuiMainMenu) {
			Minecraft mc = event.gui.mc;
			event.gui.drawCenteredString(mc.fontRendererObj, I18n.format("ias.loggedinas", mc.getSession().getUsername()), textX, textY, 0xFFCC8888);
		}
		if (event.gui instanceof GuiMultiplayer) {
			Minecraft mc = event.gui.mc;
			if (mc.getSession().getToken().equals("0") || mc.getSession().getToken().equals("-")) {
				List<String> list = mc.fontRendererObj.listFormattedStringToWidth(I18n.format("ias.offlinemode"), event.gui.width);
				for (int i = 0; i < list.size(); i++) {
					event.gui.drawCenteredString(mc.fontRendererObj, list.get(i), event.gui.width / 2, i * 9 + 1, 16737380);
				}
			}
		}
	}
}
