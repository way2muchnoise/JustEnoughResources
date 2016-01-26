package jeresources.utils;

import jeresources.api.drop.DropItem;
import jeresources.api.restrictions.Restriction;
import jeresources.entries.WorldGenEntry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IPlantable;

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

    public static String getKey(WorldGenEntry entry)
    {
        return getKey(entry.getBlock(), entry.getRestriction());
    }
}
