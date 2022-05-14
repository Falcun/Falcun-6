package falcun.xyz.dev.boredhuman.dancore.falcunfork.compute;

import falcun.xyz.dev.boredhuman.dancore.falcunfork.Credits;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.events.DanLineRenderEvent;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.events.RenderHook;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.shapes.Box;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.Pair;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.util.Position;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.Map;

@Credits("https://github.com/boredhuman/Dancore/blob/master/src/main/java/dev/boredhuman/util/LineRenderer.java")
public final class LineRenderer {

	private ByteBuffer byteBuffer = BufferUtils.createByteBuffer(2000000 * 4);
	private int vertexCount = 0;
	private RenderManager rm;
	@SubscribeEvent
	public void draw(RenderWorldLastEvent event) {

		this.rm = Minecraft.getMinecraft().getRenderManager();

		boolean setupPointers = false;

		GlStateManager.disableTexture2D();
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(770, 771);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);

		DanLineRenderEvent batchedLineStrip = new DanLineRenderEvent(event.partialTicks);
		MinecraftForge.EVENT_BUS.post(batchedLineStrip);

		for (Map.Entry<Pair<Float, RenderHook>, List<DanLineRenderEvent.LineStrip>> lineWidthStripsPair : batchedLineStrip.lineStrips.entrySet()) {

			this.vertexCount = 0;
			this.byteBuffer.clear();
			List<Pair<Integer, Integer>> startCount = new ObjectArrayList<>();

			for (DanLineRenderEvent.LineStrip line : lineWidthStripsPair.getValue()) {
				if (line.positions.size() < 2) {
					continue;
				}
				int start = this.vertexCount;
				for (Position position : line.positions) {
					int index = this.vertexCount * 16;
					this.putPositionColor(index, position, line.color);
					this.vertexCount += 1;
				}
				startCount.add(new Pair<>(start, this.vertexCount - start));
			}

			if (startCount.isEmpty()) {
				continue;
			}

			GL11.glLineWidth(lineWidthStripsPair.getKey().first);

			IntBuffer startBuf = BufferUtils.createIntBuffer(startCount.size());
			IntBuffer countBuf = BufferUtils.createIntBuffer(startCount.size());

			for (Pair<Integer, Integer> startCountPair : startCount) {
				startBuf.put(startCountPair.first);
				countBuf.put(startCountPair.second);
			}

			startBuf.flip();
			countBuf.flip();

			if (!setupPointers) {
				setupPointers = true;
				this.setupPointers();
			}
			RenderHook renderHook = lineWidthStripsPair.getKey().second;
			if (renderHook != null) {
				renderHook.onRender(RenderHook.Phase.SETUP);
			}
			GL14.glMultiDrawArrays(GL11.GL_LINE_STRIP, startBuf, countBuf);
			if (renderHook != null) {
				renderHook.onRender(RenderHook.Phase.CLEAR);
			}
		}

		for (Map.Entry<Pair<Float, RenderHook>, List<DanLineRenderEvent.Line>> lines : batchedLineStrip.lines.entrySet()) {
			this.vertexCount = 0;
			this.byteBuffer.clear();

			for (DanLineRenderEvent.Line line : lines.getValue()) {
				for (Pair<Position, Position> position : line.positions) {
					Position pos1 = position.first;
					Position pos2 = position.second;
					int index = this.vertexCount * 4 * 4;
					this.putPositionColor(index, pos1, line.color);
					this.putPositionColor(index + 16, pos2, line.color);
					this.vertexCount += 2;
				}
			}

			GL11.glLineWidth(lines.getKey().first);

			if (!setupPointers) {
				setupPointers = true;
				this.setupPointers();
			}
			RenderHook renderHook = lines.getKey().second;
			if (renderHook != null) {
				renderHook.onRender(RenderHook.Phase.SETUP);
			}
			GL11.glDrawArrays(GL11.GL_LINES, 0, this.vertexCount);
			if (renderHook != null) {
				renderHook.onRender(RenderHook.Phase.CLEAR);
			}
		}

