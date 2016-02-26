package jeresources.registry;

import jeresources.entries.VillagerEntry;
import jeresources.utils.TradeHelper;
import net.minecraft.entity.passive.EntityVillager;

import java.util.List;

public class VillagerRegistry
{
    private static VillagerRegistry instance;

    private List<VillagerEntry> villagers;

    public VillagerRegistry getInstance()
    {
        if (instance == null)
            instance = new VillagerRegistry();
        return instance;
    }

    private VillagerRegistry()
    {
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
}
