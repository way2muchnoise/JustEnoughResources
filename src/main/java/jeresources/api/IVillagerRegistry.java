package jeresources.api;

import net.minecraft.entity.passive.EntityVillager;

public interface IVillagerRegistry
{
    /**
     * Adds trades to a level
     * @param profession the profession of the {@link EntityVillager}
     * @param career the career of the {@link EntityVillager}
     * @param level the level need before the trade can be used
     * @param tradeLists the {@link net.minecraft.entity.passive.EntityVillager.ITradeList} to be added
     */
    void addVillagerTrade(int profession, int career, int level, EntityVillager.ITradeList... tradeLists);

    /**
     * Adds trades
     * @param profession the profession of the {@link EntityVillager}
     * @param career the career of the {@link EntityVillager}
     * @param tradeLists the {@link net.minecraft.entity.passive.EntityVillager.ITradeList} to be added(dim 1: level, dim 2: trades to add)
     */
    void addVillagerTrades(int profession, int career, EntityVillager.ITradeList[][] tradeLists);

    void addVillagerName(int profession, int career, String name);
}
