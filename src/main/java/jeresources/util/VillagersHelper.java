package jeresources.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import jeresources.entry.VillagerEntry;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraftforge.registries.ForgeRegistries;

public class VillagersHelper {
    public static void initRegistry(jeresources.registry.VillagerRegistry reg) {
        for (VillagerProfession profession : ForgeRegistries.PROFESSIONS) {
            try {
                reg.addVillagerEntry(new VillagerEntry(profession, getTrades(profession)));
            } catch (Exception e) {
                LogHelper.warn("Failed loading villager {} registered at {}", profession.toString(), profession.getRegistryName().toString());
                LogHelper.warn("Exception caught when registering villager", e);
            }
        }
    }

    private static Int2ObjectMap<VillagerTrades.ITrade[]> getTrades(VillagerProfession profession) {
        return VillagerTrades.TRADES.getOrDefault(profession, Int2ObjectMaps.emptyMap());
    }
}
