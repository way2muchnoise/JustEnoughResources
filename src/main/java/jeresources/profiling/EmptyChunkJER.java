package jeresources.profiling;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.EmptyLevelChunk;

import javax.annotation.Nullable;

public class EmptyChunkJER extends EmptyLevelChunk {
    public EmptyChunkJER(ServerLevel level, int x, int z) {
        super(level, new ChunkPos(x, z));
    }

    @Nullable
    @Override
    public BlockState setBlockState(BlockPos pos, BlockState state, boolean isMoving) {
        return null;
    }
}
