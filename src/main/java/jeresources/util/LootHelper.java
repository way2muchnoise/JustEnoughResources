package jeresources.util;

import jeresources.api.drop.LootDrop;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;

import java.util.*;

public class LootHelper
{
    public static List<LootDrop> toDrops(LootTable table)
    {
        List<LootDrop> drops = new ArrayList<>();

        Arrays.stream(table.pools).forEach(
            pool -> {
                final float totalWeight = Arrays.stream(pool.lootEntries).parallel().mapToInt(entry -> entry.weight).sum();
                Arrays.stream(pool.lootEntries).parallel()
                    .filter(entry -> entry instanceof LootEntryItem).map(entry -> (LootEntryItem)entry)
                    .map(entry -> new LootDrop(entry.item, entry.weight / totalWeight, entry.functions)).forEach(drops::add);
            }
        );

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
        mobTables.put(LootTableList.ENTITIES_ZOMBIE_HORSE, new EntityHorse(world){{setType(HorseArmorType.ZOMBIE);}});
        mobTables.put(LootTableList.ENTITIES_SKELETON_HORSE, new EntityHorse(world){{setType(HorseArmorType.SKELETON);}});
        mobTables.put(LootTableList.ENTITIES_COW, new EntityCow(world));
        mobTables.put(LootTableList.ENTITIES_MUSHROOM_COW, new EntityMooshroom(world));
        mobTables.put(LootTableList.ENTITIES_WOLF, new EntityWolf(world));
        mobTables.put(LootTableList.ENTITIES_OCELOT, new EntityOcelot(world));
        mobTables.put(LootTableList.ENTITIES_SHEEP, new EntitySheep(world));
        mobTables.put(LootTableList.ENTITIES_SHEEP_WHITE, new EntitySheep(world){{setFleeceColor(EnumDyeColor.WHITE);}});
        mobTables.put(LootTableList.ENTITIES_SHEEP_ORANGE, new EntitySheep(world){{setFleeceColor(EnumDyeColor.ORANGE);}});
        mobTables.put(LootTableList.ENTITIES_SHEEP_MAGENTA, new EntitySheep(world){{setFleeceColor(EnumDyeColor.MAGENTA);}});
        mobTables.put(LootTableList.ENTITIES_SHEEP_LIGHT_BLUE, new EntitySheep(world){{setFleeceColor(EnumDyeColor.LIGHT_BLUE);}});
        mobTables.put(LootTableList.ENTITIES_SHEEP_YELLOW, new EntitySheep(world){{setFleeceColor(EnumDyeColor.YELLOW);}});
        mobTables.put(LootTableList.ENTITIES_SHEEP_LIME, new EntitySheep(world){{setFleeceColor(EnumDyeColor.LIME);}});
        mobTables.put(LootTableList.ENTITIES_SHEEP_PINK, new EntitySheep(world){{setFleeceColor(EnumDyeColor.PINK);}});
        mobTables.put(LootTableList.ENTITIES_SHEEP_GRAY, new EntitySheep(world){{setFleeceColor(EnumDyeColor.GRAY);}});
        mobTables.put(LootTableList.ENTITIES_SHEEP_SILVER, new EntitySheep(world){{setFleeceColor(EnumDyeColor.SILVER);}});
        mobTables.put(LootTableList.ENTITIES_SHEEP_CYAN, new EntitySheep(world){{setFleeceColor(EnumDyeColor.CYAN);}});
        mobTables.put(LootTableList.ENTITIES_SHEEP_PURPLE, new EntitySheep(world){{setFleeceColor(EnumDyeColor.PURPLE);}});
        mobTables.put(LootTableList.ENTITIES_SHEEP_BLUE, new EntitySheep(world){{setFleeceColor(EnumDyeColor.BLUE);}});
        mobTables.put(LootTableList.ENTITIES_SHEEP_BROWN, new EntitySheep(world){{setFleeceColor(EnumDyeColor.BROWN);}});
        mobTables.put(LootTableList.ENTITIES_SHEEP_GREEN, new EntitySheep(world){{setFleeceColor(EnumDyeColor.GREEN);}});
        mobTables.put(LootTableList.ENTITIES_SHEEP_RED, new EntitySheep(world){{setFleeceColor(EnumDyeColor.RED);}});
        mobTables.put(LootTableList.ENTITIES_SHEEP_BLACK, new EntitySheep(world){{setFleeceColor(EnumDyeColor.BLACK);}});
        mobTables.put(LootTableList.ENTITIES_BAT, new EntityBat(world));
        mobTables.put(LootTableList.ENTITIES_SLIME, new EntitySlime(world));
        mobTables.put(LootTableList.ENTITIES_MAGMA_CUBE, new EntityMagmaCube(world));
        mobTables.put(LootTableList.ENTITIES_GHAST, new EntityGhast(world));
        mobTables.put(LootTableList.ENTITIES_SQUID, new EntitySquid(world));
        mobTables.put(LootTableList.ENTITIES_ENDERMITE, new EntityEndermite(world));
        mobTables.put(LootTableList.ENTITIES_ZOMBIE, new EntityZombie(world));
        mobTables.put(LootTableList.ENTITIES_ZOMBIE_PIGMAN, new EntityPigZombie(world));
        mobTables.put(LootTableList.ENTITIES_SKELETON, new EntitySkeleton(world));
        mobTables.put(LootTableList.ENTITIES_WITHER_SKELETON, new EntitySkeleton(world){{setSkeletonType(1);}});

        return mobTables;
    }
}
