package jeresources.util;

import jeresources.api.drop.LootDrop;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.functions.LootFunction;

import java.io.File;
import java.util.*;

public class LootHelper
{
    public static List<LootPool> getPools(LootTable table)
    {
        return ReflectionHelper.getPrivateValue(LootTable.class, table, "pools", "field_186466_c");
    }

    public static List<LootEntry> getEntries(LootPool pool)
    {
        return ReflectionHelper.getPrivateValue(LootPool.class, pool, "lootEntries", "field_186453_a");
    }

    public static Item getItem(LootEntryItem lootEntry)
    {
        return ReflectionHelper.getPrivateValue(LootEntryItem.class, lootEntry, "item", "field_186368_a");
    }

    public static LootFunction[] getFunctions(LootEntryItem lootEntry)
    {
        return ReflectionHelper.getPrivateValue(LootEntryItem.class, lootEntry, "functions", "field_186369_b");
    }

    public static List<LootDrop> toDrops(LootTable table)
    {
        List<LootDrop> drops = new ArrayList<>();

        getPools(table).forEach(
            pool -> {
                final float totalWeight = getEntries(pool).stream().mapToInt(entry -> entry.getEffectiveWeight(0)).sum();
                getEntries(pool).stream()
                    .filter(entry -> entry instanceof LootEntryItem).map(entry -> (LootEntryItem)entry)
                    .map(entry -> new LootDrop(getItem(entry), entry.getEffectiveWeight(0) / totalWeight, getFunctions(entry))).forEach(drops::add);

                getEntries(pool).stream()
                        .filter(entry -> entry instanceof LootEntryTable).map(entry -> (LootEntryTable)entry)
                        .map(entry -> toDrops(manager.getLootTableFromLocation(entry.table))).forEach(drops::addAll);
            }
        );

        drops.removeIf(Objects::isNull);
        return drops;
    }

    public static List<ResourceLocation> getAllChestLootTablesResourceLocations()
    {
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

        return chestTables;
    }

