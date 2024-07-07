package jeresources.util;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jeresources.api.drop.LootDrop;
import jeresources.api.restrictions.Restriction;
import jeresources.entry.WorldGenEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MapKeys {

    private static final Cache<BlockState, String> keyCache = CacheBuilder.newBuilder()
            .expireAfterAccess(30, TimeUnit.SECONDS)
            .build();

    @Nullable
    public static String getKey(BlockState state, ServerLevel serverLevel, BlockPos pos) {
        Block block = state.getBlock();
        if (!state.isRandomlyTicking()) {
            try {
                return keyCache.get(state, () -> getKeyUncached(block, state, serverLevel, pos));
            } catch (ExecutionException e) {
                LogHelper.error("Cache error", e);
            }
        }

        return getKeyUncached(block, state, serverLevel, pos);
    }

    @Nullable
    private static String getKeyUncached(Block block, BlockState state, ServerLevel serverLevel, BlockPos pos) {
        ItemStack pickBlock = null;
        try {
            pickBlock = block.getCloneItemStack(serverLevel, pos, state);
        } catch (Exception ignored) {
        }

        if (pickBlock == null || pickBlock.getItem() == null) {
            return block.getDescriptionId();
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
        String registryName = item.getDescriptionId();
        StringBuilder key = new StringBuilder(registryName);
//        if (drop.getTag() != null) // TODO fix
//            key.append(":").append(drop.getTag());
        return key.toString();
    }

    public static String getKey(BushBlock plant) {
        return PlantHelper.getPlant(plant, null, null).getBlock().getDescriptionId();
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
