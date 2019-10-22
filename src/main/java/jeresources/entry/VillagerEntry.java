package jeresources.entry;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import jeresources.collection.TradeList;
import jeresources.compatibility.CompatBase;
import mezz.jei.api.recipe.IFocus;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class VillagerEntry {
    private final List<TradeList> tradeList;
    private final VillagerProfession profession;

    public VillagerEntry(VillagerProfession profession, Int2ObjectMap<VillagerTrades.ITrade[]> tradesLists) {
        this.profession = profession;
        this.tradeList = new LinkedList<>();
        addITradeLists(tradesLists);
    }

    public void addITradeLists(Int2ObjectMap<VillagerTrades.ITrade[]> tradesLists) {
        int i = 0;
        for (VillagerTrades.ITrade[] levelList : tradesLists.values()) {
            TradeList trades = this.tradeList.size() > i ? this.tradeList.get(i) : new TradeList(this);
            trades.addITradeList(levelList);
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
                if (!trade.getMinBuyStack2().isEmpty())
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

    public String getName() {
        return this.profession.toString();
    }

    public String getDisplayName() {
        return "entity.minecraft.villager." + this.profession.toString();
    }

    public VillagerProfession getProfession() {
        return this.profession;
    }

    public List<Integer> getPossibleLevels(IFocus<ItemStack> focus) {
        List<Integer> levels = new ArrayList<>();
        for (int i = 0; i < tradeList.size(); i++)
            if (tradeList.get(i) != null && tradeList.get(i).getFocusedList(focus).size() > 0)
                levels.add(i);
        return levels;
    }

    public VillagerEntity getVillagerEntity() {
        VillagerEntity villagerEntity = EntityType.VILLAGER.create(CompatBase.getWorld());
        villagerEntity.setVillagerData(villagerEntity.getVillagerData().withProfession(this.profession));
        return villagerEntity;
    }
}
