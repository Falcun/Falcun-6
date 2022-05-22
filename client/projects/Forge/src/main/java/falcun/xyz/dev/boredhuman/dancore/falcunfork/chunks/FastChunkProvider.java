package falcun.xyz.dev.boredhuman.dancore.falcunfork.chunks;

import com.google.common.collect.Lists;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.Credits;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Credits("https://github.com/boredhuman/Dancore/blob/master/src/main/java/dev/boredhuman/mods/chunks/FastChunkProvider.java")
public class FastChunkProvider extends ChunkProviderClient {
	private final Chunk blankChunk;
	private final Map<Long, Chunk> chunkMapping = new HashMap<>();
	private final List<Chunk> chunkListing = Lists.<Chunk>newArrayList();
	private final World worldObj;
	private Chunk[][] chunks;

	public FastChunkProvider(World worldIn) {
		super(worldIn);
		this.blankChunk = new EmptyChunk(worldIn, 0, 0);
		this.worldObj = worldIn;
	}

	public void unloadChunk(int x, int z) {
		Chunk chunk = this.provideChunk(x, z);

		if (!chunk.isEmpty()) {
			chunk.onChunkUnload();
		}

		this.chunkMapping.remove(ChunkCoordIntPair.chunkXZ2Int(x, z));
		this.chunkListing.remove(chunk);
	}

	public Chunk loadChunk(int chunkX, int chunkZ) {
		Chunk chunk = new Chunk(this.worldObj, chunkX, chunkZ);
		this.chunkMapping.put(ChunkCoordIntPair.chunkXZ2Int(chunkX, chunkZ), chunk);
		this.chunkListing.add(chunk);
		net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.world.ChunkEvent.Load(chunk));
		chunk.setChunkLoaded(true);

		return chunk;
	}

	public Chunk provideChunk(int x, int z) {
		//Chunk chunk = this.chunkMapping.get(ChunkCoordIntPair.chunkXZ2Int(x, z));
		Chunk chunk = this.getChunk(x, z);
		return chunk == null ? this.blankChunk : chunk;
	}

	public String makeString() {
		return "MultiplayerChunkCache: " + this.chunkMapping.size() + ", " + this.chunkListing.size();
	}

	@Override
	public boolean unloadQueuedChunks() {
		long i = System.currentTimeMillis();

		for (Chunk chunk : this.chunkListing) {
			chunk.func_150804_b(System.currentTimeMillis() - i > 5L);
		}

		if (System.currentTimeMillis() - i > 100L) {
			System.out.println("Took " + (System.currentTimeMillis() - i) + " to unload queud chunks");
		}

		return false;
	}

	int startX, startZ;

	public void onTick() {
		Minecraft mc = Minecraft.getMinecraft();
		int radius = Minecraft.getMinecraft().gameSettings.renderDistanceChunks + 3;
		int length = (radius + 1) * 2;
		this.chunks = new Chunk[length][length];
		int centerZ = mc.thePlayer.chunkCoordZ;
		int centerX = mc.thePlayer.chunkCoordX;
		this.startX = centerX - radius;
		this.startZ = centerZ - radius;
		for (int x = 0; x < length; x++) {
			for (int z = 0; z < length; z++) {
				this.chunks[x][z] = this.chunkMapping.get(ChunkCoordIntPair.chunkXZ2Int(this.startX + x, this.startZ + z));
			}
		}
	}

	public Chunk getChunk(int x, int z) {
		if (this.chunks == null) {
			return this.chunkMapping.get(ChunkCoordIntPair.chunkXZ2Int(x, z));
		}
		int xIndex = x - this.startX;
		int zIndex = z - this.startZ;
		if (xIndex < 0 || zIndex < 0 || xIndex > this.chunks.length - 1 || zIndex > this.chunks.length - 1) {
			return this.chunkMapping.get(ChunkCoordIntPair.chunkXZ2Int(x, z));
		}
		Chunk result = this.chunks[xIndex][zIndex];
		if (result == null) {
			result = this.chunkMapping.get(ChunkCoordIntPair.chunkXZ2Int(x, z));
		}
		return result;
	}
}
