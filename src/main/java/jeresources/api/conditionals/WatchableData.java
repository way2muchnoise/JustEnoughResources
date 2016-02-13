package jeresources.api.conditionals;

import jeresources.api.drop.DropItem;
import net.minecraft.entity.DataWatcher;

import java.util.*;

public class WatchableData
{
    // Empty
    public static final WatchableData EMPTY = new WatchableData();

    // Skeleton
    public static final WatchableData REGULAR_SKELETON = new WatchableData().add(13, 0);
    public static final WatchableData WITHER_SKELETON = new WatchableData().add(13, 1);

    // Creeper
    public static final WatchableData REGULAR_CREEPER = new WatchableData().add(17, 0);
    public static final WatchableData POWERED_CREEPER = new WatchableData().add(17, 1);

    // Guardian
    public static final WatchableData REGULAR_GUARDIAN = new WatchableData().add(16, 0, 2);
    public static final WatchableData ELDER_GUARDIAN = new WatchableData().add(16, 4, 6);

    // Slimes
    public static final WatchableData SMALL_SLIME = new WatchableData().add(16, 1);
    public static final WatchableData MEDIUM_SLIME = new WatchableData().add(16, 2);
    public static final WatchableData LARGE_SLIME = new WatchableData().add(16, 4);

    private Map<Integer, Set<Object>> watches;

    /**
     * This class is used in {@link jeresources.api.IMobRegistry#registerDrops(Class, WatchableData, DropItem...)}
     * It uses {@link DataWatcher} to see if the {@link net.minecraft.entity.EntityLivingBase} fulfills the given terms
     */
    public WatchableData()
    {
        this.watches = new HashMap<>();
    }

    /**
     * Add an extra term
     *
     * @param id the id in the {@link DataWatcher}
     * @param values possible values for the given ID
     * @return the current {@link WatchableData}
     */
    public WatchableData add(int id, Object... values)
    {
        Set<Object> set = this.watches.get(id);
        if (set == null) set = new HashSet<>();
        Collections.addAll(set, values);
        this.watches.put(id, set);
        return this;
    }

    /**
     * Add all terms from given {@link WatchableData}
     *
     * @param other contains terms to add
     * @return the current {@link WatchableData}
     */
    public WatchableData add(WatchableData other)
    {
        for (Map.Entry<Integer, Set<Object>> entry : other.watches.entrySet())
            add(entry.getKey(), entry.getValue().toArray());
        return this;
    }

    /**
     * Remove an id from to check
     *
     * @param id the id to remove
     * @return the current {@link WatchableData}
     */
    public WatchableData remove(int id)
    {
        this.watches.remove(id);
        return this;
    }

    /**
     * Check if this {@link WatchableData} is valid for given {@link DataWatcher}
     *
     * @param dataWatcher the {@link DataWatcher} to check
     * @return true if the given {@link DataWatcher} fulfills all terms in the {@link WatchableData}
     */
    public boolean isValid(DataWatcher dataWatcher)
    {
        boolean isValid = true;
        for (DataWatcher.WatchableObject object : dataWatcher.getAllWatched())
        {
            if (this.watches.containsKey(object.getDataValueId()))
            {
                boolean inSet = false;
                for (Object obj : this.watches.get(object.getDataValueId()))
                {
                    inSet = object.getObject().equals(obj);
                    if (inSet) break;
                }
                isValid = inSet;
                if (!isValid) break;
            }
        }
        return isValid;
    }
}
