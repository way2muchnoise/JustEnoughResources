package jeresources.registry;

import jeresources.api.drop.LootDrop;
import jeresources.entry.WorldGenEntry;
import jeresources.util.MapKeys;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldGenRegistry {
    private Map<String, WorldGenEntry> worldGenMap;
    private static WorldGenRegistry instance;

    public static WorldGenRegistry getInstance() {
        if (instance == null)
            instance = new WorldGenRegistry();
        return instance;
    }

    private WorldGenRegistry() {
        worldGenMap = new HashMap<>();
    }


    public void registerEntry(WorldGenEntry entry) {
        if (worldGenMap.containsKey(MapKeys.getKey(entry))) {
            WorldGenEntry existing = worldGenMap.get(MapKeys.getKey(entry));
            existing.merge(entry);
        } else
            worldGenMap.put(MapKeys.getKey(entry), entry);
    }

    public void addDrops(ItemStack block, LootDrop... drops) {
        worldGenMap.entrySet().stream()
            .filter(entry -> {
                String key = MapKeys.getKey(block);
                return key != null && entry.getKey().startsWith(key);
            })
            .forEach(entry -> entry.getValue().addDrops(drops));
    }

    public List<WorldGenEntry> getWorldGen() {
        return new ArrayList<>(worldGenMap.values());
    }

    public void clear() {
        worldGenMap.clear();
    }
}
