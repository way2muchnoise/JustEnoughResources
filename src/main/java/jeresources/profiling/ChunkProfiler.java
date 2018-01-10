package jeresources.profiling;

import jeresources.util.MapKeys;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChunkProfiler implements Runnable {
    private final World world;
    private final ProfilingTimer timer;
    private final List<Chunk> chunks;
    @Nonnull
    private final ProfiledDimensionData dimensionData;
    public static final int CHUNK_SIZE = 16;
    public static final int CHUNK_HEIGHT = 256;

    public ChunkProfiler(World world, List<Chunk> chunks, @Nonnull ProfiledDimensionData dimensionData, ProfilingTimer timer) {
        this.world = world;
        this.chunks = chunks;
        this.dimensionData = dimensionData;
        this.timer = timer;
    }

    @Override
    public void run() {
        this.chunks.forEach(this::profileChunk);
    }

    private void profileChunk(Chunk chunk) {
        final int dimId = world.provider.getDimension();
        this.timer.startChunk(dimId);
        Map<String, Integer[]> temp = new HashMap<>();

        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        RayTraceResult rayTraceResult = new RayTraceResult(new Vec3d(0, 0, 0), EnumFacing.DOWN, blockPos);
        EntityPlayer player = Minecraft.getMinecraft().player;

        final int maxY = chunk.getTopFilledSegment();
        for (int y = 0; y < maxY; y++)
            for (int x = 0; x < CHUNK_SIZE; x++)
                for (int z = 0; z < CHUNK_SIZE; z++) {
                    blockPos.setPos(x + chunk.x * CHUNK_SIZE, y, z + chunk.z * CHUNK_SIZE);
                    IBlockState blockState = chunk.getBlockState(x, y, z);
                    final String key = MapKeys.getKey(blockState, rayTraceResult, world, blockPos, player);

                    if (!dimensionData.dropsMap.containsKey(key)) {
                        dimensionData.dropsMap.put(key, getDrops(world, blockPos, blockState));
                    }

                    if (!dimensionData.silkTouchMap.containsKey(key)) {
                        Block block = blockState.getBlock();
                        boolean canSilkTouch = block.canSilkHarvest(world, blockPos, blockState, player);
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

        this.timer.endChunk(dimId);
    }

    public static Map<String, Map<Integer, Float>> getDrops(IBlockAccess world, BlockPos pos, IBlockState state) {
        final int totalTries = 10000;

        Block block = state.getBlock();
        final Map<String, Map<Integer, Float>> resultMap = new HashMap<>();
        for (int fortune = 0; fortune <= 3; fortune++) {
            final Map<String, Integer> dropsMap = new HashMap<>();
            for (int i = 0; i < totalTries; i++) {
                List<ItemStack> drops = block.getDrops(world, pos, state, fortune);
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
