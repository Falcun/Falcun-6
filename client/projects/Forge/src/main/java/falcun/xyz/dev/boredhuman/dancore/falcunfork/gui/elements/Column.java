package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.Layout;

import java.util.ArrayList;
import java.util.List;

public class Column extends ContainerElement {

	// this is to maintain features such as padding
	ContainerElement innerBox = new ContainerElement();

	public Column() {
		this.innerBox.dimensionsCalculator = this::computeChildBounds;
		this.children.add(this.innerBox);
	}

	@Override
	public ContainerElement addChildren(BasicElement<?>... children) {
		for (BasicElement<?> basicElement : children) {
			basicElement.setLayout(Layout.INLINE);
		}
		this.innerBox.addChildren(children);
		return this;
	}

	public void removeChildElement(BasicElement<?> element) {
		this.innerBox.children.remove(element);
	}

	public void computeChildBounds(BasicElement<?> self) {
		List<BasicElement<?>> autoElements = new ArrayList<>();

		int excessHeight = self.height;
		int yStart = self.y;

		for (BasicElement<?> element : self.children) {
			if (!element.visible) {
				continue;
			}
			element.width = self.width;
			element.x = self.x;
			if (element.heightPixel != -1) {
				element.height = Math.min(element.heightPixel, excessHeight);

				excessHeight -= element.height;
			} else if (element.heightPercent != 1) {
				element.height = (int) Math.min(element.heightPercent * self.height, excessHeight);

				excessHeight -= element.height;
			} else {
				autoElements.add(element);
			}
		}

		for (BasicElement<?> autoElement : autoElements) {
			autoElement.height = excessHeight / autoElements.size();
		}

		for (BasicElement<?> element : self.children) {
			if (!element.visible) {
				continue;
			}
			element.y = yStart;
			yStart += element.height;
		}
	}
}