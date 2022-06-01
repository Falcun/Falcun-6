package falcun.net.dangui.mainmenu.pages;

import falcun.net.api.fonts.Fonts;
import falcun.net.api.dangui.elements.FalcunLabel;
import falcun.net.api.dangui.pages.FalcunFullScreenMenuPage;
import falcun.net.dangui.mainmenu.MainMenuPage;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.*;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.listener.ClickListener;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util.ColorHover;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util.Colors;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.LinkedList;
import java.util.List;

@SideOnly(Side.CLIENT)
public final class PageSelector implements FalcunFullScreenMenuPage {


	private final List<BasicElement<?>> elements;

	public PageSelector() {
		elements = new LinkedList<>();

		int halfw = getWidth() >> 1;
		int halfh = getHeight() >> 1;
		elements.add(new ContainerElement().setColor(0xff000000).setDimensions(0, 0, getWidth(), getHeight()));
		Row row = new Row();
		Column left = (Column) new Column().setPadding(15).setPaddingTB(67, 45),
			center = (Column) new Column().setPadding(15).setPaddingTB(45, 45),
			right = (Column) new Column().setPadding(15).setPaddingTB(67, 45);
		elements.add(row);

		int rowwid = (int) (halfw * 0.8);
		int x = halfw - rowwid;
		row.setDimensions(x, halfh, halfw + rowwid - x, halfh);
		addButtons(left, 0, 2);
		addButtons(center, 2, 5);
		addButtons(right, 5, 7);
		row.addChildren(left, center, right);
		elements.add(row);
	}

	private static void addButtons(Column column, int start, int end) {
		for (int i = start; i < end; ++i) {
			MainMenuPage p = MainMenuPage.values()[i + 1];
			column.addChildren(makeButton(p.name, p.buttonClick));
		}
	}

	private static BasicElement<?> makeButton(final String text, final Runnable r) {
		ContainerElement row = new Row().
			setPaddingTB(5, 5)
			.setHeightPx(45);
		ContainerElement button = new ContainerElement()
			.setColor(0xff454545)
			.addListener(new ColorHover(0xff454545, 0xff545454))
			.setHeightPx(45)
			.setRounding(6).setOutline(new OutlineElement<>()
				.setColor(0)
				.addListener(new ColorHover(0, 0xEEffffff))
			);
		FalcunLabel label = new FalcunLabel(text, Fonts.Roboto)
			.setColor(Colors.WHITE)
			.setVert(1)
			.setHoriz(1)
			.addListener((ClickListener) element -> {
				r.run();
				return true;
			});
		return row.addChildren(button.addChildren(label));
	}

	@Override
	public List<BasicElement<?>> getElements() {
		return elements;
	}
}
