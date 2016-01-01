package jeresources.utils;

import jeresources.api.utils.DropItem;
import jeresources.entries.MobEntry;
import net.minecraft.entity.EntityHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;

public class MobHelper
{
    public static boolean dropsItem(MobEntry entry, ItemStack item)
    {
        for (DropItem dropItem : entry.getDrops())
            if (dropItem.item.isItemEqual(item)) return true;
        return false;
    }

    public static String getEntityName(MobEntry entry)
    {
        return EntityHelper.getEntityName(entry.getEntity());
    }

    public static int getExpDrop(MobEntry entry)
    {
        if (entry.getEntity() instanceof EntityLiving)
            return EntityHelper.getExperience((EntityLiving) entry.getEntity());
        return 0;
    }
}
