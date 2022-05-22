package falcun.net.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public final class ChunkUtil {

	public ChunkUtil() {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void tick(TickEvent.ClientTickEvent e){
		chunk = null;
	}

	private static Chunk chunk = null;
	private static int cX, cZ;
	private static final BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
	private static final Minecraft mc = Minecraft.getMinecraft();

	public static IBlockState getIBlockState(int x, int y, int z) {
		Chunk chunk = getChunk(x >> 4, z >> 4);
		pos.set(x, y, z);
		return chunk.getBlockState(pos);
	}

	public static IBlockState getIBlockState(BlockPos pos) {
		return getIBlockState(pos.getX(), pos.getY(), pos.getZ());
	}

	public static Block getBlock(int x, int y, int z) {
		return getChunk(x >> 4, z >> 4).getBlock(x, y, z);
	}

	public static Block getBlock(BlockPos pos) {
		return getBlock(pos.getX(), pos.getY(), pos.getZ());
	}

	public static BlockState getBlockState(int x, int y, int z) {
		return getBlock(x, y, z).getBlockState();
	}

	public static BlockState getBlockState(BlockPos pos) {
		return getBlockState(pos.getX(), pos.getY(), pos.getZ());
	}

	public static void setBlockState(BlockPos pos, IBlockState state) {
		getChunk(pos.getX() >> 4, pos.getZ() >> 4).setBlockState(pos, state);
	}

	public static void setBlockState(int x, int y, int z, IBlockState state) {
		setBlockState(pos.set(x, y, z), state);
	}

	public static void unloadChunk(int x, int z){
		if (chunk != null && cX == x && cZ == z){
			chunk = null;
		}
	}

	public static Chunk getChunkFast(int x, int z){
		if (chunk == null)return null;
		if (cX == x && cZ == z)return chunk;
		return null;
	}

	public static Chunk getChunk(int x, int z) {
		if (chunk == null || cX != x || cZ  != z){
			return chunk = mc.theWorld.getChunkFromChunkCoords(x,z);
		}
		return chunk;
	}

}