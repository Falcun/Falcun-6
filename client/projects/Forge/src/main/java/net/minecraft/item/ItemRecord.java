package net.minecraft.item;

import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemRecord extends Item
{
    private static final Map<String, ItemRecord> RECORDS = Maps.<String, ItemRecord>newHashMap();
    public final String recordName;

    protected ItemRecord(String name)
    {
        this.recordName = name;
        this.maxStackSize = 1;
        this.setCreativeTab(CreativeTabs.tabMisc);
        RECORDS.put("records." + name, this);
    }

    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);

        if (iblockstate.getBlock() == Blocks.jukebox && !((Boolean)iblockstate.getValue(BlockJukebox.HAS_RECORD)).booleanValue())
        {
            if (worldIn.isRemote)
            {
                return true;
            }
            else
            {
                ((BlockJukebox)Blocks.jukebox).insertRecord(worldIn, pos, iblockstate, stack);
                worldIn.playAuxSFXAtEntity((EntityPlayer)null, 1005, pos, Item.getIdFromItem(this));
                --stack.stackSize;
                playerIn.triggerAchievement(StatList.field_181740_X);
                return true;
            }
        }
        else
        {
            return false;
        }
    }

    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        tooltip.add(this.getRecordNameLocal());
    }

    @SideOnly(Side.CLIENT)
    public String getRecordNameLocal()
    {
        return StatCollector.translateToLocal("item.record." + this.recordName + ".desc");
    }

    public EnumRarity getRarity(ItemStack stack)
    {
        return EnumRarity.RARE;
    }

    @SideOnly(Side.CLIENT)
    public static ItemRecord getRecord(String name)
    {
        return (ItemRecord)RECORDS.get(name);
    }

    /**
     * Retrieves the resource location of the sound to play for this record.
     *
     * @param name The name of the record to play
     * @return The resource location for the audio, null to use default.
     */
    public net.minecraft.util.ResourceLocation getRecordResource(String name)
    {
        return new net.minecraft.util.ResourceLocation(name);
    }
}