package jeresources.util;

import javax.annotation.Nullable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jeresources.api.drop.LootDrop;
import jeresources.api.restrictions.Restriction;
import jeresources.entry.WorldGenEntry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

public class MapKeys {

    private static final Cache<IBlockState, String> keyCache = CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.SECONDS)
            .build();

    @Nullable
    public static String getKey(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        Block block = state.getBlock();
        if (!block.hasTileEntity(state)) {
            try {
                return keyCache.get(state, () -> getKeyUncached(block, state, target, world, pos, player));
            } catch (ExecutionException e) {
                LogHelper.error("Cache error", e);
            }
        }

        return getKeyUncached(block, state, target, world, pos, player);
    }

    @Nullable
    private static String getKeyUncached(Block block, IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        int meta = block.getMetaFromState(state);
        ItemStack pickBlock = null;
        try {
            pickBlock = block.getPickBlock(state, target, world, pos, player);
        } catch (Exception ignored) {
        }

        if (pickBlock == null || pickBlock.getItem() == null) {
            return block.getRegistryName().toString() + ':' + meta;
        } else {
            return getKey(pickBlock);
        }
    }

    @Nullable
    public static String getKey(ItemStack drop) {
        if (drop == null)
            return null;
        Item item = drop.getItem();
        if (item == null)
            return null;
        String registryName = item.getRegistryName().toString();
        StringBuilder key = new StringBuilder(registryName);
        key.append(":").append(drop.getMetadata());
        if (drop.getTagCompound() != null)
            key.append(":").append(drop.getTagCompound());
        return key.toString();
    }

    public static String getKey(IPlantable plant) {
        return plant.getPlant(null, null).getBlock().getTranslationKey();
    }

    public static String getKey(LootDrop dropItem) {
        return getKey(dropItem.item);
    }

    public static String getKey(ItemStack drop, Restriction restriction) {
        return getKey(drop) + ":" + restriction.toString();
    }

    public static String getKey(WorldGenEntry entry) {
        return getKey(entry.getBlock(), entry.getRestriction());
    }
}
