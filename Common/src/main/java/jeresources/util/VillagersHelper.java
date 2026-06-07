package jeresources.util;

import jeresources.entry.VillagerEntry;
import jeresources.entry.WanderingTraderEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.trading.TradeSet;
import net.minecraft.world.item.trading.VillagerTrade;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class VillagersHelper {
    public static void initRegistry(jeresources.registry.VillagerRegistry reg) {
        // TODO fix loading of professions - this errors out as the registries are not loaded yet
        Registry<TradeSet> tradeSetRegistry = Minecraft.getInstance().level.registryAccess().lookupOrThrow(Registries.TRADE_SET);
        Registry<VillagerTrade> tradeRegistry = Minecraft.getInstance().level.registryAccess().lookupOrThrow(Registries.VILLAGER_TRADE);

        for (VillagerProfession profession : BuiltInRegistries.VILLAGER_PROFESSION) {
            try {
                Map<Integer, List<VillagerTrade>> tradesByLevel = getTrades(profession, tradeSetRegistry, tradeRegistry);
                reg.addVillagerEntry(new VillagerEntry(profession, tradesByLevel));
            } catch (Exception e) {
                LogHelper.warn("Failed loading villager {} registered at {}", profession.toString(), profession.name());
                LogHelper.warn("Exception caught when registering villager", e);
            }
        }
        try {
            List<VillagerTrade> wanderingTrades = getWanderingTrades(tradeRegistry);
            reg.addVillagerEntry(new WanderingTraderEntry(wanderingTrades));
        } catch (Exception e) {
            LogHelper.warn("Failed loading wandering trader");
            LogHelper.warn("Exception caught when registering wandering trader", e);
        }
    }

    private static Map<Integer, List<VillagerTrade>> getTrades(VillagerProfession profession, Registry<TradeSet> tradeSetRegistry, Registry<VillagerTrade> tradeRegistry) {
        Map<Integer, List<VillagerTrade>> result = new HashMap<>();
        for (var entry : profession.tradeSetsByLevel().int2ObjectEntrySet()) {
            int level = entry.getIntKey();
            ResourceKey<TradeSet> tradeSetKey = entry.getValue();
            TradeSet tradeSet = tradeSetRegistry.getValue(tradeSetKey);
            if (tradeSet != null) {
                List<VillagerTrade> trades = tradeSet.getTrades().stream()
                        .map(Holder::value)
                        .collect(Collectors.toList());
                result.put(level, trades);
            }
        }
        return result;
    }

    private static List<VillagerTrade> getWanderingTrades(Registry<VillagerTrade> tradeRegistry) {
        List<VillagerTrade> trades = new ArrayList<>();
        for (var holder : tradeRegistry.listElements().toList()) {
            String path = holder.key().identifier().getPath();
            if (path.startsWith("wandering_trader_")) {
                trades.add(holder.value());
            }
        }
        return trades;
    }

    public static Set<BlockState> getPoiBlocks(PoiType poiType) {
        return poiType.matchingStates();
    }

    public static Set<BlockState> getPoiBlocks(Predicate<Holder<PoiType>> heldJobSite) {
        return getPoiBlocks(BuiltInRegistries.POINT_OF_INTEREST_TYPE.listElements().filter(heldJobSite).findFirst().get().value());
    }
}
