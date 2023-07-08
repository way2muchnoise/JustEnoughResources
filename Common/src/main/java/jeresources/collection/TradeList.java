package jeresources.collection;

import mezz.jei.api.recipe.IFocus;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TradeList extends LinkedList<TradeList.Trade> {
    private static final Random r = new Random();

    private AbstractVillager entity;

    public TradeList(AbstractVillager entity) {
        this.entity = entity;
    }

    public List<ItemStack> getCostAs() {
        return this.stream().map(Trade::getMinCostA).collect(Collectors.toList());
    }

    public List<ItemStack> getCostBs() {
        return this.stream().map(Trade::getMinCostB).filter(itemStack -> !itemStack.isEmpty()).collect(Collectors.toList());
    }

    public List<ItemStack> getResults() {
        return this.stream().map(Trade::getMinResult).collect(Collectors.toList());
    }

    public TradeList getSubListSell(ItemStack itemStack) {
        return this.stream().filter(trade -> trade.sellsItem(itemStack)).collect(Collectors.toCollection(() -> new TradeList(entity)));
    }

    public TradeList getSubListBuy(ItemStack itemStack) {
        return this.stream().filter(trade -> trade.buysItem(itemStack)).collect(Collectors.toCollection(() -> new TradeList(entity)));
    }

    public TradeList getFocusedList(IFocus<ItemStack> focus) {
        if (focus == null) {
            return this;
        } else {
            return switch (focus.getRole()) {
                case INPUT -> getSubListBuy(focus.getTypedValue().getIngredient());
                case OUTPUT -> getSubListSell(focus.getTypedValue().getIngredient());
                default -> this;
            };
        }
    }

    private void addMerchantRecipe(MerchantOffers merchantOffers, VillagerTrades.ItemListing itemListing, RandomSource rand) {
        MerchantOffer offer = itemListing.getOffer(entity, rand);
        if (offer != null) {
            merchantOffers.add(offer);
        }
    }

    public void addITradeList(VillagerTrades.ItemListing[] itemListings) {
        for (VillagerTrades.ItemListing itemListing : itemListings) {
            MerchantOffers tempList = new MerchantOffers();
            RandomSource rand = RandomSource.create();
            for (int itr = 0; itr < 100; itr++)
                addMerchantRecipe(tempList, itemListing, rand);
            if (tempList.size() == 0) return; // Bad lists be bad
            ItemStack costA = tempList.get(0).getCostA();
            ItemStack costB = tempList.get(0).getCostB();
            ItemStack result = tempList.get(0).getResult();
            int minCostA, minCostB, minResult;
            int maxCostA, maxCostB, maxResult;
            minCostA = maxCostA = costA.getCount();
            if(!costB.isEmpty()) minCostB = maxCostB = costB.getCount();
            else minCostB = maxCostB = 1; // Needs to be one with the new ItemStack.EMPTY implementation
            minResult = maxResult = result.getCount();
            for (MerchantOffer merchantRecipe : tempList) {
                if(minCostA > merchantRecipe.getBaseCostA().getCount())
                    minCostA = merchantRecipe.getCostA().getCount();
                if(!costB.isEmpty() && minCostB > merchantRecipe.getCostB().getCount())
                    minCostB = merchantRecipe.getCostB().getCount();
                if(minResult > merchantRecipe.getResult().getCount())
                    minResult = merchantRecipe.getResult().getCount();

                if(maxCostA < merchantRecipe.getCostA().getCount())
                    maxCostA = merchantRecipe.getCostA().getCount();
                if(!costB.isEmpty() && maxCostB < merchantRecipe.getCostB().getCount())
                    maxCostB = merchantRecipe.getCostA().getCount();
                if(maxResult < merchantRecipe.getResult().getCount())
                    maxResult = merchantRecipe.getResult().getCount();
            }
            this.add(
                    new Trade(
                            costA, minCostA, maxCostA,
                            costB, minCostB, maxCostB,
                            result, minResult, maxResult
                    )
            );
        }
    }

    public static class Trade {
        private final ItemStack costA, costB, result;
        private final int minCostA, minCostB, minResult;
        private final int maxCostA, maxCostB, maxResult;

        Trade(
            ItemStack costA, int minCostA, int maxCostA,
            ItemStack costB, int minCostB, int maxCostB,
            ItemStack result, int minResult, int maxResult
        ) {
            this.costA = costA;
            this.minCostA = minCostA;
            this.maxCostA = maxCostA;
            this.costB = costB;
            this.minCostB = minCostB;
            this.maxCostB = maxCostB;
            this.result = result;
            this.minResult = minResult;
            this.maxResult = maxResult;
        }

        public boolean sellsItem(ItemStack itemStack) {
            return this.result.is(itemStack.getItem());
        }

        public boolean buysItem(ItemStack itemStack) {
            return this.costA.is(itemStack.getItem()) || (!this.costB.isEmpty() && this.costB.is(itemStack.getItem()));
        }

        public ItemStack getMinCostA() {
            ItemStack minBuyStack = this.costA.copy();
            minBuyStack.setCount(this.minCostA);
            return minBuyStack;
        }

        public ItemStack getMinCostB() {
            if (this.costB == null) return ItemStack.EMPTY;
            ItemStack minBuyStack = this.costB.copy();
            minBuyStack.setCount(this.minCostB);
            return minBuyStack;
        }

        public ItemStack getMinResult() {
            ItemStack minSellStack = this.result.copy();
            minSellStack.setCount(this.minResult);
            return minSellStack;
        }

        public ItemStack getMaxCostA() {
            ItemStack maxBuyStack = this.costA.copy();
            maxBuyStack.setCount(this.maxCostA);
            return maxBuyStack;
        }

        public ItemStack getMaxCostB() {
            if (this.costB == null) return ItemStack.EMPTY;
            ItemStack maxBuyStack = this.costB.copy();
            maxBuyStack.setCount(this.maxCostB);
            return maxBuyStack;
        }

        public ItemStack getMaxResult() {
            ItemStack maxSellStack = this.result.copy();
            maxSellStack.setCount(this.maxResult);
            return maxSellStack;
        }

        @Override
        public String toString() {
            return "Buy1: " + this.costA + ", Buy2: " + this.costB + ", Sell: " + this.result;
        }
    }
}