    public static Map<ResourceLocation, EntityLivingBase> getAllMobLootTables(World world)
    {
        Map<ResourceLocation, EntityLivingBase> mobTables = new HashMap<>();

        mobTables.put(LootTableList.ENTITIES_WITCH, new EntityWitch(world));
        mobTables.put(LootTableList.ENTITIES_BLAZE, new EntityBlaze(world));
        mobTables.put(LootTableList.ENTITIES_CREEPER, new EntityCreeper(world));
        mobTables.put(LootTableList.ENTITIES_SPIDER, new EntitySpider(world));
        mobTables.put(LootTableList.ENTITIES_CAVE_SPIDER, new EntityCaveSpider(world));
        mobTables.put(LootTableList.ENTITIES_GIANT, new EntityGiantZombie(world));
        mobTables.put(LootTableList.ENTITIES_SILVERFISH, new EntitySilverfish(world));
        mobTables.put(LootTableList.ENTITIES_ENDERMAN, new EntityEnderman(world));
        mobTables.put(LootTableList.ENTITIES_GUARDIAN, new EntityGuardian(world));
        mobTables.put(LootTableList.ENTITIES_ELDER_GUARDIAN, new EntityGuardian(world){{setElder();}});
        mobTables.put(LootTableList.ENTITIES_SHULKER, new EntityShulker(world));
        mobTables.put(LootTableList.ENTITIES_IRON_GOLEM, new EntityIronGolem(world));
        mobTables.put(LootTableList.ENTITIES_SNOWMAN, new EntitySnowman(world));
        mobTables.put(LootTableList.ENTITIES_RABBIT, new EntityRabbit(world));
        mobTables.put(LootTableList.ENTITIES_CHICKEN, new EntityChicken(world));
        mobTables.put(LootTableList.ENTITIES_PIG, new EntityPig(world));
        mobTables.put(LootTableList.ENTITIES_HORSE, new EntityHorse(world));
        mobTables.put(LootTableList.ENTITIES_ZOMBIE_HORSE, new EntityHorse(world){{setType(HorseType.ZOMBIE);}});
        mobTables.put(LootTableList.ENTITIES_SKELETON_HORSE, new EntityHorse(world){{setType(HorseType.SKELETON);}});
        mobTables.put(LootTableList.ENTITIES_COW, new EntityCow(world));
        mobTables.put(LootTableList.ENTITIES_MUSHROOM_COW, new EntityMooshroom(world));
        mobTables.put(LootTableList.ENTITIES_WOLF, new EntityWolf(world));
        mobTables.put(LootTableList.ENTITIES_OCELOT, new EntityOcelot(world));
        mobTables.put(LootTableList.ENTITIES_SHEEP, new EntitySheep(world));
        EntitySheep whiteSheep = new EntitySheep(world);
        whiteSheep.setFleeceColor(EnumDyeColor.WHITE);
        mobTables.put(LootTableList.ENTITIES_SHEEP_WHITE, whiteSheep);
        EntitySheep orangeSheep = new EntitySheep(world);
        orangeSheep.setFleeceColor(EnumDyeColor.ORANGE);
        mobTables.put(LootTableList.ENTITIES_SHEEP_ORANGE, orangeSheep);
        EntitySheep magentaSheep = new EntitySheep(world);
        magentaSheep.setFleeceColor(EnumDyeColor.MAGENTA);
        mobTables.put(LootTableList.ENTITIES_SHEEP_MAGENTA, magentaSheep);
        EntitySheep lightBlueSheep = new EntitySheep(world);
        lightBlueSheep.setFleeceColor(EnumDyeColor.LIGHT_BLUE);
        mobTables.put(LootTableList.ENTITIES_SHEEP_LIGHT_BLUE, lightBlueSheep);
        EntitySheep yellowSheep = new EntitySheep(world);
        yellowSheep.setFleeceColor(EnumDyeColor.YELLOW);
        mobTables.put(LootTableList.ENTITIES_SHEEP_YELLOW, yellowSheep);
        EntitySheep limeSheep = new EntitySheep(world);
        limeSheep.setFleeceColor(EnumDyeColor.LIME);
        mobTables.put(LootTableList.ENTITIES_SHEEP_LIME, limeSheep);
        EntitySheep pinkSheep = new EntitySheep(world);
        pinkSheep.setFleeceColor(EnumDyeColor.PINK);
        mobTables.put(LootTableList.ENTITIES_SHEEP_PINK, pinkSheep);
        EntitySheep graySheep = new EntitySheep(world);
        graySheep.setFleeceColor(EnumDyeColor.GRAY);
        mobTables.put(LootTableList.ENTITIES_SHEEP_GRAY, graySheep);
        EntitySheep silverSheep = new EntitySheep(world);
        silverSheep.setFleeceColor(EnumDyeColor.SILVER);
        mobTables.put(LootTableList.ENTITIES_SHEEP_SILVER, silverSheep);
        EntitySheep cyanSheep = new EntitySheep(world);
        cyanSheep.setFleeceColor(EnumDyeColor.CYAN);
        mobTables.put(LootTableList.ENTITIES_SHEEP_CYAN, cyanSheep);
        EntitySheep purpleSheep = new EntitySheep(world);
        purpleSheep.setFleeceColor(EnumDyeColor.PURPLE);
        mobTables.put(LootTableList.ENTITIES_SHEEP_PURPLE, purpleSheep);
        EntitySheep blueSheep = new EntitySheep(world);
        blueSheep.setFleeceColor(EnumDyeColor.BLUE);
        mobTables.put(LootTableList.ENTITIES_SHEEP_BLUE, blueSheep);
        EntitySheep brownSheep = new EntitySheep(world);
        brownSheep.setFleeceColor(EnumDyeColor.BROWN);
        mobTables.put(LootTableList.ENTITIES_SHEEP_BROWN, brownSheep);
        EntitySheep greenSheep = new EntitySheep(world);
        greenSheep.setFleeceColor(EnumDyeColor.GREEN);
        mobTables.put(LootTableList.ENTITIES_SHEEP_GREEN, greenSheep);
        EntitySheep redSheep = new EntitySheep(world);
        redSheep.setFleeceColor(EnumDyeColor.RED);
        mobTables.put(LootTableList.ENTITIES_SHEEP_RED, redSheep);
        EntitySheep blackSheep = new EntitySheep(world);
        blackSheep.setFleeceColor(EnumDyeColor.BLACK);
        mobTables.put(LootTableList.ENTITIES_SHEEP_BLACK, blackSheep);
        mobTables.put(LootTableList.ENTITIES_BAT, new EntityBat(world));
        mobTables.put(LootTableList.ENTITIES_SLIME, new EntitySlime(world));
        mobTables.put(LootTableList.ENTITIES_MAGMA_CUBE, new EntityMagmaCube(world));
        mobTables.put(LootTableList.ENTITIES_GHAST, new EntityGhast(world));
        mobTables.put(LootTableList.ENTITIES_SQUID, new EntitySquid(world));
        mobTables.put(LootTableList.ENTITIES_ENDERMITE, new EntityEndermite(world));
        mobTables.put(LootTableList.ENTITIES_ZOMBIE, new EntityZombie(world));
        mobTables.put(LootTableList.ENTITIES_ZOMBIE_PIGMAN, new EntityPigZombie(world));
        mobTables.put(LootTableList.ENTITIES_SKELETON, new EntitySkeleton(world));
        mobTables.put(LootTableList.ENTITIES_WITHER_SKELETON, new EntitySkeleton(world){{func_189768_a(SkeletonType.WITHER);}});

        return mobTables;
    }

    private static LootTableManager manager;

    public static LootTableManager getManager(World world)
    {
        if (world.getLootTableManager() == null)
        {
            if(manager == null)
                manager = new LootTableManager(new File(new File(world.getSaveHandler().getWorldDirectory(), "data"), "loot_tables"));
            return manager;
        }
        return world.getLootTableManager();
    }
}
