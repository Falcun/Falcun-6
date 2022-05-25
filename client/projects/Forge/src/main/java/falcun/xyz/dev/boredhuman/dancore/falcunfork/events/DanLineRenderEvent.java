package falcun.xyz.dev.boredhuman.dancore.falcunfork.events;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.Credits;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.shapes.Box;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.Pair;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.Position;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;
import java.util.Map;

@Credits("https://github.com/boredhuman/Dancore/blob/master/src/main/java/dev/boredhuman/api/events/BatchedLineRenderingEvent.java")
public final class DanLineRenderEvent extends Event {
	public final float partialTicks;
	public DanLineRenderEvent(float partialTicks){
		this.partialTicks = partialTicks;
	}
	public final Map<Pair<Float, RenderHook>, java.util.List<DanLineRenderEvent.LineStrip>> lineStrips = new Object2ObjectOpenHashMap<>();
	public final Map<Pair<Float, RenderHook>, java.util.List<DanLineRenderEvent.Line>> lines = new Object2ObjectOpenHashMap<>();
	public final List<Pair<RenderHook, Pair<Integer, java.util.List<Box>>>> boxes = new ObjectArrayList<>();
	public final Map<Pair<Float, RenderHook>, java.util.List<Box>> boxOutlines = new Object2ObjectOpenHashMap<>();

	public void addLineStripLineWidth(Pair<Float, RenderHook> lineWidthDanRenderHook, DanLineRenderEvent.LineStrip lineStrips) {
		java.util.List<DanLineRenderEvent.LineStrip> strips = this.lineStrips.computeIfAbsent(lineWidthDanRenderHook, width -> new ObjectArrayList<>());
		strips.add(lineStrips);
	}

	public void addLineStripLineWidth(float lineWidth, DanLineRenderEvent.LineStrip lineStrips) {
		this.addLineStripLineWidth(Pair.of(lineWidth, null), lineStrips);
	}

	public void addLineLineWidth(Pair<Float, RenderHook> lineWidthDanRenderHook, DanLineRenderEvent.Line lines) {
		java.util.List<DanLineRenderEvent.Line> line = this.lines.computeIfAbsent(lineWidthDanRenderHook, width -> new ObjectArrayList<>());
		line.add(lines);
	}

	public void addLineLineWidth(float lineWidth, DanLineRenderEvent.Line lines) {
		this.addLineLineWidth(Pair.of(lineWidth, null), lines);
	}

	public void addBoxesColor(int color, java.util.List<Box> boxes, RenderHook RenderHook) {
		this.boxes.add(Pair.of(RenderHook, Pair.of(color, boxes)));
	}

	public void addBoxOutlines(float lineWidth, RenderHook RenderHook, java.util.List<Box> boxOutlines, int color) {
		java.util.List<DanLineRenderEvent.Line> line = this.lines.computeIfAbsent(Pair.of(lineWidth, RenderHook), width -> new ObjectArrayList<>());

		for (Box box : boxOutlines) {
			java.util.List<Pair<Position, Position>> linesList = new ObjectArrayList<>();
			Position origin = new Position(box.minX, box.minY, box.minZ);
			Position max = new Position(box.maxX, box.maxY, box.maxZ);
			Position originX = new Position(box.maxX, box.minY, box.minZ);
			Position originZ = new Position(box.minX, box.minY, box.maxZ);
			Position originXZ = new Position(box.maxX, box.minY, box.maxZ);
			Position maxx = new Position(box.minX, box.maxY, box.maxZ);
			Position maxz = new Position(box.maxX, box.maxY, box.minZ);
			Position maxxz = new Position(box.minX, box.maxY, box.minZ);
			linesList.add(new Pair<>(origin, originX));
			linesList.add(new Pair<>(origin, originZ));
			linesList.add(new Pair<>(originX, originXZ));
			linesList.add(new Pair<>(originZ, originXZ));

			linesList.add(new Pair<>(max, maxx));
			linesList.add(new Pair<>(max, maxz));
			linesList.add(new Pair<>(maxx, maxxz));
			linesList.add(new Pair<>(maxz, maxxz));

			linesList.add(new Pair<>(origin, maxxz));
			linesList.add(new Pair<>(originX, maxz));
			linesList.add(new Pair<>(originZ, maxx));
			linesList.add(new Pair<>(originXZ, max));

			line.add(new DanLineRenderEvent.Line(linesList, color));
		}
	}

	public static class LineStrip {
		public final java.util.List<Position> positions;
		public final int color;

		public LineStrip(java.util.List<Position> positions, int color) {
			this.positions = positions;
			this.color = color;
		}
	}

	public static class Line {
		public final java.util.List<Pair<Position, Position>> positions;
		public final int color;

		public Line(List<Pair<Position, Position>> positions, int color) {
			this.positions = positions;
			this.color = color;
		}
	}
}
