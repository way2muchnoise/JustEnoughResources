package jeresources.utils;

import net.minecraft.entity.passive.EntityVillager;

public class TradeHelper
{
    public static EntityVillager.ITradeList[][][][] getTrades()
    {
        return ReflectionHelper.getPrivateValue(EntityVillager.class, null, "DEFAULT_TRADE_LIST_MAP", "field_175561_bA");
    }
}
