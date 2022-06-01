package net.mattbenson.cosmetics.bandana;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.WorldRenderer;

public class ModelBoxBandana extends ModelBox
{
    private PositionTextureVertex[] vertexPositions;
    private TexturedQuad[] quadList;
    public float posX1;
    public float posY1;
    public float posZ1;
    public float posX2;
    public float posY2;
    public float posZ2;
    public String boxName;

    private ModelRenderer renderer;
    private int textureX;
    private int textureY;
    private float p_i46301_4_;
    private float p_i46301_5_;
    private float p_i46301_6_;
    private float p_i46301_7_;
    private float p_i46301_8_;
    private float p_i46301_9_;
    private float p_i46301_10_;
    private boolean p_i46301_11_;
    
    public float x1Offset;
    public float x2Offset;
    
    public float y1Offset;
    public float y2Offset;
    
    public ModelBoxBandana(ModelRenderer renderer, int p_i46359_2_, int p_i46359_3_, float p_i46359_4_, float p_i46359_5_, float p_i46359_6_, float p_i46359_7_, float p_i46359_8_, float p_i46359_9_, float p_i46359_10_)
    {
        this(renderer, p_i46359_2_, p_i46359_3_, p_i46359_4_, p_i46359_5_, p_i46359_6_, p_i46359_7_, p_i46359_8_, p_i46359_9_, p_i46359_10_, renderer.mirror);
    }

    public ModelBoxBandana(ModelRenderer renderer, int textureX, int textureY, float p_i46301_4_, float p_i46301_5_, float p_i46301_6_, float p_i46301_7_, float p_i46301_8_, float p_i46301_9_, float p_i46301_10_, boolean p_i46301_11_)
    {
    	super(renderer, textureX, textureY, p_i46301_4_, p_i46301_5_, p_i46301_6_, p_i46301_7_, p_i46301_8_, p_i46301_9_, p_i46301_10_, p_i46301_11_);
    	this.renderer = renderer;
    	this.textureX = textureX;
    	this.textureY = textureY;
    	this.p_i46301_4_ = p_i46301_4_;
    	this.p_i46301_5_ = p_i46301_5_;
    	this.p_i46301_6_ = p_i46301_6_;
    	this.p_i46301_7_ = p_i46301_7_;
    	this.p_i46301_8_ = p_i46301_8_;
    	this.p_i46301_9_ = p_i46301_9_;
    	this.p_i46301_10_ = p_i46301_10_;
    	this.p_i46301_11_ = p_i46301_11_;
    }
    
    public void render(WorldRenderer renderer, float scale)
    {
        for (int i = 0; i < this.quadList.length; ++i)
        {
            this.quadList[i].draw(renderer, scale);
        }
    }

    public ModelBoxBandana setBoxName(String name)
    {
        this.boxName = name;
        return this;
    }
    
