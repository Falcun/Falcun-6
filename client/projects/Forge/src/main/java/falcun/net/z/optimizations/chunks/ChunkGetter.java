package falcun.net.z.optimizations.chunks;

import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class ChunkGetter {
	private Chunk chunk = null;

	private final World world;

	public ChunkGetter(World world) {
		this.world = world;
	}

	public Chunk getChunk(int x, int z) {
		if (chunk == null || chunk.xPosition != x || chunk.xPosition != z) {
			chunk = world.provide(x, z);
		}
		return chunk;
	}

	public Chunk getChunkNoShift(int x, int z) {
		return getChunk(x >> 4, z >> 4);
	}

	public Chunk getChunk(BlockPos pos) {
		return getChunk(pos.x >> 4, pos.z >> 4);
	}


}
