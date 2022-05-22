package net.minecraft.world.storage;

import net.minecraft.world.WorldSavedData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SaveDataMemoryStorage extends MapStorage
{
    public SaveDataMemoryStorage()
    {
        super((ISaveHandler)null);
    }

    public WorldSavedData loadData(Class <? extends WorldSavedData > clazz, String dataIdentifier)
    {
        return (WorldSavedData)this.loadedDataMap.get(dataIdentifier);
    }

    public void setData(String dataIdentifier, WorldSavedData data)
    {
        this.loadedDataMap.put(dataIdentifier, data);
    }

    public void saveAllData()
    {
    }

    public int getUniqueDataId(String key)
    {
        return 0;
    }
}