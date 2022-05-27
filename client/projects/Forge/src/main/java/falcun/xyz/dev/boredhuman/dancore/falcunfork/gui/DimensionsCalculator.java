package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.DimensionComputer;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.elements.BasicElement;

public class DimensionsCalculator implements DimensionComputer {

	public void computeChildBounds(BasicElement<?> element) {
		int yStart = element.y + element.paddingTop;
		int leftPadding = (int) (element.paddingLeftPercent * element.width);
		int rightPadding = (int) (element.paddingRightPercent * element.width);
		int maxWidth = element.width - (element.paddingLeft + element.paddingRight + leftPadding + rightPadding);
		int widthOccupied = 0;
		int remainingHeight = element.height - (element.paddingTop + element.paddingBottom);
		int xStart = element.x + element.paddingLeft + leftPadding;
		float occupiedPercent = 0F;

		for (int i = 0; i < element.children.size(); i++) {
			BasicElement<?> childElement = element.children.get(i);

			if (!childElement.visible) {
				continue;
			}

			if (childElement.layout == Layout.BLOCK && widthOccupied != 0) {
				int prevHeight = element.children.get(i - 1).height;
				yStart += prevHeight;
				remainingHeight -= prevHeight;
				widthOccupied = 0;
			}

			int elementWidth;
			int elementHeight;

			// if there is no pixel specified available use width percent
			if (childElement.widthPixel == -1) {
				elementWidth = Math.round(childElement.widthPercent * maxWidth);
			} else {
				elementWidth = childElement.widthPixel;
			}
			// calculate the percent of the max width the child element occupies
			float widthPercent = childElement.widthPercent;

			if (childElement.widthPixel != -1) {
				widthPercent = elementWidth / (float) maxWidth;
			}
			// handle inline overflow
			if (childElement.layout == Layout.INLINE && occupiedPercent + widthPercent > 1) {
				int prevHeight = element.children.get(i - 1).height;
				yStart += prevHeight;
				remainingHeight -= prevHeight;
				widthOccupied = 0;
				occupiedPercent = 0.0F;
			}
			// if there is no pixel specified available use height percent
			if (childElement.heightPixel == -1) {
				if (childElement.heightPercent != 1) {
					elementHeight = Math.round((childElement.heightPercent * (element.height - (element.paddingTop + element.paddingBottom))));
				} else {
					elementHeight = Math.round((childElement.heightPercent * remainingHeight));
				}
			} else {
				elementHeight = childElement.heightPixel;
			}

			childElement.x = xStart + widthOccupied;
			childElement.y = yStart;
			childElement.width = elementWidth;
			childElement.height = elementHeight;

			if (childElement.layout == Layout.BLOCK) {
				yStart += childElement.height;
				remainingHeight -= childElement.height;
			} else {
				// increase offset
				widthOccupied += childElement.width;
				occupiedPercent += widthPercent;
				// reset if overflows
				if (widthOccupied > maxWidth) {
					widthOccupied = 0;
					yStart += childElement.height;
					remainingHeight -= childElement.height;
					occupiedPercent = 0.0F;
				}
			}
		}
	}

}