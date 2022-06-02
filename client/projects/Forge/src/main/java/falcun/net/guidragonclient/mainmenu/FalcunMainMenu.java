package falcun.net.guidragonclient.mainmenu;

import falcun.net.Falcun;
import falcun.net.api.fonts.FalcunFont;
import falcun.net.api.fonts.Fonts;
import falcun.net.api.guidragon.components.Component;
import falcun.net.api.guidragon.components.gradient.HorizontalGradient;
import falcun.net.api.guidragon.components.rect.ColorSquare;
import falcun.net.api.guidragon.components.rect.ColorSquareOutline;
import falcun.net.api.guidragon.components.text.Label;
import falcun.net.api.guidragon.components.texture.TextureSquareAnimated;
import falcun.net.api.guidragon.effects.EnterExitEffect;
import falcun.net.api.guidragon.effects.OnClickEffect;
import falcun.net.api.guidragon.menu.FalcunMenu;
import falcun.net.api.guidragon.region.GuiRegion;
import falcun.net.api.guidragon.scaling.FalcunScaling;
import falcun.net.api.textures.FalcunTexture;
import falcun.net.managers.FalcunGuiManager;

import java.util.LinkedList;
import java.util.List;

public class FalcunMainMenu extends FalcunMenu {
	private final List<Component> comps = new LinkedList<>();

	@Override
	public List<Component> getComponents() {
		return comps;
	}

	@Override
	protected void init() {
		comps.clear();
		int x = 0, y = 0;
		float scale = 1f / new FalcunScaling(Falcun.minecraft).getScaleFactor();
		final int w = Math.round((float) width / scale);
		final int h = Math.round((float) height / scale);
		{
			ColorSquare cs = new ColorSquare(new GuiRegion(0, 0, w, h), () -> 0xff000000);
			comps.add(cs);
		}
		GuiRegion bg;
		if (w > h || (h >= 500 && w >= 750)) { // 750 x 500 -- 3 : 2
			int wid = w;
			if (w > 750) {
				wid = 750;
				int diff = w - wid;
				diff >>= 1;
				x = diff;
			}
			int hght = (int) (wid / 1.5D);
			int diff = h - hght;
			diff >>= 1;
			bg = new GuiRegion(x, y, wid, hght);
		} else {
			int wid = w;
			if (w > 750) {
				wid = 750;
				int diff = w - wid;
				diff >>= 1;
				x = diff;
			}
			int hght = (int) (wid / 1.5D);
			int diff = h - hght;
			diff >>= 1;
			bg = new GuiRegion(x, y + diff, wid, hght);
		}

//		final ResourceLocation[] locs = new ResourceLocation[112];
//		for (int i = 0; i < 112; i++) {
//			locs[i] = new ResourceLocation("falcun:animated/loading/falcun_layer_" + (i) + ".png");
//		}
		TextureSquareAnimated background = new TextureSquareAnimated(bg, 1L, FalcunTexture.loadingAnimation);
		comps.add(background);
		comps.add(new ColorSquare(bg.offSet(0, bg.height - (bg.height / 10)), () -> 0xff000000));
		x = w >> 1;
		y = h >> 1;
		final FalcunFont font = Fonts.Roboto;
		final int wid = Math.max(w >> 3, (int)((int)font.getStringWidth("REPLAY RECORDINGS") * 1.37));
		x -= wid >> 1;
		GuiRegion gr = new GuiRegion(x, y + 20, wid, 30);
		comps.addAll(addButton(gr, "SINGLE PLAYER", font, FalcunGuiManager::openSinglePlayerMenu));
		gr = gr.offSet(-(gr.width + 20), (gr.height + (45 >> 1)) >> 1);
		comps.addAll(addButton(gr, "OPTIONS", font,()-> FalcunGuiManager.openOptionsMenu(this)));
		gr = gr.offSet(0, 45);
		comps.addAll(addButton(gr, "MODS", font,  FalcunGuiManager::openModListMenu));
		gr.x = x;
		gr = gr.offSet(gr.width + 20, 0);
		comps.addAll(addButton(gr, "REPLAY RECORDINGS", font, () -> {
		}));
		gr = gr.offSet(0, -45);
		comps.addAll(addButton(gr, "ACCOUNTS", font, () -> FalcunGuiManager.openAccountSwitchMenu(this)));
		gr.x = x;
		gr.y = y + 20;

		gr = gr.offSet(0, 45);
		comps.addAll(addButton(gr, "MULTI PLAYER", font, FalcunGuiManager::openMultiPlayerMenu));
		gr = gr.offSet(0, 45);
		comps.addAll(addButton(gr, "QUIT GAME", font, () -> {
		}));
	}

	public static List<Component> addButton(GuiRegion gr, String text, FalcunFont font, Runnable onClick) {
		List<Component> buttonComps = new LinkedList<>();
		ColorSquare cs = new ColorSquare(gr.offSet(0, 0), () -> 0xBB111111, 15);
		buttonComps.add(cs);
		int decAmount = cs.region.width >> 5;
		GuiRegion next = cs.region;
		HorizontalGradient gradient1 = new HorizontalGradient(next.offSet(next.width >> 1, 0), () -> 0x66ad0000, () -> 0x00ffff00);
		gradient1.region.width >>= 1;
		gradient1.region.width -= decAmount;
		HorizontalGradient gradient2 = new HorizontalGradient(next.offSet(0, 0), () -> 0x00ff00ff, () -> 0x66ad0000);
		gradient2.region.width >>= 1;
		gradient2.region.x += decAmount;
		gradient2.region.width -= decAmount;
		ColorSquareOutline outline = new ColorSquareOutline(next, () -> 0xff000000, 1.5f, (next.height >> 1) - 1);
		outline.effects.add(new EnterExitEffect((comp, over) -> {
			if (over) {
				outline.color = () -> 0xAAFFFFFF;
			} else {
				outline.color = () -> 0xff000000;
			}
		}));
		outline.effects.add(new OnClickEffect(comp -> onClick.run()));
		buttonComps.add(gradient1);
		buttonComps.add(gradient2);
		buttonComps.add(outline);
		GuiRegion labelRegion = next.offSet(0, -3);
		Label label = new Label(labelRegion, text, 1, 0xffffffff, font);
		buttonComps.add(label);
		return buttonComps;
	}


}
