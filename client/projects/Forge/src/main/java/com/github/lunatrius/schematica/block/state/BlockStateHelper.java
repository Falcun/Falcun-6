package com.github.lunatrius.schematica.block.state;

import net.minecraft.block.state.*;
import net.minecraft.block.properties.*;
import java.util.*;
import com.google.common.collect.*;

import net.mattbenson.modules.types.mods.Schematica;
import net.minecraft.block.*;
import net.minecraft.util.*;

public class BlockStateHelper
{
    public static <T extends Comparable<T>> IProperty<T> getProperty(final IBlockState blockState, final String name) {
        for (final IProperty prop : blockState.getPropertyNames()) {
            if (prop.getName().equals(name)) {
                return (IProperty<T>)prop;
            }
        }
        return null;
    }
    
    public static <T extends Comparable<T>> T getPropertyValue(final IBlockState blockState, final String name) {
        final IProperty<T> property = getProperty(blockState, name);
        if (property == null) {
            throw new IllegalArgumentException(name + " does not exist in " + blockState);
        }
        return (T)blockState.getValue((IProperty)property);
    }
    
    public static List<String> getFormattedProperties(final IBlockState blockState) {
        final List<String> list = new ArrayList<String>();
        for (final Map.Entry<IProperty, Comparable> entry : blockState.getProperties().entrySet()) {
            final IProperty key = entry.getKey();
            final Comparable value = entry.getValue();
            String formattedValue = value.toString();
            if (Boolean.TRUE.equals(value)) {
                formattedValue = EnumChatFormatting.GREEN + formattedValue + EnumChatFormatting.RESET;
            }
            else if (Boolean.FALSE.equals(value)) {
                formattedValue = EnumChatFormatting.RED + formattedValue + EnumChatFormatting.RESET;
            }
            list.add(key.getName() + ": " + formattedValue);
        }
        return list;
    }
    
    public static boolean areBlockStatesEqual(final IBlockState blockStateA, final IBlockState blockStateB) {
        if (blockStateA == blockStateB) {
            return true;
        }
        final Block blockA = blockStateA.getBlock();
        final Block blockB = blockStateB.getBlock();
        if (Schematica.fixDispensers && blockA instanceof BlockDispenser && blockB instanceof BlockDispenser) {
            return blockA == blockB && getFixedDispenserMeta((BlockDispenser)blockA, blockStateA) == getFixedDispenserMeta((BlockDispenser)blockB, blockStateB);
        }
        return blockA == blockB && blockA.getMetaFromState(blockStateA) == blockB.getMetaFromState(blockStateB);
    }
    
    public static int getFixedDispenserMeta(final BlockDispenser blockDispenser, final IBlockState blockState) {
        int lvt_2_1_ = 0;
        lvt_2_1_ |= ((EnumFacing)blockState.getValue((IProperty)BlockDispenser.FACING)).getIndex();
        return lvt_2_1_;
    }
}