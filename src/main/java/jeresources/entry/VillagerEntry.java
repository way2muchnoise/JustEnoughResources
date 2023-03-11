package jeresources.entry;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import jeresources.collection.TradeList;
import jeresources.compatibility.CompatBase;
import mezz.jei.api.recipe.IFocus;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class VillagerEntry {
    private final List<TradeList> tradeList;
    private final VillagerProfession profession;
    @Nullable
    private Villager entity;

    public VillagerEntry(VillagerProfession profession, Int2ObjectMap<VillagerTrades.ItemListing[]> itemListings) {
        this.profession = profession;
        this.tradeList = new LinkedList<>();
        addITradeLists(itemListings);
    }

    public void addITradeLists(Int2ObjectMap<VillagerTrades.ItemListing[]> itemListings) {
        for (int i = 1;i < itemListings.size() + 1;i++) {
            VillagerTrades.ItemListing[] levelList = itemListings.get(i);
            TradeList trades = this.tradeList.size() > i ? this.tradeList.get(i) : new TradeList(this);
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
                if (!trade.getMinCostB().isEmpty())
                    list.add(trade.getMinCostB());
            }
        }
        return list;
    }

    public List<ItemStack> getOutputs() {
        List<ItemStack> list = new LinkedList<>();
        for (List<TradeList.Trade> trades : this.tradeList)
            list.addAll(trades.stream().map(TradeList.Trade::getMinResult).collect(Collectors.toList()));
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

    public Villager getVillagerEntity() {
        if (this.entity == null) {
            /*
             * level must be a client level here.
             * Passing in a ServerLevel can allow villagers to load all kinds of things,
             * like in the `VillagerTrades.TreasureMapForEmeralds` which loads chunks!
             */
            this.entity = EntityType.VILLAGER.create(CompatBase.getLevel());
            assert this.entity != null;
            this.entity.setVillagerData(this.entity.getVillagerData().setProfession(this.profession));
        }
        return this.entity;
    }

    public List<ItemStack> getPois() {
        return this.profession.getJobPoiType().getBlockStates().stream().map(blockstate -> new ItemStack(blockstate.getBlock())).collect(Collectors.toList());
    }

    public boolean hasPois() {
        return !this.profession.getJobPoiType().getBlockStates().isEmpty();
    }
}
