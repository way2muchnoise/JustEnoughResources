package jeresources.util;

import jeresources.api.drop.PlantDrop;
import jeresources.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class PlantHelper {
    public static List<PlantDrop> getSeeds() {
        return Services.PLATFORM.getSeedsFromTallGrassAsPlantDrops();
    }

    public static BlockState getPlant(BushBlock bushBlock, BlockGetter world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() != bushBlock) return bushBlock.defaultBlockState();
        return state;
    }
}
