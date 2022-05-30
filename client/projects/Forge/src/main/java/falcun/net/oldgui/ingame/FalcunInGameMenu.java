package falcun.net.oldgui.ingame;

import falcun.net.Falcun;
import falcun.net.api.colors.FalcunGuiColorPalette;
import falcun.net.api.fonts.FalcunFont;
import falcun.net.api.fonts.Fonts;
import falcun.net.api.oldgui.GuiUtils;
import falcun.net.api.oldgui.animations.FalcunSaberSnake;
import falcun.net.api.oldgui.components.Component;
import falcun.net.api.oldgui.components.OutlinedComponent;
import falcun.net.api.oldgui.components.rect.ColorSquare;
import falcun.net.api.oldgui.components.rect.ColorSquareOutline;
import falcun.net.api.oldgui.components.text.Label;
import falcun.net.api.oldgui.components.texture.TextureSquare;
import falcun.net.api.oldgui.components.texture.TextureSquareOpeningAnimated;
import falcun.net.api.oldgui.effects.EnterExitEffect;
import falcun.net.api.oldgui.effects.OnClickEffect;
import falcun.net.api.oldgui.menu.FalcunEmptyPage;
import falcun.net.api.oldgui.menu.FalcunMenu;
import falcun.net.api.oldgui.menu.FalcunPage;
import falcun.net.api.oldgui.region.GuiRegion;
import falcun.net.api.oldgui.scaling.FalcunScaling;
import falcun.net.api.textures.FalcunTexture;
import falcun.net.managers.FalcunConfigManager;
import falcun.net.oldgui.ingame.cosmetics.FalcunCosmeticsPage;
import falcun.net.oldgui.ingame.fps.FalcunFPSPage;
import falcun.net.oldgui.ingame.groups.FalcunGroupsPage;
import falcun.net.oldgui.ingame.hud.FalcunHUDPage;
import falcun.net.oldgui.ingame.macros.FalcunMacrosPage;
import falcun.net.oldgui.ingame.mods.FalcunModPage;
import falcun.net.oldgui.ingame.profiles.FalcunProfilesPage;
import falcun.net.util.FalcunDevEnvironment;
import net.minecraft.util.ResourceLocation;

import java.util.LinkedList;
import java.util.List;

import static falcun.net.oldgui.ingame.FalcunInGameMenu.Page.*;

public class FalcunInGameMenu extends FalcunMenu {

	public static Page page = MODS;

	public enum Page { // tt
		MODS, FPS, MACROS, PROFILES, COSMETICS, GROUPS, HUD
	}

	FalcunPage cosmeticsPage = new FalcunCosmeticsPage(width, height);
	FalcunPage fpsPage = new FalcunFPSPage(width, height);
	FalcunPage groupsPage = new FalcunGroupsPage(width, height);
	FalcunPage hudPage = new FalcunHUDPage(width, height);
	FalcunPage macrosPage = new FalcunMacrosPage(width, height);
	FalcunPage modsPage = new FalcunModPage(width, height);
	FalcunPage profilesPage = new FalcunProfilesPage(width, height);


	public static List<Component> bgComponents = new LinkedList<>();

