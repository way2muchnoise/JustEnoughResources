package jeresources.util;

import jeresources.api.drop.LootDrop;
import jeresources.compatibility.CompatBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;

import javax.annotation.Nullable;
import java.io.File;
import java.util.*;

public class LootTableHelper {
    private static final Map<EnumDyeColor, ResourceLocation> sheepColors = new HashMap<>();

    static {
        sheepColors.put(EnumDyeColor.WHITE, LootTableList.ENTITIES_SHEEP_WHITE);
        sheepColors.put(EnumDyeColor.ORANGE, LootTableList.ENTITIES_SHEEP_ORANGE);
        sheepColors.put(EnumDyeColor.MAGENTA, LootTableList.ENTITIES_SHEEP_MAGENTA);
        sheepColors.put(EnumDyeColor.LIGHT_BLUE, LootTableList.ENTITIES_SHEEP_LIGHT_BLUE);
        sheepColors.put(EnumDyeColor.YELLOW, LootTableList.ENTITIES_SHEEP_YELLOW);
        sheepColors.put(EnumDyeColor.LIME, LootTableList.ENTITIES_SHEEP_LIME);
        sheepColors.put(EnumDyeColor.PINK, LootTableList.ENTITIES_SHEEP_PINK);
        sheepColors.put(EnumDyeColor.GRAY, LootTableList.ENTITIES_SHEEP_GRAY);
        sheepColors.put(EnumDyeColor.SILVER, LootTableList.ENTITIES_SHEEP_SILVER);
        sheepColors.put(EnumDyeColor.CYAN, LootTableList.ENTITIES_SHEEP_CYAN);
        sheepColors.put(EnumDyeColor.PURPLE, LootTableList.ENTITIES_SHEEP_PURPLE);
        sheepColors.put(EnumDyeColor.BLUE, LootTableList.ENTITIES_SHEEP_BLUE);
        sheepColors.put(EnumDyeColor.BROWN, LootTableList.ENTITIES_SHEEP_BROWN);
        sheepColors.put(EnumDyeColor.GREEN, LootTableList.ENTITIES_SHEEP_GREEN);
        sheepColors.put(EnumDyeColor.RED, LootTableList.ENTITIES_SHEEP_RED);
        sheepColors.put(EnumDyeColor.BLACK, LootTableList.ENTITIES_SHEEP_BLACK);
    }

    public static List<LootPool> getPools(LootTable table) {
        return ReflectionHelper.getPrivateValue(LootTable.class, table, "pools", "field_186466_c");
    }

    public static List<LootEntry> getEntries(LootPool pool) {
        return ReflectionHelper.getPrivateValue(LootPool.class, pool, "lootEntries", "field_186453_a");
    }

    public static List<LootCondition> getConditions(LootPool pool) {
        return ReflectionHelper.getPrivateValue(LootPool.class, pool, "poolConditions", "field_186454_b");
    }

    public static Item getItem(LootEntryItem lootEntry) {
        return ReflectionHelper.getPrivateValue(LootEntryItem.class, lootEntry, "item", "field_186368_a");
    }

    public static LootFunction[] getFunctions(LootEntryItem lootEntry) {
        return ReflectionHelper.getPrivateValue(LootEntryItem.class, lootEntry, "functions", "field_186369_b");
    }

    public static List<LootDrop> toDrops(LootTable table) {
        List<LootDrop> drops = new ArrayList<>();

        final LootTableManager manager = getManager();

        getPools(table).forEach(
            pool -> {
                final float totalWeight = getEntries(pool).stream().mapToInt(entry -> entry.getEffectiveWeight(0)).sum();
                final List<LootCondition> poolConditions = getConditions(pool);
                getEntries(pool).stream()
                    .filter(entry -> entry instanceof LootEntryItem).map(entry -> (LootEntryItem) entry)
                    .map(entry -> new LootDrop(getItem(entry), entry.getEffectiveWeight(0) / totalWeight, entry.conditions, getFunctions(entry)))
                    .map(drop -> drop.addLootConditions(poolConditions))
                    .forEach(drops::add);

                getEntries(pool).stream()
                    .filter(entry -> entry instanceof LootEntryTable).map(entry -> (LootEntryTable) entry)
                    .map(entry -> toDrops(manager.getLootTableFromLocation(entry.table))).forEach(drops::addAll);
            }
        );

        drops.removeIf(Objects::isNull);
        return drops;
    }

