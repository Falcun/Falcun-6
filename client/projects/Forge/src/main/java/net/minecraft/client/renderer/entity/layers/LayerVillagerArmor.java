package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.model.ModelZombieVillager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerVillagerArmor extends LayerBipedArmor
{
    public LayerVillagerArmor(RendererLivingEntity<?> rendererIn)
    {
        super(rendererIn);
    }

    protected void initArmor()
    {
        this.field_177189_c = new ModelZombieVillager(0.5F, 0.0F, true);
        this.field_177186_d = new ModelZombieVillager(1.0F, 0.0F, true);
    }
}