package net.mattbenson.utils.legacy;

import java.awt.Color;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class GlShim
{
    public static final int GL_ALPHA_TEST = 3008;
    public static final int GL_BLEND = 3042;
    public static final int GL_CLAMP = 10496;
    public static final int GL_COLOR_BUFFER_BIT = 16384;
    public static final int GL_COLOR_CLEAR_VALUE = 3106;
    public static final int GL_CULL_FACE = 2884;
    public static final int GL_DEPTH_BUFFER_BIT = 256;
    public static final int GL_DST_ALPHA = 772;
    public static final int GL_DST_COLOR = 774;
    public static final int GL_FOG = 2912;
    public static final int GL_DEPTH_TEST = 2929;
    public static final int GL_FLAT = 7424;
    public static final int GL_FOG_DENSITY = 2914;
    public static final int GL_FOG_END = 2916;
    public static final int GL_FOG_MODE = 2917;
    public static final int GL_FOG_START = 2915;
    public static final int GL_GREATER = 516;
    public static final int GL_LIGHTING = 2896;
    public static final int GL_LINEAR = 9729;
    public static final int GL_MODELVIEW = 5888;
    public static final int GL_NEAREST = 9728;
    public static final int GL_NORMALIZE = 2977;
    public static final int GL_ONE = 1;
    public static final int GL_ONE_MINUS_DST_ALPHA = 773;
    public static final int GL_ONE_MINUS_SRC_ALPHA = 771;
    public static final int GL_POLYGON_OFFSET_FILL = 32823;
    public static final int GL_PROJECTION = 5889;
    public static final int GL_QUADS = 7;
    public static final int GL_SMOOTH = 7425;
    public static final int GL_SRC_ALPHA = 770;
    public static final int GL_TEXTURE_2D = 3553;
    public static final int GL_TEXTURE_HEIGHT = 4097;
    public static final int GL_TEXTURE_MAG_FILTER = 10240;
    public static final int GL_TEXTURE_MIN_FILTER = 10241;
    public static final int GL_TEXTURE_WIDTH = 4096;
    public static final int GL_TEXTURE_WRAP_S = 10242;
    public static final int GL_TEXTURE_WRAP_T = 10243;
    public static final int GL_TRANSFORM_BIT = 4096;
    public static final int GL_VIEWPORT_BIT = 2048;
    public static final int GL_ZERO = 0;
    public static final int GL_RESCALE_NORMAL = 32826;
    
    public static void glEnable(final int attrib) {
        switch (attrib) {
            case 3008: {
                GlStateManager.enableAlpha();
                break;
            }
            case 3042: {
                GlStateManager.enableBlend();
                break;
            }
            case 2884: {
                GlStateManager.enableCull();
                break;
            }
            case 2929: {
                GlStateManager.enableDepth();
                break;
            }
            case 2912: {
                GlStateManager.enableFog();
                break;
            }
            case 2896: {
                GlStateManager.enableLighting();
                break;
            }
            case 2977: {
                GlStateManager.enableNormalize();
                break;
            }
            case 32823: {
                GlStateManager.enablePolygonOffset();
                break;
            }
            case 32826: {
                GlStateManager.enableRescaleNormal();
                break;
            }
            case 3553: {
                GlStateManager.enableTexture2D();
                break;
            }
        }
    }
    
    public static void glDisable(final int attrib) {
        switch (attrib) {
            case 3008: {
                GlStateManager.disableAlpha();
                break;
            }
            case 3042: {
                GlStateManager.disableBlend();
                break;
            }
            case 2884: {
                GlStateManager.disableCull();
                break;
            }
            case 2929: {
                GlStateManager.disableDepth();
                break;
            }
            case 2912: {
                GlStateManager.disableFog();
                break;
            }
            case 2896: {
                GlStateManager.disableLighting();
                break;
            }
            case 2977: {
                GlStateManager.disableNormalize();
                break;
            }
            case 32823: {
                GlStateManager.disablePolygonOffset();
                break;
            }
            case 32826: {
                GlStateManager.disableRescaleNormal();
                break;
            }
            case 3553: {
                GlStateManager.disableTexture2D();
                break;
            }
        }
    }
    
    public static void glFogi(final int pname, final int param) {
        if (pname == 2917) {
            GlStateManager.setFog(param);
        }
    }
    
    public static void glFogf(final int pname, final float param) {
        switch (pname) {
            case 2914: {
                GlStateManager.setFogDensity(param);
                break;
            }
            case 2916: {
                GlStateManager.setFogEnd(param);
                break;
            }
            case 2915: {
                GlStateManager.setFogStart(param);
                break;
            }
        }
    }
    
    public static void glAlphaFunc(final int func, final float ref) {
        GlStateManager.alphaFunc(func, ref);
    }
    
    public static void glBlendFunc(final int sfactor, final int dfactor) {
        GlStateManager.blendFunc(sfactor, dfactor);
    }
    
    public static void glBlendFuncSeparate(final int sfactorRGB, final int dfactorRGB, final int sfactorAlpha, final int dfactorAlpha) {
        GlStateManager.tryBlendFuncSeparate(sfactorRGB, dfactorRGB, sfactorAlpha, dfactorAlpha);
    }
    
    public static void glCallList(final int list) {
        GlStateManager.callList(list);
    }
    
    public static void glClear(final int mask) {
        GlStateManager.clear(mask);
    }
    
    public static void glClearColor(final float red, final float green, final float blue, final float alpha) {
        GlStateManager.clearColor(red, green, blue, alpha);
    }
    
    public static void glVertex3d(final double a, final double b, final double c) {
        GL11.glVertex3d(a, b, c);
    }
    
    public static void glClearDepth(final double depth) {
        GlStateManager.clearDepth(depth);
    }
    
    public static void glLineWidth(final float s) {
        GL11.glLineWidth(s);
    }
    
    public static void glColor3f(final float red, final float green, final float blue) {
        GlStateManager.color(red, green, blue, 1.0f);
    }
    
    public static void glColor4f(final float red, final float green, final float blue, final float alpha) {
        GlStateManager.color(red, green, blue, alpha);
    }
    
    public static void glColor4d(final double red, final double green, final double blue, final double alpha) {
        GlStateManager.color((float)red, (float)green, (float)blue, (float)alpha);
    }
    
    public static void glColorMask(final boolean red, final boolean green, final boolean blue, final boolean alpha) {
        GlStateManager.colorMask(red, green, blue, alpha);
    }
    
    public static void glColorMaterial(final int face, final int mode) {
        GlStateManager.colorMaterial(face, mode);
    }
    
    public static void glCullFace(final int mode) {
        GlStateManager.cullFace(mode);
    }
    
    public static void glDepthFunc(final int func) {
        GlStateManager.depthFunc(func);
    }
    
    public static void glDepthMask(final boolean flag) {
        GlStateManager.depthMask(flag);
    }
    
    public static void glGetFloat(final int pname, final FloatBuffer params) {
        GlStateManager.getFloat(pname, params);
    }
    
    public static void glLoadIdentity() {
        GlStateManager.loadIdentity();
    }
    
    public static void glLogicOp(final int opcode) {
        GlStateManager.colorLogicOp(opcode);
    }
    
    public static void glMatrixMode(final int mode) {
        GlStateManager.matrixMode(mode);
    }
    
    public static void glMultMatrix(final FloatBuffer m) {
        GlStateManager.multMatrix(m);
    }
    
    public static void glOrtho(final double left, final double right, final double bottom, final double top, final double zNear, final double zFar) {
        GlStateManager.ortho(left, right, bottom, top, zNear, zFar);
    }
    
    public static void glPolygonOffset(final float factor, final float units) {
        GlStateManager.doPolygonOffset(factor, units);
    }
    
    public static void glPopAttrib() {
        GlStateManager.popAttrib();
    }
    
    public static void glPopMatrix() {
        GlStateManager.popMatrix();
    }
    
    public static void glPushAttrib() {
        GlStateManager.pushAttrib();
    }
    
    public static void glPushMatrix() {
        GlStateManager.pushMatrix();
    }
    
    public static void glRotatef(final float angle, final float x, final float y, final float z) {
        GlStateManager.rotate(angle, x, y, z);
    }
    
    public static void glScaled(final double x, final double y, final double z) {
        GlStateManager.scale(x, y, z);
    }
    
    public static void glScalef(final float x, final float y, final float z) {
        GlStateManager.scale(x, y, z);
    }
    
    public static void glSetActiveTextureUnit(final int texture) {
        GlStateManager.setActiveTexture(texture);
    }
    
    public static void glShadeModel(final int mode) {
        GlStateManager.shadeModel(mode);
    }
    
    public static void glTranslated(final double x, final double y, final double z) {
        GlStateManager.translate(x, y, z);
    }
    
    public static void glTranslatef(final float x, final float y, final float z) {
        GlStateManager.translate(x, y, z);
    }
    
    public static void glViewport(final int x, final int y, final int width, final int height) {
        GlStateManager.viewport(x, y, width, height);
    }
    
    public static void glBegin(final int mode) {
        GL11.glBegin(mode);
    }
    
    public static void glBindTexture(final int target, final int texture) {
        if (target == 3553) {
            GlStateManager.bindTexture(texture);
        }
        else {
            GL11.glBindTexture(target, texture);
        }
    }
    
    public static void glEnd() {
        GL11.glEnd();
    }
    
    public static int glGenTextures() {
        return GL11.glGenTextures();
    }
    
    public static int glGetTexLevelParameteri(final int target, final int level, final int pname) {
        return GL11.glGetTexLevelParameteri(target, level, pname);
    }
    
    public static void glNormal3f(final float nx, final float ny, final float nz) {
        GL11.glNormal3f(nx, ny, nz);
    }
    
    public static void glPushAttrib(final int mask) {
        GL11.glPushAttrib(mask);
    }
    
    public static void glTexImage2D(final int target, final int level, final int internalformat, final int width, final int height, final int border, final int format, final int type, final IntBuffer pixels) {
        GL11.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
    }
    
    public static void glTexParameteri(final int target, final int pname, final int param) {
        GL11.glTexParameteri(target, pname, param);
    }
    
    public static void glVertex2f(final float x, final float y) {
        GL11.glVertex2f(x, y);
    }
    
    public static void drawChromaString(final String text, final int xIn, final int y) {
        final FontRenderer renderer = Minecraft.getMinecraft().fontRendererObj;
        int x = xIn;
        for (final char c : text.toCharArray()) {
            final long dif = x * 10 - y * 10;
            final long l = System.currentTimeMillis() - dif;
            final float ff = 2000.0f;
            final int i = Color.HSBtoRGB(l % (int)ff / ff, 0.8f, 0.8f);
            final String tmp = String.valueOf(c);
            renderer.drawString(tmp, (float)(x / 1.0), (float)(y / 1.0), i, false);
            x += (int)(renderer.getCharWidth(c) * 1.0);
        }
    }
}