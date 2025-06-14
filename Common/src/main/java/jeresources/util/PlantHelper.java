package jeresources.util;

import jeresources.api.drop.PlantDrop;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.VegetationBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class PlantHelper {
    public static List<PlantDrop> getSeeds() {
        return LootTableHelper.toDrops(Blocks.GRASS_BLOCK.getLootTable().get()).stream()
            .map(lootDrop -> new PlantDrop(lootDrop.item, lootDrop.minDrop, lootDrop.maxDrop)).toList();
    }

    public static BlockState getPlant(VegetationBlock vegetationBlock, BlockGetter world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() != vegetationBlock) return vegetationBlock.defaultBlockState();
        return state;
    }
}