	public static List<Component> getBGComponents(int width, int height, FalcunPage page) {
		float scale = 1f / new FalcunScaling(Falcun.minecraft).getScaleFactor();
		final int newWidth = Math.round((float) width / scale);
		final int newHeight = Math.round((float) height / scale);
		List<Component> components = new LinkedList<>();
		int left = (newWidth >> 1) - (page.getBackgroundWidth() >> 1);
		int top = (newHeight >> 1) - (page.getBackgroundHeight() >> 1);
		GuiRegion full = new GuiRegion(0, 0, newWidth, newHeight);
		ColorSquare bg = new ColorSquare(full, () -> 0x33000000);
		GuiRegion topBarGR = new GuiRegion(left, top, page.getBackgroundWidth(), 58);
		ColorSquare topBar = new ColorSquare(topBarGR, FalcunGuiColorPalette::getBackgroundColor, 1);

		GuiRegion fullOutline = new GuiRegion(left, top, page.getBackgroundWidth(), page.getBackgroundHeight());
		ColorSquareOutline fullOutlineC = new ColorSquareOutline(fullOutline, FalcunGuiColorPalette::getLineColor, 2);
		fullOutlineC.pinTo(topBar);
		components.add(bg);

		components.add(topBar);

		{
			GuiRegion animatedTopGR = new GuiRegion(left, top, page.getBackgroundWidth(), page.getBackgroundHeight());
			int left2 = topBarGR.x + (topBarGR.width >> 1);
			left2 -= 275 >> 1;
			animatedTopGR.x = left2;
			animatedTopGR.width = 275;
			animatedTopGR.y -= 200;
			animatedTopGR.height = 200;
//			ResourceLocation[] locs = new ResourceLocation[60];
//			for (int i = 0; i < 60; i++) {
//				locs[i] = new ResourceLocation("falcun:animated/logo/falcun-logo-" + (i + 1) + ".png");
//			}

			TextureSquareOpeningAnimated animation = new TextureSquareOpeningAnimated(animatedTopGR, 22L, FalcunTexture.logoAnimation);
			animation.pinTo(topBar);
			components.add(animation);

		}

		GuiRegion logoGR = new GuiRegion(left + 12, top, 175, 58);
		ResourceLocation loc = new ResourceLocation("falcun:gui/falcun-bg.png");
		TextureSquare logo = new TextureSquare(logoGR, loc);
		logo.pinTo(topBar);
		components.add(logo);

		GuiRegion gr = new GuiRegion(left, top + topBarGR.height, page.getBackgroundWidth(), page.getBackgroundHeight() - topBarGR.height);
		OutlinedComponent outline = new OutlinedComponent(gr.offSet(0, -topBarGR.height), () -> 0x00000000, 1);
		outline.region.height += topBarGR.height;
		outline.pinTo(topBar);

		ColorSquare csBG = new ColorSquare(gr, FalcunGuiColorPalette::getBackgroundColor2, 0);
		csBG.pinTo(topBar);
		components.add(csBG);

		GuiRegion underLineGR = new GuiRegion(left, topBarGR.getBottom(), page.getBackgroundWidth(), 2);
		ColorSquare underLine = new ColorSquare(underLineGR, FalcunGuiColorPalette::getLineColor, 0);
		underLine.pinTo(topBar);
		components.add(underLine);

		List<Component> gradients = GuiUtils.makeBoxShadows(fullOutlineC.region.offSet(0, 0), 5);
		for (Component gradient : gradients) {
			gradient.pinTo(topBar);
			components.add(gradient);
		}
		components.add(fullOutlineC);

		{
			int x = left + 20 + logo.region.width;
			FalcunFont font = Fonts.Roboto12;
			for (final Page value : values()) {
				String name = value.name().toUpperCase();
				int wid = (int) font.getStringWidth(name);
				wid += 10;
				GuiRegion nextGr = new GuiRegion(x, top, wid, 58);
				GuiRegion selectedGr = nextGr.offSet(0, 0);
				selectedGr.x -= 10;
				selectedGr.width += 20;
				ColorSquare selected = new ColorSquare(selectedGr, () -> FalcunInGameMenu.page == value ? 0xff111111 : 0x00000000, 0);
				components.add(selected);
				selected.pinTo(topBar);

				OutlinedComponent snakey = new OutlinedComponent(selectedGr.offSet(2, 2), () -> 0x00000000, 1);
				snakey.region.width -= 4;
				snakey.region.height -= 4;
				snakey.effects.add(new FalcunSaberSnake(snakey, () -> FalcunInGameMenu.page == value ? 0x55bf0000 : 0x00000000));

				components.add(snakey);
				snakey.pinTo(topBar);
				Label txt = new Label(nextGr, name, 1, 0xffffffff, font);
				txt.effects.add(new EnterExitEffect((comp, over) -> txt.underline = over));
				txt.effects.add(new OnClickEffect(comp -> FalcunInGameMenu.page = value));
				txt.pinTo(topBar);
				components.add(txt);
				x += 20;
				x += wid;
			}
		}


		return components;
	}

	@Override
	public List<Component> getComponents() {
		if (bgComponents.isEmpty()) {
			return bgComponents = getBGComponents(width, height, FalcunEmptyPage.emptyPage);
		}
//		GuiUtil.doBlur(1);
		List<Component> returnComps = new LinkedList<>();
		switch (page) {
			case MODS:
				returnComps.addAll(modsPage.getComponents());
				break;
			case FPS:
				returnComps.addAll(fpsPage.getComponents());
				break;
			case MACROS:
				returnComps.addAll(macrosPage.getComponents());
				break;
			case PROFILES:
				returnComps.addAll(profilesPage.getComponents());
				break;
			case COSMETICS:
				returnComps.addAll(cosmeticsPage.getComponents());
				break;
			case GROUPS:
				returnComps.addAll(groupsPage.getComponents());
				break;
			case HUD:
				returnComps.addAll(hudPage.getComponents());
				break;
			default:
				break;
		}
		return returnComps;
	}

	@Override
	protected void init() {
		bgComponents.clear();
		float scale = 1f / new FalcunScaling(Falcun.minecraft).getScaleFactor();
		final int newWidth = Math.round((float) width / scale);
		final int newHeight = Math.round((float) height / scale);
		int left = (newWidth >> 1) - (FalcunEmptyPage.emptyPage.getBackgroundWidth() >> 1);
		int top = (newHeight >> 1) - (FalcunEmptyPage.emptyPage.getBackgroundHeight() >> 1);
		top += 58;
		bgComponents = getBGComponents(width, height, FalcunEmptyPage.emptyPage);
		if (isScaled || FalcunDevEnvironment.isDevEnvironment) {
			modsPage = new FalcunModPage(left, top);
			fpsPage = new FalcunFPSPage(left, top);
			macrosPage = new FalcunMacrosPage(left, top);
			profilesPage = new FalcunProfilesPage(left, top);
			cosmeticsPage = new FalcunCosmeticsPage(left, top);
			groupsPage = new FalcunGroupsPage(left, top);
			hudPage = new FalcunHUDPage(left, top);
		}
	}

	public void onGuiClosed() {
		Falcun.saveConfig();
	}
}
