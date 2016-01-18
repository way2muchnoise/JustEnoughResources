package jeresources.entries;

import jeresources.api.messages.RegisterMobMessage;
import jeresources.api.utils.DropItem;
import jeresources.api.utils.LightLevel;
import jeresources.utils.MobHelper;
import jeresources.utils.ReflectionHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MobEntry
{
    private EntityLivingBase entity;
    private List<DropItem> drops = new ArrayList<DropItem>();
    private LightLevel lightLevel;
    private List<String> biomes = new ArrayList<String>();
    private int minExp, maxExp;

    public MobEntry(EntityLivingBase entity, LightLevel lightLevel, int minExp, int maxExp, String[] biomes, DropItem... drops)
    {
        this.entity = entity;
        this.lightLevel = lightLevel;
        this.biomes.addAll(Arrays.asList(biomes));
        this.drops.addAll(Arrays.asList(drops));
        this.maxExp = maxExp;
        this.minExp = minExp;
    }

    public MobEntry(EntityLivingBase entity, LightLevel lightLevel, String[] biomes, DropItem... drops)
    {
        this(entity, lightLevel, 0, 0, biomes, drops);
        this.maxExp = this.minExp = MobHelper.getExpDrop(this);
    }

    public MobEntry(EntityLivingBase entity, LightLevel lightLevel, int exp, String[] biomes, DropItem... drops)
    {
        this(entity, lightLevel, exp, exp, biomes, drops);
    }

    public MobEntry(EntityLivingBase entity, LightLevel lightLevel, int exp, DropItem... drops)
    {
        this(entity, lightLevel, exp, exp, drops);
    }

    public MobEntry(EntityLivingBase entity, LightLevel lightLevel, int minExp, int maxExp, DropItem... drops)
    {
        this.entity = entity;
        this.lightLevel = lightLevel;
        this.biomes.add("Any");
        this.drops.addAll(Arrays.asList(drops));
        this.maxExp = maxExp;
        this.minExp = minExp;
    }

    public MobEntry(EntityLivingBase entity, LightLevel lightLevel, DropItem... drops)
    {
        this(entity, lightLevel, 0, 0, drops);
        this.maxExp = this.minExp = MobHelper.getExpDrop(this);
    }

    public MobEntry(RegisterMobMessage message)
    {
        if (!ReflectionHelper.isInstanceOf(message.getMobClass(), EntityLivingBase.class)) return;
        entity = (EntityLivingBase) ReflectionHelper.initialize(message.getMobClass(), World.class, null);
        this.lightLevel = message.getLightLevel();
        Collections.addAll(drops, message.getDrops());
        Collections.sort(drops);
    }

    public EntityLivingBase getEntity()
    {
        return entity;
    }

    public String getMobName()
    {
        return entity.getName();
    }

    public DropItem[] getDrops()
    {
        return drops.toArray(new DropItem[drops.size()]);
    }

    public List<ItemStack> getDropsItemStacks()
    {
        List<ItemStack> drops = new ArrayList<>();
        for (DropItem drop : this.drops)
            drops.add(drop.item);
        return drops;
    }

    public String[] getBiomes()
    {
        return biomes.toArray(new String[biomes.size()]);
    }

    public boolean addDrop(DropItem item)
    {
        for (DropItem drop : drops)
            if (drop.item.isItemEqual(item.item)) return false;
        drops.add(item);
        Collections.sort(drops);
        return true;
    }

    public LightLevel getLightLevel()
    {
        return lightLevel;
    }

    public void removeDrop(ItemStack item)
    {
        int i = 0;
        for (; i < drops.size(); i++)
            if (drops.get(i).item.isItemEqual(item)) break;
        if (i < drops.size()) drops.remove(i);
    }

    public String getExp()
    {
        return this.minExp + (this.maxExp == this.minExp ? "" : " - " + this.maxExp);
    }
}
