package jeresources.util;

import jeresources.api.drop.LootDrop;
import jeresources.compatibility.CompatBase;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.resources.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.packs.ModFileResourcePack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class LootTableHelper {
    private static final Map<DyeColor, ResourceLocation> sheepColors = new HashMap<>();

    static {
        sheepColors.put(DyeColor.WHITE, LootTables.ENTITIES_SHEEP_WHITE);
        sheepColors.put(DyeColor.ORANGE, LootTables.ENTITIES_SHEEP_ORANGE);
        sheepColors.put(DyeColor.MAGENTA, LootTables.ENTITIES_SHEEP_MAGENTA);
        sheepColors.put(DyeColor.LIGHT_BLUE, LootTables.ENTITIES_SHEEP_LIGHT_BLUE);
        sheepColors.put(DyeColor.YELLOW, LootTables.ENTITIES_SHEEP_YELLOW);
        sheepColors.put(DyeColor.LIME, LootTables.ENTITIES_SHEEP_LIME);
        sheepColors.put(DyeColor.PINK, LootTables.ENTITIES_SHEEP_PINK);
        sheepColors.put(DyeColor.GRAY, LootTables.ENTITIES_SHEEP_GRAY);
        sheepColors.put(DyeColor.LIGHT_GRAY, LootTables.ENTITIES_SHEEP_LIGHT_GRAY);
        sheepColors.put(DyeColor.CYAN, LootTables.ENTITIES_SHEEP_CYAN);
        sheepColors.put(DyeColor.PURPLE, LootTables.ENTITIES_SHEEP_PURPLE);
        sheepColors.put(DyeColor.BLUE, LootTables.ENTITIES_SHEEP_BLUE);
        sheepColors.put(DyeColor.BROWN, LootTables.ENTITIES_SHEEP_BROWN);
        sheepColors.put(DyeColor.GREEN, LootTables.ENTITIES_SHEEP_GREEN);
        sheepColors.put(DyeColor.RED, LootTables.ENTITIES_SHEEP_RED);
        sheepColors.put(DyeColor.BLACK, LootTables.ENTITIES_SHEEP_BLACK);
    }

    public static List<LootPool> getPools(LootTable table) {
        // net.minecraft.world.storage.loot.LootTable field_186466_c # pools
        return ReflectionHelper.getPrivateValue(LootTable.class, table, "field_186466_c");
    }

    public static List<LootEntry> getLootEntries(LootPool pool) {
        // net.minecraft.world.storage.loot.LootPool field_186453_a # lootEntries
        return ReflectionHelper.getPrivateValue(LootPool.class, pool, "field_186453_a");
    }

    public static List<ILootCondition> getLootConditions(LootPool pool) {
        // public net.minecraft.world.storage.loot.LootPool field_186454_b # conditions
        return ReflectionHelper.getPrivateValue(LootPool.class, pool, "field_186454_b");
    }

    public static List<LootDrop> toDrops(LootTable table) {
        List<LootDrop> drops = new ArrayList<>();

        final LootTableManager manager = getManager();

        getPools(table).forEach(
            pool -> {
                final float totalWeight = getLootEntries(pool).stream()
                    .filter(entry -> entry instanceof StandaloneLootEntry).map(entry -> (StandaloneLootEntry) entry)
                    .mapToInt(entry -> entry.weight).sum();
                final List<ILootCondition> poolConditions = getLootConditions(pool);
                getLootEntries(pool).stream()
                    .filter(entry -> entry instanceof ItemLootEntry).map(entry -> (ItemLootEntry) entry)
                    .map(entry -> new LootDrop(entry.item, entry.weight / totalWeight, entry.conditions, entry.functions))
                    .map(drop -> drop.addLootConditions(poolConditions))
                    .forEach(drops::add);

                getLootEntries(pool).stream()
                    .filter(entry -> entry instanceof TableLootEntry).map(entry -> (TableLootEntry) entry)
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

        chestTables.add(LootTables.CHESTS_END_CITY_TREASURE);
        chestTables.add(LootTables.CHESTS_SIMPLE_DUNGEON);
        chestTables.add(LootTables.CHESTS_VILLAGE_VILLAGE_WEAPONSMITH);
        chestTables.add(LootTables.CHESTS_VILLAGE_VILLAGE_TOOLSMITH);
        chestTables.add(LootTables.CHESTS_VILLAGE_VILLAGE_ARMORER);
        chestTables.add(LootTables.CHESTS_VILLAGE_VILLAGE_CARTOGRAPHER);
        chestTables.add(LootTables.CHESTS_VILLAGE_VILLAGE_MASON);
        chestTables.add(LootTables.CHESTS_VILLAGE_VILLAGE_SHEPHERD);
        chestTables.add(LootTables.CHESTS_VILLAGE_VILLAGE_BUTCHER);
        chestTables.add(LootTables.CHESTS_VILLAGE_VILLAGE_FLETCHER);
        chestTables.add(LootTables.CHESTS_VILLAGE_VILLAGE_FISHER);
        chestTables.add(LootTables.CHESTS_VILLAGE_VILLAGE_TANNERY);
        chestTables.add(LootTables.CHESTS_VILLAGE_VILLAGE_TEMPLE);
        chestTables.add(LootTables.CHESTS_VILLAGE_VILLAGE_DESERT_HOUSE);
        chestTables.add(LootTables.CHESTS_VILLAGE_VILLAGE_PLAINS_HOUSE);
        chestTables.add(LootTables.CHESTS_VILLAGE_VILLAGE_TAIGA_HOUSE);
        chestTables.add(LootTables.CHESTS_VILLAGE_VILLAGE_SNOWY_HOUSE);
        chestTables.add(LootTables.CHESTS_VILLAGE_VILLAGE_SAVANNA_HOUSE);
        chestTables.add(LootTables.CHESTS_ABANDONED_MINESHAFT);
        chestTables.add(LootTables.CHESTS_NETHER_BRIDGE);
        chestTables.add(LootTables.CHESTS_STRONGHOLD_LIBRARY);
        chestTables.add(LootTables.CHESTS_STRONGHOLD_CROSSING);
        chestTables.add(LootTables.CHESTS_STRONGHOLD_CORRIDOR);
        chestTables.add(LootTables.CHESTS_DESERT_PYRAMID);
        chestTables.add(LootTables.CHESTS_JUNGLE_TEMPLE);
        chestTables.add(LootTables.CHESTS_IGLOO_CHEST);
        chestTables.add(LootTables.CHESTS_WOODLAND_MANSION);
        chestTables.add(LootTables.CHESTS_UNDERWATER_RUIN_SMALL);
        chestTables.add(LootTables.CHESTS_UNDERWATER_RUIN_BIG);
        chestTables.add(LootTables.CHESTS_BURIED_TREASURE);
        chestTables.add(LootTables.CHESTS_SHIPWRECK_MAP);
        chestTables.add(LootTables.CHESTS_SHIPWRECK_SUPPLY);
        chestTables.add(LootTables.CHESTS_SHIPWRECK_TREASURE);
        chestTables.add(LootTables.CHESTS_PILLAGER_OUTPOST);

        return chestTables;
    }

    public static Map<LivingEntity, ResourceLocation> getAllMobLootTables(World world) {
        MobTableBuilder mobTableBuilder = new MobTableBuilder(world);

        for (Map.Entry<DyeColor, ResourceLocation> entry : sheepColors.entrySet()) {
            ResourceLocation lootTableList = entry.getValue();
            DyeColor dyeColor = entry.getKey();
            mobTableBuilder.addSheep(lootTableList, EntityType.SHEEP, dyeColor);
        }

        for (EntityType entityType : ForgeRegistries.ENTITIES) {
            if (entityType.getClassification() != EntityClassification.MISC && entityType != EntityType.SHEEP) {
                mobTableBuilder.add(entityType.getLootTable(), entityType);
            }
        }

        return mobTableBuilder.getMobTables();
    }

    private static LootTableManager manager;

    public static LootTableManager getManager(@Nullable World world) {
        if (world == null || world.getServer() == null) {
            if (manager == null) {
                manager = new LootTableManager(new LootPredicateManager());
                SimpleReloadableResourceManager serverResourceManger = new SimpleReloadableResourceManager(ResourcePackType.SERVER_DATA);
                List<IResourcePack> packs = new LinkedList<>();
                packs.add(new VanillaPack("minecraft"));
                for (ModFileInfo mod : ModList.get().getModFiles()) {
                    packs.add(new ModFileResourcePack(mod.getFile()));
                }
                packs.forEach(serverResourceManger::addResourcePack);
                serverResourceManger.addReloadListener(manager);
                CompletableFuture<Unit> completableFuture = serverResourceManger.reloadResourcesAndThen(Util.getServerExecutor(), Minecraft.getInstance(), packs, CompletableFuture.completedFuture(Unit.INSTANCE));
                Minecraft.getInstance().driveUntil(completableFuture::isDone);
            }
            return manager;
        }
        return world.getServer().getLootTableManager();
    }

    public static LootTableManager getManager() {
        return getManager(CompatBase.getWorld());
    }
}
