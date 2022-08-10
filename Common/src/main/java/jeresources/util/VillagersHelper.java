package jeresources.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import jeresources.entry.VillagerEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;
import java.util.function.Predicate;

public class VillagersHelper {
    public static void initRegistry(jeresources.registry.VillagerRegistry reg) {
        for (VillagerProfession profession : Registry.VILLAGER_PROFESSION) {
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
        return poiType.matchingStates();
    }

    public static Set<BlockState> getPoiBlocks(Predicate<Holder<PoiType>> heldJobSite) {
        return getPoiBlocks(Registry.POINT_OF_INTEREST_TYPE.holders().filter(heldJobSite).findFirst().get().value());
    }
}
