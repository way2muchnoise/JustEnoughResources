package jeresources.platform;

import jeresources.api.IJERAPI;
import jeresources.api.drop.PlantDrop;
import jeresources.proxy.CommonProxy;
import jeresources.util.LootTableHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.lang.module.Configuration;
import java.nio.file.Path;
import java.util.List;

public interface IPlatformHelper {
    String getPlatformName();

    boolean isClient();

    CommonProxy getProxy();

    IModList getModsList();

    void injectApi(IJERAPI instance);

    boolean isCorrectToolForBlock(Block block, BlockState blockState, BlockGetter level, BlockPos blockPos, Player player);

    List<PlantDrop> getSeedsFromTallGrassAsPlantDrops();

    Path getConfigDir();

    boolean isAllowedOnBooks(Enchantment enchantment);

    ILootTableHelper getLootTableHelper();
}
