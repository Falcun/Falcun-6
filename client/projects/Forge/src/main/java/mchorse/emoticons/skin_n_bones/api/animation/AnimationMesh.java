package mchorse.emoticons.skin_n_bones.api.animation;

import mchorse.emoticons.skin_n_bones.api.bobj.BOBJArmature;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJBone;
import mchorse.emoticons.skin_n_bones.api.bobj.BOBJLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Animation mesh class
 * 
 * This class is responsible for managing an animated mesh. It's 
 * includes binding its texture, animating the vertices based on 
 * armature and weight data, and managing OpengGL resources associated 
 * with this mesh.
 */
@SideOnly(Side.CLIENT)
public class AnimationMesh
{
    public static final boolean DEBUG = false;

    /**
     * Owner of this mesh 
     */
    public Animation owner;

    /**
     * Texture which is going binded when playing back
     */
    public ResourceLocation texture;

    /**
     * Name of this mesh 
     */
    public String name;

    /**
     * Compiled data which has all needed information about the mesh 
     */
    public BOBJLoader.CompiledData data;

    /**
     * Local reference for armature 
     */
    public BOBJArmature armature;

    /**
     * Color alpha
     */
    public float alpha = 1F;

    /* Buffers */
    public FloatBuffer vertices;
    public FloatBuffer normals;
    public FloatBuffer textcoords;
    public IntBuffer indices;

    /* GL buffers */
    public int vertexBuffer;
    public int normalBuffer;
    public int texcoordBuffer;
    public int indexBuffer;

    public AnimationMesh(Animation owner, String name, BOBJLoader.CompiledData data)
    {
        this.owner = owner;
        this.name = name;
        this.data = data;
        this.armature = this.data.mesh.armature;
        this.armature.initArmature();

        this.initBuffers();
    }

    /**
     * Initiate buffers. This method is responsible for allocating 
     * buffers for the data to be passed to VBOs and also generating the 
     * VBOs themselves. 
     */
    private void initBuffers()
    {
        this.vertices = BufferUtils.createFloatBuffer(this.data.posData.length);
        this.vertices.put(this.data.posData).flip();

        this.normals = BufferUtils.createFloatBuffer(this.data.normData.length);
        this.normals.put(this.data.normData).flip();

        this.textcoords = BufferUtils.createFloatBuffer(this.data.texData.length);
        this.textcoords.put(this.data.texData).flip();

        this.indices = BufferUtils.createIntBuffer(this.data.indexData.length);
        this.indices.put(this.data.indexData).flip();

        this.vertexBuffer = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vertexBuffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, this.vertices, GL15.GL_DYNAMIC_DRAW);

