package jeresources.compatibility;

import jeresources.api.IVillagerRegistry;
import jeresources.registry.VillagerRegistry;
import net.minecraft.entity.passive.EntityVillager;

import java.util.*;

public class VillagerRegistryImpl implements IVillagerRegistry
{
    private static Map<Integer, Map<Integer, Map<Integer, List<EntityVillager.ITradeList>>>> newVillagerTrades = new HashMap<>();

    @Override
    public void addVillagerTrade(int profession, int career, int level, EntityVillager.ITradeList... tradeLists)
    {
        Map<Integer, Map<Integer, List<EntityVillager.ITradeList>>> careerMap = newVillagerTrades.get(profession);
        if (careerMap == null) careerMap = new HashMap<>();
        Map<Integer, List<EntityVillager.ITradeList>> levelMap = careerMap.get(career);
        if (levelMap == null) levelMap = new HashMap<>();
        List<EntityVillager.ITradeList> trades = levelMap.get(career);
        if (trades == null) trades = new ArrayList<>();
        trades.addAll(Arrays.asList(tradeLists));
        levelMap.put(level, trades);
        careerMap.put(career, levelMap);
        newVillagerTrades.put(profession, careerMap);
    }

    @Override
    public void addVillagerTrades(int profession, int career, EntityVillager.ITradeList[][] tradeLists)
    {
        for (int level = 0; level < tradeLists.length; level++)
            addVillagerTrade(profession, career, level, tradeLists[level]);
    }

    @Override
    public void addVillagerName(int profession, int career, String name)
    {
        VillagerRegistry.getInstance().addVillagerName(profession, career, name);
    }

    protected static void commit()
    {
        for (Map.Entry<Integer, Map<Integer, Map<Integer, List<EntityVillager.ITradeList>>>> professionMap : newVillagerTrades.entrySet())
            for (Map.Entry<Integer, Map<Integer, List<EntityVillager.ITradeList>>> careerMap : professionMap.getValue().entrySet())
                for (Map.Entry<Integer, List<EntityVillager.ITradeList>> levelMap : careerMap.getValue().entrySet())
                    VillagerRegistry.getInstance().addVillagerTrade(professionMap.getKey(), careerMap.getKey(), levelMap.getKey(), levelMap.getValue());
    }
}
