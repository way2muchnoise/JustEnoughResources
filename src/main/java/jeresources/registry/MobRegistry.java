package jeresources.registry;

import jeresources.api.drop.DropItem;
import jeresources.entries.MobEntry;
import jeresources.utils.ReflectionHelper;
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

    public void addDrops(Class<? extends EntityLivingBase> entity, DropItem... drops)
    {
        for (MobEntry entry : registry)
            if (ReflectionHelper.isInstanceOf(entry.getClass(), entity))
                entry.addDrops(drops);
    }
}
