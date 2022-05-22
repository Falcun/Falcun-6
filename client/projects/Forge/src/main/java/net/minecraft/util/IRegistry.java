package net.minecraft.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IRegistry<K, V> extends Iterable<V>
{
    @SideOnly(Side.CLIENT)
    V getObject(K name);

    @SideOnly(Side.CLIENT)
    void putObject(K key, V value);
}