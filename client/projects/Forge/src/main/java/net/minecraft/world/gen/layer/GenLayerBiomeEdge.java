package net.minecraft.world.gen.layer;

import net.minecraft.world.biome.BiomeGenBase;

public class GenLayerBiomeEdge extends GenLayer
{
    public GenLayerBiomeEdge(long p_i45475_1_, GenLayer p_i45475_3_)
    {
        super(p_i45475_1_);
        this.parent = p_i45475_3_;
    }

    public int[] getInts(int areaX, int areaY, int areaWidth, int areaHeight)
    {
        int[] aint = this.parent.getInts(areaX - 1, areaY - 1, areaWidth + 2, areaHeight + 2);
        int[] aint1 = IntCache.getIntCache(areaWidth * areaHeight);

        for (int i = 0; i < areaHeight; ++i)
        {
            for (int j = 0; j < areaWidth; ++j)
            {
                this.initChunkSeed((long)(j + areaX), (long)(i + areaY));
                int k = aint[j + 1 + (i + 1) * (areaWidth + 2)];

                if (!this.replaceBiomeEdgeIfNecessary(aint, aint1, j, i, areaWidth, k, BiomeGenBase.extremeHills.biomeID, BiomeGenBase.extremeHillsEdge.biomeID) && !this.replaceBiomeEdge(aint, aint1, j, i, areaWidth, k, BiomeGenBase.mesaPlateau_F.biomeID, BiomeGenBase.mesa.biomeID) && !this.replaceBiomeEdge(aint, aint1, j, i, areaWidth, k, BiomeGenBase.mesaPlateau.biomeID, BiomeGenBase.mesa.biomeID) && !this.replaceBiomeEdge(aint, aint1, j, i, areaWidth, k, BiomeGenBase.megaTaiga.biomeID, BiomeGenBase.taiga.biomeID))
                {
                    if (k == BiomeGenBase.desert.biomeID)
                    {
                        int l1 = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
                        int i2 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
                        int j2 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
                        int k2 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];

                        if (l1 != BiomeGenBase.icePlains.biomeID && i2 != BiomeGenBase.icePlains.biomeID && j2 != BiomeGenBase.icePlains.biomeID && k2 != BiomeGenBase.icePlains.biomeID)
                        {
                            aint1[j + i * areaWidth] = k;
                        }
                        else
                        {
                            aint1[j + i * areaWidth] = BiomeGenBase.extremeHillsPlus.biomeID;
                        }
                    }
                    else if (k == BiomeGenBase.swampland.biomeID)
                    {
                        int l = aint[j + 1 + (i + 1 - 1) * (areaWidth + 2)];
                        int i1 = aint[j + 1 + 1 + (i + 1) * (areaWidth + 2)];
                        int j1 = aint[j + 1 - 1 + (i + 1) * (areaWidth + 2)];
                        int k1 = aint[j + 1 + (i + 1 + 1) * (areaWidth + 2)];

                        if (l != BiomeGenBase.desert.biomeID && i1 != BiomeGenBase.desert.biomeID && j1 != BiomeGenBase.desert.biomeID && k1 != BiomeGenBase.desert.biomeID && l != BiomeGenBase.coldTaiga.biomeID && i1 != BiomeGenBase.coldTaiga.biomeID && j1 != BiomeGenBase.coldTaiga.biomeID && k1 != BiomeGenBase.coldTaiga.biomeID && l != BiomeGenBase.icePlains.biomeID && i1 != BiomeGenBase.icePlains.biomeID && j1 != BiomeGenBase.icePlains.biomeID && k1 != BiomeGenBase.icePlains.biomeID)
                        {
                            if (l != BiomeGenBase.jungle.biomeID && k1 != BiomeGenBase.jungle.biomeID && i1 != BiomeGenBase.jungle.biomeID && j1 != BiomeGenBase.jungle.biomeID)
                            {
                                aint1[j + i * areaWidth] = k;
                            }
                            else
                            {
                                aint1[j + i * areaWidth] = BiomeGenBase.jungleEdge.biomeID;
                            }
                        }
                        else
                        {
                            aint1[j + i * areaWidth] = BiomeGenBase.plains.biomeID;
                        }
                    }
                    else
                    {
                        aint1[j + i * areaWidth] = k;
                    }
                }
            }
        }

