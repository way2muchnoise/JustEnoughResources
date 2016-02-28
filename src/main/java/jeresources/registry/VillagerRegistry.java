package jeresources.registry;

import jeresources.entries.VillagerEntry;
import jeresources.utils.TradeHelper;
import net.minecraft.entity.passive.EntityVillager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VillagerRegistry
{
    private static VillagerRegistry instance;

    private List<VillagerEntry> villagers;
    private Map<Integer, Map<Integer, String>> villagerNames;

    public static VillagerRegistry getInstance()
    {
        if (instance == null)
            instance = new VillagerRegistry();
        return instance;
    }

    private VillagerRegistry()
    {
        this.villagers = new ArrayList<>();
        this.villagerNames = new HashMap<>();
        initVillagerNames();
        EntityVillager.ITradeList[][][][] tradeMapList = TradeHelper.getTrades();
        int level, career;
        level = career = 0;
        for (EntityVillager.ITradeList[][][] professionList : tradeMapList)
        {
            for (EntityVillager.ITradeList[][] careerList : professionList)
                addVillagerEntry(new VillagerEntry(level, career++, careerList));
            career = 0;
            level++;
        }
    }

    public void addVillagerEntry(VillagerEntry entry)
    {
        this.villagers.add(entry);
    }

    public void addVillagerName(int profession, int career, String name)
    {
        Map<Integer, String> map = this.villagerNames.get(profession);
        if (map == null) map = new HashMap<>();
        map.put(career, name);
        this.villagerNames.put(profession, map);
    }

    public List<VillagerEntry> getVillagers()
    {
        return villagers;
    }

    public void initVillagerNames()
    {
        addVillagerName(0, 0, "farmer");
        addVillagerName(0, 1, "fisherman");
        addVillagerName(0, 2, "shepherd");
        addVillagerName(0, 3, "fletcher");
        addVillagerName(1, 0, "librarian");
        addVillagerName(2, 0, "cleric");
        addVillagerName(3, 0, "armor");
        addVillagerName(3, 1, "weapon");
        addVillagerName(3, 2, "tool");
        addVillagerName(4, 0, "butcher");
        addVillagerName(4, 1, "leather");
    }

    public String getVillagerName(int profession, int career)
    {
        Map<Integer, String> map = this.villagerNames.get(profession);
        if (map == null) return "Unknown";
        String id = map.get(career);
        if (id == null) return "Unknown";
        return "entity.Villager." + id;
    }
}
