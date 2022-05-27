package falcun.net.oldgui.ingame.mods;

import falcun.net.api.colors.FalcunGuiColorPalette;
import falcun.net.api.fonts.FalcunFont;
import falcun.net.api.fonts.Fonts;
import falcun.net.api.oldgui.GuiUtils;
import falcun.net.api.oldgui.animations.FalcunSaberSnake;
import falcun.net.api.oldgui.components.Component;

import falcun.net.api.oldgui.components.OutlinedComponent;
import falcun.net.api.oldgui.components.rect.ColorSquare;
import falcun.net.api.oldgui.components.rect.ColorSquareOutline;
import falcun.net.api.oldgui.components.scroll.VerticalScroll;
import falcun.net.api.oldgui.components.text.Label;
import falcun.net.api.oldgui.components.text.Paragraph;
import falcun.net.api.oldgui.components.texture.TextureSquare;
import falcun.net.api.oldgui.effects.EnterExitEffect;
import falcun.net.api.oldgui.effects.OnClickEffect;
import falcun.net.api.oldgui.menu.FalcunPage;

import falcun.net.api.oldgui.region.GuiRegion;
import falcun.net.api.modules.FalcunModule;
import falcun.net.api.modules.inheritance.FalcunSettingsModule;
import falcun.net.oldgui.ingame.FalcunInGameMenu;
import falcun.net.managers.FalcunModuleManager;
import falcun.net.modules.ModuleCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FalcunModPage implements FalcunPage {

	final List<Component> components;
	final List<Component> modulesComponents;

	private static final ResourceLocation settingsIcon = new ResourceLocation("falcun:gui/cog.png");
	private static ModuleCategory category = ModuleCategory.ALL;

	public static FalcunModule selectedModule = null;

	private static FalcunModSettingsPage settingsPage;

	public FalcunModPage(int left, int top) {
		settingsPage = new FalcunModSettingsPage(left, top);
		components = new LinkedList<>(FalcunInGameMenu.bgComponents);
		modulesComponents = new LinkedList<>();
		if (components.isEmpty()) return;
		Component pin = components.get(1);

		int width = getBackgroundWidth();
		int height = getBackgroundHeight() - 58;
		int bottom = top + height;
		int right = left + width;

		GuiRegion categoriesBoxGR = new GuiRegion(left, top, width >> 2, height);
		categoriesBoxGR.width /= 1.3;
		ColorSquare sq = new ColorSquare(categoriesBoxGR, () -> 0x6c0f1313);
		sq.pinTo(pin);
		components.add(sq);
		GuiRegion catLineR = categoriesBoxGR.offSet(categoriesBoxGR.width, 0);
		catLineR.width = 2;
		ColorSquare sq2 = new ColorSquare(catLineR, FalcunGuiColorPalette::getLineColor);
		sq2.pinTo(pin);
		components.add(sq2);

		{ // TODO: MODULE CATEGORIES SCROLL
			ModuleCategory[] categories = ModuleCategory.values();
			int y = top + 5;

			for (final ModuleCategory moduleCategory : categories) {
				String name = moduleCategory.name.toUpperCase();
				FalcunFont font = Fonts.Roboto;
				GuiRegion gr = new GuiRegion(left, y, catLineR.getRight() - left, 32);
				ColorSquare sq3 = new ColorSquare(gr.offSet(0, 0), () -> 0x80111111);
				OutlinedComponent snakey = new OutlinedComponent(gr, () -> 0x00000000, 1);
				snakey.pinTo(pin);
				snakey.effects.add(new FalcunSaberSnake(snakey, () -> FalcunModPage.category == moduleCategory ? 0x55bf0000 : 0x00000000));
				GuiRegion iconRegion = gr.offSet(4, 1);
				iconRegion.height -= 4;
				iconRegion.width = iconRegion.height;
				TextureSquare square = new TextureSquare(iconRegion, moduleCategory.icon);
				square.pinTo(pin);
				Label label = new Label(iconRegion.offSet((iconRegion.width << 1) - (iconRegion.width >> 1), 0), name, 0, 0xffffffff, font);
				label.pinTo(pin);
				sq3.effects.add(new EnterExitEffect((comp, over) -> {
					sq3.color = over ? () -> 0xff111111 : () -> 0x80111111;
					label.underline = over;
				}));
				sq3.effects.add(new OnClickEffect(comp -> {
					FalcunModPage.category = moduleCategory;
					int L = catLineR.getRight();
					int X = L + 30;
					int Y = top + 30;
					selectedModule = null;
					addModules(X, Y, right - 2, bottom - 2, pin);
				}));

				List<Component> gradients = GuiUtils.makeBoxVerticalShadows(gr, 4);

				components.add(sq3);
				components.add(label);
				components.add(square);
				components.add(snakey);
				for (Component gradient : gradients) {
					components.add(gradient);
					gradient.pinTo(pin);
				}
				y += 32;
				y += 12;
			}
		}
		left = catLineR.getRight();

		int x = left + 30;
		int y = top + 30;


		addModules(x, y, right - 2, bottom - 2, pin);
	}

	String filter = "";


	void addModules(int x, int y, int right, int bottom, Component pin) {
		modulesComponents.clear();
		{ // TODO: MODULES TOP BAR
			GuiRegion gr = new GuiRegion(x - 30, y - 30, right - x + 30, 60);
			ColorSquare top = new ColorSquare(gr, FalcunGuiColorPalette::getLineColor);
			top.pinTo(pin);
//			modulesComponents.add(top);
			String txt = category.name.toUpperCase();
			FalcunFont font = Fonts.RobotoTitle;
			int wid = (int) font.getStringWidth(txt);
			GuiRegion labelReg = gr.offSet(35, -2);
			labelReg.width = wid;
			Label label = new Label(labelReg, txt, 1, 0xffffffff, font);
			label.pinTo(pin);
			modulesComponents.add(label);
			GuiRegion lineGr = new GuiRegion(x, y, right - x, 1);
		}

		y += 50;
		final int startX = x + 7;
		GuiRegion boxRegion = new GuiRegion(x, y, right - x, bottom - y);
		y += 7;
		x += 7;
		final Collection<FalcunModule> modules = filter.isEmpty() || filter.replaceAll(" ", "").isEmpty() ? FalcunModuleManager.getModulesSorted(category) : FalcunModuleManager.getModulesSorted(category).stream().filter(m -> m.getName().toLowerCase().contains(filter.toLowerCase())).collect(Collectors.toList());

		block1: { // TODO: REMOVE FULL BLOCK WHEN FINISHED
			if (true){
				break block1;
			}
			for (int i = 0; i < 99; ++i) { // TODO: REMOVE THIS ONCE MODULE DEVELOPMENT HAS STARTED
				for (FalcunModule module : modules) {
					modules.add(module);
					break;
				}
			}
		}

		GuiRegion scrollRegion = new GuiRegion(right - 20, y, 8, bottom - y - 10);
		VerticalScroll scroll = new VerticalScroll(scrollRegion, () -> 0x80646464, () -> 0xff333333, boxRegion);

		scroll.pinTo(pin);
		for (final FalcunModule falcunModule : modules) {
			GuiRegion gr = new GuiRegion(x, y, 170, 150);
			Paragraph label = new Paragraph(gr.offSet(0, 50), () -> 0xffffffff, falcunModule.getName(), Fonts.RobotoMiniHeader);
			ColorSquare square = new ColorSquare(gr, () -> 0x80111111);
			square.effects.add(new EnterExitEffect((comp, over) -> square.color = over ? () -> 0xff111111 : () -> 0x80111111));
			ColorSquareOutline csoutline = new ColorSquareOutline(gr, () -> 0xff646464, 1);
			GuiRegion cogRegion = csoutline.region.offSet(0, 0);
			cogRegion.x = cogRegion.getRight();
			cogRegion.x -= 30;
			cogRegion.y += 5;
			cogRegion.width = 25;
			cogRegion.height = 25;
			TextureSquare sq = new TextureSquare(cogRegion, settingsIcon);
			ColorSquare sqHover = new ColorSquare(cogRegion, () -> 0x00000000);
			sqHover.effects.add(new EnterExitEffect((comp, over) -> sqHover.color = over ? () -> 0xff282828 : () -> 0x00000000));
			sq.effects.add(new OnClickEffect(comp -> {
				selectedModule = falcunModule;
				settingsPage.update();
			}));
			ColorSquareOutline sqoutline = new ColorSquareOutline(cogRegion, () -> 0xff646464, 1);
			List<Component> gradients = GuiUtils.makeBoxShadows(gr, 7);

			scroll.addComponent(square);
			scroll.addComponent(sqHover);
			scroll.addComponent(sq);
			scroll.addComponent(sqoutline);
			gradients.forEach(scroll::addComponent);
			scroll.addComponent(csoutline);
			scroll.addComponent(label);
			if (!(falcunModule instanceof FalcunSettingsModule)) {
				GuiRegion bottomGr = new GuiRegion(gr.x + 15, gr.getBottom() - 50, gr.width - 30, 30);
				ColorSquare enabledArea = new ColorSquare(bottomGr, () -> 0x00000000);
				enabledArea.effects.add(new EnterExitEffect((comp, over) -> enabledArea.color = () -> over ? 0xff282828 : 0x00000000));
				ColorSquareOutline enabledOutline = new ColorSquareOutline(bottomGr, () -> getEnabledColor(falcunModule), 1.6f);
				Label enabledLabel = new Label(bottomGr.offSet(0, -2), () -> getEnabledText(falcunModule), 1, () -> 0xffffffff, Fonts.Roboto);
				enabledOutline.effects.add(new EnterExitEffect((comp, over) -> enabledLabel.underline = over));
				enabledOutline.effects.add(new OnClickEffect(comp -> {
					if (falcunModule.serverDisabled) {
						Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("This mod is disabled on this server."));
					} else {
						falcunModule.toggle();
					}
				}));
				scroll.addComponent(enabledOutline);
				scroll.addComponent(enabledLabel);
			}
			x += 16;
			x += 170;
			if (x + 170 + 16 > right) {
				x = startX;
				y += 150;
				y += 16;
			}
		}
		modulesComponents.add(scroll);
	}

	private static String getDisabledModMessage(FalcunModule mod) { // TODO: ADD CHAT FORMATTING
		return mod.getName() + " is disabled on this server.";
	}

	String getEnabledText(FalcunModule mod) {
		if (mod.serverDisabled) {
			return "SERVER DISABLED";
		}
		return mod.isEnabled() ? "ENABLED" : "DISABLED";
	}

	int getEnabledColor(FalcunModule mod) {
		if (mod.serverDisabled) {
			return 0xff646464;
		}
		return mod.isEnabled() ? 0xff0a5a20 : 0xff5a0a0c;

	}


	@Override
	public List<Component> getComponents() {
		List<Component> comps = new LinkedList<>(components);
		if (selectedModule == null) {
			comps.addAll(modulesComponents);
		} else {
			comps.addAll(settingsPage.getComponents());
		}
		return comps;
	}
}
