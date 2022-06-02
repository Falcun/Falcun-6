package falcun.net.guidragonclient.mainmenu;

import com.google.common.base.Strings;
import falcun.net.Falcun;
import falcun.net.api.colors.FalcunGuiColorPalette;
import falcun.net.api.fonts.FalcunFont;
import falcun.net.api.fonts.Fonts;
import falcun.net.api.guidragon.components.Component;
import falcun.net.api.guidragon.components.rect.ColorSquare;
import falcun.net.api.guidragon.components.rect.ColorSquareOutline;
import falcun.net.api.guidragon.components.scroll.VerticalScroll;
import falcun.net.api.guidragon.components.text.Label;
import falcun.net.api.guidragon.components.text.Paragraph;
import falcun.net.api.guidragon.components.texture.TextureSquare;
import falcun.net.api.guidragon.components.texture.TextureSquareColored;
import falcun.net.api.guidragon.effects.EnterExitEffect;
import falcun.net.api.guidragon.effects.OnClickEffect;
import falcun.net.api.guidragon.menu.FalcunMenu;
import falcun.net.api.guidragon.region.GuiRegion;
import falcun.net.api.guidragon.scaling.FalcunScaling;
import falcun.net.api.textures.FalcunTexture;
import falcun.net.managers.FalcunGuiManager;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

import java.util.LinkedList;
import java.util.List;

public class FalcunModListMenu extends FalcunMenu {
	private final List<Component> components = new LinkedList<>();
	private final List<Component> modComponents = new LinkedList<>();
	private static ModContainer selectedMod = null;
	private final List<Component> configButtonList = new LinkedList<>();


	@Override
	public List<Component> getComponents() {
		List<Component> comps = new LinkedList<>(components);
		comps.addAll(modComponents);
		return comps;
	}

	@Override
	protected void init() {
		components.clear();
		modComponents.clear();
		configButtonList.clear();
		int x = 0, y = 0;
		float scale = 1f / new FalcunScaling(Falcun.minecraft).getScaleFactor();
		int w = Math.round(width / scale);
		int h = Math.round(height / scale);
		components.add(new ColorSquare(new GuiRegion(0, 0, w, h), () -> 0xff000000));
		addMods();
		clickMod(null);
	}

	void addMods() {
		int x = 40, y = 40;
		float scale = 1f / new FalcunScaling(Falcun.minecraft).getScaleFactor();
		int w = Math.round(width / scale);
		int h = Math.round(height / scale);
		List<ModContainer> mods = new LinkedList<>();
		FalcunFont font = Fonts.Roboto;
		int listWid = 0;
		for (ModContainer mod : Loader.instance().getModList()) {
			if (mod.getMetadata() != null && mod.getMetadata().parentMod == null && !Strings.isNullOrEmpty(mod.getMetadata().parent)) {
				String parentMod = mod.getMetadata().parent;
				ModContainer parentContainer = Loader.instance().getIndexedModList().get(parentMod);
				if (parentContainer != null) {
					mod.getMetadata().parentMod = parentContainer;
					parentContainer.getMetadata().childMods.add(mod);
					continue;
				}
			} else if (mod.getMetadata() != null && mod.getMetadata().parentMod != null) {
				continue;
			}
			mods.add(mod);
			listWid = Math.max(listWid, (int) font.getStringWidth(mod.getName()));
		}
		listWid += 10;
		ColorSquareOutline box = new ColorSquareOutline(new GuiRegion(x, y, listWid, h - 140), FalcunGuiColorPalette::getLineColor, 1.4f);
		components.add(box);
		components.add(new Label(new GuiRegion(20, 17, box.region.width, 9), "MOD LIST", 1, 0xFFFFFFFF, Fonts.Roboto));
		listWid -= 10;
		{
			int wid =(int) (listWid / 0.937885802D);
			int diff = (h - 140 ) - wid;
			GuiRegion gr = new GuiRegion(x + 5,  (diff >> 1 ) + 40, listWid, wid);
			TextureSquare tSquare = new TextureSquareColored(gr, FalcunTexture.glowLogo, 0.5f,1f,1f,1f);
			components.add(tSquare);
		}
		listWid += 10;
		GuiRegion buttonGr = box.region.offSet(0, box.region.height + 10);
		buttonGr.height = 30;
		mods.sort((a,b)-> a.getName().compareToIgnoreCase(b.getName()));
		VerticalScroll scroll = new VerticalScroll(box.region.offSet(box.region.width + 8, 20), () -> 0x80646464, () ->  0xff333333, box.region);
		scroll.region.height = 40;
		scroll.region.width = 8;
		components.add(scroll);
		y += 10;
		x += 2;
		listWid -= 4;
		for (final ModContainer mod : mods) {
			String str = mod.getName() + " " + "\n" + mod.getVersion();
			GuiRegion gr = new GuiRegion(x,y, listWid, 45);
			ColorSquare square = new ColorSquare(gr, ()-> 0xb319191c, 2);
			Paragraph paragraph = new Paragraph(gr.offSet(0,2), ()-> 0xffffffff, str,  Fonts.Roboto);
			paragraph.region.height = 11;
			y +=60;
			ColorSquareOutline outliner = new ColorSquareOutline(gr, ()-> 0x00000000, 2);
			outliner.effects.add(new EnterExitEffect((comp, over)->{
				outliner.color = () -> over ? 0xffffffff : 0x00000000;
			}));
			square.effects.add(new OnClickEffect(comp->{
				selectedMod = mod;
				components.removeAll(configButtonList);
				configButtonList.clear();;
				configButtonList.addAll(FalcunMainMenu.addButton(buttonGr, "CONFIG", Fonts.Roboto, ()-> {
					if (selectedMod != null){
						FalcunGuiManager.openModConfigMenu(selectedMod);
					}
				}));
				this.clickMod(selectedMod);
			}));
		}
	}

	void clickMod(ModContainer mod) {

	}
}
