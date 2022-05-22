package net.minecraft.world;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.biome.WorldChunkManager;
import net.minecraft.world.biome.WorldChunkManagerHell;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderDebug;
import net.minecraft.world.gen.ChunkProviderFlat;
import net.minecraft.world.gen.ChunkProviderGenerate;
import net.minecraft.world.gen.FlatGeneratorInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class WorldProvider
{
    public static final float[] moonPhaseFactors = new float[] {1.0F, 0.75F, 0.5F, 0.25F, 0.0F, 0.25F, 0.5F, 0.75F};
    protected World worldObj;
    private WorldType terrainType;
    private String generatorSettings;
    protected WorldChunkManager worldChunkMgr;
    protected boolean isHellWorld;
    protected boolean hasNoSky;
    protected final float[] lightBrightnessTable = new float[16];
    protected int dimensionId;
    private final float[] colorsSunriseSunset = new float[4];

    public final void registerWorld(World worldIn)
    {
        this.worldObj = worldIn;
        this.terrainType = worldIn.getWorldInfo().getTerrainType();
        this.generatorSettings = worldIn.getWorldInfo().getGeneratorOptions();
        this.registerWorldChunkManager();
        this.generateLightBrightnessTable();
    }

    protected void generateLightBrightnessTable()
    {
        float f = 0.0F;

        for (int i = 0; i <= 15; ++i)
        {
            float f1 = 1.0F - (float)i / 15.0F;
            this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
        }
    }

    protected void registerWorldChunkManager()
    {
        this.worldChunkMgr = terrainType.getChunkManager(worldObj);
    }

    public IChunkProvider createChunkGenerator()
    {
        return terrainType.getChunkGenerator(worldObj, generatorSettings);
    }

    public boolean canCoordinateBeSpawn(int x, int z)
    {
        return this.worldObj.getGroundAboveSeaLevel(new BlockPos(x, 0, z)) == Blocks.grass;
    }

    public float calculateCelestialAngle(long p_76563_1_, float p_76563_3_)
    {
        int i = (int)(p_76563_1_ % 24000L);
        float f = ((float)i + p_76563_3_) / 24000.0F - 0.25F;

        if (f < 0.0F)
        {
            ++f;
        }

        if (f > 1.0F)
        {
            --f;
        }

        float f1 = 1.0F - (float)((Math.cos((double)f * Math.PI) + 1.0D) / 2.0D);
        f = f + (f1 - f) / 3.0F;
        return f;
    }

    public int getMoonPhase(long p_76559_1_)
    {
        return (int)(p_76559_1_ / 24000L % 8L + 8L) % 8;
    }

    public boolean isSurfaceWorld()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public float[] calcSunriseSunsetColors(float celestialAngle, float partialTicks)
    {
        float f = 0.4F;
        float f1 = MathHelper.cos(celestialAngle * (float)Math.PI * 2.0F) - 0.0F;
        float f2 = -0.0F;

        if (f1 >= f2 - f && f1 <= f2 + f)
        {
            float f3 = (f1 - f2) / f * 0.5F + 0.5F;
            float f4 = 1.0F - (1.0F - MathHelper.sin(f3 * (float)Math.PI)) * 0.99F;
            f4 = f4 * f4;
            this.colorsSunriseSunset[0] = f3 * 0.3F + 0.7F;
            this.colorsSunriseSunset[1] = f3 * f3 * 0.7F + 0.2F;
            this.colorsSunriseSunset[2] = f3 * f3 * 0.0F + 0.2F;
            this.colorsSunriseSunset[3] = f4;
            return this.colorsSunriseSunset;
        }
        else
        {
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    public Vec3 getFogColor(float p_76562_1_, float p_76562_2_)
    {
        float f = MathHelper.cos(p_76562_1_ * (float)Math.PI * 2.0F) * 2.0F + 0.5F;
        f = MathHelper.clamp_float(f, 0.0F, 1.0F);
        float f1 = 0.7529412F;
        float f2 = 0.84705883F;
        float f3 = 1.0F;
        f1 = f1 * (f * 0.94F + 0.06F);
        f2 = f2 * (f * 0.94F + 0.06F);
        f3 = f3 * (f * 0.91F + 0.09F);
        return new Vec3((double)f1, (double)f2, (double)f3);
    }

    public boolean canRespawnHere()
    {
        return true;
    }

    public static WorldProvider getProviderForDimension(int dimension)
    {
        return net.minecraftforge.common.DimensionManager.createProviderFor(dimension);
    }

    @SideOnly(Side.CLIENT)
    public float getCloudHeight()
    {
        return this.terrainType.getCloudHeight();
    }

    @SideOnly(Side.CLIENT)
    public boolean isSkyColored()
    {
        return true;
    }

    public BlockPos getSpawnCoordinate()
    {
        return null;
    }

    public int getAverageGroundLevel()
    {
        return this.terrainType.getMinimumSpawnHeight(this.worldObj);
    }

    @SideOnly(Side.CLIENT)
    public double getVoidFogYFactor()
    {
        return this.terrainType.voidFadeMagnitude();
    }

    @SideOnly(Side.CLIENT)
    public boolean doesXZShowFog(int x, int z)
    {
        return false;
    }

    public abstract String getDimensionName();

    public abstract String getInternalNameSuffix();

    public WorldChunkManager getWorldChunkManager()
    {
        return this.worldChunkMgr;
    }

    public boolean doesWaterVaporize()
    {
        return this.isHellWorld;
    }

    public boolean getHasNoSky()
    {
        return this.hasNoSky;
    }

    public float[] getLightBrightnessTable()
    {
        return this.lightBrightnessTable;
    }

    public int getDimensionId()
    {
        return this.dimensionId;
    }

    public WorldBorder getWorldBorder()
    {
        return new WorldBorder();
    }

    /*======================================= Forge Start =========================================*/
    private net.minecraftforge.client.IRenderHandler skyRenderer = null;
    private net.minecraftforge.client.IRenderHandler cloudRenderer = null;
    private net.minecraftforge.client.IRenderHandler weatherRenderer = null;

    /**
     * Sets the providers current dimension ID, used in default getSaveFolder()
     * Added to allow default providers to be registered for multiple dimensions.
     *
     * @param dim Dimension ID
     */
    public void setDimension(int dim)
    {
        this.dimensionId = dim;
    }

    /**
     * Returns the sub-folder of the world folder that this WorldProvider saves to.
     * EXA: DIM1, DIM-1
     * @return The sub-folder name to save this world's chunks to.
     */
    public String getSaveFolder()
    {
        return (dimensionId == 0 ? null : "DIM" + dimensionId);
    }

    /**
     * A message to display to the user when they transfer to this dimension.
     *
     * @return The message to be displayed
     */
    public String getWelcomeMessage()
    {
        if (this instanceof WorldProviderEnd)
        {
            return "Entering the End";
        }
        else if (this instanceof WorldProviderHell)
        {
            return "Entering the Nether";
        }
        return null;
    }

    /**
     * A Message to display to the user when they transfer out of this dismension.
     *
     * @return The message to be displayed
     */
    public String getDepartMessage()
    {
        if (this instanceof WorldProviderEnd)
        {
            return "Leaving the End";
        }
        else if (this instanceof WorldProviderHell)
        {
            return "Leaving the Nether";
        }
        return null;
    }

    /**
     * The dimensions movement factor. Relative to normal overworld.
     * It is applied to the players position when they transfer dimensions.
     * Exa: Nether movement is 8.0
     * @return The movement factor
     */
    public double getMovementFactor()
    {
        if (this instanceof WorldProviderHell)
        {
            return 8.0;
        }
        return 1.0;
    }

    @SideOnly(Side.CLIENT)
    public net.minecraftforge.client.IRenderHandler getSkyRenderer()
    {
        return this.skyRenderer;
    }

    @SideOnly(Side.CLIENT)
    public void setSkyRenderer(net.minecraftforge.client.IRenderHandler skyRenderer)
    {
        this.skyRenderer = skyRenderer;
    }

    @SideOnly(Side.CLIENT)
    public net.minecraftforge.client.IRenderHandler getCloudRenderer()
    {
        return cloudRenderer;
    }

    @SideOnly(Side.CLIENT)
    public void setCloudRenderer(net.minecraftforge.client.IRenderHandler renderer)
    {
        cloudRenderer = renderer;
    }

    @SideOnly(Side.CLIENT)
    public net.minecraftforge.client.IRenderHandler getWeatherRenderer()
    {
        return weatherRenderer;
    }

    @SideOnly(Side.CLIENT)
    public void setWeatherRenderer(net.minecraftforge.client.IRenderHandler renderer)
    {
        weatherRenderer = renderer;
    }

    public BlockPos getRandomizedSpawnPoint()
    {
        BlockPos ret = this.worldObj.getSpawnPoint();

        boolean isAdventure = worldObj.getWorldInfo().getGameType() == WorldSettings.GameType.ADVENTURE;
        int spawnFuzz = terrainType.getSpawnFuzz();
        int border = MathHelper.floor_double(worldObj.getWorldBorder().getClosestDistance(ret.getX(), ret.getZ()));
        if (border < spawnFuzz) spawnFuzz = border;
        if (spawnFuzz < 1) spawnFuzz = 1;
        int spawnFuzzHalf = spawnFuzz / 2;

        if (!getHasNoSky() && !isAdventure)
        {
            ret = worldObj.getTopSolidOrLiquidBlock(ret.add(worldObj.rand.nextInt(spawnFuzzHalf) - spawnFuzz, 0, worldObj.rand.nextInt(spawnFuzzHalf) - spawnFuzz));
        }

        return ret;
    }
    /**
     * Determine if the cursor on the map should 'spin' when rendered, like it does for the player in the nether.
     *
     * @param entity The entity holding the map, playername, or frame-ENTITYID
     * @param x X Position
     * @param y Y Position
     * @param z Z Position
     * @return True to 'spin' the cursor
     */
    public boolean shouldMapSpin(String entity, double x, double y, double z)
    {
        return dimensionId < 0;
    }

    /**
     * Determines the dimension the player will be respawned in, typically this brings them back to the overworld.
     *
     * @param player The player that is respawning
     * @return The dimension to respawn the player in
     */
    public int getRespawnDimension(net.minecraft.entity.player.EntityPlayerMP player)
    {
        return 0;
    }

    /*======================================= Start Moved From World =========================================*/

    public BiomeGenBase getBiomeGenForCoords(BlockPos pos)
    {
        return worldObj.getBiomeGenForCoordsBody(pos);
    }

    public boolean isDaytime()
    {
        return worldObj.getSkylightSubtracted() < 4;
    }

    /**
     * The current sun brightness factor for this dimension.
     * 0.0f means no light at all, and 1.0f means maximum sunlight.
     * This will be used for the "calculateSkylightSubtracted"
     * which is for Sky light value calculation.
     *
     * @return The current brightness factor
     * */
    public float getSunBrightnessFactor(float par1)
    {
        return worldObj.getSunBrightnessFactor(par1);
    }

    /**
     * Calculates the current moon phase factor.
     * This factor is effective for slimes.
     * (This method do not affect the moon rendering)
     * */
    public float getCurrentMoonPhaseFactor()
    {
        return worldObj.getCurrentMoonPhaseFactorBody();
    }

    @SideOnly(Side.CLIENT)
    public Vec3 getSkyColor(net.minecraft.entity.Entity cameraEntity, float partialTicks)
    {
        return worldObj.getSkyColorBody(cameraEntity, partialTicks);
    }

    @SideOnly(Side.CLIENT)
    public Vec3 drawClouds(float partialTicks)
    {
        return worldObj.drawCloudsBody(partialTicks);
    }

    /**
     * Gets the Sun Brightness for rendering sky.
     * */
    @SideOnly(Side.CLIENT)
    public float getSunBrightness(float par1)
    {
        return worldObj.getSunBrightnessBody(par1);
    }

    /**
     * Gets the Star Brightness for rendering sky.
     * */
    @SideOnly(Side.CLIENT)
    public float getStarBrightness(float par1)
    {
        return worldObj.getStarBrightnessBody(par1);
    }

    public void setAllowedSpawnTypes(boolean allowHostile, boolean allowPeaceful)
    {
        worldObj.spawnHostileMobs = allowHostile;
        worldObj.spawnPeacefulMobs = allowPeaceful;
    }

    public void calculateInitialWeather()
    {
        worldObj.calculateInitialWeatherBody();
    }

    public void updateWeather()
    {
        worldObj.updateWeatherBody();
    }

    public boolean canBlockFreeze(BlockPos pos, boolean byWater)
    {
        return worldObj.canBlockFreezeBody(pos, byWater);
    }

    public boolean canSnowAt(BlockPos pos, boolean checkLight)
    {
        return worldObj.canSnowAtBody(pos, checkLight);
    }
    public void setWorldTime(long time)
    {
        worldObj.worldInfo.setWorldTime(time);
    }

    public long getSeed()
    {
        return worldObj.worldInfo.getSeed();
    }

    public long getWorldTime()
    {
        return worldObj.worldInfo.getWorldTime();
    }

    public BlockPos getSpawnPoint()
    {
        net.minecraft.world.storage.WorldInfo info = worldObj.worldInfo;
        return new BlockPos(info.getSpawnX(), info.getSpawnY(), info.getSpawnZ());
    }

    public void setSpawnPoint(BlockPos pos)
    {
        worldObj.worldInfo.setSpawn(pos);
    }

    public boolean canMineBlock(net.minecraft.entity.player.EntityPlayer player, BlockPos pos)
    {
        return worldObj.canMineBlockBody(player, pos);
    }

    public boolean isBlockHighHumidity(BlockPos pos)
    {
        return worldObj.getBiomeGenForCoords(pos).isHighHumidity();
    }

    public int getHeight()
    {
        return 256;
    }

    public int getActualHeight()
    {
        return hasNoSky ? 128 : 256;
    }

    public double getHorizon()
    {
        return worldObj.worldInfo.getTerrainType().getHorizon(worldObj);
    }

    public void resetRainAndThunder()
    {
        worldObj.worldInfo.setRainTime(0);
        worldObj.worldInfo.setRaining(false);
        worldObj.worldInfo.setThunderTime(0);
        worldObj.worldInfo.setThundering(false);
    }

    public boolean canDoLightning(net.minecraft.world.chunk.Chunk chunk)
    {
        return true;
    }

    public boolean canDoRainSnowIce(net.minecraft.world.chunk.Chunk chunk)
    {
        return true;
    }
}