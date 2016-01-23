package jeresources.utils;

import jeresources.api.utils.DropItem;

import jeresources.api.utils.restrictions.Restriction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.oredict.OreDictionary;

public class MapKeys
{
    public static String getKey(ItemStack drop)
    {
        if (drop == null)
            return null;
        Item item = drop.getItem();
        if (item == null)
            return null;
        return item.getRegistryName() + ':' + drop.getMetadata();
    }

    public static String getKey(IPlantable plant)
    {
        return plant.getPlant(null, null).getBlock().getUnlocalizedName();
    }

    public static String getKey(DropItem dropItem)
    {
        return getKey(dropItem.item);
    }

    public static String getKey(ItemStack drop, Restriction restriction)
    {
        return getKey(drop) + ":" + restriction.toString();
    }
}
