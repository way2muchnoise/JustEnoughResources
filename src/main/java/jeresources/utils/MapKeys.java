package jeresources.utils;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class MapKeys
{

    public static String[] getKeys(ItemStack itemStack)
    {
        if (itemStack != null && itemStack.getItem() != null)
        {
            int[] oreDictIds = OreDictionary.getOreIDs(itemStack);
            String[] oreDictNames = new String[oreDictIds.length];
            for (int i = 0; i < oreDictIds.length; i++)
                oreDictNames[i] = OreDictionary.getOreName(oreDictIds[i]);
            if (oreDictNames.length == 0)
                oreDictNames = new String[]{key(itemStack)};
            return oreDictNames;
        }
        return new String[0];
    }

    public static String getKey(ItemStack drop)
    {
        String[] keys = getKeys(drop);
        if (keys.length > 0)
        {
            if (keys[0].equals("oreCertusQuartz") && keys.length > 1) return keys[1];
            return keys[0];
        }
        return null;
    }

    public static String key(ItemStack itemStack)
    {
        return itemStack.getItem().hashCode() + ":" + itemStack.getItemDamage();
    }
}
