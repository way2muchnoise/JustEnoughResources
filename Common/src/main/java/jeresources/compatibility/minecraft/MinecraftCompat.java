package jeresources.compatibility.minecraft;

import jeresources.api.conditionals.Conditional;
import jeresources.api.distributions.DistributionSquare;
import jeresources.api.distributions.DistributionTriangular;
import jeresources.api.drop.LootDrop;
import jeresources.api.drop.PlantDrop;
import jeresources.api.restrictions.BiomeRestriction;
import jeresources.api.restrictions.DimensionRestriction;
import jeresources.api.restrictions.Restriction;
import jeresources.compatibility.CompatBase;
import jeresources.entry.DungeonEntry;
import jeresources.entry.MobEntry;
import jeresources.entry.PlantEntry;
import jeresources.entry.WorldGenEntry;
import jeresources.util.LootTableHelper;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.storage.loot.LootDataManager;

import java.util.Map;

public class MinecraftCompat extends CompatBase {
    @Override
    public void init(boolean worldGen) {
        registerVanillaMobs();
        registerDungeonLoot();
        if (worldGen)
            registerOres();
        registerVanillaPlants();
    }

    private void registerVanillaMobs() {
        LootDataManager lootDataManager = LootTableHelper.getLootDataManager();
        LootTableHelper.getAllMobLootTables().entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .map(entry -> MobEntry.create(entry.getValue(), lootDataManager.getLootTable(entry.getKey())))
            .forEach(this::registerMob);

        registerMobRenderHook(Bat.class, RenderHooks.BAT);
        registerMobRenderHook(EnderDragon.class, RenderHooks.ENDER_DRAGON);
        registerMobRenderHook(ElderGuardian.class, RenderHooks.ELDER_GUARDIAN);
        registerMobRenderHook(Squid.class, RenderHooks.SQUID);
        registerMobRenderHook(Giant.class, RenderHooks.GIANT);
        registerMobRenderHook(Shulker.class, RenderHooks.SHULKER);
        registerMobRenderHook(AbstractSchoolingFish.class, RenderHooks.GROUP_FISH);
    }

    private void registerDungeonLoot() {
        LootDataManager lootDataManager = LootTableHelper.getLootDataManager();
        LootTableHelper.getAllChestLootTablesResourceLocations().stream()
            .map(resourceLocation -> new DungeonEntry(resourceLocation.getPath(), lootDataManager.getLootTable(resourceLocation)))
            .forEach(this::registerDungeonEntry);
    }

