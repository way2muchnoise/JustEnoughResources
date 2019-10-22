package jeresources.profiling;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.EmptyChunk;

import javax.annotation.Nullable;

public class EmptyChunkJER extends EmptyChunk {
    public EmptyChunkJER(World worldIn, int x, int z) {
        super(worldIn, new ChunkPos(x, z));
    }

    @Nullable
    @Override
    public BlockState setBlockState(BlockPos pos, BlockState state, boolean isMoving) {
        return null;
    }
}
