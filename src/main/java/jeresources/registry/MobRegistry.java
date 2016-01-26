package jeresources.registry;

import jeresources.api.drop.DropItem;
import jeresources.entries.MobEntry;
import jeresources.utils.MobHelper;
import jeresources.utils.ReflectionHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MobRegistry
{
    private Set<MobEntry> registry = new LinkedHashSet<>();

    private static MobRegistry instance = null;

    public static MobRegistry getInstance()
    {
        if (instance == null)
            return instance = new MobRegistry();
        return instance;
    }

    public boolean registerMob(MobEntry entry)
    {
        return registry.add(entry);
    }

    public List<MobEntry> getMobsThatDropItem(ItemStack item)
    {
        List<MobEntry> list = new ArrayList<MobEntry>();
        for (MobEntry entry : registry)
            if (MobHelper.dropsItem(entry, item)) list.add(entry);
        return list;
    }

    public List<MobEntry> getMobs()
    {
        return new ArrayList<MobEntry>(registry);
    }

    public void addDrops(Class<? extends EntityLivingBase> entity, DropItem... drops)
    {
        for (MobEntry entry : registry)
            if (ReflectionHelper.isInstanceOf(entry.getClass(), entity))
                entry.addDrops(drops);
    }
}
