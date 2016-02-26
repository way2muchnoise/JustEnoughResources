package jeresources.entries;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraft.village.MerchantRecipe;
import net.minecraft.village.MerchantRecipeList;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VillagerEntry
{
    private static final Random r = new Random();

    private List<List<Tuple<MerchantRecipe, MerchantRecipe>>> merchantRecipes;
    private final int profession, career;

    public VillagerEntry(int profession, int career, EntityVillager.ITradeList[][] tradesList)
    {
        this.profession = profession;
        this.career = career;
        this.merchantRecipes = new ArrayList<>();
        for (EntityVillager.ITradeList[] levelList : tradesList)
        {
            List<Tuple<MerchantRecipe, MerchantRecipe>> levelMerchantRecipes = new ArrayList<>();
            for (EntityVillager.ITradeList trade : levelList)
            {
                MerchantRecipeList tempList = new MerchantRecipeList();
                for (int itr = 0; itr < 100; itr++)
                    trade.modifyMerchantRecipeList(tempList, r);
                ItemStack minBuy1 = tempList.get(0).getItemToBuy();
                ItemStack minBuy2 = tempList.get(0).getSecondItemToBuy();
                ItemStack minSell = tempList.get(0).getItemToSell();
                ItemStack maxBuy1 = tempList.get(0).getItemToBuy();
                ItemStack maxBuy2 = tempList.get(0).getSecondItemToBuy();
                ItemStack maxSell = tempList.get(0).getItemToSell();
                for (MerchantRecipe merchantRecipe : tempList)
                {
                    if (merchantRecipe.getItemToBuy().stackSize < minBuy1.stackSize)
                        minBuy1 = merchantRecipe.getItemToBuy();
                    if (merchantRecipe.getSecondItemToBuy().stackSize < minBuy2.stackSize)
                        minBuy2 = merchantRecipe.getItemToBuy();
                    if (merchantRecipe.getItemToSell().stackSize < minSell.stackSize)
                        minSell = merchantRecipe.getItemToBuy();

                    if (merchantRecipe.getItemToBuy().stackSize > maxBuy1.stackSize)
                        maxBuy1 = merchantRecipe.getItemToBuy();
                    if (merchantRecipe.getSecondItemToBuy().stackSize > maxBuy2.stackSize)
                        maxBuy2 = merchantRecipe.getItemToBuy();
                    if (merchantRecipe.getItemToSell().stackSize > maxSell.stackSize)
                        maxSell = merchantRecipe.getItemToBuy();
                }
                levelMerchantRecipes.add(new Tuple<>(
                        new MerchantRecipe(minBuy1, minBuy2, minSell),
                        new MerchantRecipe(maxBuy1, maxBuy2, maxSell)
                ));
            }
            merchantRecipes.add(levelMerchantRecipes);
        }
    }

    public List<Tuple<MerchantRecipe, MerchantRecipe>> getMerchantRecipes(int level)
    {
        return merchantRecipes.get(level);
    }

    public int getMaxLevel()
    {
        return merchantRecipes.size()-1;
    }
}