    public static List<LootDrop> toDrops(World world, ResourceLocation lootTable) {
        return toDrops(getManager(world).getLootTableFromLocation(lootTable));
    }

    public static List<ResourceLocation> getAllChestLootTablesResourceLocations() {
        ArrayList<ResourceLocation> chestTables = new ArrayList<>();

        chestTables.add(LootTableList.CHESTS_END_CITY_TREASURE);
        chestTables.add(LootTableList.CHESTS_SIMPLE_DUNGEON);
        chestTables.add(LootTableList.CHESTS_VILLAGE_BLACKSMITH);
        chestTables.add(LootTableList.CHESTS_ABANDONED_MINESHAFT);
        chestTables.add(LootTableList.CHESTS_NETHER_BRIDGE);
        chestTables.add(LootTableList.CHESTS_STRONGHOLD_LIBRARY);
        chestTables.add(LootTableList.CHESTS_STRONGHOLD_CROSSING);
        chestTables.add(LootTableList.CHESTS_STRONGHOLD_CORRIDOR);
        chestTables.add(LootTableList.CHESTS_DESERT_PYRAMID);
        chestTables.add(LootTableList.CHESTS_JUNGLE_TEMPLE);
        chestTables.add(LootTableList.CHESTS_IGLOO_CHEST);
        chestTables.add(LootTableList.CHESTS_WOODLAND_MANSION);

        return chestTables;
    }

