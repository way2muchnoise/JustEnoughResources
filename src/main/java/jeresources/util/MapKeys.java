package jeresources.util;

import jeresources.api.drop.LootDrop;
import jeresources.api.restrictions.Restriction;
import jeresources.entry.WorldGenEntry;
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
        String key = item.getRegistryName().toString() + ':' + drop.getMetadata();
        if (drop.getTagCompound() != null)
            key += ':' + drop.getTagCompound().toString();
        return key;
    }

    public static String getKey(IPlantable plant)
    {
        return plant.getPlant(null, null).getBlock().getUnlocalizedName();
    }

    public static String getKey(LootDrop dropItem)
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
