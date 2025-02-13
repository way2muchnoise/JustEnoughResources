package jeresources.registry;

import jeresources.entry.MobEntry;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MobRegistry {
    private Set<MobEntry> registry;
    private static MobRegistry instance;

    public static MobRegistry getInstance() {
        if (instance == null)
            return instance = new MobRegistry();
        return instance;
    }

    private MobRegistry() {
        this.registry = new LinkedHashSet<>();
    }

    public boolean registerMob(MobEntry entry) {
        return entry != null && registry.add(entry);
    }

    public List<MobEntry> getMobs() {
        return new ArrayList<>(registry);
    }

    public void clearEntities() {
        registry.forEach(MobEntry::clearEntity);
    }

    public void clear() {
        registry.clear();
    }
}
