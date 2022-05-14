package net.mattbenson.patcher;

import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraft.init.Blocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;

public class ChunkHook
{
    public static IBlockState getBlockState(final Chunk chunk, final BlockPos pos) {
        final int y = pos.getY();
        if (y >= 0 && y >> 4 < chunk.getBlockStorageArray().length) {
            final ExtendedBlockStorage storage = chunk.getBlockStorageArray()[y >> 4];
            if (storage != null) {
                return storage.get(pos.getX() & 0xF, y & 0xF, pos.getZ() & 0xF);
            }
        }
        return Blocks.air.getDefaultState();
    }
}