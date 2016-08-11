package jeresources.entry;

import jeresources.api.conditionals.LightLevel;
import jeresources.api.drop.LootDrop;
import jeresources.util.LootHelper;
import jeresources.util.MobHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootTable;

import java.util.*;
import java.util.stream.Collectors;

public class MobEntry
{
    private EntityLivingBase entity;
    private Set<LootDrop> drops;
    private LightLevel lightLevel;
    private List<String> biomes;
    private int minExp, maxExp;

    public MobEntry(EntityLivingBase entity, LightLevel lightLevel, int minExp, int maxExp, String[] biomes, LootDrop... drops)
    {
        this.entity = entity;
        this.lightLevel = lightLevel;
        this.biomes = new ArrayList<>();
        this.biomes.addAll(Arrays.asList(biomes));
        this.drops = new TreeSet<>();
        this.drops.addAll(Arrays.asList(drops));
        this.maxExp = maxExp;
        this.minExp = minExp;
    }

    public MobEntry(EntityLivingBase entity, LightLevel lightLevel, String[] biomes, LootDrop... drops)
    {
        this(entity, lightLevel, 0, 0, biomes, drops);
        this.maxExp = this.minExp = MobHelper.getExpDrop(this);
    }

    public MobEntry(EntityLivingBase entity, LightLevel lightLevel, int exp, String[] biomes, LootDrop... drops)
    {
        this(entity, lightLevel, exp, exp, biomes, drops);
    }

    public MobEntry(EntityLivingBase entity, LightLevel lightLevel, int exp, LootDrop... drops)
    {
        this(entity, lightLevel, exp, exp, drops);
    }

    public MobEntry(EntityLivingBase entity, LightLevel lightLevel, int minExp, int maxExp, LootDrop... drops)
    {
        this.entity = entity;
        this.lightLevel = lightLevel;
        this.biomes = new ArrayList<>();
        this.biomes.add("Any");
        this.drops = new TreeSet<>();
        this.drops.addAll(Arrays.asList(drops));
        this.maxExp = maxExp;
        this.minExp = minExp;
    }

    public MobEntry(EntityLivingBase entity, LightLevel lightLevel, LootDrop... drops)
    {
        this(entity, lightLevel, 0, 0, drops);
        this.maxExp = this.minExp = MobHelper.getExpDrop(this);
    }

    public MobEntry(EntityLivingBase entity, LootTable lootTable)
    {
        this(entity, LightLevel.any);
        this.drops.addAll(LootHelper.toDrops(lootTable));
    }

    public EntityLivingBase getEntity()
    {
        return entity;
    }

    public String getMobName()
    {
        return MobHelper.getExpandedName(this);
    }

    public LootDrop[] getDrops()
    {
        return drops.toArray(new LootDrop[drops.size()]);
    }

    public List<ItemStack> getDropsItemStacks()
    {
        return this.drops.stream().map(LootDrop::getDrops).flatMap(List::stream).collect(Collectors.toList());
    }

    public String[] getBiomes()
    {
        return biomes.toArray(new String[biomes.size()]);
    }

    public boolean addDrop(LootDrop item)
    {
        for (LootDrop drop : drops)
            if (drop.item.isItemEqual(item.item)) return false;
        drops.add(item);
        return true;
    }

    public void addDrops(LootDrop... drops)
    {
        for (LootDrop drop : drops)
            addDrop(drop);
    }

    public LightLevel getLightLevel()
    {
        return lightLevel;
    }

    public String getExp()
    {
        return this.minExp + (this.maxExp == this.minExp ? "" : " - " + this.maxExp);
    }
}
