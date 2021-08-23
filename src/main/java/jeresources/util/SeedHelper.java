package jeresources.util;

import jeresources.api.drop.PlantDrop;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;

import java.util.ArrayList;
import java.util.List;

public class SeedHelper {
    @SuppressWarnings("unchecked")
    public static List<PlantDrop> getSeeds() {
        List<PlantDrop> result = new ArrayList<>();
        Class seedEntry = ReflectionHelper.findClass("net.minecraftforge.common.ForgeHooks$SeedEntry");
        if (seedEntry == null) return result;
        List seedList = ReflectionHelper.getPrivateValue(ForgeHooks.class, null, "seedList");
        for (Object o : seedList) {
            if (o == null) continue;
            ItemStack seed = (ItemStack) ReflectionHelper.getPrivateValue(seedEntry, o, "seed");
            if (seed == null || seed.getItem() == null) continue;
            result.add(new PlantDrop(seed, ((WeightedEntry.IntrusiveBase) o).weight.asInt()));
        }
        return result;
    }
}
