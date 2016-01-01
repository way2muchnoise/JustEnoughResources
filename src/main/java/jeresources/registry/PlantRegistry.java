package jeresources.registry;

import jeresources.api.utils.PlantDrop;
import jeresources.entries.PlantEntry;
import jeresources.utils.MapKeys;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlantRegistry
{
    private Map<String, PlantEntry> registry = new LinkedHashMap<String, PlantEntry>();
    private Map<String, List<String>> dropLinks = new LinkedHashMap<String, List<String>>();
    private static PlantRegistry instance = null;

    public static PlantRegistry getInstance()
    {
        if (instance == null)
            return instance = new PlantRegistry();
        return instance;
    }

    public PlantRegistry()
    {
        registerPlant(PlantEntry.registerGrass());
    }

    public boolean registerPlant(PlantEntry entry)
    {
        String key = MapKeys.getKey(entry.getPlant());
        if (contains(key)) return false;
        registry.put(key, entry);
        for (PlantDrop drop : entry.getDrops())
        {
            String dropKey = MapKeys.getKey(drop.getDrop());
            if (dropLinks.containsKey(key))
                dropLinks.get(key).add(key);
            else
            {
                List<String> list = new ArrayList<String>();
                list.add(key);
                dropLinks.put(dropKey, list);
            }
        }
        return true;
    }

    public boolean contains(ItemStack itemStack)
    {
        return contains(MapKeys.getKey(itemStack));
    }

    private boolean contains(String key)
    {
        return registry.containsKey(key);
    }

    public boolean containsDrop(ItemStack itemStack)
    {
        return containsDrop(MapKeys.getKey(itemStack));
    }

    private boolean containsDrop(String key)
    {
        return dropLinks.containsKey(key);
    }

    public List<PlantEntry> getEntriesForDrop(ItemStack itemStack)
    {
        String key = MapKeys.getKey(itemStack);
        if (!containsDrop(key)) return null;

        List<PlantEntry> list = new ArrayList<PlantEntry>();
        for (String plantKey : dropLinks.get(key))
            if (registry.containsKey(plantKey))
                list.add(registry.get(plantKey));
        return list;
    }

    public PlantEntry getEntry(ItemStack itemStack)
    {
        String key = MapKeys.getKey(itemStack);
        if (!contains(key)) return null;

        return registry.get(key);
    }

    public List<PlantEntry> getAllPlants()
    {
        return new ArrayList<PlantEntry>(registry.values());
    }

    public Map<ItemStack, Float> getDrops(PlantEntry entry)
    {
        Map<ItemStack, Float> map = new LinkedHashMap<ItemStack, Float>();
        float totalWeight = entry.getTotalWeight();
        for (PlantDrop drop : entry.getDrops())
            map.put(drop.getDrop(), drop.getWeight() / totalWeight);
        return map;
    }
    
    public void clear()
    {
        instance = new PlantRegistry();
    }
}
