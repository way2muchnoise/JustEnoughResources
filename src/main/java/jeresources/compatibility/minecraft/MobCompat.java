package jeresources.compatibility.minecraft;

import java.util.HashMap;

import jeresources.api.conditionals.LightLevel;
import jeresources.entry.MobEntry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
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
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class MobCompat {
    private static World world = MinecraftCompat.getWorld();
    private final HashMap<Class, Integer[]> MOB_XP = new HashMap<>();
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
        
        entry.setLightLevel(LIGHT_LEVEL.get(entityClass));
    }

    public void setExperience(MobEntry entry) {
        Class entityClass = entry.getEntity().getClass();

        entry.setMinExp(MOB_XP.get(entityClass)[0]);
        entry.setMaxExp(MOB_XP.get(entityClass)[1]);
    }

    private void initMobXp() {
        for (EntityType entityType : ForgeRegistries.ENTITIES) {
            Entity entity = entityType.create(world);

            if (entity instanceof MobEntity) {
                Class entityClass = entity.getClass();
                Integer[] exp = new Integer[2];

                if(entity instanceof AnimalEntity || entity instanceof WaterMobEntity) {
                    exp[0] = 1;
                    exp[1] = 3;
                }
                else if(entity instanceof EnderDragonEntity) {
                    exp[0] = 500;
                    exp[1] = 12000;
                }
                else if(entity instanceof SlimeEntity || entity instanceof MagmaCubeEntity) {
                    exp[0] = 1;
                    exp[1] = 4;
                }
                else {
                    exp[0] = exp[1] = ((MobEntity)entity).experienceValue;
                }

                MOB_XP.put(entityClass, exp);
            }
        }
    }

    private void initLightLevel() {
        for (EntityType entityType : ForgeRegistries.ENTITIES)
        {
            Entity entity = entityType.create(world);

            if (entity instanceof MobEntity) {
                Class entityClass = entity.getClass();

                if (cannotSpawnNaturally(entity)) {
                    LIGHT_LEVEL.put(entityClass, LightLevel.any);
                }
                else if (entity instanceof BlazeEntity) {
                    LIGHT_LEVEL.put(entityClass, LightLevel.blaze);
                }
                else if (entity instanceof MonsterEntity || entity instanceof SlimeEntity || entity instanceof PhantomEntity) {
                    LIGHT_LEVEL.put(entityClass, LightLevel.hostile);
                }
                else if (entity instanceof BatEntity) {
                    LIGHT_LEVEL.put(entityClass, LightLevel.bat);
                }
                else {
                    LIGHT_LEVEL.put(entityClass, LightLevel.any);
                }
            }
        }
    }

    private boolean cannotSpawnNaturally(Entity entity) {
        return entity instanceof VexEntity || entity instanceof GuardianEntity || (entity instanceof PatrollerEntity && !(entity instanceof WitchEntity));
    }
}