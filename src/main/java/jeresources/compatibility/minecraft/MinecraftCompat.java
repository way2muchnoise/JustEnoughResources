package jeresources.compatibility.minecraft;

import java.util.Comparator;

import jeresources.api.distributions.DistributionSquare;
import jeresources.api.distributions.DistributionTriangular;
import jeresources.api.distributions.DistributionUnderWater;
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
import net.minecraft.block.*;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.ElderGuardianEntity;
import net.minecraft.entity.monster.GiantEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.fish.AbstractGroupFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTableManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public class MinecraftCompat extends CompatBase {
    @Override
    public void init(boolean worldGen) {
        registerVanillaMobs();
        registerDungeonLoot();
        if (worldGen)
            registerOres();
        registerVanillaPlants();
    }

    @Override
    protected void registerMob(MobEntry entry) {
        MobCompat.getInstance().setLightLevel(entry);
        MobCompat.getInstance().setExperience(entry);
        super.registerMob(entry);
    }

    private void registerVanillaMobs() {
        World world = getWorld();
        LootTableManager manager = LootTableHelper.getManager(world);
        LootTableHelper.getAllMobLootTables(world).entrySet().stream()
            .map(entry -> new MobEntry(entry.getValue(), manager.getLootTableFromLocation(entry.getKey())))
            .sorted(Comparator.comparing(MobEntry::getMobName))
            .forEach(this::registerMob);

        registerMobRenderHook(BatEntity.class, RenderHooks.BAT);
        registerMobRenderHook(EnderDragonEntity.class, RenderHooks.ENDER_DRAGON);
        registerMobRenderHook(ElderGuardianEntity.class, RenderHooks.ELDER_GUARDIAN);
        registerMobRenderHook(SquidEntity.class, RenderHooks.SQUID);
        registerMobRenderHook(GiantEntity.class, RenderHooks.GIANT);
        registerMobRenderHook(ShulkerEntity.class, RenderHooks.SHULKER);
        registerMobRenderHook(AbstractGroupFishEntity.class, RenderHooks.GROUP_FISH);
    }

    private void registerDungeonLoot() {
        World world = getWorld();
        LootTableManager manager = LootTableHelper.getManager(world);
        LootTableHelper.getAllChestLootTablesResourceLocations().stream()
            .map(resourceLocation -> new DungeonEntry(resourceLocation.getPath(), manager.getLootTableFromLocation(resourceLocation)))
            .forEach(this::registerDungeonEntry);
    }

    private void registerOres() {
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.LAPIS_ORE), new DistributionTriangular(15, 15, 0.001F), true, new LootDrop(new ItemStack(Items.LAPIS_LAZULI, 4, new CompoundNBT()))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.IRON_ORE), new DistributionSquare(20, 8, 1, 64)));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.REDSTONE_ORE), new DistributionSquare(8, 7, 1, 16), true, new LootDrop(new ItemStack(Items.REDSTONE, 4))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.DIAMOND_ORE), new DistributionSquare(1, 7, 1, 16), true, new LootDrop(new ItemStack(Items.DIAMOND))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.EMERALD_ORE), new DistributionSquare(6, 1, 4, 32), new Restriction(BiomeRestriction.EXTREME_HILLS), true, new LootDrop(new ItemStack(Items.EMERALD))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.GOLD_ORE), new DistributionSquare(2, 8, 1, 32)));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.GOLD_ORE), new DistributionSquare(20, 9, 32, 80), new Restriction(BiomeRestriction.MESA)));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.COAL_ORE), new DistributionSquare(20, 16, 1, 128), true, new LootDrop(new ItemStack(Items.COAL))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.NETHER_QUARTZ_ORE), new DistributionSquare(20, 14, 1, 126), new Restriction(DimensionRestriction.NETHER), true, new LootDrop(new ItemStack(Items.QUARTZ, 4))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.CLAY), new DistributionUnderWater(0.0035F), new LootDrop(new ItemStack(Items.CLAY_BALL, 4))));
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
        registerPlant(new PlantEntry((CropsBlock) Blocks.WHEAT, wheat, seeds));

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
        netherWartEntry.setSoil(Blocks.SOUL_SAND.getDefaultState());
        registerPlant(netherWartEntry);

        // Sweet berries
        PlantDrop sweetBerriesDrop = new PlantDrop(new ItemStack(Items.SWEET_BERRIES), 1, 3); // Drops 1-2 at age 2, 2-3 at age 3
        PlantEntry sweetBerriesEntry = new PlantEntry((SweetBerryBushBlock) Blocks.SWEET_BERRY_BUSH, sweetBerriesDrop);
        sweetBerriesEntry.setSoil(Blocks.GRASS_BLOCK.getDefaultState());
        registerPlant(sweetBerriesEntry);
    }
}
