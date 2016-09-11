package jeresources.compatibility;

import jeresources.api.IMobRegistry;
import jeresources.api.conditionals.LightLevel;
import jeresources.api.render.IMobRenderHook;
import jeresources.api.render.IScissorHook;
import jeresources.entry.MobEntry;
import jeresources.registry.MobRegistry;
import jeresources.util.LootTableHelper;
import jeresources.util.ReflectionHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobRegistryImpl implements IMobRegistry
{
    private static Map<MobEntry, ResourceLocation> rawRegisters = new HashMap<>();
    private static Map<Class<? extends EntityLivingBase>, List<IMobRenderHook>> renderHooks = new HashMap<>();
    private static Map<String, List<IScissorHook>> scissorHooks = new HashMap<>();

    protected MobRegistryImpl()
    {

    }

    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, int minExp, int maxExp, String[] biomes, ResourceLocation lootTable)
    {
        rawRegisters.put(new MobEntry(entity, lightLevel, minExp, maxExp, biomes), lootTable);
    }

    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, int minExp, int maxExp, ResourceLocation lootTable)
    {
        rawRegisters.put(new MobEntry(entity, lightLevel, minExp, maxExp), lootTable);
    }

    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, int exp, String[] biomes, ResourceLocation lootTable)
    {
        rawRegisters.put(new MobEntry(entity, lightLevel, exp, biomes), lootTable);
    }

    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, int exp, ResourceLocation lootTable)
    {
        rawRegisters.put(new MobEntry(entity, lightLevel, exp), lootTable);
    }

    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, String[] biomes, ResourceLocation lootTable)
    {
        rawRegisters.put(new MobEntry(entity, lightLevel, biomes), lootTable);
    }

    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, ResourceLocation lootTable)
    {
        rawRegisters.put(new MobEntry(entity, lightLevel), lootTable);
    }

    @Override
    public void register(EntityLivingBase entity, ResourceLocation lootTable)
    {
        rawRegisters.put(new MobEntry(entity), lootTable);
    }

    @Override
    public void registerRenderHook(Class<? extends EntityLivingBase> entity, IMobRenderHook renderHook)
    {
        List<IMobRenderHook> list = renderHooks.get(entity);
        if (list == null) list = new ArrayList<>();
        list.add(renderHook);
        renderHooks.put(entity, list);
    }

    @Override
    public void registerScissorHook(Class caller, IScissorHook scissorHook)
    {
        List<IScissorHook> list = scissorHooks.get(caller.getName());
        if (list == null) list = new ArrayList<>();
        list.add(scissorHook);
        scissorHooks.put(caller.getName(), list);
    }

    @SuppressWarnings("unchecked")
    public static IMobRenderHook.RenderInfo applyRenderHooks(EntityLivingBase entity, IMobRenderHook.RenderInfo renderInfo)
    {
        for (Map.Entry<Class<? extends EntityLivingBase>, List<IMobRenderHook>> entry : renderHooks.entrySet())
            if (ReflectionHelper.isInstanceOf(entity.getClass(), entry.getKey()))
                for (IMobRenderHook renderHook : entry.getValue())
                    renderInfo = renderHook.transform(renderInfo, entity);
        return renderInfo;
    }

    public static IScissorHook.ScissorInfo applyScissorHooks(IScissorHook.ScissorInfo scissorInfo)
    {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        int depth = 0;
        for (StackTraceElement element : stack)
        {
            if (scissorHooks.containsKey(element.getClassName()))
            {
                for (IScissorHook scissorHook : scissorHooks.get(element.getClassName()))
                    scissorInfo = scissorHook.transformScissor(scissorInfo);
                break;
            }
            if (depth++ > 10) break; // Don't go looking to deep in the stacktrace
        }
        return scissorInfo;
    }

    protected static void commit()
    {
        rawRegisters.entrySet().forEach(entry ->
                entry.getKey().addDrops(LootTableHelper.toDrops(CompatBase.getWorld(), entry.getValue())));
        rawRegisters.keySet().forEach(MobRegistry.getInstance()::registerMob);
        rawRegisters.clear();
    }
}
