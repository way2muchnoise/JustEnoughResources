package jeresources.util;

import jeresources.api.drop.PlantDrop;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class PlantHelper {
    public static List<PlantDrop> getSeeds() {
        return LootTableHelper.toDrops(Blocks.GRASS_BLOCK.getLootTable()).stream()
            .map(lootDrop -> new PlantDrop(lootDrop.item, lootDrop.minDrop, lootDrop.maxDrop)).toList();
    }

    public static BlockState getPlant(BushBlock bushBlock, BlockGetter world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() != bushBlock) return bushBlock.defaultBlockState();
        return state;
    }
}
