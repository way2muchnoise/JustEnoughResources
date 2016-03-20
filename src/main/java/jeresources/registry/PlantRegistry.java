package jeresources.registry;

import jeresources.api.drop.PlantDrop;
import jeresources.entry.PlantEntry;
import jeresources.util.MapKeys;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlantRegistry
{
    private Map<String, PlantEntry> registry;
    private static PlantRegistry instance;

    public static PlantRegistry getInstance()
    {
        if (instance == null)
            return instance = new PlantRegistry();
        return instance;
    }

    public PlantRegistry()
    {
        registry = new LinkedHashMap<>();
        registerPlant(PlantEntry.registerGrass());
    }

    public boolean registerPlant(PlantEntry entry)
    {
        String key = MapKeys.getKey(entry.getPlantItemStack());
        if (contains(key)) return false;
        registry.put(key, entry);
        return true;
    }

    private boolean contains(String key)
    {
        return registry.containsKey(key);
    }

    public List<PlantEntry> getAllPlants()
    {
        return new ArrayList<>(registry.values());
    }

    public void addDrops(ItemStack itemStack, PlantDrop[] drops)
    {
        String key = MapKeys.getKey(itemStack);
        if (contains(key))
            for (PlantDrop drop : drops)
                registry.get(key).add(drop);
    }
}
