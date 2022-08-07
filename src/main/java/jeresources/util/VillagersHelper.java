package jeresources.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import jeresources.entry.VillagerEntry;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;

public class VillagersHelper {
    public static void initRegistry(jeresources.registry.VillagerRegistry reg) {
        for (VillagerProfession profession : ForgeRegistries.VILLAGER_PROFESSIONS) {
            try {
                reg.addVillagerEntry(new VillagerEntry(profession, getTrades(profession)));
            } catch (Exception e) {
                LogHelper.warn("Failed loading villager {} registered at {}", profession.toString(), profession.name());
                LogHelper.warn("Exception caught when registering villager", e);
            }
        }
    }

    private static Int2ObjectMap<VillagerTrades.ItemListing[]> getTrades(VillagerProfession profession) {
        return VillagerTrades.TRADES.getOrDefault(profession, Int2ObjectMaps.emptyMap());
    }

    public static Set<BlockState> getPoiBlocks(PoiType poiType) {
        return ForgeRegistries.POI_TYPES.getDelegateOrThrow(poiType).get().matchingStates();
    }

    public static Set<BlockState> getPoiBlocks(Predicate<Holder<PoiType>> heldJobSite) {
        // TODO: there has to be a better way of doing this
        return getPoiBlocks(ForgeRegistries.POI_TYPES.getValues().stream().map(ForgeRegistries.POI_TYPES::getHolder)
            .filter(poiType -> heldJobSite.test(poiType.get())).findFirst().get().get().value());
    }
}
