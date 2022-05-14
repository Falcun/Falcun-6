package net.minecraft.world.gen.layer;

import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.ChunkProviderSettings;

public class GenLayerBiome extends GenLayer
{
    @SuppressWarnings("unchecked")
    private java.util.List<net.minecraftforge.common.BiomeManager.BiomeEntry>[] biomes = new java.util.ArrayList[net.minecraftforge.common.BiomeManager.BiomeType.values().length];

    private final ChunkProviderSettings field_175973_g;

    public GenLayerBiome(long p_i45560_1_, GenLayer p_i45560_3_, WorldType p_i45560_4_, String p_i45560_5_)
    {
        super(p_i45560_1_);
        this.parent = p_i45560_3_;

        for (net.minecraftforge.common.BiomeManager.BiomeType type : net.minecraftforge.common.BiomeManager.BiomeType.values())
        {
            com.google.common.collect.ImmutableList<net.minecraftforge.common.BiomeManager.BiomeEntry> biomesToAdd = net.minecraftforge.common.BiomeManager.getBiomes(type);
            int idx = type.ordinal();

            if (biomes[idx] == null) biomes[idx] = new java.util.ArrayList<net.minecraftforge.common.BiomeManager.BiomeEntry>();
            if (biomesToAdd != null) biomes[idx].addAll(biomesToAdd);
        }

        int desertIdx = net.minecraftforge.common.BiomeManager.BiomeType.DESERT.ordinal();

        biomes[desertIdx].add(new net.minecraftforge.common.BiomeManager.BiomeEntry(BiomeGenBase.desert, 30));
        biomes[desertIdx].add(new net.minecraftforge.common.BiomeManager.BiomeEntry(BiomeGenBase.savanna, 20));
        biomes[desertIdx].add(new net.minecraftforge.common.BiomeManager.BiomeEntry(BiomeGenBase.plains, 10));

        if (p_i45560_4_ == WorldType.DEFAULT_1_1)
        {
            biomes[desertIdx].clear();
            biomes[desertIdx].add(new net.minecraftforge.common.BiomeManager.BiomeEntry(BiomeGenBase.desert, 10));
            biomes[desertIdx].add(new net.minecraftforge.common.BiomeManager.BiomeEntry(BiomeGenBase.forest, 10));
            biomes[desertIdx].add(new net.minecraftforge.common.BiomeManager.BiomeEntry(BiomeGenBase.extremeHills, 10));
            biomes[desertIdx].add(new net.minecraftforge.common.BiomeManager.BiomeEntry(BiomeGenBase.swampland, 10));
            biomes[desertIdx].add(new net.minecraftforge.common.BiomeManager.BiomeEntry(BiomeGenBase.plains, 10));
            biomes[desertIdx].add(new net.minecraftforge.common.BiomeManager.BiomeEntry(BiomeGenBase.taiga, 10));
            this.field_175973_g = null;
        }
        else if (p_i45560_4_ == WorldType.CUSTOMIZED)
        {
            this.field_175973_g = ChunkProviderSettings.Factory.jsonToFactory(p_i45560_5_).func_177864_b();
        }
        else
        {
            this.field_175973_g = null;
        }
    }

    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
    {
        int[] aint = this.parent.getInts(areaX, areaY, areaWidth, areaHeight);
        int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);

        for (int i = 0; i < areaHeight; ++i)
        {
            for (int j = 0; j < areaWidth; ++j)
            {
                this.initChunkSeed((long)(j + areaX), (long)(i + areaY));
                int k = aint[j + i * areaWidth];
                int l = (k & 3840) >> 8;
                k = k & -3841;

                if (this.field_175973_g != null && this.field_175973_g.fixedBiome >= 0)
                {
                    aint1[j + i * areaWidth] = this.field_175973_g.fixedBiome;
                }
                else if (isBiomeOceanic(k))
                {
                    aint1[j + i * areaWidth] = k;
                }
                else if (k == BiomeGenBase.mushroomIsland.biomeID)
                {
                    aint1[j + i * areaWidth] = k;
                }
                else if (k == 1)
                {
                    if (l > 0)
                    {
                        if (this.nextInt(3) == 0)
                        {
                            aint1[j + i * areaWidth] = BiomeGenBase.mesaPlateau.biomeID;
                        }
                        else
                        {
                            aint1[j + i * areaWidth] = BiomeGenBase.mesaPlateau_F.biomeID;
                        }
                    }
                    else
                    {
                        aint1[j + i * areaWidth] = getWeightedBiomeEntry(net.minecraftforge.common.BiomeManager.BiomeType.DESERT).biome.biomeID;
                    }
                }
                else if (k == 2)
                {
                    if (l > 0)
                    {
                        aint1[j + i * areaWidth] = BiomeGenBase.jungle.biomeID;
                    }
                    else
                    {
                        aint1[j + i * areaWidth] = getWeightedBiomeEntry(net.minecraftforge.common.BiomeManager.BiomeType.WARM).biome.biomeID;
                    }
                }
                else if (k == 3)
                {
                    if (l > 0)
                    {
                        aint1[j + i * areaWidth] = BiomeGenBase.megaTaiga.biomeID;
                    }
                    else
                    {
                        aint1[j + i * areaWidth] = getWeightedBiomeEntry(net.minecraftforge.common.BiomeManager.BiomeType.COOL).biome.biomeID;
                    }
                }
                else if (k == 4)
                {
                    aint1[j + i * areaWidth] = getWeightedBiomeEntry(net.minecraftforge.common.BiomeManager.BiomeType.ICY).biome.biomeID;
                }
                else
                {
                    aint1[j + i * areaWidth] = BiomeGenBase.mushroomIsland.biomeID;
                }
            }
        }

        return aint1;
    }

    protected net.minecraftforge.common.BiomeManager.BiomeEntry getWeightedBiomeEntry(net.minecraftforge.common.BiomeManager.BiomeType type)
    {
        java.util.List<net.minecraftforge.common.BiomeManager.BiomeEntry> biomeList = biomes[type.ordinal()];
        int totalWeight = net.minecraft.util.WeightedRandom.getTotalWeight(biomeList);
        int weight = net.minecraftforge.common.BiomeManager.isTypeListModded(type)?nextInt(totalWeight):nextInt(totalWeight / 10) * 10;
        return (net.minecraftforge.common.BiomeManager.BiomeEntry)net.minecraft.util.WeightedRandom.getRandomItem(biomeList, weight);
    }
}