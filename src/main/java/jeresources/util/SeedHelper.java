package jeresources.util;

import jeresources.api.drop.PlantDrop;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.common.ForgeHooks;

import java.util.ArrayList;
import java.util.List;

public class SeedHelper
{
    @SuppressWarnings("unchecked")
    public static List<PlantDrop> getSeeds()
    {
        List<PlantDrop> result = new ArrayList<PlantDrop>();
        Class seedEntry = ReflectionHelper.findClass("net.minecraftforge.common.ForgeHooks$SeedEntry");
        if (seedEntry == null) return result;
        List seedList = ReflectionHelper.getPrivateValue(ForgeHooks.class, null, "seedList");
        for (Object o : seedList)
        {
            if (o == null) continue;
            ItemStack seed = (ItemStack) ReflectionHelper.getPrivateValue(seedEntry, o, "seed");
            if (seed == null || seed.getItem() == null) continue;
            int weight = ReflectionHelper.getPrivateValue(WeightedRandom.Item.class, (WeightedRandom.Item) o, "field_76292_a", "itemWeight");
            result.add(new PlantDrop(seed, weight));
        }
        return result;
    }
}
