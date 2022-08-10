package jeresources.compatibility.minecraft;

import jeresources.api.conditionals.LightLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Witch;

public class MobCompat {
    private MobCompat() {}

    public static ExperienceRange getExperience(LivingEntity entity) {
        if(entity instanceof Mob) {
            if(entity instanceof Animal || entity instanceof WaterAnimal) {
                return new ExperienceRange(1, 3);
            }
            else if(entity instanceof EnderDragon) {
                return new ExperienceRange(500, 12000);
            }
            else if(entity instanceof Slime || entity instanceof MagmaCube) {
                return new ExperienceRange(1, 4);
            }
            else {
                int xp = ((Mob)entity).xpReward;
                return new ExperienceRange(xp, xp);
            }
        }
        return ExperienceRange.ZERO;
    }

    public static LightLevel getLightLevel(Entity entity) {
        if (entity instanceof Mob) {
            if (cannotSpawnNaturally(entity)) {
                return LightLevel.any;
            }
            else if (entity instanceof Blaze) {
                return LightLevel.blaze;
            }
            else if (entity instanceof Monster || entity instanceof Slime || entity instanceof Phantom) {
                return LightLevel.hostile;
            }
            else if (entity instanceof Bat) {
                return LightLevel.bat;
            }
        }
        return LightLevel.any;
    }

    private static boolean cannotSpawnNaturally(Entity entity) {
        return entity instanceof Vex || entity instanceof Guardian || (entity instanceof PatrollingMonster && !(entity instanceof Witch));
    }
}
