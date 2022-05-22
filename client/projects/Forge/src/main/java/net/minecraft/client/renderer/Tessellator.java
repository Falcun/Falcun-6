package net.minecraft.client.renderer;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Tessellator
{
    private WorldRenderer worldRenderer;
    private WorldVertexBufferUploader vboUploader = new WorldVertexBufferUploader();
    private static final Tessellator instance = new Tessellator(2097152);

    public static Tessellator getInstance()
    {
        return instance;
    }

    public Tessellator(int bufferSize)
    {
        this.worldRenderer = new WorldRenderer(bufferSize);
    }

    public void draw()
    {
        this.worldRenderer.finishDrawing();
        this.vboUploader.draw(this.worldRenderer);
    }

    public WorldRenderer getWorldRenderer()
    {
        return this.worldRenderer;
    }
}