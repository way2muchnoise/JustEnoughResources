package jeresources.compatibility;

import jeresources.api.render.IMobRenderHook;
import jeresources.entry.DungeonEntry;
import jeresources.entry.MobEntry;
import jeresources.entry.PlantEntry;
import jeresources.entry.WorldGenEntry;
import jeresources.registry.DungeonRegistry;
import jeresources.registry.MobRegistry;
import jeresources.registry.PlantRegistry;
import jeresources.registry.WorldGenRegistry;
import jeresources.util.FakeClientLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public abstract class CompatBase {
    public static Level getLevel() {
        Level level = Minecraft.getInstance().level;
        if (level == null) {
            level = new FakeClientLevel();
        }
        return level;
    }

    public abstract void init(boolean worldGen);

    protected void registerMob(MobEntry entry) {
        MobRegistry.getInstance().registerMob(entry);
    }

    protected void registerDungeonEntry(DungeonEntry entry) {
        DungeonRegistry.getInstance().registerDungeonEntry(entry);
    }

    protected void registerWorldGen(WorldGenEntry entry) {
        WorldGenRegistry.getInstance().registerEntry(entry);
    }

    protected void registerPlant(PlantEntry entry) {
        PlantRegistry.getInstance().registerPlant(entry);
    }

    protected void registerMobRenderHook(Class<? extends LivingEntity> clazz, IMobRenderHook renderHook) {
        JERAPI.getInstance().getMobRegistry().registerRenderHook(clazz, renderHook);
    }
}
