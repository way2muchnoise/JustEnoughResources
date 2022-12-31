package jeresources.util;

import jeresources.entry.MobEntry;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.passive.SheepEntity;

public class MobHelper {
    public static int getExpDrop(MobEntry entry) {
        if (entry.getEntity() instanceof MobEntity)
            return ((MobEntity)entry.getEntity()).xpReward;
        return 0;
    }

    public static String getExpandedName(MobEntry entry) {
        String raw = entry.getEntity().getName().getString();
        if (entry.getEntity() instanceof SheepEntity)
            raw += " (" + TranslationHelper.translateAndFormat("color.minecraft."+((SheepEntity) entry.getEntity()).getColor().getName()) + ")";
        StringBuilder sb = new StringBuilder();
        for (String s : raw.split(" "))
            if (s.length() >= 1) {
                sb.append(s.substring(0, 1).toUpperCase()).append(s.substring(1)).append(" ");
            }
        return sb.toString().trim();
    }
}
