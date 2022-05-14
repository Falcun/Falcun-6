package falcun.xyz.dev.boredhuman.dancore.falcunfork.util;

import falcun.net.util.ChunkUtil;
import falcun.xyz.dev.boredhuman.dancore.falcunfork.Credits;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.chunk.Chunk;

import java.util.Map;
import java.util.Objects;

@Credits("https://github.com/boredhuman/Cannon-Tools/blob/master/src/main/java/dev/boredhuman/cannontools/MiniChunk.java")
public final class MiniChunk {
	private final int x, z;
	private long lastCheck;
	private boolean containsDispenser;
	private static final Minecraft minecraft = Minecraft.getMinecraft();
	public static final Map<Pair<Integer, Integer>, MiniChunk> dispenserCache = new Object2ObjectOpenHashMap<>();

	public static boolean inDispenserRegion(Entity entity, long now) {
		return inDispenserRegion(entity.getPosition(), now);
	}

	public static boolean inDispenserRegion(BlockPos pos1, long now) {
		int cX = MathHelper.floor_double(pos1.getX() / 4.0D);
		int cZ = MathHelper.floor_double(pos1.getZ() / 4.0D);
		Pair<Integer, Integer> miniChunkPosition = new Pair<>(cX, cZ);
		MiniChunk miniChunk = dispenserCache.computeIfAbsent(miniChunkPosition, pos -> new MiniChunk(cX, cZ));
		Pair<Integer, Integer> cXP = new Pair<>(cX + 1, cZ);
		MiniChunk mc2 = dispenserCache.computeIfAbsent(cXP, pos -> new MiniChunk(cX + 1, cZ));
		Pair<Integer, Integer> cXN = new Pair<>(cX - 1, cZ);
		MiniChunk mc3 = dispenserCache.computeIfAbsent(cXN, pos -> new MiniChunk(cX - 1, cZ));
		Pair<Integer, Integer> cZP = new Pair<>(cX, cZ + 1);
		MiniChunk mc4 = dispenserCache.computeIfAbsent(cZP, pos -> new MiniChunk(cX, cZ + 1));
		Pair<Integer, Integer> cZN = new Pair<>(cX, cZ - 1);
		MiniChunk mc5 = dispenserCache.computeIfAbsent(cZN, pos -> new MiniChunk(cX, cZ - 1));
		return miniChunk.containsDispensers(now) || mc2.containsDispensers(now) || mc3.containsDispensers(now) || mc4.containsDispensers(now) || mc5.containsDispensers(now);
	}

	private MiniChunk(int x, int z) {
		this.x = x;
		this.z = z;
	}

	public boolean containsDispensers(long currentTime) {
		WorldClient worldClient = minecraft.theWorld;

		if (worldClient == null) {
			return false;
		}

		if (currentTime - this.lastCheck < 10000L) {
			return this.containsDispenser;
		}

		Chunk chunk = ChunkUtil.getChunk(this.x >> 2, this.z >> 2);
		BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 256; y++) {
				for (int z = 0; z < 4; z++) {
					mutableBlockPos.set((this.x << 2) + x, y, (this.z << 2) + z);
					Block block = chunk.getBlock(mutableBlockPos);
					if (block instanceof BlockDispenser) {
						this.containsDispenser = true;
						break;
					}
				}
			}
		}

		this.lastCheck = currentTime;
		return this.containsDispenser;
	}

	public boolean hasExpired() {
		return System.currentTimeMillis() - this.lastCheck > 1000;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || this.getClass() != o.getClass()) {
			return false;
		}
		MiniChunk miniChunk = (MiniChunk) o;
		return this.x == miniChunk.x && this.z == miniChunk.z;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.x, this.z);
	}
}