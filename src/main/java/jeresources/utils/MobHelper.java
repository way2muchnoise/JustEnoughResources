package jeresources.utils;

import jeresources.api.drop.LootDrop;
import jeresources.entries.MobEntry;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.item.ItemStack;

public class MobHelper
{
    public static boolean dropsItem(MobEntry entry, ItemStack item)
    {
        for (LootDrop dropItem : entry.getDrops())
            if (dropItem.item.isItemEqual(item)) return true;
        return false;
    }

    public static int getExpDrop(MobEntry entry)
    {
        if (entry.getEntity() instanceof EntityLiving)
            return ((EntityLiving) entry.getEntity()).experienceValue;
        return 0;
    }

    public static EntitySlime setSlimeSize(EntitySlime slime, int size)
    {
        //slime.setSlimeSize(size);
        return slime;
    }
}
