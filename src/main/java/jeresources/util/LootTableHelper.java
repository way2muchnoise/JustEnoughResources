package jeresources.util;

import jeresources.api.drop.LootDrop;
import jeresources.compatibility.CompatBase;
import jeresources.config.ConfigValues;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.*;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.VanillaPackResources;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.packs.resources.SimpleReloadableResourceManager;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.entries.*;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.resource.PathResourcePack;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class LootTableHelper {
    private static final Map<DyeColor, ResourceLocation> sheepColors = new HashMap<>();

    static {
        sheepColors.put(DyeColor.WHITE, BuiltInLootTables.SHEEP_WHITE);
        sheepColors.put(DyeColor.ORANGE, BuiltInLootTables.SHEEP_ORANGE);
        sheepColors.put(DyeColor.MAGENTA, BuiltInLootTables.SHEEP_MAGENTA);
        sheepColors.put(DyeColor.LIGHT_BLUE, BuiltInLootTables.SHEEP_LIGHT_BLUE);
        sheepColors.put(DyeColor.YELLOW, BuiltInLootTables.SHEEP_YELLOW);
        sheepColors.put(DyeColor.LIME, BuiltInLootTables.SHEEP_LIME);
        sheepColors.put(DyeColor.PINK, BuiltInLootTables.SHEEP_PINK);
        sheepColors.put(DyeColor.GRAY, BuiltInLootTables.SHEEP_GRAY);
        sheepColors.put(DyeColor.LIGHT_GRAY, BuiltInLootTables.SHEEP_LIGHT_GRAY);
        sheepColors.put(DyeColor.CYAN, BuiltInLootTables.SHEEP_CYAN);
        sheepColors.put(DyeColor.PURPLE, BuiltInLootTables.SHEEP_PURPLE);
        sheepColors.put(DyeColor.BLUE, BuiltInLootTables.SHEEP_BLUE);
        sheepColors.put(DyeColor.BROWN, BuiltInLootTables.SHEEP_BROWN);
        sheepColors.put(DyeColor.GREEN, BuiltInLootTables.SHEEP_GREEN);
        sheepColors.put(DyeColor.RED, BuiltInLootTables.SHEEP_RED);
        sheepColors.put(DyeColor.BLACK, BuiltInLootTables.SHEEP_BLACK);
    }

    public static List<LootPool> getPools(LootTable table) {
        // public net.minecraft.world.level.storage.loot.LootTable f_79109_ # pools
        return ReflectionHelper.getPrivateValue(LootTable.class, table, "f_79109_");
    }

    public static List<LootPoolEntryContainer> getLootEntries(LootPool pool) {
        // public net.minecraft.world.level.storage.loot.LootPool f_79023_ # entries
        return ReflectionHelper.getPrivateArrayValueAsList(LootPool.class, pool, "f_79023_");
    }

    public static List<LootItemCondition> getLootConditions(LootPool pool) {
        // public net.minecraft.world.level.storage.loot.LootPool f_79024_ # conditions
        return ReflectionHelper.getPrivateArrayValueAsList(LootPool.class, pool, "f_79024_");
    }

    public static List<LootDrop> toDrops(LootTable table) {
        List<LootDrop> drops = new ArrayList<>();

        final LootTables lootTables = getLootTables();

        getPools(table).forEach(
            pool -> {
                final float totalWeight = getLootEntries(pool).stream()
                    .filter(entry -> entry instanceof LootPoolSingletonContainer).map(entry -> (LootPoolSingletonContainer) entry)
                    .mapToInt(entry -> entry.weight).sum();
                final List<LootItemCondition> poolConditions = getLootConditions(pool);
                getLootEntries(pool).stream()
                    .filter(entry -> entry instanceof LootItem).map(entry -> (LootItem) entry)
                    .map(entry -> new LootDrop(entry.item, entry.weight / totalWeight, entry.conditions, entry.functions))
                    .map(drop -> drop.addLootConditions(poolConditions))
                    .forEach(drops::add);

                getLootEntries(pool).stream()
                    .filter(entry -> entry instanceof LootTableReference).map(entry -> (LootTableReference) entry)
                    .map(entry -> toDrops(lootTables.get(entry.name))).forEach(drops::addAll);
            }
        );

        drops.removeIf(Objects::isNull);
        return drops;
    }

    public static List<LootDrop> toDrops(ResourceLocation lootTable) {
        return toDrops(getLootTables().get(lootTable));
    }

    public static List<ResourceLocation> getAllChestLootTablesResourceLocations() {
        ArrayList<ResourceLocation> chestTables = new ArrayList<>();

        chestTables.add(BuiltInLootTables.END_CITY_TREASURE);
        chestTables.add(BuiltInLootTables.SIMPLE_DUNGEON);
        chestTables.add(BuiltInLootTables.VILLAGE_WEAPONSMITH);
        chestTables.add(BuiltInLootTables.VILLAGE_TOOLSMITH);
        chestTables.add(BuiltInLootTables.VILLAGE_ARMORER);
        chestTables.add(BuiltInLootTables.VILLAGE_CARTOGRAPHER);
        chestTables.add(BuiltInLootTables.VILLAGE_MASON);
        chestTables.add(BuiltInLootTables.VILLAGE_SHEPHERD);
        chestTables.add(BuiltInLootTables.VILLAGE_BUTCHER);
        chestTables.add(BuiltInLootTables.VILLAGE_FLETCHER);
        chestTables.add(BuiltInLootTables.VILLAGE_FISHER);
        chestTables.add(BuiltInLootTables.VILLAGE_TANNERY);
        chestTables.add(BuiltInLootTables.VILLAGE_TEMPLE);
        chestTables.add(BuiltInLootTables.VILLAGE_DESERT_HOUSE);
        chestTables.add(BuiltInLootTables.VILLAGE_PLAINS_HOUSE);
        chestTables.add(BuiltInLootTables.VILLAGE_TAIGA_HOUSE);
        chestTables.add(BuiltInLootTables.VILLAGE_SNOWY_HOUSE);
        chestTables.add(BuiltInLootTables.VILLAGE_SAVANNA_HOUSE);
        chestTables.add(BuiltInLootTables.ABANDONED_MINESHAFT);
        chestTables.add(BuiltInLootTables.NETHER_BRIDGE);
        chestTables.add(BuiltInLootTables.STRONGHOLD_LIBRARY);
        chestTables.add(BuiltInLootTables.STRONGHOLD_CROSSING);
        chestTables.add(BuiltInLootTables.STRONGHOLD_CORRIDOR);
        chestTables.add(BuiltInLootTables.DESERT_PYRAMID);
        chestTables.add(BuiltInLootTables.JUNGLE_TEMPLE);
        chestTables.add(BuiltInLootTables.IGLOO_CHEST);
        chestTables.add(BuiltInLootTables.WOODLAND_MANSION);
        chestTables.add(BuiltInLootTables.UNDERWATER_RUIN_SMALL);
        chestTables.add(BuiltInLootTables.UNDERWATER_RUIN_BIG);
        chestTables.add(BuiltInLootTables.BURIED_TREASURE);
        chestTables.add(BuiltInLootTables.SHIPWRECK_MAP);
        chestTables.add(BuiltInLootTables.SHIPWRECK_SUPPLY);
        chestTables.add(BuiltInLootTables.SHIPWRECK_TREASURE);
        chestTables.add(BuiltInLootTables.PILLAGER_OUTPOST);
        chestTables.add(BuiltInLootTables.BASTION_TREASURE);
        chestTables.add(BuiltInLootTables.BASTION_OTHER);
        chestTables.add(BuiltInLootTables.BASTION_BRIDGE);
        chestTables.add(BuiltInLootTables.BASTION_HOGLIN_STABLE);
        chestTables.add(BuiltInLootTables.RUINED_PORTAL);

        return chestTables;
    }

    public static Map<ResourceLocation, Supplier<LivingEntity>> getAllMobLootTables() {
        MobTableBuilder mobTableBuilder = new MobTableBuilder();

        for (Map.Entry<DyeColor, ResourceLocation> entry : sheepColors.entrySet()) {
            ResourceLocation lootTableList = entry.getValue();
            DyeColor dyeColor = entry.getKey();
            mobTableBuilder.addSheep(lootTableList, EntityType.SHEEP, dyeColor);
        }

        for (EntityType<?> entityType : ForgeRegistries.ENTITIES) {
            if (entityType.getCategory() != MobCategory.MISC && entityType != EntityType.SHEEP) {
                mobTableBuilder.add(entityType.getDefaultLootTable(), entityType);
            }
        }

        return mobTableBuilder.getMobTables();
    }

    private static LootTables lootTables;

    public static LootTables getLootTables() {
        Level level = CompatBase.getServerLevel()
                .orElseGet(CompatBase::getLevel);
        if (level.getServer() == null) {
            if (lootTables == null) {
                lootTables = new LootTables(new PredicateManager());

                if(ConfigValues.disableLootManagerReloading.get()) {
                    return lootTables;
                }

                SimpleReloadableResourceManager serverResourceManger = new SimpleReloadableResourceManager(PackType.SERVER_DATA);
                List<PackResources> packs = new LinkedList<>();
                packs.add(new VanillaPackResources(ServerPacksSource.BUILT_IN_METADATA, "minecraft"));
                for (IModFileInfo mod : ModList.get().getModFiles()) {
                    packs.add(new PathResourcePack(mod.getFile().getFileName(), mod.getFile().getFilePath()));
                }
                packs.forEach(serverResourceManger::add);
                serverResourceManger.registerReloadListener(lootTables);
                CompletableFuture<Unit> completableFuture = serverResourceManger.reload(Util.backgroundExecutor(), Minecraft.getInstance(), packs, CompletableFuture.completedFuture(Unit.INSTANCE));
                Minecraft.getInstance().managedBlock(completableFuture::isDone);
            }
            return lootTables;
        }
        return level.getServer().getLootTables();
    }
}
