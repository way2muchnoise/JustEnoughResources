package jeresources.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import jeresources.entry.VillagerEntry;
import jeresources.entry.WanderingTraderEntry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Arrays;
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
        try {
            reg.addVillagerEntry(new WanderingTraderEntry(getWanderingTrades()));
        } catch (Exception e) {
            LogHelper.warn("Failed loading wandering trader");
            LogHelper.warn("Exception caught when registering wandering traderr", e);
        }
    }

    private static Int2ObjectMap<VillagerTrades.ItemListing[]> getTrades(VillagerProfession profession) {
        return VillagerTrades.TRADES.getOrDefault(profession, Int2ObjectMaps.emptyMap());
    }

    private static Int2ObjectMap<VillagerTrades.ItemListing[]> getWanderingTrades() {
        // Wandering trader doesn't have levels, but has a separate array for special items
        // This combines all wandering trader lists, so it can be treated as a villager with 1 level
        VillagerTrades.ItemListing[] allWanderingTrades = VillagerTrades.WANDERING_TRADER_TRADES.values()
            .stream()
            .flatMap(x -> Arrays.stream(x))
            .toArray(VillagerTrades.ItemListing[]::new);
        return new Int2ObjectOpenHashMap<>(new int[]{1}, new VillagerTrades.ItemListing[][]{allWanderingTrades});
    }

    public static Set<BlockState> getPoiBlocks(PoiType poiType) {
        return poiType.matchingStates();
    }

    public static Set<BlockState> getPoiBlocks(Predicate<Holder<PoiType>> heldJobSite) {
        return getPoiBlocks(Registry.POINT_OF_INTEREST_TYPE.holders().filter(heldJobSite).findFirst().get().value());
    }
}
