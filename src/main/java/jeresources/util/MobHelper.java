package jeresources.util;

import jeresources.entry.MobEntry;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntitySheep;

public class MobHelper
{
    public static int getExpDrop(MobEntry entry)
    {
        if (entry.getEntity() instanceof EntityLiving)
            return ((EntityLiving) entry.getEntity()).experienceValue;
        return 0;
    }

    public static String getExpandedName(MobEntry entry)
    {
        String raw =  entry.getEntity().getName();
        if (entry.getEntity() instanceof EntitySheep)
            raw = ((EntitySheep) entry.getEntity()).getFleeceColor().getName().replace("_", " ") + " " + raw;
        StringBuilder sb = new StringBuilder();
        for (String s : raw.split(" "))
            sb.append(s.substring(0, 1).toUpperCase()).append(s.substring(1).toLowerCase()).append(" ");
        return sb.toString().trim();
    }
}
