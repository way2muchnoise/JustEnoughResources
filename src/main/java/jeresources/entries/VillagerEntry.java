package jeresources.entries;

import jeresources.registry.VillagerRegistry;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class VillagerEntry
{
    private static final Random r = new Random();

    private final List<TradeList> tradeList;
    private final int profession, career;

    public VillagerEntry(int profession, int career, EntityVillager.ITradeList[][] tradesList)
    {
        this.profession = profession;
        this.career = career;
        this.tradeList = new LinkedList<>();
        for (EntityVillager.ITradeList[] levelList : tradesList)
        {
            TradeList tradeList = new TradeList();
            for (EntityVillager.ITradeList trade : levelList)
            {
                MerchantRecipeList tempList = new MerchantRecipeList();
                for (int itr = 0; itr < 100; itr++)
                    trade.modifyMerchantRecipeList(tempList, r);
                ItemStack buy1 = tempList.get(0).getItemToBuy();
                ItemStack buy2 = tempList.get(0).getSecondItemToBuy();
                ItemStack sell = tempList.get(0).getItemToSell();
                int minBuy1, minBuy2, minSell;
                int maxBuy1, maxBuy2, maxSell;
                minBuy1 = maxBuy1 = buy1.stackSize;
                if (buy2 != null) minBuy2 = maxBuy2 = buy2.stackSize;
                else minBuy2 = maxBuy2 = 0;
                minSell = maxSell = sell.stackSize;
                for (MerchantRecipe merchantRecipe : tempList)
                {
                    if (minBuy1 > merchantRecipe.getItemToBuy().stackSize)
                        minBuy1 = merchantRecipe.getItemToBuy().stackSize;
                    if (buy2 != null && minBuy2 > merchantRecipe.getSecondItemToBuy().stackSize)
                        minBuy2 = merchantRecipe.getSecondItemToBuy().stackSize;
                    if (minSell > merchantRecipe.getItemToSell().stackSize)
                        minSell = merchantRecipe.getItemToSell().stackSize;

                    if (maxBuy1 < merchantRecipe.getItemToBuy().stackSize)
                        maxBuy1 = merchantRecipe.getItemToBuy().stackSize;
                    if (buy2 != null && maxBuy2 < merchantRecipe.getSecondItemToBuy().stackSize)
                        maxBuy2 = merchantRecipe.getSecondItemToBuy().stackSize;
                    if (maxSell < merchantRecipe.getItemToSell().stackSize)
                        maxSell = merchantRecipe.getItemToSell().stackSize;
                }
                tradeList.add(
                    new Trade(
                        buy1, minBuy1, maxBuy1,
                        buy2, minBuy2, maxBuy2,
                        sell, minSell, maxSell
                    )
                );
            }
            this.tradeList.add(tradeList);
        }
    }

    public TradeList getVillagerTrades(int level)
    {
        return tradeList.get(level);
    }

    public List<ItemStack> getInputs()
    {
        List<ItemStack> list = new LinkedList<>();
        for (List<Trade> trades : this.tradeList)
        {
            for (Trade trade : trades)
            {
                list.add(trade.getMinBuyStack1());
                list.add(trade.getMinBuyStack2());
            }
        }
        return list;
    }

    public List<ItemStack> getOutputs()
    {
        List<ItemStack> list = new LinkedList<>();
        for (List<Trade> trades : this.tradeList)
            for (Trade trade : trades)
                list.add(trade.getMinSellStack());
        return list;
    }

    public int getMaxLevel()
    {
        return tradeList.size();
    }

    public int getProfession()
    {
        return profession;
    }

    public String getName()
    {
        return VillagerRegistry.getInstance().getVillagerName(profession, career);
    }

    public class TradeList extends LinkedList<Trade>
    {
        public List<ItemStack> getFirstBuyStacks()
        {
            List<ItemStack> list = new LinkedList<>();
            for (Trade trade : this)
                list.add(trade.getMinBuyStack1());
            return list;
        }

        public List<ItemStack> getSecondBuyStacks()
        {
            List<ItemStack> list = new LinkedList<>();
            for (Trade trade : this)
                list.add(trade.getMinBuyStack2());
            return list;
        }

        public List<ItemStack> getSellStacks()
        {
            List<ItemStack> list = new LinkedList<>();
            for (Trade trade : this)
                list.add(trade.getMinSellStack());
            return list;
        }

        public TradeList getSubListSell(ItemStack itemStack)
        {
            TradeList list = new TradeList();
            for (Trade trade : this)
                if (trade.sellsItem(itemStack))
                    list.add(trade);
            return list;
        }

        public TradeList getSubListBuy(ItemStack itemStack)
        {
            TradeList list = new TradeList();
            for (Trade trade : this)
                if (trade.buy1.isItemEqual(itemStack))
                    list.add(trade);
            return list;
        }
    }

    public class Trade
    {
        private final ItemStack buy1, buy2, sell;
        private final int minBuy1, minBuy2, minSell;
        private final int maxBuy1, maxBuy2, maxSell;

        private Trade(
                ItemStack buy1, int minBuy1, int maxBuy1,
                ItemStack buy2, int minBuy2, int maxBuy2,
                ItemStack sell, int minSell, int maxSell
                )
        {
            this.buy1 = buy1;
            this.minBuy1 = minBuy1;
            this.maxBuy1 = maxBuy1;
            this.buy2 = buy2;
            this.minBuy2 = minBuy2;
            this.maxBuy2 = maxBuy2;
            this.sell = sell;
            this.minSell = minSell;
            this.maxSell = maxSell;
        }

        public boolean sellsItem(ItemStack itemStack)
        {
            return this.sell.isItemEqual(itemStack);
        }

        public boolean buysItem(ItemStack itemStack)
        {
            return this.buy1.isItemEqual(itemStack) || this.buy2.isItemEqual(itemStack);
        }

        public ItemStack getMinBuyStack1()
        {
            ItemStack minBuyStack = this.buy1.copy();
            minBuyStack.stackSize = this.minBuy1;
            return minBuyStack;
        }

        public ItemStack getMinBuyStack2()
        {
            if (this.buy2 == null) return null;
            ItemStack minBuyStack = this.buy2.copy();
            minBuyStack.stackSize = this.minBuy2;
            return minBuyStack;
        }

        public ItemStack getMinSellStack()
        {
            ItemStack minSellStack = this.sell.copy();
            minSellStack.stackSize = this.minSell;
            return minSellStack;
        }

        public ItemStack getMaxBuyStack1()
        {
            ItemStack maxBuyStack = this.buy1.copy();
            maxBuyStack.stackSize = this.maxBuy1;
            return maxBuyStack;
        }

        public ItemStack getMaxBuyStack2()
        {
            if (this.buy2 == null) return null;
            ItemStack maxBuyStack = this.buy2.copy();
            maxBuyStack.stackSize = this.maxBuy2;
            return maxBuyStack;
        }

        public ItemStack getMaxSellStack()
        {
            ItemStack maxSellStack = this.sell.copy();
            maxSellStack.stackSize = this.maxSell;
            return maxSellStack;
        }

        @Override
        public String toString()
        {
            return "Buy1: " + this.buy1 + ", Buy2: " + this.buy2 + ", Sell: " + this.sell;
        }
    }
}
