package net.minecraft.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IMerchant
{
    void setCustomer(EntityPlayer p_70932_1_);

    EntityPlayer getCustomer();

    MerchantRecipeList getRecipes(EntityPlayer p_70934_1_);

    @SideOnly(Side.CLIENT)
    void setRecipes(MerchantRecipeList recipeList);

    void useRecipe(MerchantRecipe recipe);

    void verifySellingItem(ItemStack stack);

    IChatComponent getDisplayName();
}