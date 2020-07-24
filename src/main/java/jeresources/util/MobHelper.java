package jeresources.util;

import jeresources.api.conditionals.LightLevel;
import jeresources.entry.MobEntry;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.monster.GuardianEntity;
import net.minecraft.entity.monster.MagmaCubeEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.PatrollerEntity;
import net.minecraft.entity.monster.PhantomEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.WaterMobEntity;

public class MobHelper {
    public static int getExpDrop(MobEntry entry, boolean max) {
        if (entry.getEntity() instanceof MobEntity) {
            int experience = ((MobEntity)entry.getEntity()).experienceValue;

            if (experience != 0)
                return experience;
            else if (entry.getEntity() instanceof AnimalEntity || entry.getEntity() instanceof WaterMobEntity)
                return max ? 3 : 1;
            else if (entry.getEntity() instanceof EnderDragonEntity)
                return max ? 12000 : 500;
            else if (entry.getEntity() instanceof SlimeEntity || entry.getEntity() instanceof MagmaCubeEntity)
                return max ? 4 : 1;
        }
        return 0;
    }

    public static LightLevel getLightLevel(MobEntry entry) {
        if (entry.getEntity() instanceof VexEntity || entry.getEntity() instanceof GuardianEntity || (entry.getEntity() instanceof PatrollerEntity && !(entry.getEntity() instanceof WitchEntity))) //all the entities that cannot spawn naturally
            return LightLevel.any;
        else if (entry.getEntity() instanceof BlazeEntity)
            return LightLevel.blaze;
        else if (entry.getEntity() instanceof MonsterEntity || entry.getEntity() instanceof SlimeEntity || entry.getEntity() instanceof PhantomEntity)
            return LightLevel.hostile;
        else if (entry.getEntity() instanceof BatEntity)
            return LightLevel.bat;
        return LightLevel.any;
    }

    public static String getExpandedName(MobEntry entry) {
        String raw = entry.getEntity().getName().getString();
        if (entry.getEntity() instanceof SheepEntity)
            raw += " (" + TranslationHelper.translateAndFormat("color.minecraft."+((SheepEntity) entry.getEntity()).getFleeceColor().getString()) + ")";
        StringBuilder sb = new StringBuilder();
        for (String s : raw.split(" "))
            sb.append(s.substring(0, 1).toUpperCase()).append(s.substring(1).toLowerCase()).append(" ");
        return sb.toString().trim();
    }
}