    public static Map<ResourceLocation, EntityLivingBase> getAllMobLootTables(World world) {
        MobTableBuilder mobTableBuilder = new MobTableBuilder(world);

        mobTableBuilder.add(LootTableList.ENTITIES_WITCH, EntityWitch.class);
        mobTableBuilder.add(LootTableList.ENTITIES_BLAZE, EntityBlaze.class);
        mobTableBuilder.add(LootTableList.ENTITIES_CREEPER, EntityCreeper.class);
        mobTableBuilder.add(LootTableList.ENTITIES_SPIDER, EntitySpider.class);
        mobTableBuilder.add(LootTableList.ENTITIES_CAVE_SPIDER, EntityCaveSpider.class);
        mobTableBuilder.add(LootTableList.ENTITIES_GIANT, EntityGiantZombie.class);
        mobTableBuilder.add(LootTableList.ENTITIES_SILVERFISH, EntitySilverfish.class);
        mobTableBuilder.add(LootTableList.ENTITIES_ENDERMAN, EntityEnderman.class);
        mobTableBuilder.add(LootTableList.ENTITIES_GUARDIAN, EntityGuardian.class);
        mobTableBuilder.add(LootTableList.ENTITIES_ELDER_GUARDIAN, EntityElderGuardian.class);
        mobTableBuilder.add(LootTableList.ENTITIES_SHULKER, EntityShulker.class);
        mobTableBuilder.add(LootTableList.ENTITIES_IRON_GOLEM, EntityIronGolem.class);
        mobTableBuilder.add(LootTableList.ENTITIES_SNOWMAN, EntitySnowman.class);
        mobTableBuilder.add(LootTableList.ENTITIES_RABBIT, EntityRabbit.class);
        mobTableBuilder.add(LootTableList.ENTITIES_CHICKEN, EntityChicken.class);
        mobTableBuilder.add(LootTableList.ENTITIES_PIG, EntityPig.class);
        mobTableBuilder.add(LootTableList.ENTITIES_HORSE, EntityHorse.class);
        mobTableBuilder.add(LootTableList.ENTITIES_DONKEY, EntityDonkey.class);
        mobTableBuilder.add(LootTableList.ENTITIES_MULE, EntityMule.class);
        mobTableBuilder.add(LootTableList.ENTITIES_ZOMBIE_HORSE, EntityZombieHorse.class);
        mobTableBuilder.add(LootTableList.ENTITIES_SKELETON_HORSE, EntitySkeletonHorse.class);
        mobTableBuilder.add(LootTableList.ENTITIES_COW, EntityCow.class);
        mobTableBuilder.add(LootTableList.ENTITIES_MUSHROOM_COW, EntityMooshroom.class);
        mobTableBuilder.add(LootTableList.ENTITIES_WOLF, EntityWolf.class);
        mobTableBuilder.add(LootTableList.ENTITIES_OCELOT, EntityOcelot.class);

        for (Map.Entry<EnumDyeColor, ResourceLocation> entry : sheepColors.entrySet()) {
            ResourceLocation lootTableList = entry.getValue();
            EnumDyeColor dyeColor = entry.getKey();
            mobTableBuilder.add(lootTableList, EntitySheep.class, entity -> entity.setFleeceColor(dyeColor));
        }

        mobTableBuilder.add(LootTableList.ENTITIES_BAT, EntityBat.class);
        mobTableBuilder.add(LootTableList.ENTITIES_SLIME, EntitySlime.class);
        mobTableBuilder.add(LootTableList.ENTITIES_MAGMA_CUBE, EntityMagmaCube.class);
        mobTableBuilder.add(LootTableList.ENTITIES_GHAST, EntityGhast.class);
        mobTableBuilder.add(LootTableList.ENTITIES_SQUID, EntitySquid.class);
        mobTableBuilder.add(LootTableList.ENTITIES_ENDERMITE, EntityEndermite.class);
        mobTableBuilder.add(LootTableList.ENTITIES_ZOMBIE, EntityZombie.class);
        mobTableBuilder.add(LootTableList.ENTITIES_ZOMBIE_PIGMAN, EntityPigZombie.class);
        mobTableBuilder.add(LootTableList.ENTITIES_SKELETON, EntitySkeleton.class);
        mobTableBuilder.add(LootTableList.ENTITIES_WITHER_SKELETON, EntityWitherSkeleton.class);
        mobTableBuilder.add(LootTableList.ENTITIES_STRAY, EntityStray.class);
        mobTableBuilder.add(LootTableList.ENTITIES_HUSK, EntityHusk.class);
        mobTableBuilder.add(LootTableList.ENTITIES_ZOMBIE_VILLAGER, EntityZombieVillager.class);
        mobTableBuilder.add(LootTableList.ENTITIES_VILLAGER, EntityVillager.class);
        mobTableBuilder.add(LootTableList.ENTITIES_EVOCATION_ILLAGER, EntityEvoker.class);
        mobTableBuilder.add(LootTableList.ENTITIES_VINDICATION_ILLAGER, EntityVindicator.class);
        mobTableBuilder.add(LootTableList.ENTITIES_LLAMA, EntityLlama.class);
        mobTableBuilder.add(LootTableList.ENTITIES_VEX, EntityVex.class);
        mobTableBuilder.add(LootTableList.ENTITIES_ENDER_DRAGON, EntityDragon.class);

        return mobTableBuilder.getMobTables();
    }

    private static LootTableManager manager;

    public static LootTableManager getManager(@Nullable World world) {
        if (world == null || world.getLootTableManager() == null) {
            if (manager == null) {
                ISaveHandler saveHandler = FakeClientWorld.saveHandler;
                manager = new LootTableManager(new File(new File(saveHandler.getWorldDirectory(), "data"), "loot_tables"));
            }
            return manager;
        }
        return world.getLootTableManager();
    }

    public static LootTableManager getManager() {
        return getManager(CompatBase.getWorld());
    }
}
