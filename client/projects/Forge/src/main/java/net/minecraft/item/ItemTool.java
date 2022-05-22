package net.minecraft.item;

import com.google.common.collect.Multimap;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemTool extends Item
{
    private Set<Block> effectiveBlocks;
    protected float efficiencyOnProperMaterial = 4.0F;
    private float damageVsEntity;
    protected Item.ToolMaterial toolMaterial;

    protected ItemTool(float attackDamage, Item.ToolMaterial material, Set<Block> effectiveBlocks)
    {
        this.toolMaterial = material;
        this.effectiveBlocks = effectiveBlocks;
        this.maxStackSize = 1;
        this.setMaxDamage(material.getMaxUses());
        this.efficiencyOnProperMaterial = material.getEfficiencyOnProperMaterial();
        this.damageVsEntity = attackDamage + material.getDamageVsEntity();
        this.setCreativeTab(CreativeTabs.tabTools);
        if (this instanceof ItemPickaxe)
        {
            toolClass = "pickaxe";
        }
        else if (this instanceof ItemAxe)
        {
            toolClass = "axe";
        }
        else if (this instanceof ItemSpade)
        {
            toolClass = "shovel";
        }
    }

    public float getStrVsBlock(ItemStack stack, Block block)
    {
        return this.effectiveBlocks.contains(block) ? this.efficiencyOnProperMaterial : 1.0F;
    }

    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker)
    {
        stack.damageItem(2, attacker);
        return true;
    }

    public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn)
    {
        if ((double)blockIn.getBlockHardness(worldIn, pos) != 0.0D)
        {
            stack.damageItem(1, playerIn);
        }

        return true;
    }

    @SideOnly(Side.CLIENT)
    public boolean isFull3D()
    {
        return true;
    }

    public Item.ToolMaterial getToolMaterial()
    {
        return this.toolMaterial;
    }

    public int getItemEnchantability()
    {
        return this.toolMaterial.getEnchantability();
    }

    public String getToolMaterialName()
    {
        return this.toolMaterial.toString();
    }

    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
    {
        ItemStack mat = this.toolMaterial.getRepairItemStack();
        if (mat != null && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false)) return true;
        return super.getIsRepairable(toRepair, repair);
    }

    public Multimap<String, AttributeModifier> getItemAttributeModifiers()
    {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers();
        multimap.put(SharedMonsterAttributes.attackDamage.getAttributeUnlocalizedName(), new AttributeModifier(itemModifierUUID, "Tool modifier", (double)this.damageVsEntity, 0));
        return multimap;
    }

    /*===================================== FORGE START =================================*/
    private String toolClass;
    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass)
    {
        int level = super.getHarvestLevel(stack, toolClass);
        if (level == -1 && toolClass != null && toolClass.equals(this.toolClass))
        {
            return this.toolMaterial.getHarvestLevel();
        }
        else
        {
            return level;
        }
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack)
    {
        return toolClass != null ? com.google.common.collect.ImmutableSet.of(toolClass) : super.getToolClasses(stack);
    }

    @Override
    public float getDigSpeed(ItemStack stack, net.minecraft.block.state.IBlockState state)
    {
        for (String type : getToolClasses(stack))
        {
            if (state.getBlock().isToolEffective(type, state))
                return efficiencyOnProperMaterial;
        }
        return super.getDigSpeed(stack, state);
    }
    /*===================================== FORGE END =================================*/
}