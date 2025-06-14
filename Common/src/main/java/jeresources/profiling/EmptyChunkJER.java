package jeresources.profiling;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import org.jetbrains.annotations.Nullable;


public class EmptyChunkJER extends EmptyLevelChunk {
    public EmptyChunkJER(ServerLevel level, int x, int z) {
        super(level, new ChunkPos(x, z), null);
    }

    @Override
    public @Nullable BlockState setBlockState(BlockPos pos, BlockState state, int flags) {
        return null;
    }
}
