package jeresources.registry;

import jeresources.api.conditionals.WatchableData;
import jeresources.api.drop.LootDrop;
import jeresources.entry.MobEntry;
import jeresources.util.ReflectionHelper;
import net.minecraft.entity.EntityLivingBase;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MobRegistry
{
    private Set<MobEntry> registry;
    private static MobRegistry instance ;

    public static MobRegistry getInstance()
    {
        if (instance == null)
            return instance = new MobRegistry();
        return instance;
    }

    private MobRegistry()
    {
        this.registry = new LinkedHashSet<>();
    }

    public boolean registerMob(MobEntry entry)
    {
        return registry.add(entry);
    }

    public List<MobEntry> getMobs()
    {
        return new ArrayList<>(registry);
    }

    public void addDrops(Class<? extends EntityLivingBase> entity, WatchableData watchableData, LootDrop... drops)
    {
        registry.stream()
                .filter(entry -> ReflectionHelper.isInstanceOf(entry.getEntity().getClass(), entity))
                .filter(entry -> !watchableData.getExactClassMatchFlag() || entry.getEntity().getClass() == entity)
                .filter(entry -> watchableData.isValid(entry.getEntity().getDataManager()))
                .forEach(entry -> entry.addDrops(drops));
    }
}
