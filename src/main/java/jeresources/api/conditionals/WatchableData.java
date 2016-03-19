package jeresources.api.conditionals;

import jeresources.api.drop.LootDrop;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;

import java.util.*;

public class WatchableData
{
    // Empty
    public static final WatchableData EMPTY = new WatchableData();

    // Exact match
    public static final WatchableData EXACT_CLASS = new WatchableData().setExactClassMatch();

    /*// Skeleton
    public static final WatchableData REGULAR_SKELETON = new WatchableData().add(13, (byte)0);
    public static final WatchableData WITHER_SKELETON = new WatchableData().add(13, (byte)1);

    // Creeper
    public static final WatchableData REGULAR_CREEPER = new WatchableData().add(17, (byte)0);
    public static final WatchableData POWERED_CREEPER = new WatchableData().add(17, (byte)1);

    // Guardian
    public static final WatchableData REGULAR_GUARDIAN = new WatchableData().add(16, 0, 2);
    public static final WatchableData ELDER_GUARDIAN = new WatchableData().add(16, 4, 6);

    // Slimes
    public static final WatchableData SMALL_SLIME = new WatchableData().add(16, (byte)1);
    public static final WatchableData MEDIUM_SLIME = new WatchableData().add(16, (byte)2);
    public static final WatchableData LARGE_SLIME = new WatchableData().add(16, (byte)4);*/

    private Map<DataParameter<?>, Set<Object>> watches;
    private boolean exactClassMatch;

    /**
     * This class is used in {@link jeresources.api.IMobRegistry#registerDrops(Class, WatchableData, LootDrop...)}
     * It uses {@link EntityDataManager} to see if the {@link net.minecraft.entity.EntityLivingBase} fulfills the given terms
     * You can also set an exactClassMatch flag that will make it only apply to exact class match and not child classes
     */
    public WatchableData()
    {
        this.watches = new HashMap<>();
        this.exactClassMatch = false;
    }

    /**
     * Set the {@link #exactClassMatch} flag
     *
     * @return the current {@link WatchableData}
     */
    public WatchableData setExactClassMatch()
    {
        this.exactClassMatch = true;
        return this;
    }

    /**
     * Unset the {@link #exactClassMatch} flag
     *
     * @return the current {@link WatchableData}
     */
    public WatchableData unsetExactClassMatch()
    {
        this.exactClassMatch = false;
        return this;
    }

    /**
     * Add an extra term
     *
     * @param dataParameter the dataParameter in the {@link EntityDataManager}
     * @param values possible values for the given ID
     * @return the current {@link WatchableData}
     */
    public WatchableData add(DataParameter<?> dataParameter, Object... values)
    {
        Set<Object> set = this.watches.get(dataParameter);
        if (set == null) set = new HashSet<>();
        Collections.addAll(set, values);
        this.watches.put(dataParameter, set);
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
        for (Map.Entry<DataParameter<?>, Set<Object>> entry : other.watches.entrySet())
            add(entry.getKey(), entry.getValue().toArray());
        return this;
    }

    /**
     * Remove an id from to check
     *
     * @param dataParameter the {@link DataParameter} to remove
     * @return the current {@link WatchableData}
     */
    public WatchableData remove(DataParameter dataParameter)
    {
        this.watches.remove(dataParameter);
        return this;
    }

    /**
     * Check if this {@link WatchableData} is valid for given {@link EntityDataManager}
     *
     * @param dataManager the {@link EntityDataManager} to check
     * @return true if the given {@link EntityDataManager} fulfills all terms in the {@link WatchableData}
     */
    public boolean isValid(EntityDataManager dataManager)
    {
        for (Map.Entry<DataParameter<?>, Set<Object>> entry : this.watches.entrySet())
            if (!entry.getValue().contains(dataManager.get(entry.getKey())))
                return false;
        return true;
    }

    /**
     * Get the {@link #exactClassMatch} flag
     *
     * @return the {@link #exactClassMatch} flag
     */
    public boolean getExactClassMatchFlag()
    {
        return this.exactClassMatch;
    }
}
