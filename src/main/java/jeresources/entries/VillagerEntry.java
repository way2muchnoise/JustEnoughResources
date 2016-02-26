package jeresources.entries;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.village.MerchantRecipeList;

import java.util.Random;

public class VillagerEntry
{
    private static final Random r = new Random();

    private MerchantRecipeList[] merchantRecipes;
    private int profession, career;

    public VillagerEntry(int profession, int career, EntityVillager.ITradeList[][] tradesList)
    {
        this.profession = profession;
        this.career = career;
        int i = 0;
        for (EntityVillager.ITradeList[] levelList : tradesList)
        {
            MerchantRecipeList merchantRecipeList = new MerchantRecipeList();
            for (EntityVillager.ITradeList trade : levelList)
                trade.modifyMerchantRecipeList(merchantRecipeList, r); // TODO: work around this random thing
            merchantRecipes[i++] = merchantRecipeList;
        }
    }
}