    public void reinit() {
        this.posX1 = p_i46301_4_;
        this.posY1 = p_i46301_5_;
        this.posZ1 = p_i46301_6_;
        this.posX2 = p_i46301_4_ + (float)p_i46301_7_;
        this.posY2 = p_i46301_5_ + (float)p_i46301_8_;
        this.posZ2 = p_i46301_6_ + (float)p_i46301_9_;
        this.vertexPositions = new PositionTextureVertex[8];
        this.quadList = new TexturedQuad[6];
        float f = p_i46301_4_ + (float)p_i46301_7_;
        float f1 = p_i46301_5_ + (float)p_i46301_8_;
        float f2 = p_i46301_6_ + (float)p_i46301_9_;
        p_i46301_4_ = p_i46301_4_ - p_i46301_10_;
        p_i46301_5_ = p_i46301_5_ - p_i46301_10_;
        p_i46301_6_ = p_i46301_6_ - p_i46301_10_;
        f = f + p_i46301_10_;
        f1 = f1 + p_i46301_10_;
        f2 = f2 + p_i46301_10_;

        if (p_i46301_11_)
        {
            float f3 = f;
            f = p_i46301_4_;
            p_i46301_4_ = f3;
        }
                
        PositionTextureVertex positiontexturevertex7 = new PositionTextureVertex(p_i46301_4_, p_i46301_5_ + y1Offset + x1Offset, p_i46301_6_, 0.0F, 0.0F);
        PositionTextureVertex positiontexturevertex = new PositionTextureVertex(f, p_i46301_5_ + y2Offset + x1Offset, p_i46301_6_, 0.0F, 8.0F);
        PositionTextureVertex positiontexturevertex1 = new PositionTextureVertex(f, f1 + y2Offset + x1Offset, p_i46301_6_, 8.0F, 8.0F);
        PositionTextureVertex positiontexturevertex2 = new PositionTextureVertex(p_i46301_4_, f1 + y1Offset + x1Offset, p_i46301_6_, 8.0F, 0.0F);
        PositionTextureVertex positiontexturevertex3 = new PositionTextureVertex(p_i46301_4_, p_i46301_5_ + y1Offset + x2Offset, f2, 0.0F, 0.0F);
        PositionTextureVertex positiontexturevertex4 = new PositionTextureVertex(f, p_i46301_5_ + y2Offset + x2Offset, f2, 0.0F, 8.0F);
        PositionTextureVertex positiontexturevertex5 = new PositionTextureVertex(f, f1 + y2Offset + x2Offset, f2, 8.0F, 8.0F);
        PositionTextureVertex positiontexturevertex6 = new PositionTextureVertex(p_i46301_4_, f1 + y1Offset + x2Offset, f2, 8.0F, 0.0F);
        this.vertexPositions[0] = positiontexturevertex7;
        this.vertexPositions[1] = positiontexturevertex;
        this.vertexPositions[2] = positiontexturevertex1;
        this.vertexPositions[3] = positiontexturevertex2;
        this.vertexPositions[4] = positiontexturevertex3;
        this.vertexPositions[5] = positiontexturevertex4;
        this.vertexPositions[6] = positiontexturevertex5;
        this.vertexPositions[7] = positiontexturevertex6;
        this.quadList[0] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex4, positiontexturevertex, positiontexturevertex1, positiontexturevertex5}, textureX + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_, textureX + p_i46301_9_ + p_i46301_7_ + p_i46301_9_, textureY + p_i46301_9_ + p_i46301_8_, renderer.textureWidth, renderer.textureHeight);
        this.quadList[1] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex7, positiontexturevertex3, positiontexturevertex6, positiontexturevertex2}, textureX, textureY + p_i46301_9_, textureX + p_i46301_9_, textureY + p_i46301_9_ + p_i46301_8_, renderer.textureWidth, renderer.textureHeight);
        this.quadList[2] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex4, positiontexturevertex3, positiontexturevertex7, positiontexturevertex}, textureX + p_i46301_9_, textureY, textureX + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_, renderer.textureWidth, renderer.textureHeight);
        this.quadList[3] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex1, positiontexturevertex2, positiontexturevertex6, positiontexturevertex5}, textureX + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_, textureX + p_i46301_9_ + p_i46301_7_ + p_i46301_7_, textureY, renderer.textureWidth, renderer.textureHeight);
        this.quadList[4] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex, positiontexturevertex7, positiontexturevertex2, positiontexturevertex1}, textureX + p_i46301_9_, textureY + p_i46301_9_, textureX + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_ + p_i46301_8_, renderer.textureWidth, renderer.textureHeight);
        this.quadList[5] = new TexturedQuad(new PositionTextureVertex[] {positiontexturevertex3, positiontexturevertex4, positiontexturevertex5, positiontexturevertex6}, textureX + p_i46301_9_ + p_i46301_7_ + p_i46301_9_, textureY + p_i46301_9_, textureX + p_i46301_9_ + p_i46301_7_ + p_i46301_9_ + p_i46301_7_, textureY + p_i46301_9_ + p_i46301_8_, renderer.textureWidth, renderer.textureHeight);

        if (p_i46301_11_)
        {
            for (int i = 0; i < this.quadList.length; ++i)
            {
                this.quadList[i].flipFace();
            }
        }
    }
}