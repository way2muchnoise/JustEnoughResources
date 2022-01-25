package jeresources.entry;

import jeresources.api.conditionals.LightLevel;
import jeresources.api.drop.LootDrop;
import jeresources.compatibility.minecraft.ExperienceRange;
import jeresources.compatibility.minecraft.MobCompat;
import jeresources.util.LootTableHelper;
import jeresources.util.MobHelper;
import jeresources.util.TranslationHelper;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class MobEntry {
    private static final List<String> ANY_BIOMES = List.of("jer.any");

    private static List<String> distinctBiomes(String... biomes) {
        return Arrays.stream(biomes).distinct().toList();
    }

    private static List<LootDrop> distinctDrops(LootDrop... drops) {
        return distinctDrops(Arrays.stream(drops));
    }

    private static List<LootDrop> distinctDrops(Stream<LootDrop> dropsStream) {
        Set<Item> seen =  Collections.newSetFromMap(new IdentityHashMap<>());
        return dropsStream
                .filter(drop -> drop != null && drop.item != null && seen.add(drop.item.getItem()))
                .toList();
    }

    public static MobEntry create(Supplier<LivingEntity> entity, LightLevel lightLevel, int minExp, int maxExp, String[] biomes, LootDrop... drops) {
        return new MobEntry(entity, lightLevel, new ExperienceRange(minExp, maxExp), distinctBiomes(biomes), distinctDrops(drops));
    }

    public static MobEntry create(Supplier<LivingEntity> entity, LightLevel lightLevel, String[] biomes, LootDrop... drops) {
        return new MobEntry(entity, lightLevel, null, distinctBiomes(biomes), distinctDrops(drops));
    }

    public static MobEntry create(Supplier<LivingEntity> entity, LightLevel lightLevel, int exp, String[] biomes, LootDrop... drops) {
        return new MobEntry(entity, lightLevel, new ExperienceRange(exp, exp), distinctBiomes(biomes), distinctDrops(drops));
    }

    public static MobEntry create(Supplier<LivingEntity> entity, LightLevel lightLevel, int exp, LootDrop... drops) {
        return new MobEntry(entity, lightLevel, new ExperienceRange(exp, exp), ANY_BIOMES, distinctDrops(drops));
    }

    public static MobEntry create(Supplier<LivingEntity> entity, LightLevel lightLevel, int minExp, int maxExp, LootDrop... drops) {
        return new MobEntry(entity, lightLevel, new ExperienceRange(minExp, maxExp), ANY_BIOMES, distinctDrops(drops));
    }

    public static MobEntry create(Supplier<LivingEntity> entity, LightLevel lightLevel, LootDrop... drops) {
        return new MobEntry(entity, lightLevel, null, ANY_BIOMES, distinctDrops(drops));
    }

    public static MobEntry create(Supplier<LivingEntity> entity, LootDrop... drops) {
        return new MobEntry(entity, null, null, ANY_BIOMES, distinctDrops(drops));
    }

    public static MobEntry create(Supplier<LivingEntity> entity, LootTable lootTable) {
        List<LootDrop> drops = LootTableHelper.toDrops(lootTable);
        return new MobEntry(entity, null, null, ANY_BIOMES, drops);
    }

    public static MobEntry create(Supplier<LivingEntity> entity) {
        List<LootDrop> drops = new ArrayList<>();
        return new MobEntry(entity, null, null, ANY_BIOMES, drops);
    }

    private final List<String> biomes;
    private final Supplier<LivingEntity> entitySupplier;
    private @Nullable LivingEntity entity;
    private @Nullable LightLevel lightLevel;
    private @Nullable ExperienceRange experience;
    private List<LootDrop> drops;

    private MobEntry(Supplier<LivingEntity> entitySupplier, @Nullable LightLevel lightLevel, @Nullable ExperienceRange experience, List<String> biomes, List<LootDrop> drops) {
        this.entitySupplier = entitySupplier;
        this.entity = null;
        this.lightLevel = lightLevel;
        this.biomes = biomes;
        this.drops = drops;
        this.experience = experience;
    }

    public LivingEntity getEntity() {
        if (this.entity == null) {
            this.entity = this.entitySupplier.get();
        }
        return entity;
    }

    public String getMobName() {
        LivingEntity entity = getEntity();
        return MobHelper.getExpandedName(entity);
    }

    public List<LootDrop> getDrops() {
        return this.drops;
    }

    public List<ItemStack> getDropsItemStacks() {
        return this.drops.stream().map(LootDrop::getDrops).flatMap(List::stream).toList();
    }

    public boolean hasMultipleBiomes() {
        return this.biomes.size() > 1;
    }

    public Stream<String> getTranslatedBiomes() {
        return this.biomes.stream().map(TranslationHelper::translateAndFormat);
    }

    public void setDrops(Collection<LootDrop> drops) {
        this.drops = distinctDrops(drops.stream());
    }

    public LightLevel getLightLevel() {
        if (this.lightLevel == null) {
            LivingEntity entity = getEntity();
            this.lightLevel = MobCompat.getLightLevel(entity);
        }
        return this.lightLevel;
    }

    public String getExp() {
        if (this.experience == null) {
            LivingEntity entity = getEntity();
            this.experience = MobCompat.getExperience(entity);
        }
        return this.experience.getExpString();
    }
}
