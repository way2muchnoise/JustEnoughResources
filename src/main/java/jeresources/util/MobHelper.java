package jeresources.util;

import jeresources.entry.MobEntry;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Sheep;

public class MobHelper {
    public static int getExpDrop(MobEntry entry) {
        if (entry.getEntity() instanceof Mob)
            return ((Mob)entry.getEntity()).xpReward;
        return 0;
    }

    public static String getExpandedName(MobEntry entry) {
        String raw = entry.getEntity().getName().getString();
        if (entry.getEntity() instanceof Sheep)
            raw += " (" + TranslationHelper.translateAndFormat("color.minecraft."+((Sheep) entry.getEntity()).getColor().getName()) + ")";
        StringBuilder sb = new StringBuilder();
        for (String s : raw.split(" "))
            sb.append(s.substring(0, 1).toUpperCase()).append(s.substring(1)).append(" ");
        return sb.toString().trim();
    }
}
