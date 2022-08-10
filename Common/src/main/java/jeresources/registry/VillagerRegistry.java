package jeresources.registry;

import jeresources.entry.VillagerEntry;

import java.util.LinkedList;
import java.util.List;

public class VillagerRegistry {
    private static VillagerRegistry instance;

    private List<VillagerEntry> villagers;

    public static VillagerRegistry getInstance() {
        if (instance == null) {
            instance = new VillagerRegistry();
        }
        return instance;
    }

    private VillagerRegistry() {
        this.villagers = new LinkedList<>();
    }

    public void addVillagerEntry(VillagerEntry entry) {
        if (entry.getVillagerTrades(0).size() > 0) {
            this.villagers.add(entry);
        }
    }

    public List<VillagerEntry> getVillagers() {
        return this.villagers;
    }

    public void clear() {
        villagers.clear();
    }
}
