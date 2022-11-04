package jeresources.entry;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import jeresources.collection.TradeList;
import mezz.jei.api.recipe.IFocus;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractVillagerEntry<T extends AbstractVillager> {
    private final List<TradeList> tradeList;
    @Nullable
    protected T entity;

    public AbstractVillagerEntry(Int2ObjectMap<VillagerTrades.ItemListing[]> itemListings) {
        this.tradeList = new LinkedList<>();
        addITradeLists(itemListings);
    }

    public void addITradeLists(Int2ObjectMap<VillagerTrades.ItemListing[]> itemListings) {
        for (int i = 1;i < itemListings.size() + 1;i++) {
            VillagerTrades.ItemListing[] levelList = itemListings.get(i);
            TradeList trades = this.tradeList.size() > i ? this.tradeList.get(i) : new TradeList(entity);
            trades.addITradeList(levelList);
            this.tradeList.add(trades);
        }
    }

    public TradeList getVillagerTrades(int level) {
        if (tradeList.size() > level) {
            return tradeList.get(level);
        } else {
            return new TradeList(null);
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
            list.addAll(trades.stream().map(TradeList.Trade::getMinResult).collect(Collectors.toList()));
        }
        return list;
    }

    public int getMaxLevel() {
        return tradeList.size();
    }

    public abstract String getName();

    public abstract String getDisplayName();

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

    public abstract List<ItemStack> getPois();

    public abstract boolean hasPois();

    public abstract boolean hasLevels();
}
