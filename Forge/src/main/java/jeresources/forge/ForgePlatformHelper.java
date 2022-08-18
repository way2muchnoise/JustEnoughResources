package jeresources.forge;

import jeresources.JEResources;
import jeresources.api.IJERAPI;
import jeresources.api.IJERPlugin;
import jeresources.platform.ILootTableHelper;
import jeresources.platform.IModList;
import jeresources.platform.IPlatformHelper;
import jeresources.proxy.CommonProxy;
import jeresources.util.LogHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class ForgePlatformHelper implements IPlatformHelper {
    @Override
    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public boolean isClient() {
        return FMLEnvironment.dist == Dist.CLIENT;
    }

    @Override
    public CommonProxy getProxy() {
        return JEResources.PROXY;
    }

    @Override
    public IModList getModsList() {
        return new jeresources.forge.ModList(ModList.get());
    }

    @Override
    public void injectApi(IJERAPI instance) {
        Type pluginAnnotation = Type.getType(IJERPlugin.class);
        List<ModFileScanData> allScanData = ModList.get().getAllScanData();
        for (ModFileScanData scanData : allScanData) {
            Iterable<ModFileScanData.AnnotationData> annotations = scanData.getAnnotations();
            for (ModFileScanData.AnnotationData a : annotations) {
                if (Objects.equals(a.annotationType(), pluginAnnotation)) {
                    try {
                        Class<?> clazz = Class.forName(a.clazz().getClassName());
                        IJERPlugin plugin = (IJERPlugin) clazz.getDeclaredConstructor().newInstance();
                        plugin.receive(instance);
                    } catch (ClassNotFoundException | IllegalAccessException | NoSuchMethodException
                             | InstantiationException | InvocationTargetException e) {
                        LogHelper.warn("Failed to set: {}" + a.clazz().getClassName() + "." + a.memberName());
                    }
                }
            }
        }
    }

    @Override
    public boolean isCorrectToolForBlock(Block block, BlockState blockState, BlockGetter level, BlockPos blockPos, Player player) {
        return block.canHarvestBlock(blockState, level, blockPos, player);
    }

    @Override
    public Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get();
    }

    @Override
    public boolean isAllowedOnBooks(Enchantment enchantment) {
        return enchantment.isAllowedOnBooks();
    }

    @Override
    public ILootTableHelper getLootTableHelper() {
        return LootTableHelper.instance();
    }
}
