package net.mattbenson.w2s;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;

public class W2SUtils {
	//Rebuilt gluProject & gluUnProject since it has a flaw built in, it normally uses a global in & out preallocated variables for processing, which introduces several bugs.
	private static final float[] IDENTITY_MATRIX = new float[] { 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f };
	
	public static float[] getScreenPos(double x, double y, double z, FloatBuffer modelViewMatrix, FloatBuffer projectionMatrix, IntBuffer viewport) {
		return getEntityScreenBounds(new AxisAlignedBB(x, y, z, x + 1D, y + 1D, z + 1D), modelViewMatrix, projectionMatrix, viewport);
	}
	
	public static float[] getScreenPos(BlockPos pos, FloatBuffer modelViewMatrix, FloatBuffer projectionMatrix, IntBuffer viewport) {
		return getEntityScreenBounds(new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1D, pos.getY() + 1D, pos.getZ() + 1D), modelViewMatrix, projectionMatrix, viewport);
	}
	
	public static float[] getEntityScreenBounds(TileEntity entity, FloatBuffer modelViewMatrix, FloatBuffer projectionMatrix, IntBuffer viewport) {
		BlockPos pos = entity.getPos();
		return getEntityScreenBounds(new AxisAlignedBB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1D, pos.getY() + 1D, pos.getZ() + 1D), modelViewMatrix, projectionMatrix, viewport);
	}
	
	public static float[] getEntityScreenBounds(Entity entity, FloatBuffer modelViewMatrix, FloatBuffer projectionMatrix, IntBuffer viewport) {
		return getEntityScreenBounds(entity.getEntityBoundingBox().expand(0.2D, 0.2D, 0.2D), modelViewMatrix, projectionMatrix, viewport);
	}
	
	public static float[] getEntityScreenBounds(AxisAlignedBB box, FloatBuffer modelViewMatrix, FloatBuffer projectionMatrix, IntBuffer viewport) {
		Minecraft mc = Minecraft.getMinecraft();
		
		AxisAlignedBB[] points = new AxisAlignedBB[] {
			getBoundingBoxPoint(box.maxX, box.minY, box.minZ),
			getBoundingBoxPoint(box.maxX, box.maxY, box.minZ),
			getBoundingBoxPoint(box.maxX, box.maxY, box.maxZ),
			getBoundingBoxPoint(box.maxX, box.minY, box.maxZ),
			getBoundingBoxPoint(box.minX, box.minY, box.minZ),
			getBoundingBoxPoint(box.minX, box.maxY, box.minZ),
			getBoundingBoxPoint(box.minX, box.maxY, box.maxZ),
			getBoundingBoxPoint(box.minX, box.minY, box.maxZ)
		};
		
		float minX = Integer.MAX_VALUE;
		float minY = Integer.MAX_VALUE;
		float maxX = -Integer.MAX_VALUE;
		float maxY = -Integer.MAX_VALUE;
		
		double renderPosX = mc.getRenderManager().renderPosX;
		double renderPosY = mc.getRenderManager().renderPosY;
		double renderPosZ = mc.getRenderManager().renderPosZ;
		
		/*
		if(ClientProxy.schematic != null && ClientProxy.schematic.position != null) {
			renderPosX += ClientProxy.schematic.position.x;
			renderPosY += ClientProxy.schematic.position.y;
			renderPosZ += ClientProxy.schematic.position.z;
		}
		*/
		
		float lastZ = 0;
		boolean behind = false;
		
		FloatBuffer screenPos = BufferUtils.createFloatBuffer(16);
		
		for(AxisAlignedBB point : points) {
			AxisAlignedBB worldPoint = point.offset(-renderPosX, -renderPosY, -renderPosZ);
			
			if(!gluProject((float)worldPoint.minX, (float)worldPoint.minY, (float)worldPoint.minZ, modelViewMatrix, projectionMatrix, viewport, screenPos)) {
				break;
			}
			
			if(screenPos.get(2) > 1) {
				behind = true;
				break;
			}
						
			lastZ = screenPos.get(2);
			
			int x = (int)(screenPos.get(0));
			int y = (int)(viewport.get(3) - screenPos.get(1));
			
			if(x > maxX) {
				maxX = x;
			}
			
			if(x < minX) {
				minX = x;
			}
			
			if(y > maxY) {
				maxY = y;
			}
			
			if(y < minY) {
				minY = y;
			}
		}
		
		if(behind) {
			return null;
		}
		
		if((minX > viewport.get(2) && maxX > viewport.get(2) && minY > viewport.get(3) && maxY > viewport.get(3)) || (maxX < 0 && maxY < 0 && minX < 0 && minY < 0)) {
			return null;
		}
		
		int width = (int)(maxX - minX);
		int height = (int)(maxY - minY);
		int x = (int)(minX);
		int y = (int)(minY);
		
		return new float[] {x, y, width, height, lastZ};
	}
	
	public static float[] getWorldPoint(float x, float y, float z) {
		GL11.glPushAttrib(GL11.GL_TRANSFORM_BIT);
		GlStateManager.matrixMode(GL11.GL_PROJECTION);
		GlStateManager.pushMatrix();
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().entityRenderer.setupCameraTransform(0, 0);
		
		IntBuffer viewport = BufferUtils.createIntBuffer(16);
		FloatBuffer modelViewMatrix = BufferUtils.createFloatBuffer(16);
		FloatBuffer projectionMatrix = BufferUtils.createFloatBuffer(16);
		
		GlStateManager.getFloat(GL11.GL_MODELVIEW_MATRIX, modelViewMatrix);
		GlStateManager.getFloat(GL11.GL_PROJECTION_MATRIX, projectionMatrix);
		GL11.glGetInteger(GL11.GL_VIEWPORT, viewport); //Missing in glstatemanager lol
		
		GlStateManager.matrixMode(GL11.GL_MODELVIEW);
		GlStateManager.popMatrix();
		GlStateManager.matrixMode(GL11.GL_PROJECTION);
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
		
		FloatBuffer worldPos = BufferUtils.createFloatBuffer(16);
		
		if(!gluUnProject(x, y, z, modelViewMatrix, projectionMatrix, viewport, worldPos)) {
			return null;
		}
		
		return new float[] {worldPos.get(0), worldPos.get(1), worldPos.get(2)};
	}
	
	public static boolean gluProject(float worldX, float worldY, float worldZ, FloatBuffer modelViewMatrix, FloatBuffer projectionMatrix, IntBuffer viewport, FloatBuffer screenPos) {
		float[] in = new float[4];
		float[] out = new float[4];
		
		in[0] = worldX;
		in[1] = worldY;
		in[2] = worldZ;
		in[3] = 1.0f;
		
		__gluMultMatrixVecf(modelViewMatrix, in, out);
		__gluMultMatrixVecf(projectionMatrix, out, in);
		
		if(in[3] == 0.0) {
			return false;
		}
		
		in[3] = (1.0f / in[3]) * 0.5f;
		
		in[0] = in[0] * in[3] + 0.5f;
		in[1] = in[1] * in[3] + 0.5f;
		
		if(in[3] < 0) {
			return false;
		}
		
		in[2] = 0;
		//in[2] = in[2] * in[3] + 0.5f;
		
		screenPos.put(0, in[0] * viewport.get(viewport.position() + 2) + viewport.get(viewport.position() + 0));
		screenPos.put(1, in[1] * viewport.get(viewport.position() + 3) + viewport.get(viewport.position() + 1));
		screenPos.put(2, in[2]);
		
		return true;
	}
	
	public static boolean gluUnProject(float screenX, float screenY, float screenZ, FloatBuffer modelViewMatrix, FloatBuffer projectionMatrix, IntBuffer viewport, FloatBuffer worldPos) {
		float[] in = new float[4];
		float[] out = new float[4];
		FloatBuffer finalMatrix = BufferUtils.createFloatBuffer(16);
		
		__gluMultMatricesf(modelViewMatrix, projectionMatrix, finalMatrix);
		
		if(!__gluInvertMatrixf(finalMatrix, finalMatrix)) {
			return false;
		}
		
		in[0] = screenX;
		in[1] = screenY;
		in[2] = screenZ;
		in[3] = 1.0f;
		
		in[0] = (in[0] - viewport.get(viewport.position() + 0)) / viewport.get(viewport.position() + 2);
		in[1] = (in[1] - viewport.get(viewport.position() + 1)) / viewport.get(viewport.position() + 3);
		
		in[0] = in[0] * 2 - 1;
		in[1] = in[1] * 2 - 1;
		in[2] = in[2] * 2 - 1;
		
		__gluMultMatrixVecf(finalMatrix, in, out);
		
		if(out[3] == 0.0) {
			return false;
		}
		
		out[3] = 1.0f / out[3];
		
		worldPos.put(worldPos.position() + 0, out[0] * out[3]);
		worldPos.put(worldPos.position() + 1, out[1] * out[3]);
		worldPos.put(worldPos.position() + 2, out[2] * out[3]);
		
		return true;
	}
	
	private static void __gluMultMatricesf(FloatBuffer a, FloatBuffer b, FloatBuffer r) {
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				r.put(r.position() + i*4 + j, a.get(a.position() + i*4 + 0) * b.get(b.position() + 0*4 + j) + a.get(a.position() + i*4 + 1) * b.get(b.position() + 1*4 + j) + a.get(a.position() + i*4 + 2) * b.get(b.position() + 2*4 + j) + a.get(a.position() + i*4 + 3) * b.get(b.position() + 3*4 + j));
			}
		}
	}

	
	private static void __gluMultMatrixVecf(FloatBuffer m, float[] in, float[] out) {
		for(int i = 0; i < 4; i++) {
			out[i] = in[0] * m.get(m.position() + 0*4 + i)
					+ in[1] * m.get(m.position() + 1*4 + i)
					+ in[2] * m.get(m.position() + 2*4 + i)
					+ in[3] * m.get(m.position() + 3*4 + i);
		}
	}

	private static boolean __gluInvertMatrixf(FloatBuffer src, FloatBuffer inverse) {
		int i, j, k, swap;
		float t;
		FloatBuffer temp = BufferUtils.createFloatBuffer(16);
		
		for(i = 0; i < 16; i++) {
			temp.put(i, src.get(i + src.position()));
		}
		__gluMakeIdentityf(inverse);
		
		for(i = 0; i < 4; i++) {
			swap = i;
			for(j = i + 1; j < 4; j++) {
				if(Math.abs(temp.get(j*4 + i)) > Math.abs(temp.get(i* 4 + i))) {
					swap = j;
				}
			}
			
			if(swap != i) {
				for(k = 0; k < 4; k++) {
					t = temp.get(i*4 + k);
					temp.put(i*4 + k, temp.get(swap*4 + k));
					temp.put(swap*4 + k, t);
					
					t = inverse.get(i*4 + k);
					inverse.put(i*4 + k, inverse.get(swap*4 + k));
					inverse.put(swap*4 + k, t);
				}
			}
			
			if(temp.get(i*4 + i) == 0) {
				return false;
			}
			
			t = temp.get(i*4 + i);
			
			for(k = 0; k < 4; k++) {
				temp.put(i*4 + k, temp.get(i*4 + k)/t);
				inverse.put(i*4 + k, inverse.get(i*4 + k)/t);
			}
			
			for(j = 0; j < 4; j++) {
				if(j != i) {
					t = temp.get(j*4 + i);
					
					for(k = 0; k < 4; k++) {
						temp.put(j*4 + k, temp.get(j*4 + k) - temp.get(i*4 + k) * t);
						inverse.put(j*4 + k, inverse.get(j*4 + k) - inverse.get(i*4 + k) * t);
					}
				}
			}
		}
		return true;
	}
	
	private static void __gluMakeIdentityf(FloatBuffer m) {
		int oldPos = m.position();
		m.put(IDENTITY_MATRIX);
		m.position(oldPos);
	}
	
	private static AxisAlignedBB getBoundingBoxPoint(double x, double y, double z) {
		return new AxisAlignedBB(x, y, z, x, y, z);
	}
	
	public static Vector3f getRenderPos(Entity entity, boolean interpolate) {
		float interpolateDamper = 1f;
		float x = (float)(entity.posX - (interpolate ? ((entity.posX - entity.prevPosX) * interpolateDamper) : 0)); 
		float y = (float)(entity.posY - (interpolate ? ((entity.posY - entity.prevPosY) * interpolateDamper) : 0)); 
		float z = (float)(entity.posZ - (interpolate ? ((entity.posZ - entity.prevPosZ) * interpolateDamper) : 0)); 
		return new Vector3f(x, y, z);
	}
	
	public static Color mergeAlphaWithColor(Color color, int alpha) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}
}
