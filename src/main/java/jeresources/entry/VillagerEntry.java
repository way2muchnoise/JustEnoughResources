package jeresources.entry;

import jeresources.collection.TradeList;
import mezz.jei.api.recipe.IFocus;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class VillagerEntry {
    private final List<TradeList> tradeList;
    private final int profession, career;
    private final String name;

    public VillagerEntry(String name, int profession, int career, List<List<EntityVillager.ITradeList>> tradesLists) {
        this.name = name;
        this.profession = profession;
        this.career = career;
        this.tradeList = new LinkedList<>();
        addITradeLists(tradesLists);
    }

    public void addITradeLists(List<List<EntityVillager.ITradeList>> tradesLists) {
        int i = 0;
        for (List<EntityVillager.ITradeList> levelList : tradesLists) {
            TradeList trades = this.tradeList.size() > i ? this.tradeList.get(i) : new TradeList(this);
            levelList.forEach(trades::addITradeList);
            this.tradeList.add(trades);
            i++;
        }
    }

    public TradeList getVillagerTrades(int level) {
        return tradeList.get(level);
    }

    public List<ItemStack> getInputs() {
        List<ItemStack> list = new LinkedList<>();
        for (List<TradeList.Trade> trades : this.tradeList) {
            for (TradeList.Trade trade : trades) {
                list.add(trade.getMinBuyStack1());
                list.add(trade.getMinBuyStack2());
            }
        }
        return list;
    }

    public List<ItemStack> getOutputs() {
        List<ItemStack> list = new LinkedList<>();
        for (List<TradeList.Trade> trades : this.tradeList)
            list.addAll(trades.stream().map(TradeList.Trade::getMinSellStack).collect(Collectors.toList()));
        return list;
    }

    public int getMaxLevel() {
        return tradeList.size();
    }

    public int getCareer() {
        return career;
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return "entity.Villager." + this.name;
    }

    public int getProfession() {
        return this.profession;
    }

    public List<Integer> getPossibleLevels(IFocus<ItemStack> focus) {
        List<Integer> levels = new ArrayList<>();
        for (int i = 0; i < tradeList.size(); i++)
            if (tradeList.get(i) != null && tradeList.get(i).getFocusedList(focus).size() > 0)
                levels.add(i + 1);
        return levels;
    }

}
