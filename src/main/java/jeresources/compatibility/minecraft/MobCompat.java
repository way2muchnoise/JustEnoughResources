package jeresources.compatibility.minecraft;

import java.util.HashMap;

import jeresources.api.conditionals.LightLevel;
import jeresources.entry.MobEntry;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class MobCompat {
    private static Level level = MinecraftCompat.getLevel();
    private final HashMap<Class, Tuple<Integer, Integer>> MOB_XP = new HashMap<>();
    private final HashMap<Class, LightLevel> LIGHT_LEVEL = new HashMap<>();
    private static MobCompat instance;

    public static MobCompat getInstance() {
        if (instance == null)
            return instance = new MobCompat();
        return instance;
    }

    private MobCompat() {
        initMobXp();
        initLightLevel();
    }

    public void setLightLevel(MobEntry entry) {
        Class entityClass = entry.getEntity().getClass();
        
        entry.setLightLevel(LIGHT_LEVEL.getOrDefault(entityClass, LightLevel.any));
    }

    public void setExperience(MobEntry entry) {
        Class entityClass = entry.getEntity().getClass();

        Tuple<Integer, Integer> minMaxExp = MOB_XP.getOrDefault(entityClass, new Tuple<>(0,0));

        entry.setMinExp(minMaxExp.getA());
        entry.setMaxExp(minMaxExp.getB());
    }

    private void initMobXp() {
        for (EntityType entityType : ForgeRegistries.ENTITIES) {
            Entity entity = entityType.create(level);

            if (entity instanceof Mob) {
                Class entityClass = entity.getClass();
                Tuple<Integer, Integer> exp;

                if(entity instanceof Animal || entity instanceof WaterAnimal) {
                    exp = new Tuple<>(1, 3);
                }
                else if(entity instanceof EnderDragon) {
                    exp = new Tuple<>(500, 12000);
                }
                else if(entity instanceof Slime || entity instanceof MagmaCube) {
                    exp = new Tuple<>(1, 4);
                }
                else {
                    exp = new Tuple<>(((Mob)entity).xpReward, ((Mob)entity).xpReward);
                }

                MOB_XP.put(entityClass, exp);
            }
        }
    }

    private void initLightLevel() {
        for (EntityType entityType : ForgeRegistries.ENTITIES) {
            Entity entity = entityType.create(level);

            if (entity instanceof Mob) {
                Class entityClass = entity.getClass();

                if (cannotSpawnNaturally(entity)) {
                    LIGHT_LEVEL.put(entityClass, LightLevel.any);
                }
                else if (entity instanceof Blaze) {
                    LIGHT_LEVEL.put(entityClass, LightLevel.blaze);
                }
                else if (entity instanceof Monster || entity instanceof Slime || entity instanceof Phantom) {
                    LIGHT_LEVEL.put(entityClass, LightLevel.hostile);
                }
                else if (entity instanceof Bat) {
                    LIGHT_LEVEL.put(entityClass, LightLevel.bat);
                }
                else {
                    LIGHT_LEVEL.put(entityClass, LightLevel.any);
                }
            }
        }
    }

    private boolean cannotSpawnNaturally(Entity entity) {
        return entity instanceof Vex || entity instanceof Guardian || (entity instanceof PatrollingMonster && !(entity instanceof Witch));
    }
}
