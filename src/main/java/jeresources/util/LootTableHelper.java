package jeresources.util;

import jeresources.api.drop.LootDrop;
import jeresources.compatibility.CompatBase;
import jeresources.config.ConfigValues;
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
        sheepColors.put(DyeColor.WHITE, LootTables.SHEEP_WHITE);
        sheepColors.put(DyeColor.ORANGE, LootTables.SHEEP_ORANGE);
        sheepColors.put(DyeColor.MAGENTA, LootTables.SHEEP_MAGENTA);
        sheepColors.put(DyeColor.LIGHT_BLUE, LootTables.SHEEP_LIGHT_BLUE);
        sheepColors.put(DyeColor.YELLOW, LootTables.SHEEP_YELLOW);
        sheepColors.put(DyeColor.LIME, LootTables.SHEEP_LIME);
        sheepColors.put(DyeColor.PINK, LootTables.SHEEP_PINK);
        sheepColors.put(DyeColor.GRAY, LootTables.SHEEP_GRAY);
        sheepColors.put(DyeColor.LIGHT_GRAY, LootTables.SHEEP_LIGHT_GRAY);
        sheepColors.put(DyeColor.CYAN, LootTables.SHEEP_CYAN);
        sheepColors.put(DyeColor.PURPLE, LootTables.SHEEP_PURPLE);
        sheepColors.put(DyeColor.BLUE, LootTables.SHEEP_BLUE);
        sheepColors.put(DyeColor.BROWN, LootTables.SHEEP_BROWN);
        sheepColors.put(DyeColor.GREEN, LootTables.SHEEP_GREEN);
        sheepColors.put(DyeColor.RED, LootTables.SHEEP_RED);
        sheepColors.put(DyeColor.BLACK, LootTables.SHEEP_BLACK);
    }

    public static List<LootPool> getPools(LootTable table) {
        // public net.minecraft.loot.LootTable field_186466_c # pools
        return ReflectionHelper.getPrivateValue(LootTable.class, table, "field_186466_c");
    }

    public static List<LootEntry> getLootEntries(LootPool pool) {
        // public net.minecraft.loot.LootPool field_186453_a # lootEntries
        return ReflectionHelper.getPrivateValue(LootPool.class, pool, "field_186453_a");
    }

    public static List<ILootCondition> getLootConditions(LootPool pool) {
        // public net.minecraft.loot.LootPool field_186454_b # conditions
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
                    .map(entry -> toDrops(manager.get(entry.name))).forEach(drops::addAll);
            }
        );

        drops.removeIf(Objects::isNull);
        return drops;
    }

    public static List<LootDrop> toDrops(World world, ResourceLocation lootTable) {
        return toDrops(getManager(world).get(lootTable));
    }

    public static List<ResourceLocation> getAllChestLootTablesResourceLocations() {
        ArrayList<ResourceLocation> chestTables = new ArrayList<>();

        chestTables.add(LootTables.END_CITY_TREASURE);
        chestTables.add(LootTables.SIMPLE_DUNGEON);
        chestTables.add(LootTables.VILLAGE_WEAPONSMITH);
        chestTables.add(LootTables.VILLAGE_TOOLSMITH);
        chestTables.add(LootTables.VILLAGE_ARMORER);
        chestTables.add(LootTables.VILLAGE_CARTOGRAPHER);
        chestTables.add(LootTables.VILLAGE_MASON);
        chestTables.add(LootTables.VILLAGE_SHEPHERD);
        chestTables.add(LootTables.VILLAGE_BUTCHER);
        chestTables.add(LootTables.VILLAGE_FLETCHER);
        chestTables.add(LootTables.VILLAGE_FISHER);
        chestTables.add(LootTables.VILLAGE_TANNERY);
        chestTables.add(LootTables.VILLAGE_TEMPLE);
        chestTables.add(LootTables.VILLAGE_DESERT_HOUSE);
        chestTables.add(LootTables.VILLAGE_PLAINS_HOUSE);
        chestTables.add(LootTables.VILLAGE_TAIGA_HOUSE);
        chestTables.add(LootTables.VILLAGE_SNOWY_HOUSE);
        chestTables.add(LootTables.VILLAGE_SAVANNA_HOUSE);
        chestTables.add(LootTables.ABANDONED_MINESHAFT);
        chestTables.add(LootTables.NETHER_BRIDGE);
        chestTables.add(LootTables.STRONGHOLD_LIBRARY);
        chestTables.add(LootTables.STRONGHOLD_CROSSING);
        chestTables.add(LootTables.STRONGHOLD_CORRIDOR);
        chestTables.add(LootTables.DESERT_PYRAMID);
        chestTables.add(LootTables.JUNGLE_TEMPLE);
        chestTables.add(LootTables.IGLOO_CHEST);
        chestTables.add(LootTables.WOODLAND_MANSION);
        chestTables.add(LootTables.UNDERWATER_RUIN_SMALL);
        chestTables.add(LootTables.UNDERWATER_RUIN_BIG);
        chestTables.add(LootTables.BURIED_TREASURE);
        chestTables.add(LootTables.SHIPWRECK_MAP);
        chestTables.add(LootTables.SHIPWRECK_SUPPLY);
        chestTables.add(LootTables.SHIPWRECK_TREASURE);
        chestTables.add(LootTables.PILLAGER_OUTPOST);
        chestTables.add(LootTables.BASTION_TREASURE);
        chestTables.add(LootTables.BASTION_OTHER);
        chestTables.add(LootTables.BASTION_BRIDGE);
        chestTables.add(LootTables.BASTION_HOGLIN_STABLE);
        chestTables.add(LootTables.RUINED_PORTAL);

        return chestTables;
    }

    public static Map<ResourceLocation, LivingEntity> getAllMobLootTables(World world) {
        MobTableBuilder mobTableBuilder = new MobTableBuilder(world);

        for (Map.Entry<DyeColor, ResourceLocation> entry : sheepColors.entrySet()) {
            ResourceLocation lootTableList = entry.getValue();
            DyeColor dyeColor = entry.getKey();
            mobTableBuilder.addSheep(lootTableList, EntityType.SHEEP, dyeColor);
        }

        for (EntityType entityType : ForgeRegistries.ENTITIES) {
            if (entityType.getCategory() != EntityClassification.MISC && entityType != EntityType.SHEEP) {
                mobTableBuilder.add(entityType.getDefaultLootTable(), entityType);
            }
        }

        return mobTableBuilder.getMobTables();
    }

    private static LootTableManager manager;

    public static LootTableManager getManager(@Nullable World world) {
        if (world == null || world.getServer() == null) {
            if (manager == null) {
                manager = new LootTableManager(new LootPredicateManager());

                if(ConfigValues.disableLootManagerReloading.get()) {
                    return manager;
                }

                SimpleReloadableResourceManager serverResourceManger = new SimpleReloadableResourceManager(ResourcePackType.SERVER_DATA);
                List<IResourcePack> packs = new LinkedList<>();
                packs.add(new VanillaPack("minecraft"));
                for (ModFileInfo mod : ModList.get().getModFiles()) {
                    packs.add(new ModFileResourcePack(mod.getFile()));
                }
                packs.forEach(serverResourceManger::add);
                serverResourceManger.registerReloadListener(manager);
                CompletableFuture<Unit> completableFuture = serverResourceManger.reload(Util.backgroundExecutor(), Minecraft.getInstance(), packs, CompletableFuture.completedFuture(Unit.INSTANCE));
                Minecraft.getInstance().managedBlock(completableFuture::isDone);
            }
            return manager;
        }
        return world.getServer().getLootTables();
    }

    public static LootTableManager getManager() {
        return getManager(CompatBase.getWorld());
    }
}