    private void registerOres() {
        // Overworld
        // Coal ore
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.COAL_ORE), new ItemStack(Blocks.DEEPSLATE_COAL_ORE), new DistributionSquare(30, 16, 136, 256), new Restriction(DimensionRestriction.OVERWORLD), true, new LootDrop(new ItemStack(Items.COAL), 1, 4, Conditional.affectedByFortune)));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.COAL_ORE), new ItemStack(Blocks.DEEPSLATE_COAL_ORE), new DistributionTriangular(20, 16, 96, 96), new Restriction(DimensionRestriction.OVERWORLD), true, new LootDrop(new ItemStack(Items.COAL), 1, 4, Conditional.affectedByFortune)));
        // Copper ore
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.COPPER_ORE), new ItemStack(Blocks.DEEPSLATE_COPPER_ORE), new DistributionTriangular(16, 8, 46, 66), new Restriction(DimensionRestriction.OVERWORLD), true, new LootDrop(new ItemStack(Items.RAW_COPPER), 2, 20, Conditional.affectedByFortune)));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.COPPER_ORE), new ItemStack(Blocks.DEEPSLATE_COPPER_ORE), new DistributionTriangular(16, 16, 46, 66), new Restriction(BiomeRestriction.DRIPSTONE_CAVES, DimensionRestriction.OVERWORLD), true, new LootDrop(new ItemStack(Items.RAW_COPPER), 2, 20, Conditional.affectedByFortune)));
        // Lapis
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.LAPIS_ORE), new ItemStack(Blocks.DEEPSLATE_LAPIS_ORE), new DistributionSquare(4, 8, -64, 64), new Restriction(DimensionRestriction.OVERWORLD), true, new LootDrop(new ItemStack(Items.LAPIS_LAZULI), 4, 36, Conditional.affectedByFortune)));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.LAPIS_ORE), new ItemStack(Blocks.DEEPSLATE_LAPIS_ORE), new DistributionTriangular(2, 8, 0, 32), new Restriction(DimensionRestriction.OVERWORLD), true, new LootDrop(new ItemStack(Items.LAPIS_LAZULI), 4, 36, Conditional.affectedByFortune)));
        // Iron ore
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.IRON_ORE), new ItemStack(Blocks.DEEPSLATE_IRON_ORE), new DistributionTriangular(90, 8, 232, 152), new Restriction(DimensionRestriction.OVERWORLD), true, new LootDrop(new ItemStack(Items.RAW_IRON), 1, 4, Conditional.affectedByFortune)));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.IRON_ORE), new ItemStack(Blocks.DEEPSLATE_IRON_ORE), new DistributionTriangular(10, 8, 16, 40), new Restriction(DimensionRestriction.OVERWORLD), true, new LootDrop(new ItemStack(Items.RAW_IRON), 1, 4, Conditional.affectedByFortune)));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.IRON_ORE), new ItemStack(Blocks.DEEPSLATE_IRON_ORE), new DistributionSquare(10, 8, -64, 72), new Restriction(DimensionRestriction.OVERWORLD), true, new LootDrop(new ItemStack(Items.RAW_IRON), 1, 4, Conditional.affectedByFortune)));
        // Gold ore
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.GOLD_ORE), new ItemStack(Blocks.DEEPSLATE_GOLD_ORE), new DistributionSquare(1, 8, -64, -48), new Restriction(DimensionRestriction.OVERWORLD), true, new LootDrop(new ItemStack(Items.RAW_GOLD), 1, 4, Conditional.affectedByFortune)));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.GOLD_ORE), new ItemStack(Blocks.DEEPSLATE_GOLD_ORE), new DistributionTriangular(4, 8, -16, 48), new Restriction(DimensionRestriction.OVERWORLD), true, new LootDrop(new ItemStack(Items.RAW_GOLD), 1, 4, Conditional.affectedByFortune)));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.GOLD_ORE), new ItemStack(Blocks.DEEPSLATE_GOLD_ORE), new DistributionTriangular(50, 8, -16, 48), new Restriction(BiomeRestriction.BADLANDS, DimensionRestriction.OVERWORLD), true, new LootDrop(new ItemStack(Items.RAW_GOLD), 1, 4, Conditional.affectedByFortune)));
        // Redstone
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.REDSTONE_ORE), new ItemStack(Blocks.DEEPSLATE_REDSTONE_ORE), new DistributionTriangular(8, 8, -64, 32), new Restriction(DimensionRestriction.OVERWORLD), true, new LootDrop(new ItemStack(Items.REDSTONE), 1, 32, Conditional.affectedByFortune)));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.REDSTONE_ORE), new ItemStack(Blocks.DEEPSLATE_REDSTONE_ORE), new DistributionSquare(4, 8, -64, 15), new Restriction(DimensionRestriction.OVERWORLD), true, new LootDrop(new ItemStack(Items.REDSTONE), 1, 32, Conditional.affectedByFortune)));
        // Diamonds
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.DIAMOND_ORE), new ItemStack(Blocks.DEEPSLATE_DIAMOND_ORE), new DistributionTriangular(6, 6, -64, 64), new Restriction(DimensionRestriction.OVERWORLD), true, new LootDrop(new ItemStack(Items.DIAMOND), 1, 4, Conditional.affectedByFortune)));
        // Emeralds
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.EMERALD_ORE), new ItemStack(Blocks.DEEPSLATE_EMERALD_ORE), new DistributionTriangular(100, 4, 202, 218), new Restriction(DimensionRestriction.OVERWORLD), true, new LootDrop(new ItemStack(Items.EMERALD), 1, 4, Conditional.affectedByFortune)));
        // Nether
        // Quartz
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.NETHER_QUARTZ_ORE), new DistributionSquare(16, 16, 10, 128), new Restriction(DimensionRestriction.NETHER), true, new LootDrop(new ItemStack(Items.QUARTZ), 1, 4, Conditional.affectedByFortune)));
        // Gold
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.NETHER_GOLD_ORE), new DistributionSquare(8, 16, 15, 117), new Restriction(DimensionRestriction.NETHER), true, new LootDrop(new ItemStack(Items.GOLD_NUGGET), 2, 24, Conditional.affectedByFortune)));
        // Ancient Debris
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.ANCIENT_DEBRIS), new DistributionTriangular(14, 9, 0.00016F), new Restriction(DimensionRestriction.NETHER)));
    }

    private void registerVanillaPlants() {
        // Potato
        PlantDrop potato = new PlantDrop(new ItemStack(Items.POTATO), 1, 4);
        PlantDrop poisonous = new PlantDrop(new ItemStack(Items.POISONOUS_POTATO), 0.02F);
        registerPlant(new PlantEntry((PotatoBlock) Blocks.POTATOES, potato, poisonous));

        //Carrot
        PlantDrop carrot = new PlantDrop(new ItemStack(Items.CARROT), 1, 4);
        registerPlant(new PlantEntry((CarrotBlock) Blocks.CARROTS, carrot));

        //Wheat
        PlantDrop wheat = new PlantDrop(new ItemStack(Items.WHEAT), 1, 1);
        PlantDrop seeds = new PlantDrop(new ItemStack(Items.WHEAT_SEEDS), 0, 3);
        registerPlant(new PlantEntry((CropBlock) Blocks.WHEAT, wheat, seeds));

        //Melon
        PlantDrop melonSlice = new PlantDrop(new ItemStack(Items.MELON_SLICE), 3, 7);
        registerPlant(new PlantEntry((StemBlock) Blocks.MELON_STEM, melonSlice));

        //Pumpkin
        PlantDrop pumpkin = new PlantDrop(new ItemStack(Blocks.PUMPKIN), 1, 1);
        registerPlant(new PlantEntry((StemBlock) Blocks.PUMPKIN_STEM, pumpkin));

        // Beetroot
        PlantDrop beetroot = new PlantDrop(new ItemStack(Items.BEETROOT), 1, 1);
        PlantDrop beetrootSeeds = new PlantDrop(new ItemStack(Items.BEETROOT_SEEDS), 0, 3);
        registerPlant(new PlantEntry((BeetrootBlock) Blocks.BEETROOTS, beetroot, beetrootSeeds));

        //Nether Wart
        PlantDrop netherWartDrop = new PlantDrop(new ItemStack(Items.NETHER_WART), 2, 4);
        PlantEntry netherWartEntry = new PlantEntry((NetherWartBlock) Blocks.NETHER_WART, netherWartDrop);
        netherWartEntry.setSoil(Blocks.SOUL_SAND.defaultBlockState());
        registerPlant(netherWartEntry);

        // Sweet berries
        PlantDrop sweetBerriesDrop = new PlantDrop(new ItemStack(Items.SWEET_BERRIES), 1, 3); // Drops 1-2 at age 2, 2-3 at age 3
        PlantEntry sweetBerriesEntry = new PlantEntry((SweetBerryBushBlock) Blocks.SWEET_BERRY_BUSH, sweetBerriesDrop);
        sweetBerriesEntry.setSoil(Blocks.GRASS_BLOCK.defaultBlockState());
        registerPlant(sweetBerriesEntry);
    }
}