        return aint1;
    }

    private boolean replaceBiomeEdgeIfNecessary(int[] p_151636_1_, int[] p_151636_2_, int p_151636_3_, int p_151636_4_, int p_151636_5_, int p_151636_6_, int p_151636_7_, int p_151636_8_)
    {
        if (!biomesEqualOrMesaPlateau(p_151636_6_, p_151636_7_))
        {
            return false;
        }
        else
        {
            int i = p_151636_1_[p_151636_3_ + 1 + (p_151636_4_ + 1 - 1) * (p_151636_5_ + 2)];
            int j = p_151636_1_[p_151636_3_ + 1 + 1 + (p_151636_4_ + 1) * (p_151636_5_ + 2)];
            int k = p_151636_1_[p_151636_3_ + 1 - 1 + (p_151636_4_ + 1) * (p_151636_5_ + 2)];
            int l = p_151636_1_[p_151636_3_ + 1 + (p_151636_4_ + 1 + 1) * (p_151636_5_ + 2)];

            if (this.canBiomesBeNeighbors(i, p_151636_7_) && this.canBiomesBeNeighbors(j, p_151636_7_) && this.canBiomesBeNeighbors(k, p_151636_7_) && this.canBiomesBeNeighbors(l, p_151636_7_))
            {
                p_151636_2_[p_151636_3_ + p_151636_4_ * p_151636_5_] = p_151636_6_;
            }
            else
            {
                p_151636_2_[p_151636_3_ + p_151636_4_ * p_151636_5_] = p_151636_8_;
            }

            return true;
        }
    }

    private boolean replaceBiomeEdge(int[] p_151635_1_, int[] p_151635_2_, int p_151635_3_, int p_151635_4_, int p_151635_5_, int p_151635_6_, int p_151635_7_, int p_151635_8_)
    {
        if (p_151635_6_ != p_151635_7_)
        {
            return false;
        }
        else
        {
            int i = p_151635_1_[p_151635_3_ + 1 + (p_151635_4_ + 1 - 1) * (p_151635_5_ + 2)];
            int j = p_151635_1_[p_151635_3_ + 1 + 1 + (p_151635_4_ + 1) * (p_151635_5_ + 2)];
            int k = p_151635_1_[p_151635_3_ + 1 - 1 + (p_151635_4_ + 1) * (p_151635_5_ + 2)];
            int l = p_151635_1_[p_151635_3_ + 1 + (p_151635_4_ + 1 + 1) * (p_151635_5_ + 2)];

            if (biomesEqualOrMesaPlateau(i, p_151635_7_) && biomesEqualOrMesaPlateau(j, p_151635_7_) && biomesEqualOrMesaPlateau(k, p_151635_7_) && biomesEqualOrMesaPlateau(l, p_151635_7_))
            {
                p_151635_2_[p_151635_3_ + p_151635_4_ * p_151635_5_] = p_151635_6_;
            }
            else
            {
                p_151635_2_[p_151635_3_ + p_151635_4_ * p_151635_5_] = p_151635_8_;
            }

            return true;
        }
    }

    private boolean canBiomesBeNeighbors(int p_151634_1_, int p_151634_2_)
    {
        if (biomesEqualOrMesaPlateau(p_151634_1_, p_151634_2_))
        {
            return true;
        }
        else
        {
            BiomeGenBase biomegenbase = BiomeGenBase.getBiome(p_151634_1_);
            BiomeGenBase biomegenbase1 = BiomeGenBase.getBiome(p_151634_2_);

            if (biomegenbase != null && biomegenbase1 != null)
            {
                BiomeGenBase.TempCategory biomegenbase$tempcategory = biomegenbase.getTempCategory();
                BiomeGenBase.TempCategory biomegenbase$tempcategory1 = biomegenbase1.getTempCategory();
                return biomegenbase$tempcategory == biomegenbase$tempcategory1 || biomegenbase$tempcategory == BiomeGenBase.TempCategory.MEDIUM || biomegenbase$tempcategory1 == BiomeGenBase.TempCategory.MEDIUM;
            }
            else
            {
                return false;
            }
        }
    }
}