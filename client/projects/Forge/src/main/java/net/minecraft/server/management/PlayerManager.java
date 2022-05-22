package net.minecraft.server.management;

import com.google.common.collect.Lists;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S21PacketChunkData;
import net.minecraft.network.play.server.S22PacketMultiBlockChange;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PlayerManager
{
    private static final Logger pmLogger = LogManager.getLogger();
    private final WorldServer theWorldServer;
    private final List<EntityPlayerMP> players = Lists.<EntityPlayerMP>newArrayList();
    private final LongHashMap<PlayerManager.PlayerInstance> playerInstances = new LongHashMap();
    private final List<PlayerManager.PlayerInstance> playerInstancesToUpdate = Lists.<PlayerManager.PlayerInstance>newArrayList();
    private final List<PlayerManager.PlayerInstance> playerInstanceList = Lists.<PlayerManager.PlayerInstance>newArrayList();
    private int playerViewRadius;
    private long previousTotalWorldTime;
    private final int[][] xzDirectionsConst = new int[][] {{1, 0}, {0, 1}, { -1, 0}, {0, -1}};

    public PlayerManager(WorldServer serverWorld)
    {
        this.theWorldServer = serverWorld;
        this.setPlayerViewRadius(serverWorld.getMinecraftServer().getConfigurationManager().getViewDistance());
    }

    public WorldServer getWorldServer()
    {
        return this.theWorldServer;
    }

    public void updatePlayerInstances()
    {
        long i = this.theWorldServer.getTotalWorldTime();

        if (i - this.previousTotalWorldTime > 8000L)
        {
            this.previousTotalWorldTime = i;

            for (int j = 0; j < this.playerInstanceList.size(); ++j)
            {
                PlayerManager.PlayerInstance playermanager$playerinstance = (PlayerManager.PlayerInstance)this.playerInstanceList.get(j);
                playermanager$playerinstance.onUpdate();
                playermanager$playerinstance.processChunk();
            }
        }
        else
        {
            for (int k = 0; k < this.playerInstancesToUpdate.size(); ++k)
            {
                PlayerManager.PlayerInstance playermanager$playerinstance1 = (PlayerManager.PlayerInstance)this.playerInstancesToUpdate.get(k);
                playermanager$playerinstance1.onUpdate();
            }
        }

        this.playerInstancesToUpdate.clear();

        if (this.players.isEmpty())
        {
            WorldProvider worldprovider = this.theWorldServer.provider;

            if (!worldprovider.canRespawnHere())
            {
                this.theWorldServer.theChunkProviderServer.unloadAllChunks();
            }
        }
    }

    public boolean hasPlayerInstance(int chunkX, int chunkZ)
    {
        long i = (long)chunkX + 2147483647L | (long)chunkZ + 2147483647L << 32;
        return this.playerInstances.getValueByKey(i) != null;
    }

    private PlayerManager.PlayerInstance getPlayerInstance(int chunkX, int chunkZ, boolean createIfAbsent)
    {
        long i = (long)chunkX + 2147483647L | (long)chunkZ + 2147483647L << 32;
        PlayerManager.PlayerInstance playermanager$playerinstance = (PlayerManager.PlayerInstance)this.playerInstances.getValueByKey(i);

        if (playermanager$playerinstance == null && createIfAbsent)
        {
            playermanager$playerinstance = new PlayerManager.PlayerInstance(chunkX, chunkZ);
            this.playerInstances.add(i, playermanager$playerinstance);
            this.playerInstanceList.add(playermanager$playerinstance);
        }

        return playermanager$playerinstance;
    }

    public void markBlockForUpdate(BlockPos pos)
    {
        int i = pos.getX() >> 4;
        int j = pos.getZ() >> 4;
        PlayerManager.PlayerInstance playermanager$playerinstance = this.getPlayerInstance(i, j, false);

        if (playermanager$playerinstance != null)
        {
            playermanager$playerinstance.flagChunkForUpdate(pos.getX() & 15, pos.getY(), pos.getZ() & 15);
        }
    }

    public void addPlayer(EntityPlayerMP player)
    {
        int i = (int)player.posX >> 4;
        int j = (int)player.posZ >> 4;
        player.managedPosX = player.posX;
        player.managedPosZ = player.posZ;
        // Load nearby chunks first
        List<ChunkCoordIntPair> chunkList = Lists.newArrayList();

        for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; ++k)
        {
            for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l)
            {
                chunkList.add(new ChunkCoordIntPair(k, l));
            }
        }

        java.util.Collections.sort(chunkList, new net.minecraftforge.common.util.ChunkCoordComparator(player));

        for (ChunkCoordIntPair pair : chunkList)
        {
            this.getPlayerInstance(pair.chunkXPos, pair.chunkZPos, true).addPlayer(player);
        }

        this.players.add(player);
        this.filterChunkLoadQueue(player);
    }

    public void filterChunkLoadQueue(EntityPlayerMP player)
    {
        List<ChunkCoordIntPair> list = Lists.newArrayList(player.loadedChunks);
        int i = 0;
        int j = this.playerViewRadius;
        int k = (int)player.posX >> 4;
        int l = (int)player.posZ >> 4;
        int i1 = 0;
        int j1 = 0;
        ChunkCoordIntPair chunkcoordintpair = this.getPlayerInstance(k, l, true).chunkCoords;
        player.loadedChunks.clear();

        if (list.contains(chunkcoordintpair))
        {
            player.loadedChunks.add(chunkcoordintpair);
        }

        for (int k1 = 1; k1 <= j * 2; ++k1)
        {
            for (int l1 = 0; l1 < 2; ++l1)
            {
                int[] aint = this.xzDirectionsConst[i++ % 4];

                for (int i2 = 0; i2 < k1; ++i2)
                {
                    i1 += aint[0];
                    j1 += aint[1];
                    chunkcoordintpair = this.getPlayerInstance(k + i1, l + j1, true).chunkCoords;

                    if (list.contains(chunkcoordintpair))
                    {
                        player.loadedChunks.add(chunkcoordintpair);
                    }
                }
            }
        }

        i = i % 4;

        for (int j2 = 0; j2 < j * 2; ++j2)
        {
            i1 += this.xzDirectionsConst[i][0];
            j1 += this.xzDirectionsConst[i][1];
            chunkcoordintpair = this.getPlayerInstance(k + i1, l + j1, true).chunkCoords;

            if (list.contains(chunkcoordintpair))
            {
                player.loadedChunks.add(chunkcoordintpair);
            }
        }
    }

    public void removePlayer(EntityPlayerMP player)
    {
        int i = (int)player.managedPosX >> 4;
        int j = (int)player.managedPosZ >> 4;

        for (int k = i - this.playerViewRadius; k <= i + this.playerViewRadius; ++k)
        {
            for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l)
            {
                PlayerManager.PlayerInstance playermanager$playerinstance = this.getPlayerInstance(k, l, false);

                if (playermanager$playerinstance != null)
                {
                    playermanager$playerinstance.removePlayer(player);
                }
            }
        }

        this.players.remove(player);
    }

    private boolean overlaps(int x1, int z1, int x2, int z2, int radius)
    {
        int i = x1 - x2;
        int j = z1 - z2;
        return i >= -radius && i <= radius ? j >= -radius && j <= radius : false;
    }

    public void updateMountedMovingPlayer(EntityPlayerMP player)
    {
        int i = (int)player.posX >> 4;
        int j = (int)player.posZ >> 4;
        double d0 = player.managedPosX - player.posX;
        double d1 = player.managedPosZ - player.posZ;
        double d2 = d0 * d0 + d1 * d1;

        if (d2 >= 64.0D)
        {
            int k = (int)player.managedPosX >> 4;
            int l = (int)player.managedPosZ >> 4;
            int i1 = this.playerViewRadius;
            int j1 = i - k;
            int k1 = j - l;
            List<ChunkCoordIntPair> chunksToLoad = Lists.newArrayList();

            if (j1 != 0 || k1 != 0)
            {
                for (int l1 = i - i1; l1 <= i + i1; ++l1)
                {
                    for (int i2 = j - i1; i2 <= j + i1; ++i2)
                    {
                        if (!this.overlaps(l1, i2, k, l, i1))
                        {
                            chunksToLoad.add(new ChunkCoordIntPair(l1, i2));
                        }

                        if (!this.overlaps(l1 - j1, i2 - k1, i, j, i1))
                        {
                            PlayerManager.PlayerInstance playermanager$playerinstance = this.getPlayerInstance(l1 - j1, i2 - k1, false);

                            if (playermanager$playerinstance != null)
                            {
                                playermanager$playerinstance.removePlayer(player);
                            }
                        }
                    }
                }

                this.filterChunkLoadQueue(player);
                player.managedPosX = player.posX;
                player.managedPosZ = player.posZ;
                // send nearest chunks first
                java.util.Collections.sort(chunksToLoad, new net.minecraftforge.common.util.ChunkCoordComparator(player));

                for (ChunkCoordIntPair pair : chunksToLoad)
                {
                    this.getPlayerInstance(pair.chunkXPos, pair.chunkZPos, true).addPlayer(player);
                }

                if (i1 > 1 || i1 < -1 || j1 > 1 || j1 < -1)
                {
                    java.util.Collections.sort(player.loadedChunks, new net.minecraftforge.common.util.ChunkCoordComparator(player));
                }
            }
        }
    }

    public boolean isPlayerWatchingChunk(EntityPlayerMP player, int chunkX, int chunkZ)
    {
        PlayerManager.PlayerInstance playermanager$playerinstance = this.getPlayerInstance(chunkX, chunkZ, false);
        return playermanager$playerinstance != null && playermanager$playerinstance.playersWatchingChunk.contains(player) && !player.loadedChunks.contains(playermanager$playerinstance.chunkCoords);
    }

    public void setPlayerViewRadius(int radius)
    {
        radius = MathHelper.clamp_int(radius, 3, 32);

        if (radius != this.playerViewRadius)
        {
            int i = radius - this.playerViewRadius;

            for (EntityPlayerMP entityplayermp : Lists.newArrayList(this.players))
            {
                int j = (int)entityplayermp.posX >> 4;
                int k = (int)entityplayermp.posZ >> 4;

                if (i > 0)
                {
                    for (int j1 = j - radius; j1 <= j + radius; ++j1)
                    {
                        for (int k1 = k - radius; k1 <= k + radius; ++k1)
                        {
                            PlayerManager.PlayerInstance playermanager$playerinstance = this.getPlayerInstance(j1, k1, true);

                            if (!playermanager$playerinstance.playersWatchingChunk.contains(entityplayermp))
                            {
                                playermanager$playerinstance.addPlayer(entityplayermp);
                            }
                        }
                    }
                }
                else
                {
                    for (int l = j - this.playerViewRadius; l <= j + this.playerViewRadius; ++l)
                    {
                        for (int i1 = k - this.playerViewRadius; i1 <= k + this.playerViewRadius; ++i1)
                        {
                            if (!this.overlaps(l, i1, j, k, radius))
                            {
                                this.getPlayerInstance(l, i1, true).removePlayer(entityplayermp);
                            }
                        }
                    }
                }
            }

            this.playerViewRadius = radius;
        }
    }

    public static int getFurthestViewableBlock(int distance)
    {
        return distance * 16 - 16;
    }

    class PlayerInstance
    {
        private final List<EntityPlayerMP> playersWatchingChunk = Lists.<EntityPlayerMP>newArrayList();
        private final ChunkCoordIntPair chunkCoords;
        private short[] locationOfBlockChange = new short[64];
        private int numBlocksToUpdate;
        private int flagsYAreasToUpdate;
        private long previousWorldTime;
        private final java.util.HashMap<EntityPlayerMP, Runnable> players = new java.util.HashMap<EntityPlayerMP, Runnable>();
        private boolean loaded = false;
        private Runnable loadedRunnable = new Runnable()
        {
            public void run()
            {
                PlayerInstance.this.loaded = true;
            }
        };

        public PlayerInstance(int chunkX, int chunkZ)
        {
            this.chunkCoords = new ChunkCoordIntPair(chunkX, chunkZ);
            PlayerManager.this.getWorldServer().theChunkProviderServer.loadChunk(chunkX, chunkZ, this.loadedRunnable);
        }

        public void addPlayer(EntityPlayerMP player)
        {
            if (this.playersWatchingChunk.contains(player))
            {
                PlayerManager.pmLogger.debug("Failed to add player. {} already is in chunk {}, {}", new Object[] {player, Integer.valueOf(this.chunkCoords.chunkXPos), Integer.valueOf(this.chunkCoords.chunkZPos)});
            }
            else
            {
                if (this.playersWatchingChunk.isEmpty())
                {
                    this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
                }

                this.playersWatchingChunk.add(player);
                Runnable playerRunnable = null;
                if (this.loaded)
                {
                player.loadedChunks.add(this.chunkCoords);
                }
                else
                {
                    final EntityPlayerMP tmp = player;
                    playerRunnable = new Runnable()
                    {
                        public void run()
                        {
                            tmp.loadedChunks.add(PlayerInstance.this.chunkCoords);
                        }
                    };
                    PlayerManager.this.getWorldServer().theChunkProviderServer.loadChunk(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos, playerRunnable);
                }
                this.players.put(player, playerRunnable);
            }
        }

        public void removePlayer(EntityPlayerMP player)
        {
            if (this.playersWatchingChunk.contains(player))
            {
                // If we haven't loaded yet don't load the chunk just so we can clean it up
                if (!this.loaded)
                {
                    net.minecraftforge.common.chunkio.ChunkIOExecutor.dropQueuedChunkLoad(PlayerManager.this.getWorldServer(), this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos, this.players.get(player));
                    this.playersWatchingChunk.remove(player);
                    this.players.remove(player);

                    if (this.playersWatchingChunk.isEmpty())
                    {
                        net.minecraftforge.common.chunkio.ChunkIOExecutor.dropQueuedChunkLoad(PlayerManager.this.getWorldServer(), this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos, this.loadedRunnable);
                        long i = (long) this.chunkCoords.chunkXPos + 2147483647L | (long) this.chunkCoords.chunkZPos + 2147483647L << 32;
                        PlayerManager.this.playerInstances.remove(i);
                        PlayerManager.this.playerInstanceList.remove(this);
                    }

                    return;
                }

                Chunk chunk = PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);

                if (chunk.isPopulated())
                {
                    player.playerNetServerHandler.sendPacket(new S21PacketChunkData(chunk, true, 0));
                }

                this.players.remove(player);
                this.playersWatchingChunk.remove(player);
                player.loadedChunks.remove(this.chunkCoords);

                net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.world.ChunkWatchEvent.UnWatch(chunkCoords, player));

                if (this.playersWatchingChunk.isEmpty())
                {
                    long i = (long)this.chunkCoords.chunkXPos + 2147483647L | (long)this.chunkCoords.chunkZPos + 2147483647L << 32;
                    this.increaseInhabitedTime(chunk);
                    PlayerManager.this.playerInstances.remove(i);
                    PlayerManager.this.playerInstanceList.remove(this);

                    if (this.numBlocksToUpdate > 0)
                    {
                        PlayerManager.this.playerInstancesToUpdate.remove(this);
                    }

                    PlayerManager.this.getWorldServer().theChunkProviderServer.dropChunk(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos);
                }
            }
        }

        public void processChunk()
        {
            this.increaseInhabitedTime(PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos));
        }

        private void increaseInhabitedTime(Chunk theChunk)
        {
            theChunk.setInhabitedTime(theChunk.getInhabitedTime() + PlayerManager.this.theWorldServer.getTotalWorldTime() - this.previousWorldTime);
            this.previousWorldTime = PlayerManager.this.theWorldServer.getTotalWorldTime();
        }

        public void flagChunkForUpdate(int x, int y, int z)
        {
            if (this.numBlocksToUpdate == 0)
            {
                PlayerManager.this.playerInstancesToUpdate.add(this);
            }

            this.flagsYAreasToUpdate |= 1 << (y >> 4);

            //Forge; Cache everything, so always run
            {
                short short1 = (short)(x << 12 | z << 8 | y);

                for (int i = 0; i < this.numBlocksToUpdate; ++i)
                {
                    if (this.locationOfBlockChange[i] == short1)
                    {
                        return;
                    }
                }

                if (numBlocksToUpdate == locationOfBlockChange.length)
                {
                    locationOfBlockChange = java.util.Arrays.copyOf(locationOfBlockChange, locationOfBlockChange.length << 1);
                }
                this.locationOfBlockChange[this.numBlocksToUpdate++] = short1;
            }
        }

        public void sendToAllPlayersWatchingChunk(Packet thePacket)
        {
            for (int i = 0; i < this.playersWatchingChunk.size(); ++i)
            {
                EntityPlayerMP entityplayermp = (EntityPlayerMP)this.playersWatchingChunk.get(i);

                if (!entityplayermp.loadedChunks.contains(this.chunkCoords))
                {
                    entityplayermp.playerNetServerHandler.sendPacket(thePacket);
                }
            }
        }

        @SuppressWarnings("unused")
        public void onUpdate()
        {
            if (this.numBlocksToUpdate != 0)
            {
                if (this.numBlocksToUpdate == 1)
                {
                    int i = (this.locationOfBlockChange[0] >> 12 & 15) + this.chunkCoords.chunkXPos * 16;
                    int j = this.locationOfBlockChange[0] & 255;
                    int k = (this.locationOfBlockChange[0] >> 8 & 15) + this.chunkCoords.chunkZPos * 16;
                    BlockPos blockpos = new BlockPos(i, j, k);
                    this.sendToAllPlayersWatchingChunk(new S23PacketBlockChange(PlayerManager.this.theWorldServer, blockpos));

                    if (PlayerManager.this.theWorldServer.getBlockState(blockpos).getBlock().hasTileEntity(PlayerManager.this.theWorldServer.getBlockState(blockpos)))
                    {
                        this.sendTileToAllPlayersWatchingChunk(PlayerManager.this.theWorldServer.getTileEntity(blockpos));
                    }
                }
                else if (this.numBlocksToUpdate >= net.minecraftforge.common.ForgeModContainer.clumpingThreshold)
                {
                    int i1 = this.chunkCoords.chunkXPos * 16;
                    int k1 = this.chunkCoords.chunkZPos * 16;
                    this.sendToAllPlayersWatchingChunk(new S21PacketChunkData(PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos), false, this.flagsYAreasToUpdate));

                    // Forge: Grabs ALL tile entities is costly on a modded server, only send needed ones
                    for (int i2 = 0; false && i2 < 16; ++i2)
                    {
                        if ((this.flagsYAreasToUpdate & 1 << i2) != 0)
                        {
                            int k2 = i2 << 4;
                            List<TileEntity> list = PlayerManager.this.theWorldServer.getTileEntitiesIn(i1, k2, k1, i1 + 16, k2 + 16, k1 + 16);

                            for (int l = 0; l < list.size(); ++l)
                            {
                                this.sendTileToAllPlayersWatchingChunk((TileEntity)list.get(l));
                            }
                        }
                    }
                }
                else
                {
                    this.sendToAllPlayersWatchingChunk(new S22PacketMultiBlockChange(this.numBlocksToUpdate, this.locationOfBlockChange, PlayerManager.this.theWorldServer.getChunkFromChunkCoords(this.chunkCoords.chunkXPos, this.chunkCoords.chunkZPos)));
                }
                { // Forge: Send only the tile entities that are updated, Adding this brace lets us keep the indent and the patch small
                    WorldServer world = PlayerManager.this.theWorldServer;
                    for (int j1 = 0; j1 < this.numBlocksToUpdate; ++j1)
                    {
                        int l1 = (this.locationOfBlockChange[j1] >> 12 & 15) + this.chunkCoords.chunkXPos * 16;
                        int j2 = this.locationOfBlockChange[j1] & 255;
                        int l2 = (this.locationOfBlockChange[j1] >> 8 & 15) + this.chunkCoords.chunkZPos * 16;
                        BlockPos blockpos1 = new BlockPos(l1, j2, l2);

                        if (world.getBlockState(blockpos1).getBlock().hasTileEntity(world.getBlockState(blockpos1)))
                        {
                            this.sendTileToAllPlayersWatchingChunk(PlayerManager.this.theWorldServer.getTileEntity(blockpos1));
                        }
                    }
                }

                this.numBlocksToUpdate = 0;
                this.flagsYAreasToUpdate = 0;
            }
        }

        private void sendTileToAllPlayersWatchingChunk(TileEntity theTileEntity)
        {
            if (theTileEntity != null)
            {
                Packet packet = theTileEntity.getDescriptionPacket();

                if (packet != null)
                {
                    this.sendToAllPlayersWatchingChunk(packet);
                }
            }
        }
    }
}