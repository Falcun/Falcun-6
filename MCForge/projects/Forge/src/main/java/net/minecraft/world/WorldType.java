package net.minecraft.world;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldType
{
    public static WorldType[] worldTypes = new WorldType[16];
    public static final WorldType DEFAULT = (new WorldType(0, "default", 1)).setVersioned();
    public static final WorldType FLAT = new WorldType(1, "flat");
    public static final WorldType LARGE_BIOMES = new WorldType(2, "largeBiomes");
    public static final WorldType AMPLIFIED = (new WorldType(3, "amplified")).setNotificationData();
    public static final WorldType CUSTOMIZED = new WorldType(4, "customized");
    public static final WorldType DEBUG_WORLD = new WorldType(5, "debug_all_block_states");
    public static final WorldType DEFAULT_1_1 = (new WorldType(8, "default_1_1", 0)).setCanBeCreated(false);
    private final int worldTypeId;
    private final String worldType;
    private final int generatorVersion;
    private boolean canBeCreated;
    private boolean isWorldTypeVersioned;
    private boolean hasNotificationData;

    private WorldType(int id, String name)
    {
        this(id, name, 0);
    }

    private WorldType(int id, String name, int version)
    {
        if (name.length() > 16 && DEBUG_WORLD != null) throw new IllegalArgumentException("World type names must not be longer then 16: " + name);
        this.worldType = name;
        this.generatorVersion = version;
        this.canBeCreated = true;
        this.worldTypeId = id;
        worldTypes[id] = this;
    }

    public String getWorldTypeName()
    {
        return this.worldType;
    }

    @SideOnly(Side.CLIENT)
    public String getTranslateName()
    {
        return "generator." + this.worldType;
    }

    @SideOnly(Side.CLIENT)
    public String func_151359_c()
    {
        return this.getTranslateName() + ".info";
    }

    public int getGeneratorVersion()
    {
        return this.generatorVersion;
    }

    public WorldType getWorldTypeForGeneratorVersion(int version)
    {
        return this == DEFAULT && version == 0 ? DEFAULT_1_1 : this;
    }

    private WorldType setCanBeCreated(boolean enable)
    {
        this.canBeCreated = enable;
        return this;
    }

    @SideOnly(Side.CLIENT)
    public boolean getCanBeCreated()
    {
        return this.canBeCreated;
    }

    private WorldType setVersioned()
    {
        this.isWorldTypeVersioned = true;
        return this;
    }

    public boolean isVersioned()
    {
        return this.isWorldTypeVersioned;
    }

    public static WorldType parseWorldType(String type)
    {
        for (int i = 0; i < worldTypes.length; ++i)
        {
            if (worldTypes[i] != null && worldTypes[i].worldType.equalsIgnoreCase(type))
            {
                return worldTypes[i];
            }
        }

        return null;
    }

    public int getWorldTypeID()
    {
        return this.worldTypeId;
    }

    @SideOnly(Side.CLIENT)
    public boolean showWorldInfoNotice()
    {
        return this.hasNotificationData;
    }

    private WorldType setNotificationData()
    {
        this.hasNotificationData = true;
        return this;
    }

    public net.minecraft.world.biome.WorldChunkManager getChunkManager(World world)
    {
        if (this == FLAT)
        {
            net.minecraft.world.gen.FlatGeneratorInfo flatgeneratorinfo = net.minecraft.world.gen.FlatGeneratorInfo.createFlatGeneratorFromString(world.getWorldInfo().getGeneratorOptions());
            return new net.minecraft.world.biome.WorldChunkManagerHell(net.minecraft.world.biome.BiomeGenBase.getBiomeFromBiomeList(flatgeneratorinfo.getBiome(), net.minecraft.world.biome.BiomeGenBase.field_180279_ad), 0.5F);
        }
        else if (this == DEBUG_WORLD)
        {
            return new net.minecraft.world.biome.WorldChunkManagerHell(net.minecraft.world.biome.BiomeGenBase.plains, 0.0F);
        }
        else
        {
            return new net.minecraft.world.biome.WorldChunkManager(world);
        }
    }

    public net.minecraft.world.chunk.IChunkProvider getChunkGenerator(World world, String generatorOptions)
    {
        if (this == FLAT) return new net.minecraft.world.gen.ChunkProviderFlat(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), generatorOptions);
        if (this == DEBUG_WORLD) return new net.minecraft.world.gen.ChunkProviderDebug(world);
        return new net.minecraft.world.gen.ChunkProviderGenerate(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled(), generatorOptions);
    }

    public int getMinimumSpawnHeight(World world)
    {
        return this == FLAT ? 4 : 64;
    }

    public double getHorizon(World world)
    {
        return this == FLAT ? 0.0D : 63.0D;
    }

    public double voidFadeMagnitude()
    {
        return this == FLAT ? 1.0D : 0.03125D;
    }

    public boolean handleSlimeSpawnReduction(java.util.Random random, World world)
    {
        return this == FLAT ? random.nextInt(4) != 1 : false;
    }

    /*=================================================== FORGE START ======================================*/
    private static int getNextID()
    {
        for (int x = 0; x < worldTypes.length; x++)
        {
            if (worldTypes[x] == null)
            {
                return x;
            }
        }

        int oldLen = worldTypes.length;
        worldTypes = java.util.Arrays.copyOf(worldTypes, oldLen + 16);
        return oldLen;
    }

    /**
     * Creates a new world type, the ID is hidden and should not be referenced by modders.
     * It will automatically expand the underlying workdType array if there are no IDs left.
     * @param name
     */
    public WorldType(String name)
    {
        this(getNextID(), name);
    }

    /**
     * Called when 'Create New World' button is pressed before starting game
     */
    public void onGUICreateWorldPress() { }

    /**
     * Gets the spawn fuzz for players who join the world.
     * Useful for void world types.
     * @return Fuzz for entity initial spawn in blocks.
     */
    public int getSpawnFuzz()
    {
        return Math.max(5, net.minecraft.server.MinecraftServer.getServer().getSpawnProtectionSize() - 6);
    }

    /**
     * Called when the 'Customize' button is pressed on world creation GUI
     * @param mc The Minecraft instance
     * @param guiCreateWorld the createworld GUI
     */
    @SideOnly(Side.CLIENT)
    public void onCustomizeButton(net.minecraft.client.Minecraft mc, net.minecraft.client.gui.GuiCreateWorld guiCreateWorld)
    {
        if (this == WorldType.FLAT)
        {
            mc.displayGuiScreen(new net.minecraft.client.gui.GuiCreateFlatWorld(guiCreateWorld, guiCreateWorld.chunkProviderSettingsJson));
        }
        else if (this == WorldType.CUSTOMIZED)
        {
            mc.displayGuiScreen(new net.minecraft.client.gui.GuiCustomizeWorldScreen(guiCreateWorld, guiCreateWorld.chunkProviderSettingsJson));
        }
    }

    /**
     * Should world creation GUI show 'Customize' button for this world type?
     * @return if this world type has customization parameters
     */
    public boolean isCustomizable()
    {
        return this == FLAT || this == WorldType.CUSTOMIZED;
    }


    /**
     * Get the height to render the clouds for this world type
     * @return The height to render clouds at
     */
    public float getCloudHeight()
    {
        return 128.0F;
    }

    /**
     * Creates the GenLayerBiome used for generating the world with the specified ChunkProviderSettings JSON String
     * *IF AND ONLY IF* this WorldType == WorldType.CUSTOMIZED.
     *
     *
     * @param worldSeed The world seed
     * @param parentLayer The parent layer to feed into any layer you return
     * @param chunkProviderSettingsJson The JSON string to use when initializing ChunkProviderSettings.Factory
     * @return A GenLayer that will return ints representing the Biomes to be generated, see GenLayerBiome
     */
    public net.minecraft.world.gen.layer.GenLayer getBiomeLayer(long worldSeed, net.minecraft.world.gen.layer.GenLayer parentLayer, String chunkProviderSettingsJson)
    {
        net.minecraft.world.gen.layer.GenLayer ret = new net.minecraft.world.gen.layer.GenLayerBiome(200L, parentLayer, this, chunkProviderSettingsJson);
        ret = net.minecraft.world.gen.layer.GenLayerZoom.magnify(1000L, ret, 2);
        ret = new net.minecraft.world.gen.layer.GenLayerBiomeEdge(1000L, ret);
        return ret;
    }
}