		for (Pair<RenderHook, Pair<Integer, List<Box>>> renderHookPairPair : batchedLineStrip.boxes) {
			this.vertexCount = 0;
			this.byteBuffer.clear();
			RenderHook renderHook = renderHookPairPair.first;
			Pair<Integer, List<Box>> boxes = renderHookPairPair.second;

			for (Box box : boxes.second) {

				this.addVertex(box.maxX, box.minY, box.maxZ, boxes.first);
				this.addVertex(box.maxX, box.maxY, box.maxZ, boxes.first);
				this.addVertex(box.minX, box.maxY, box.maxZ, boxes.first);
				this.addVertex(box.minX, box.minY, box.maxZ, boxes.first);

				this.addVertex(box.minX, box.minY, box.minZ, boxes.first);
				this.addVertex(box.minX, box.maxY, box.minZ, boxes.first);
				this.addVertex(box.maxX, box.maxY, box.minZ, boxes.first);
				this.addVertex(box.maxX, box.minY, box.minZ, boxes.first);

				this.addVertex(box.minX, box.maxY, box.minZ, boxes.first);
				this.addVertex(box.minX, box.maxY, box.maxZ, boxes.first);
				this.addVertex(box.maxX, box.maxY, box.maxZ, boxes.first);
				this.addVertex(box.maxX, box.maxY, box.minZ, boxes.first);

				this.addVertex(box.minX, box.minY, box.maxZ, boxes.first);
				this.addVertex(box.minX, box.minY, box.minZ, boxes.first);
				this.addVertex(box.maxX, box.minY, box.minZ, boxes.first);
				this.addVertex(box.maxX, box.minY, box.maxZ, boxes.first);

				this.addVertex(box.maxX, box.minY, box.maxZ, boxes.first);
				this.addVertex(box.maxX, box.minY, box.minZ, boxes.first);
				this.addVertex(box.maxX, box.maxY, box.minZ, boxes.first);
				this.addVertex(box.maxX, box.maxY, box.maxZ, boxes.first);

				this.addVertex(box.minX, box.minY, box.minZ, boxes.first);
				this.addVertex(box.minX, box.minY, box.maxZ, boxes.first);
				this.addVertex(box.minX, box.maxY, box.maxZ, boxes.first);
				this.addVertex(box.minX, box.maxY, box.minZ, boxes.first);
			}

			if (renderHook != null) {
				renderHook.onRender(RenderHook.Phase.SETUP);
			}
			if (!setupPointers) {
				setupPointers = true;
				this.setupPointers();
			}
			GL11.glDrawArrays(GL11.GL_QUADS, 0, this.vertexCount);
			if (renderHook != null) {
				renderHook.onRender(RenderHook.Phase.CLEAR);
			}
		}

		if (setupPointers) {
			GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
			GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
		}

		GL11.glLineWidth(1F);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
		GlStateManager.disableBlend();
		GlStateManager.enableTexture2D();
		GlStateManager.color(1F, 1F, 1F, 1F);
	}

	private void setupPointers() {
		this.byteBuffer.position(0);
		GL11.glVertexPointer(3, GL11.GL_FLOAT, 16, this.byteBuffer);
		GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		this.byteBuffer.position(12);
		GL11.glColorPointer(4, GL11.GL_UNSIGNED_BYTE, 16, this.byteBuffer);
		GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
	}

	public void addVertex(double x, double y, double z, int color) {
		this.byteBuffer.putFloat((float) (x - this.rm.viewerPosX));
		this.byteBuffer.putFloat((float) (y - this.rm.viewerPosY));
		this.byteBuffer.putFloat((float) (z - this.rm.viewerPosZ));
		this.byteBuffer.putInt(color);
		this.vertexCount += 1;
	}

	private void putPositionColor(int index, Position position, int color) {
		this.byteBuffer.putFloat(index, (float) (position.x - this.rm.viewerPosX));
		this.byteBuffer.putFloat(index + 4, (float) (position.y - this.rm.viewerPosY));
		this.byteBuffer.putFloat(index + 8, (float) (position.z - this.rm.viewerPosZ));
		this.byteBuffer.putInt(index + 12, color);
	}

}
