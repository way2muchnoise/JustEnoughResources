package jeresources.registry;

import jeresources.entry.VillagerEntry;
import jeresources.util.VillagersHelper;

import java.util.*;

public class VillagerRegistry
{
    private static VillagerRegistry instance;

    private Map<String, Map<Integer, VillagerEntry>> villagers;

    public static VillagerRegistry getInstance()
    {
        if (instance == null)
            instance = new VillagerRegistry();
        return instance;
    }

    private VillagerRegistry()
    {
        this.villagers = new HashMap<>();
        VillagersHelper.initRegistry(this);
    }

    public void addVillagerEntry(VillagerEntry entry)
    {
        Map<Integer, VillagerEntry> entryMap = this.villagers.get(entry.getName());
        if (entryMap == null) entryMap = new HashMap<>();
        entryMap.put(entry.getCareer(), entry);
        this.villagers.put(entry.getName(), entryMap);
    }

    public List<VillagerEntry> getVillagers()
    {
        List<VillagerEntry> list = new ArrayList<>();
        for (Map<Integer, VillagerEntry> villagerMap : this.villagers.values())
            list.addAll(villagerMap.values());
        return list;
    }

    public Map<Integer, VillagerEntry> getVillagerCareer(String name)
    {
        return this.villagers.get(name);
    }

    public VillagerEntry getVillager(String name, int profession)
    {
        return this.villagers.containsKey(name) ? this.villagers.get(name).get(profession) : null;
    }
}
