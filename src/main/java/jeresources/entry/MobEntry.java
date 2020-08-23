package jeresources.entry;

import jeresources.api.conditionals.LightLevel;
import jeresources.api.drop.LootDrop;
import jeresources.util.LootTableHelper;
import jeresources.util.MobHelper;
import jeresources.util.TranslationHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;

import java.util.*;
import java.util.stream.Collectors;

public class MobEntry {
    private LivingEntity entity;
    private Set<LootDrop> drops;
    private LightLevel lightLevel;
    private List<String> biomes;
    private int minExp, maxExp;

    public MobEntry(LivingEntity entity, LightLevel lightLevel, int minExp, int maxExp, String[] biomes, LootDrop... drops) {
        this.entity = entity;
        this.lightLevel = lightLevel;
        this.biomes = new ArrayList<>();
        this.biomes.addAll(Arrays.asList(biomes));
        this.drops = new TreeSet<>();
        this.drops.addAll(Arrays.asList(drops));
        this.maxExp = maxExp;
        this.minExp = minExp;
    }

    public MobEntry(LivingEntity entity, LightLevel lightLevel, String[] biomes, LootDrop... drops) {
        this(entity, lightLevel, 0, 0, biomes, drops);
        this.maxExp = this.minExp = MobHelper.getExpDrop(this);
    }

    public MobEntry(LivingEntity entity, LightLevel lightLevel, int exp, String[] biomes, LootDrop... drops) {
        this(entity, lightLevel, exp, exp, biomes, drops);
    }

    public MobEntry(LivingEntity entity, LightLevel lightLevel, int exp, LootDrop... drops) {
        this(entity, lightLevel, exp, exp, drops);
    }

    public MobEntry(LivingEntity entity, LightLevel lightLevel, int minExp, int maxExp, LootDrop... drops) {
        this.entity = entity;
        this.lightLevel = lightLevel;
        this.biomes = new ArrayList<>();
        this.biomes.add("jer.any");
        this.drops = new TreeSet<>();
        this.drops.addAll(Arrays.asList(drops));
        this.maxExp = maxExp;
        this.minExp = minExp;
    }

    public MobEntry(LivingEntity entity, LightLevel lightLevel, LootDrop... drops) {
        this(entity, lightLevel, 0, 0, drops);
        this.maxExp = this.minExp = MobHelper.getExpDrop(this);
    }

    public MobEntry(LivingEntity entity, LootDrop... drops) {
        this(entity, LightLevel.any, drops);
    }

    public MobEntry(LivingEntity entity, LootTable lootTable) {
        this(entity, LightLevel.any);
        this.drops.addAll(LootTableHelper.toDrops(lootTable));
    }

    public MobEntry(LivingEntity entity) {
        this(entity, LightLevel.any);
    }

    public LivingEntity getEntity() {
        return entity;
    }

    public String getMobName() {
        return MobHelper.getExpandedName(this);
    }

    public LootDrop[] getDrops() {
        return drops.toArray(new LootDrop[drops.size()]);
    }

    public List<ItemStack> getDropsItemStacks() {
        return this.drops.stream().map(LootDrop::getDrops).flatMap(List::stream).collect(Collectors.toList());
    }

    public String[] getBiomes() {
        String[] translatedBiomes = new String[biomes.size()];
        for (int i = 0; i < biomes.size(); i++) {
            translatedBiomes[i] = TranslationHelper.translateAndFormat(biomes.get(i));
        }
        return translatedBiomes;
    }

    public boolean addDrop(LootDrop item) {
        for (LootDrop drop : drops)
            if (drop.item.isItemEqual(item.item)) return false;
        drops.add(item);
        return true;
    }

    public void addDrops(LootDrop... drops) {
        for (LootDrop drop : drops)
            addDrop(drop);
    }

    public void addDrops(Collection<LootDrop> drops) {
        drops.stream().filter(Objects::nonNull).forEach(this::addDrop);
    }

    public LightLevel getLightLevel() {
        return lightLevel;
    }

    public String getExp() {
        return this.minExp + (this.maxExp == this.minExp ? "" : " - " + this.maxExp);
    }

    public void setLightLevel(LightLevel lightLevel) {
        this.lightLevel = lightLevel;
    }

    public void setMinExp(int xp) {
        this.minExp = xp;
    }

    public void setMaxExp(int xp) {
        this.maxExp = xp;
    }
}