        this.normalBuffer = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.normalBuffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, this.normals, GL15.GL_STATIC_DRAW);

        this.texcoordBuffer = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.texcoordBuffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, this.textcoords, GL15.GL_STATIC_DRAW);

        this.indexBuffer = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.indexBuffer);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, this.indices, GL15.GL_STATIC_DRAW);

        /* Unbind the buffer. REQUIRED to avoid OpenGL crash */
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
    }

    /**
     * Set texture filtering
     */
    public void setFiltering(int filtering)
    {
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filtering);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filtering);
    }

    /**
     * Get texture filtering (not really used)
     */
    public int getFiltering()
    {
        return GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER);
    }

    /**
     * Clean up resources which were used by this  
     */
    public void delete()
    {
        GL15.glDeleteBuffers(this.vertexBuffer);
        GL15.glDeleteBuffers(this.normalBuffer);
        GL15.glDeleteBuffers(this.texcoordBuffer);
        GL15.glDeleteBuffers(this.indexBuffer);

        this.vertices = null;
        this.normals = null;
        this.textcoords = null;
        this.indices = null;
    }

    /**
     * Update this mesh. This method is responsible for applying 
     * matrix transformations to vertices and normals according to its 
     * bone owners and these bone influences.
     */
    public void updateMesh()
    {
        Vector4f sumVertex = new Vector4f();
        Vector4f resultVertex = new Vector4f(0, 0, 0, 0);

        Vector3f sumNormal = new Vector3f();
        Vector3f resultNormal = new Vector3f(0, 0, 0);

        float[] oldVertices = this.data.posData;
        float[] newVertices = new float[oldVertices.length];

        float[] oldNormals = this.data.normData;
        float[] newNormals = new float[oldNormals.length];

        Matrix4f[] matrices = this.armature.matrices;

        for (int i = 0, c = newVertices.length / 4; i < c; i++)
        {
            int count = 0;

            for (int w = 0; w < 4; w++)
            {
                float weight = this.data.weightData[i * 4 + w];

                if (weight > 0)
                {
                    int index = this.data.boneIndexData[i * 4 + w];

                    sumVertex.set(oldVertices[i * 4], oldVertices[i * 4 + 1], oldVertices[i * 4 + 2], 1);
                    matrices[index].transform(sumVertex);
                    sumVertex.scale(weight);
                    resultVertex.add(sumVertex);

                    sumNormal.set(oldNormals[i * 3], oldNormals[i * 3 + 1], oldNormals[i * 3 + 2]);
                    matrices[index].transform(sumNormal);
                    sumNormal.scale(weight);
                    resultNormal.add(sumNormal);

                    count++;
                }
            }

            if (count == 0)
            {
                resultNormal.set(oldNormals[i * 3], oldNormals[i * 3 + 1], oldNormals[i * 3 + 2]);
                resultVertex.set(oldVertices[i * 4], oldVertices[i * 4 + 1], oldVertices[i * 4 + 2], 1);
            }

            newVertices[i * 4] = resultVertex.x;
            newVertices[i * 4 + 1] = resultVertex.y;
            newVertices[i * 4 + 2] = resultVertex.z;
            newVertices[i * 4 + 3] = resultVertex.w;

            newNormals[i * 3] = resultNormal.x;
            newNormals[i * 3 + 1] = resultNormal.y;
            newNormals[i * 3 + 2] = resultNormal.z;

            resultVertex.set(0, 0, 0, 0);
            resultNormal.set(0, 0, 0);
        }

        this.updateVertices(newVertices);
        this.updateNormals(newNormals);
    }

    /**
     * Update mesh with given data 
     */
    public void updateVertices(float[] data)
    {
        this.vertices.clear();
        this.vertices.put(data).flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vertexBuffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, this.vertices, GL15.GL_DYNAMIC_DRAW);
    }

    /**
     * Update mesh with given data 
     */
    public void updateNormals(float[] data)
    {
        this.normals.clear();
        this.normals.put(data).flip();

        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.normalBuffer);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, this.normals, GL15.GL_DYNAMIC_DRAW);
    }

    /**
     * Just renders this mesh with whatever data it has
     */
    public void render(Minecraft mc, AnimationMeshConfig config)
    {
        if (config != null && (!config.visible || this.alpha <= 0))
        {
            return;
        }

        ResourceLocation texture = this.getTexture(config);
        boolean smooth = config == null ? false : config.smooth;
        boolean normals = config == null ? false : config.normals;
        boolean lighting = config == null ? true : config.lighting;

        float lastX = OpenGlHelper.lastBrightnessX;
        float lastY = OpenGlHelper.lastBrightnessY;

        /* Bind the texture */
        if (texture != null)
        {
            mc.renderEngine.bindTexture(texture);

            if (config != null)
            {
                this.setFiltering(config.filtering);
            }
        }

        if (smooth && normals) GL11.glShadeModel(GL11.GL_SMOOTH);
        if (!normals) RenderHelper.disableStandardItemLighting();
        if (!lighting) OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

        int color = config != null ? config.color : 0xffffff;
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        float a = this.alpha;

        GlStateManager.color(r, g, b, a);

        /* Bind vertex array */
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.vertexBuffer);
        GL11.glVertexPointer(4, GL11.GL_FLOAT, 0, 0);

        /* Bind normal array */
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.normalBuffer);
        GL11.glNormalPointer(GL11.GL_FLOAT, 0, 0);

        /* Bind UV array */
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, this.texcoordBuffer);
        GL11.glTexCoordPointer(2, GL11.GL_FLOAT, 0, 0);

        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
        GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

        /* Render with index buffer */
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, this.indexBuffer);
        GL11.glDrawElements(GL11.GL_TRIANGLES, this.data.indexData.length, GL11.GL_UNSIGNED_INT, 0);

        /* Unbind the buffer. REQUIRED to avoid OpenGL crash */
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);

        if (smooth && normals) GL11.glShadeModel(GL11.GL_FLAT);
        if (!normals) RenderHelper.enableStandardItemLighting();
        if (!lighting) OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastX, lastY);

        /* Rendering skeletal debug information */
        if (mc.gameSettings.showDebugInfo && !mc.gameSettings.hideGUI && DEBUG)
        {
            /* Skeletal information shouldn't be affected by lighting, 
             * depth (i.e. it will render a top) or textures */
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();

            for (BOBJBone bone : this.data.mesh.armature.orderedBones)
            {
                Vector4f vec1 = new Vector4f(0, 0, 0, 1);
                Vector4f vec2 = new Vector4f(0, bone.length, 0, 1);
                Vector4f x = new Vector4f(0.1F, 0, 0, 1);
                Vector4f y = new Vector4f(0, 0.1F, 0, 1);
                Vector4f z = new Vector4f(0, 0, 0.1F, 1);
                Matrix4f mat = bone.mat;

                mat.transform(vec1);
                mat.transform(vec2);
                mat.transform(x);
                mat.transform(y);
                mat.transform(z);

                /* Draw a point of bone's head */
                GL11.glPointSize(5);
                GL11.glBegin(GL11.GL_POINTS);
                GlStateManager.color(1, 1, 1);
                GL11.glVertex3f(vec1.x, vec1.y, vec1.z);
                GL11.glEnd();

                /* Draw a line between bone's head and tail */
                GL11.glLineWidth(1);
                GL11.glBegin(GL11.GL_LINES);
                GlStateManager.color(0.9F, 0.9F, 0.9F);
                GL11.glVertex3f(vec1.x, vec1.y, vec1.z);
                GL11.glVertex3f(vec2.x, vec2.y, vec2.z);
                GL11.glEnd();

                /* Draw head's axes */
                GL11.glLineWidth(2);
                GL11.glBegin(GL11.GL_LINES);
                GlStateManager.color(1, 0, 0);
                GL11.glVertex3f(vec1.x, vec1.y, vec1.z);
                GL11.glVertex3f(x.x, x.y, x.z);
                GL11.glEnd();

                GL11.glBegin(GL11.GL_LINES);
                GlStateManager.color(0, 1, 0);
                GL11.glVertex3f(vec1.x, vec1.y, vec1.z);
                GL11.glVertex3f(y.x, y.y, y.z);
                GL11.glEnd();

                GL11.glBegin(GL11.GL_LINES);
                GlStateManager.color(0, 0, 1);
                GL11.glVertex3f(vec1.x, vec1.y, vec1.z);
                GL11.glVertex3f(z.x, z.y, z.z);
                GL11.glEnd();

                GlStateManager.color(1, 1, 1);
                GL11.glLineWidth(1);
            }

            GlStateManager.enableDepth();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
        }

        this.alpha = 1;
    }

    /**
     * Get resource location based on the passed config 
     */
    private ResourceLocation getTexture(AnimationMeshConfig config)
    {
        if (config == null)
        {
            return this.texture;
        }

        return config.texture == null ? this.texture : config.texture;
    }
}