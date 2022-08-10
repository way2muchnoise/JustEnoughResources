package jeresources.profiling;

import jeresources.platform.Services;
import jeresources.util.MapKeys;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChunkProfiler implements Runnable {
    private final ServerLevel level;
    private final ResourceKey<Level> dimensionKey;
    private final ProfilingTimer timer;
    private final ProfilingBlacklist blacklist;
    private final List<ChunkAccess> chunks;
    @Nonnull
    private final ProfiledDimensionData dimensionData;
    public static final int CHUNK_SIZE = 16;
    public static final int CHUNK_HEIGHT = 256;

    public ChunkProfiler(ServerLevel level, ResourceKey<Level> dimensionKey, List<ChunkAccess> chunks, @Nonnull ProfiledDimensionData dimensionData, ProfilingTimer timer, ProfilingBlacklist blacklist) {
        this.level = level;
        this.dimensionKey = dimensionKey;
        this.chunks = chunks;
        this.dimensionData = dimensionData;
        this.timer = timer;
        this.blacklist = blacklist;
    }

    @Override
    public void run() {
        this.chunks.forEach(this::profileChunk);
    }

    private void profileChunk(ChunkAccess chunk) {
        final ResourceKey<Level> worldRegistryKey = level.dimension();
        this.timer.startChunk(worldRegistryKey);
        Map<String, Integer[]> temp = new HashMap<>();

        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        HitResult rayTraceResult = new BlockHitResult(new Vec3(0, 0, 0), Direction.DOWN, blockPos, true);
        Player player = Minecraft.getInstance().player;

        final int maxY = chunk.getHighestSectionPosition();
        for (int y = 0; y < maxY; y++)
            for (int x = 0; x < CHUNK_SIZE; x++)
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    blockPos.set(x + chunk.getPos().x * CHUNK_SIZE, y, z + chunk.getPos().z * CHUNK_SIZE);
                    BlockState blockState = chunk.getBlockState(new BlockPos(x, y, z));
                    if (blacklist.contains(blockState)) continue;
                    final String key = MapKeys.getKey(blockState, level, blockPos);

                    if (!dimensionData.dropsMap.containsKey(key)) {
                        dimensionData.dropsMap.put(key, getDrops(level, blockPos, blockState));
                    }

                    if (!dimensionData.silkTouchMap.containsKey(key)) {
                        Block block = blockState.getBlock();
                        boolean canSilkTouch = Services.PLATFORM.isCorrectToolForBlock(block, blockState, level, blockPos, player);
                        dimensionData.silkTouchMap.put(key, canSilkTouch);
                    }

                    Integer[] array = temp.get(key);
                    if (array == null) {
                        array = new Integer[CHUNK_HEIGHT];
                        Arrays.fill(array, 0);
                    }
                    array[y]++;
                    temp.put(key, array);
                }

        for (Map.Entry<String, Integer[]> entry : temp.entrySet()) {
            Integer[] array = dimensionData.distributionMap.get(entry.getKey());
            if (array == null) {
                array = new Integer[CHUNK_HEIGHT];
                Arrays.fill(array, 0);
            }
            for (int i = 0; i < CHUNK_HEIGHT; i++)
                array[i] += entry.getValue()[i];
            dimensionData.distributionMap.put(entry.getKey(), array);
        }

        this.timer.endChunk(dimensionKey);
    }

    public static Map<String, Map<Integer, Float>> getDrops(ServerLevel level, BlockPos pos, BlockState state) {
        final int totalTries = 10000;

        final Map<String, Map<Integer, Float>> resultMap = new HashMap<>();
        for (int fortune = 0; fortune <= 3; fortune++) {
            final Map<String, Integer> dropsMap = new HashMap<>();
            for (int i = 0; i < totalTries; i++) {
                NonNullList<ItemStack> drops = NonNullList.create();
                // TODO reintroduce fortune
                // block.getDrops(state, drops, world, pos, fortune);
                Block.getDrops(state, level, pos, null);
                //TODO: Add handling for tile entities (not chests)
                for (ItemStack drop : drops) {
                    if (drop == null)
                        continue;
                    String key = MapKeys.getKey(drop);
                    Integer count = dropsMap.get(key);
                    if (count != null) count += drop.getCount();
                    else count = drop.getCount();
                    dropsMap.put(key, count);
                }
            }

            for (Map.Entry<String, Integer> dropEntry : dropsMap.entrySet()) {
                Map<Integer, Float> fortuneMap = resultMap.get(dropEntry.getKey());
                if (fortuneMap == null) fortuneMap = new HashMap<>();
                fortuneMap.put(fortune, dropEntry.getValue() / (float) totalTries);
                resultMap.put(dropEntry.getKey(), fortuneMap);
            }
        }

        return resultMap;
    }
}
