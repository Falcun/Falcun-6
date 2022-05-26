package falcun.net.z.optimizations.explosions;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

@SideOnly(Side.CLIENT)
public class DurabilityHandler {

	private final Long2ObjectMap<DurabilityInformation> blocks = new Long2ObjectOpenHashMap<>(4096);
	private final Map<BlockPos, DamageInformation> duplicateLock = new HashMap<>(512);
	private final List<BlockPos> blockList = new ArrayList<>(512);
	private final World world;

	public DurabilityHandler(World world) {
		this.world = world;
	}

	public void clearBlocks() {
		this.duplicateLock.clear();
		this.blockList.clear();
	}

	public List<BlockPos> getBlockList() {
		return this.blockList;
	}

	public int getSize() {
		return this.blocks.size();
	}

	public boolean contains(BlockPos blockPosition) {
		return this.duplicateLock.containsKey(blockPosition);
	}

	public DurabilityHandler.DurabilityInformation get(BlockPos blockPosition) {
		return this.blocks.get(blockPosition.toLong());
	}

	public boolean isDamageable(BlockPos blockPosition, IBlockState iblockdata) {
		return this.isDamageable(blockPosition.toLong(), blockPosition.getX(), blockPosition.getY(), blockPosition.getZ(), iblockdata);
	}


	public boolean isDamageable(long key, int x, int y, int z, IBlockState iblockdata) {
		if (iblockdata.getBlock() instanceof BlockObsidian) {
			if (!this.blocks.containsKey(key)) {
				int durability = 3;
//				Block block = this.world.chunkUtils.getBlockAt(x, y, z);
//				Block block = ChunkUtil.getBlock(x,y,z);
				--durability;
				this.blocks.put(key, new DurabilityHandler.DurabilityInformation(durability));
			}

			return true;
		} else {
			return false;
		}
	}

	public void addBlock(BlockPos blockPosition) {
		this.addBlock(blockPosition, 1, 1);
	}

	public void addBlock(BlockPos blockPosition, int damage) {
		this.addBlock(blockPosition, damage, 1);
	}

	public void addBlock(BlockPos blockPosition, int damage, int potential) {
		if (!this.duplicateLock.containsKey(blockPosition)) {
			this.duplicateLock.put(blockPosition, new DurabilityHandler.DamageInformation(damage, potential));
			this.blockList.add(blockPosition);
		}
	}

	public void removeBlocks(List<BlockPos> blocks) {
		Iterator var2 = blocks.iterator();

		while (var2.hasNext()) {
			BlockPos blockPosition = (BlockPos) var2.next();
			this.duplicateLock.remove(blockPosition);
		}
	}

	public void damageBlocks() {
		Iterator var1 = this.duplicateLock.entrySet().iterator();

		while (var1.hasNext()) {
			Map.Entry<BlockPos, DamageInformation> entry = (Map.Entry) var1.next();
			this.damage(entry.getKey(), entry.getValue().getDamage(), entry.getValue().getPotential(), 7);
		}

		this.clearBlocks();
	}

	public boolean damage(BlockPos blockposition, int amount) {
		return this.damage(blockposition, amount, 0, 7);
	}

	private static final IBlockState air = Blocks.air.getDefaultState();

	public boolean damage(BlockPos blockposition, int amount, int potential, int flags) {
		long key = blockposition.toLong();
		DurabilityHandler.DurabilityInformation durabilityInformation = this.blocks.get(key);
		if (durabilityInformation != null) {
			durabilityInformation.durability -= amount;
			if (durabilityInformation.getDurability() <= 0 || potential >= durabilityInformation.getBreakLimit()) {
//				this.world.chunkUtils.setTypeAndData(blockposition, air, flags);
//				ChunkUtil.getBlock(blockposition);
				this.world.getChunkFromBlockCoords(blockposition).setBlockState(blockposition, air);
//				ChunkUtil.getChunk(blockposition.getX() >> 4, blockposition.getZ() >> 4).setBlockState(blockposition, air);
				this.blocks.remove(key);
				return true;
			}
		}

		return false;
	}

	private static class DamageInformation {
		private final int potential;
		private final int damage;

		DamageInformation(int damage, int potential) {
			this.damage = damage;
			this.potential = potential;
		}

		int getDamage() {
			return this.damage;
		}

		int getPotential() {
			return this.potential;
		}
	}

	public static class DurabilityInformation {
		private final int breakLimit;
		int durability;

		DurabilityInformation(int durability) {
			this.durability = durability;
			this.breakLimit = durability;
		}

		public int getDurability() {
			return this.durability;
		}

		public int getBreakLimit() {
			return this.breakLimit;
		}
	}
}
