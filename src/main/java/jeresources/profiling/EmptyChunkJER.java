package jeresources.profiling;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.EmptyChunk;

import javax.annotation.Nullable;

public class EmptyChunkJER extends EmptyChunk {
    public EmptyChunkJER(World worldIn, int x, int z) {
        super(worldIn, x, z);
    }

    @Nullable
    @Override
    public IBlockState setBlockState(BlockPos pos, IBlockState state) {
        return null;
    }
}
