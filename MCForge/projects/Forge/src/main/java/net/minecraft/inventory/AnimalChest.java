package net.minecraft.inventory;

import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class AnimalChest extends InventoryBasic
{
    public AnimalChest(String inventoryName, int slotCount)
    {
        super(inventoryName, false, slotCount);
    }

    @SideOnly(Side.CLIENT)
    public AnimalChest(IChatComponent invTitle, int slotCount)
    {
        super(invTitle, slotCount);
    }
}