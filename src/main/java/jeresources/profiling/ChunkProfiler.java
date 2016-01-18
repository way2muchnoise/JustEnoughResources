package jeresources.profiling;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class ChunkProfiler implements Runnable
{
    private final World world;
    private final ProfilingTimer timer;
    private final List<Chunk> chunks;
    private ConcurrentMap<String, Integer[]> map;
    public static final int CHUNK_SIZE = 16;
    public static final int CHUNK_HEIGHT = 256;

    public ChunkProfiler(World world, List<Chunk> chunks, ConcurrentMap<String, Integer[]> map, ProfilingTimer timer)
    {
        this.world = world;
        this.chunks = chunks;
        this.map = map;
        this.timer = timer;
    }

    @Override
    public void run()
    {
        for (Chunk chunk : this.chunks)
            profileChunk(chunk);
    }

    private void profileChunk(Chunk chunk) {
        this.timer.startChunk();
        Map<String, Integer[]> temp = new HashMap<>();

        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        MovingObjectPosition movingObjectPosition = new MovingObjectPosition(new Vec3(0, 0, 0), EnumFacing.DOWN, blockPos);
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;

        final int maxY = chunk.getTopFilledSegment();
        for (int y = 0; y < maxY; y++)
            for (int x = 0; x < CHUNK_SIZE; x++)
                for (int z = 0; z < CHUNK_SIZE; z++)
                {
                    blockPos.set(x + chunk.xPosition * CHUNK_SIZE, y, z + chunk.zPosition * CHUNK_SIZE);
                    Block block = chunk.getBlock(x, y, z);
                    ItemStack pickBlock = block.getPickBlock(movingObjectPosition, world, blockPos, player);
                    String key;
                    if (pickBlock == null) {
                        int meta = chunk.getBlockMetadata(blockPos);
                        key = block.getRegistryName() + ':' + meta;
                    } else {
                        Item item = pickBlock.getItem();
                        key = item.getRegistryName() + ':' + pickBlock.getMetadata();
                    }
                    Integer[] array = temp.get(key);
                    if (array == null)
                    {
                        array = new Integer[CHUNK_HEIGHT];
                        Arrays.fill(array, 0);
                    }
                    array[y]++;
                    temp.put(key, array);
                }

        for (Map.Entry<String, Integer[]> entry : temp.entrySet())
        {
            Integer[] array = this.map.get(entry.getKey());
            if (array == null)
            {
                array = new Integer[CHUNK_HEIGHT];
                Arrays.fill(array, 0);
            }
            for (int i = 0; i < CHUNK_HEIGHT; i++)
                array[i] += entry.getValue()[i];
            this.map.put(entry.getKey(), array);
        }

        this.timer.endChunk();
    }
}
