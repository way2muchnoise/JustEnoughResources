package jeresources.util;

import jeresources.entry.MobEntry;
import net.minecraft.entity.EntityLiving;

public class MobHelper
{
    public static int getExpDrop(MobEntry entry)
    {
        if (entry.getEntity() instanceof EntityLiving)
            return ((EntityLiving) entry.getEntity()).experienceValue;
        return 0;
    }
}
