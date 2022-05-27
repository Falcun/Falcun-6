package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.Layout;

import java.util.ArrayList;
import java.util.List;

public class Row extends ContainerElement {

	// this is to maintain features such as padding
	ContainerElement innerBox = new ContainerElement();

	public Row() {
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

		int excessWidth = self.width;
		int xStart = self.x;

		for (BasicElement<?> element : self.children) {
			if (!element.visible) {
				continue;
			}
			element.height = self.height;
			element.y = self.y;
			if (element.widthPixel != -1) {
				element.width = Math.min(element.widthPixel, excessWidth);

				excessWidth -= element.width;
			} else if (element.widthPercent != 1) {
				element.width = (int) Math.min(element.widthPercent * self.width, excessWidth);

				excessWidth -= element.width;
			} else {
				autoElements.add(element);
			}
		}

		for (BasicElement<?> autoElement : autoElements) {
			autoElement.width = excessWidth / autoElements.size();
		}

		for (BasicElement<?> element : self.children) {
			if (!element.visible) {
				continue;
			}
			element.x = xStart;
			xStart += element.width;
		}
	}
}