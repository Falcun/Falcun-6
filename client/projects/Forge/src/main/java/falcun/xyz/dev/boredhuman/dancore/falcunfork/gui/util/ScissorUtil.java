package falcun.xyz.dev.boredhuman.dancore.falcunfork.gui.util;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScissorUtil {

	static List<ScissorData> scissorStack = new ArrayList<>();

	public static void scissorBegin(int x, int y, int width, int height) {
		ScissorData scissorData = new ScissorData();
		scissorData.x = x;
		scissorData.y = y;
		scissorData.width = width;
		scissorData.height = height;
		if (ScissorUtil.scissorStack.isEmpty()) {
			GL11.glEnable(GL11.GL_SCISSOR_TEST);
			GL11.glScissor(x, Display.getHeight() - (y + height), width, height);
		} else {
			ScissorData parent = ScissorUtil.scissorStack.get(ScissorUtil.scissorStack.size() - 1);
			ScissorData intersection = ScissorUtil.calculateIntersect(parent, scissorData);
			if (intersection != null) {
				GL11.glScissor(intersection.x, Display.getHeight() - (intersection.y + intersection.height), intersection.width, intersection.height);
			}
		}

		ScissorUtil.scissorStack.add(scissorData);
	}

	public static void scissorEnd() {
		ScissorUtil.scissorStack.remove(ScissorUtil.scissorStack.size() - 1);
		if (ScissorUtil.scissorStack.isEmpty()) {
			GL11.glDisable(GL11.GL_SCISSOR_TEST);
		} else {
			ScissorData data = ScissorUtil.scissorStack.get(ScissorUtil.scissorStack.size() - 1);
			GL11.glScissor(data.x, Display.getHeight() - (data.y + data.height), data.width, data.height);
		}
	}

	public static ScissorData calculateIntersect(ScissorData parent, ScissorData child) {
		if (parent.equals(child)) {
			return child;
		}
		int parentRight = parent.x + parent.width;
		int parentBottom = parent.y + parent.height;
		int childRight = child.x + child.width;
		int childBottom = child.y + child.height;

		if ((childRight < parent.x && child.x > parentRight) || childBottom < parent.y || child.y > parentBottom) {
			return null;
		}

		ScissorData intersection = new ScissorData();
		intersection.x = Math.max(parent.x, child.x);
		intersection.y = Math.max(parent.y, child.y);
		intersection.width = Math.min(parentRight, childRight) - intersection.x;
		intersection.height = Math.min(parentBottom, childBottom) - intersection.y;
		return intersection;
	}


	public static class ScissorData {
		public int x, y, width, height;

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (o == null || this.getClass() != o.getClass()) {
				return false;
			}
			ScissorData that = (ScissorData) o;
			return this.x == that.x && this.y == that.y && this.width == that.width && this.height == that.height;
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.x, this.y, this.width, this.height);
		}
	}

}