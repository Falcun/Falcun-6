package net.mattbenson.patcher;

import net.mattbenson.patcher.transformers.ModelRendererTransformer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Vec3;

public class TexturedQuadHook {
	public static void draw(TexturedQuad parent, WorldRenderer renderer, float scale) {
		Vec3 xVertex = (parent.vertexPositions[1]).vector3D.subtractReverse((parent.vertexPositions[0]).vector3D);
		Vec3 zVertex = (parent.vertexPositions[1]).vector3D.subtractReverse((parent.vertexPositions[2]).vector3D);
		Vec3 crossVertex = zVertex.crossProduct(xVertex).normalize();
		float xCoord = (float) crossVertex.xCoord;
		float yCoord = (float) crossVertex.yCoord;
		float zCoord = (float) crossVertex.zCoord;
		if (parent.invertNormal) {
			xCoord = -xCoord;
			yCoord = -yCoord;
			zCoord = -zCoord;
		}
		boolean drawOnSelf = !renderer.isDrawing;
		if (drawOnSelf || !ModelRendererTransformer.batchModelRendering)
			renderer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		for (int i = 0; i < 4; i++) {
			PositionTextureVertex vertex = parent.vertexPositions[i];
			renderer.pos(vertex.vector3D.xCoord * scale,
					vertex.vector3D.yCoord * scale, vertex.vector3D.zCoord * scale)
					.tex(vertex.texturePositionX, vertex.texturePositionY).normal(xCoord, yCoord, zCoord)
					.endVertex();
		}
		if (drawOnSelf || !ModelRendererTransformer.batchModelRendering)
			Tessellator.getInstance().draw();
	}
}
