package jeresources.forge;

import jeresources.JEResources;
import jeresources.api.IJERAPI;
import jeresources.api.drop.PlantDrop;
import jeresources.compatibility.api.JERAPI;
import jeresources.platform.ILootTableHelper;
import jeresources.platform.IModInfo;
import jeresources.platform.IModList;
import jeresources.platform.IPlatformHelper;
import jeresources.proxy.CommonProxy;
import jeresources.util.LogHelper;
import jeresources.util.ReflectionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;
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
        Type annotationType = Type.getType(IJERAPI.class);
        List<ModFileScanData> allScanData = ModList.get().getAllScanData();
        for (ModFileScanData scanData : allScanData) {
            Iterable<ModFileScanData.AnnotationData> annotations = scanData.getAnnotations();
            for (ModFileScanData.AnnotationData a : annotations) {
                if (Objects.equals(a.annotationType(), annotationType)) {
                    try {
                        Class<?> clazz = Class.forName(a.clazz().getClassName());
                        Field field = clazz.getField(a.memberName());
                        if (field.getType() == JERAPI.class)
                            field.set(null, instance);
                    } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
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

    @SuppressWarnings({"rawtypes", "ConstantConditions", "unchecked"})
    @Override
    public List<PlantDrop> getSeedsFromTallGrassAsPlantDrops() {
        List<PlantDrop> result = new ArrayList<>();
        Class seedEntry = ReflectionHelper.findClass("net.minecraftforge.common.ForgeHooks$SeedEntry");
        if (seedEntry == null) return result;
        List seedList = ObfuscationReflectionHelper.getPrivateValue(ForgeHooks.class, null, "seedList");
        for (Object o : seedList) {
            if (o == null) continue;
            ItemStack seed = (ItemStack) ObfuscationReflectionHelper.getPrivateValue(seedEntry, o, "seed");
            if (seed == null || seed.getItem() == null) continue;
            result.add(new PlantDrop(seed, ((WeightedEntry.IntrusiveBase) o).weight.asInt()));
        }
        return result;
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
