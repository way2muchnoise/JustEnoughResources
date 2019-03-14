package jeresources.compatibility;

import jeresources.api.IMobRegistry;
import jeresources.api.conditionals.LightLevel;
import jeresources.api.drop.LootDrop;
import jeresources.api.render.IMobRenderHook;
import jeresources.api.render.IScissorHook;
import jeresources.entry.MobEntry;
import jeresources.registry.MobRegistry;
import jeresources.util.LogHelper;
import jeresources.util.LootTableHelper;
import jeresources.util.ReflectionHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobRegistryImpl implements IMobRegistry {
    private static Map<MobEntry, ResourceLocation> rawRegisters = new HashMap<>();
    private static List<MobEntry> preppedRegisters = new ArrayList<>();
    private static Map<Class<? extends EntityLivingBase>, List<IMobRenderHook>> renderHooks = new HashMap<>();
    private static Map<String, List<IScissorHook>> scissorHooks = new HashMap<>();

    protected MobRegistryImpl() {

    }

    //region lootTables
    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, int minExp, int maxExp, String[] biomes, ResourceLocation lootTable) {
        try {
            rawRegisters.put(new MobEntry(entity, lightLevel, minExp, maxExp, biomes), lootTable);
        } catch (Exception e) {
            LogHelper.debug("Bad mob register for %s", entity.getClass().getName());
        }
    }

    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, int minExp, int maxExp, ResourceLocation lootTable) {
        try {
            rawRegisters.put(new MobEntry(entity, lightLevel, minExp, maxExp), lootTable);
        } catch (Exception e) {
            LogHelper.debug("Bad mob register for %s", entity.getClass().getName());
        }
    }

    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, int exp, String[] biomes, ResourceLocation lootTable) {
        try {
            rawRegisters.put(new MobEntry(entity, lightLevel, exp, biomes), lootTable);
        } catch (Exception e) {
            LogHelper.debug("Bad mob register for %s", entity.getClass().getName());
        }
    }

    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, int exp, ResourceLocation lootTable) {
        try {
            rawRegisters.put(new MobEntry(entity, lightLevel, exp), lootTable);
        } catch (Exception e) {
            LogHelper.debug("Bad mob register for %s", entity.getClass().getName());
        }
    }

    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, String[] biomes, ResourceLocation lootTable) {
        try {
            rawRegisters.put(new MobEntry(entity, lightLevel, biomes), lootTable);
        } catch (Exception e) {
            LogHelper.debug("Bad mob register for %s", entity.getClass().getName());
        }
    }

    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, ResourceLocation lootTable) {
        try {
            rawRegisters.put(new MobEntry(entity, lightLevel), lootTable);
        } catch (Exception e) {
            LogHelper.debug("Bad mob register for %s", entity.getClass().getName());
        }
    }

    @Override
    public void register(EntityLivingBase entity, ResourceLocation lootTable) {
        try {
            rawRegisters.put(new MobEntry(entity), lootTable);
        } catch (Exception e) {
            LogHelper.debug("Bad mob register for %s", entity.getClass().getName());
        }
    }
    //endregion

    //region lootDrops
    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, int minExp, int maxExp, String[] biomes, LootDrop... lootDrops) {
        try {
            preppedRegisters.add(new MobEntry(entity, lightLevel, minExp, maxExp, biomes, lootDrops));
        } catch (Exception e) {
            LogHelper.debug("Bad mob register for %s", entity.getClass().getName());
        }
    }

    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, int minExp, int maxExp, LootDrop... lootDrops) {
        try {
            preppedRegisters.add(new MobEntry(entity, lightLevel, minExp, maxExp, lootDrops));
        } catch (Exception e) {
            LogHelper.debug("Bad mob register for %s", entity.getClass().getName());
        }
    }

    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, int exp, String[] biomes, LootDrop... lootDrops) {
        try {
            preppedRegisters.add(new MobEntry(entity, lightLevel, exp, biomes, lootDrops));
        } catch (Exception e) {
            LogHelper.debug("Bad mob register for %s", entity.getClass().getName());
        }
    }

    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, int exp, LootDrop... lootDrops) {
        try {
            preppedRegisters.add(new MobEntry(entity, lightLevel, exp, lootDrops));
        } catch (Exception e) {
            LogHelper.debug("Bad mob register for %s", entity.getClass().getName());
        }
    }

    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, String[] biomes, LootDrop... lootDrops) {
        try {
            preppedRegisters.add(new MobEntry(entity, lightLevel, biomes, lootDrops));
        } catch (Exception e) {
            LogHelper.debug("Bad mob register for %s", entity.getClass().getName());
        }
    }

    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, LootDrop... lootDrops) {
        try {
            preppedRegisters.add(new MobEntry(entity, lightLevel, lootDrops));
        } catch (Exception e) {
            LogHelper.debug("Bad mob register for %s", entity.getClass().getName());
        }
    }

    @Override
    public void register(EntityLivingBase entity, LootDrop... lootDrops) {
        try {
            preppedRegisters.add(new MobEntry(entity, lootDrops));
        } catch (Exception e) {
            LogHelper.debug("Bad mob register for %s", entity.getClass().getName());
        }
    }
    //endregion

    @Override
    public void registerRenderHook(Class<? extends EntityLivingBase> entity, IMobRenderHook renderHook) {
        List<IMobRenderHook> list = renderHooks.get(entity);
        if (list == null) list = new ArrayList<>();
        list.add(renderHook);
        renderHooks.put(entity, list);
    }

    @Override
    public void registerScissorHook(Class caller, IScissorHook scissorHook) {
        List<IScissorHook> list = scissorHooks.get(caller.getName());
        if (list == null) list = new ArrayList<>();
        list.add(scissorHook);
        scissorHooks.put(caller.getName(), list);
    }

    @SuppressWarnings("unchecked")
    public static IMobRenderHook.RenderInfo applyRenderHooks(EntityLivingBase entity, IMobRenderHook.RenderInfo renderInfo) {
        for (Map.Entry<Class<? extends EntityLivingBase>, List<IMobRenderHook>> entry : renderHooks.entrySet())
            if (ReflectionHelper.isInstanceOf(entity.getClass(), entry.getKey()))
                for (IMobRenderHook renderHook : entry.getValue())
                    renderInfo = renderHook.transform(renderInfo, entity);
        return renderInfo;
    }

    public static IScissorHook.ScissorInfo applyScissorHooks(IScissorHook.ScissorInfo scissorInfo) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        int depth = 0;
        for (StackTraceElement element : stack) {
            if (scissorHooks.containsKey(element.getClassName())) {
                for (IScissorHook scissorHook : scissorHooks.get(element.getClassName()))
                    scissorInfo = scissorHook.transformScissor(scissorInfo);
                break;
            }
            if (depth++ > 10) break; // Don't go looking to deep in the stacktrace
        }
        return scissorInfo;
    }

    protected static void commit() {
        preppedRegisters.forEach(MobRegistry.getInstance()::registerMob);
        World world = CompatBase.getWorld();
        rawRegisters.forEach((key, value) -> key.addDrops(LootTableHelper.toDrops(world, value)));
        rawRegisters.keySet().forEach(MobRegistry.getInstance()::registerMob);
    }
}
