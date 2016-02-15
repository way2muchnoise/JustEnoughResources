package jeresources.compatibility;

import jeresources.api.IMobRegistry;
import jeresources.api.conditionals.LightLevel;
import jeresources.api.conditionals.WatchableData;
import jeresources.api.drop.DropItem;
import jeresources.api.render.IMobRenderHook;
import jeresources.api.render.IScissorHook;
import jeresources.entries.MobEntry;
import jeresources.registry.MobRegistry;
import jeresources.utils.ReflectionHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MobRegistryImpl implements IMobRegistry
{
    private static List<MobEntry> registers = new ArrayList<>();
    private static List<Tuple<Class<? extends EntityLivingBase>, Tuple<WatchableData, DropItem[]>>> addedDrops = new ArrayList<>();
    private static Map<Class<? extends EntityLivingBase>, List<IMobRenderHook>> renderHooks = new HashMap<>();
    private static Map<String, List<IScissorHook>> scissorHooks = new HashMap<>();

    protected MobRegistryImpl()
    {

    }

    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, int minExp, int maxExp, String[] biomes, DropItem... drops)
    {
        registers.add(new MobEntry(entity, lightLevel, maxExp, maxExp, biomes, drops));
    }

    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, String[] biomes, DropItem... drops)
    {
        registers.add(new MobEntry(entity, lightLevel, biomes, drops));
    }

    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, int exp, String[] biomes, DropItem... drops)
    {
        registers.add(new MobEntry(entity, lightLevel, exp, biomes, drops));
    }

    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, int exp, DropItem... drops)
    {
        registers.add(new MobEntry(entity, lightLevel, exp, drops));
    }

    @Override
    public void register(EntityLivingBase entity, LightLevel lightLevel, int minExp, int maxExp, DropItem... drops)
    {
        registers.add(new MobEntry(entity, lightLevel, maxExp, maxExp, drops));
    }

    @Override
    public void registerDrops(Class<? extends EntityLivingBase> entity, DropItem... drops)
    {
        registerDrops(entity, WatchableData.EMPTY, drops);
    }

    @Override
    public void registerDrops(Class<? extends EntityLivingBase> entity, WatchableData watchableData, DropItem... drops)
    {
        addedDrops.add(new Tuple<Class<? extends EntityLivingBase>, Tuple<WatchableData, DropItem[]>>(entity, new Tuple<>(watchableData, drops)));
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
        for (MobEntry entry : registers)
            MobRegistry.getInstance().registerMob(entry);
        registers.clear();
        for (Tuple<Class<? extends EntityLivingBase>, Tuple<WatchableData, DropItem[]>> tuple : addedDrops)
            MobRegistry.getInstance().addDrops(tuple.getFirst(), tuple.getSecond().getFirst(), tuple.getSecond().getSecond());
        addedDrops.clear();
    }
}
