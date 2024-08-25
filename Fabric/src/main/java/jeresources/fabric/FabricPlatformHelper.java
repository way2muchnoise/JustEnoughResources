package jeresources.fabric;

import jeresources.api.IJERAPI;
import jeresources.api.IJERPlugin;
import jeresources.platform.ILootTableHelper;
import jeresources.platform.IModList;
import jeresources.platform.IPlatformHelper;
import jeresources.proxy.CommonProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.nio.file.Path;

public class FabricPlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "Fabric";
    }

    @Override
    public boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    @Override
    public CommonProxy getProxy() {
        return JEResources.PROXY;
    }

    @Override
    public IModList getModsList() {
        return new ModList();
    }

    @Override
    public void injectApi(IJERAPI instance) {
        FabricLoader.getInstance()
            .getEntrypoints(IJERPlugin.entry_point, IJERPlugin.class)
            .forEach(plugin -> plugin.receive(instance));
    }

    @Override
    public boolean isCorrectToolForBlock(Block block, BlockState blockState, BlockGetter level, BlockPos blockPos, Player player) {
        return player.hasCorrectToolForDrops(blockState);
    }

    @Override
    public Path getConfigDir() {
        return FabricLoader.getInstance().getConfigDir();
    }

    @Override
    public ILootTableHelper getLootTableHelper() {
        if (lootTableHelper == null) {
            lootTableHelper = new ILootTableHelper() {};
        }
        return lootTableHelper;
    }

    private static ILootTableHelper lootTableHelper;
}
