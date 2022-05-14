package com.github.lunatrius.core.world.chunk;

import net.minecraft.util.BlockPos;

import java.util.Random;

public class ChunkHelper {
    private static final Random RANDOM = new Random();

    public static boolean isSlimeChunk(final long seed, final BlockPos pos) {
        final int x = pos.getX() >> 4;
        final int z = pos.getZ() >> 4;
        RANDOM.setSeed(seed + (x * x * 4987142) + (x * 5947611) + (z * z * 4392871) + (z * 389711) ^ 987234911);
        return RANDOM.nextInt(10) == 0;
    }
}
