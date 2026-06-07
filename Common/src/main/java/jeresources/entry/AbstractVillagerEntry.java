package jeresources.entry;

import jeresources.collection.TradeList;
import mezz.jei.api.recipe.IFocus;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.npc.villager.AbstractVillager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AbstractVillagerEntry<T extends AbstractVillager> {
    private final List<TradeList> tradeList;
    protected T entity;

    public AbstractVillagerEntry(Map<Integer, List<VillagerTrade>> tradesByLevel) {
        this.tradeList = new LinkedList<>();
        addITradeLists(tradesByLevel);
    }

    public AbstractVillagerEntry() {
        this.tradeList = new LinkedList<>();
    }

    public void addITradeLists(Map<Integer, List<VillagerTrade>> tradesByLevel) {
        for (int i = 1;i < tradesByLevel.size() + 1;i++) {
            List<VillagerTrade> levelTrades = tradesByLevel.get(i);
            if (levelTrades != null) {
                TradeList trades = new TradeList(this);
                trades.addTrades(levelTrades);
                this.tradeList.add(trades);
            }
        }
    }

    public TradeList getVillagerTrades(int level) {
        if (tradeList.size() > level) {
            return tradeList.get(level);
        } else {
            return new TradeList(this);
        }
    }

    public List<ItemStack> getInputs() {
        List<ItemStack> list = new LinkedList<>();
        for (List<TradeList.Trade> trades : this.tradeList) {
            for (TradeList.Trade trade : trades) {
                list.add(trade.getMinCostA());
                if (!trade.getMinCostB().isEmpty()) {
                    list.add(trade.getMinCostB());
                }
            }
        }
        return list;
    }

    public List<ItemStack> getOutputs() {
        List<ItemStack> list = new LinkedList<>();
        for (List<TradeList.Trade> trades : this.tradeList) {
            list.addAll(trades.stream().map(TradeList.Trade::getMinResult).toList());
        }
        return list;
    }

    public int getMaxLevel() {
        return tradeList.size();
    }

    public abstract Component getDisplayName();

    public List<Integer> getPossibleLevels(IFocus<ItemStack> focus) {
        List<Integer> levels = new ArrayList<>();
        for (int i = 0; i < tradeList.size(); i++) {
            if (tradeList.get(i) != null && tradeList.get(i).getFocusedList(focus).size() > 0) {
                levels.add(i);
            }
        }
        return levels;
    }

    public abstract T getVillagerEntity();

    public void clearEntity(){
        this.entity = null;
    }

    public abstract List<ItemStack> getPois();

    public abstract boolean hasPois();

    public abstract boolean hasLevels();
}
