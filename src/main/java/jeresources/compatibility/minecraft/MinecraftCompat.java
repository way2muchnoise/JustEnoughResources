package jeresources.compatibility.minecraft;

import jeresources.api.distributions.DistributionSquare;
import jeresources.api.distributions.DistributionTriangular;
import jeresources.api.distributions.DistributionUnderWater;
import jeresources.api.drop.LootDrop;
import jeresources.api.drop.PlantDrop;
import jeresources.api.restrictions.BiomeRestriction;
import jeresources.api.restrictions.BlockRestriction;
import jeresources.api.restrictions.DimensionRestriction;
import jeresources.api.restrictions.Restriction;
import jeresources.compatibility.CompatBase;
import jeresources.entry.DungeonEntry;
import jeresources.entry.MobEntry;
import jeresources.entry.PlantEntry;
import jeresources.entry.WorldGenEntry;
import jeresources.util.LootHelper;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootTableManager;

public class MinecraftCompat extends CompatBase
{
    @Override
    public void init(boolean worldGen)
    {
        registerVanillaMobs();
        registerDungeonLoot();
        if (worldGen)
            registerOres();
        registerVanillaPlants();
    }

    private void registerVanillaMobs()
    {
        LootHelper.getAllMobLootTables(getWorld()).entrySet().forEach(
                entry -> registerMob(new MobEntry(
                entry.getValue(),
                LootHelper.getManager(getWorld()).getLootTableFromLocation(entry.getKey()))
        ));

        registerMobRenderHook(EntityBat.class, RenderHooks.BAT);
        registerMobRenderHook(EntityDragon.class, RenderHooks.ENDER_DRAGON);
        registerMobRenderHook(EntityGuardian.class, RenderHooks.ELDER_GUARDIAN);
        registerMobRenderHook(EntitySquid.class, RenderHooks.SQUID);
    }

    private void registerDungeonLoot()
    {
        LootHelper.getAllChestLootTablesResourceLocations().forEach(
                resourceLocation -> registerDungeonEntry(new DungeonEntry(
                resourceLocation.getResourcePath(),
                LootHelper.getManager(getWorld()).getLootTableFromLocation(resourceLocation))
        ));
    }

    private void registerOres()
    {
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.LAPIS_ORE), new DistributionTriangular(15, 15, 0.001F), true, new LootDrop(new ItemStack(Items.DYE, 4, 4))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.IRON_ORE), new DistributionSquare(20, 8, 1, 64)));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.REDSTONE_ORE), new DistributionSquare(8, 7, 1, 16), true, new LootDrop(new ItemStack(Items.REDSTONE, 4))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.DIAMOND_ORE), new DistributionSquare(1, 7, 1, 16), true, new LootDrop(new ItemStack(Items.DIAMOND))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.EMERALD_ORE), new DistributionSquare(6, 1, 4, 32), new Restriction(BiomeRestriction.EXTREME_HILLS), true, new LootDrop(new ItemStack(Items.EMERALD))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.GOLD_ORE), new DistributionSquare(2, 8, 1, 32)));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.COAL_ORE), new DistributionSquare(20, 16, 1, 128), true, new LootDrop(new ItemStack(Items.COAL))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.QUARTZ_ORE), new DistributionSquare(20, 14, 1, 126), new Restriction(BlockRestriction.NETHER, DimensionRestriction.NETHER), true, new LootDrop(new ItemStack(Items.QUARTZ, 4))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.CLAY), new DistributionUnderWater(0.0035F), new LootDrop(new ItemStack(Items.CLAY_BALL, 4))));
    }

    private void registerVanillaPlants()
    {
        // Potato
        ItemSeedFood potatoPlant = (ItemSeedFood) Items.POTATO;
        PlantDrop potato = new PlantDrop(new ItemStack(Items.POTATO), 1, 4);
        PlantDrop poisonous = new PlantDrop(new ItemStack(Items.POISONOUS_POTATO), 0.02F);
        registerPlant(new PlantEntry(potatoPlant, potato, poisonous));

        //Carrot
        ItemSeedFood carrotPlant = (ItemSeedFood) Items.CARROT;
        PlantDrop carrot = new PlantDrop(new ItemStack(Items.CARROT), 1, 4);
        registerPlant(new PlantEntry(carrotPlant, carrot));

        //Wheat
        ItemSeeds wheatPlant = (ItemSeeds) Items.WHEAT_SEEDS;
        PlantDrop wheat = new PlantDrop(new ItemStack(Items.WHEAT), 1, 1);
        PlantDrop seeds = new PlantDrop(new ItemStack(Items.WHEAT_SEEDS), 0, 3);
        registerPlant(new PlantEntry(wheatPlant, wheat, seeds));

        //Melon
        ItemSeeds melonStem = (ItemSeeds) Items.MELON_SEEDS;
        PlantDrop melonSlice = new PlantDrop(new ItemStack(Items.MELON), 3, 7);
        registerPlant(new PlantEntry(melonStem, melonSlice));

        //Pumpkin
        ItemSeeds pumpkinStem = (ItemSeeds) Items.PUMPKIN_SEEDS;
        PlantDrop pumpkin = new PlantDrop(new ItemStack(Blocks.PUMPKIN), 1, 1);
        registerPlant(new PlantEntry(pumpkinStem, pumpkin));
    }
}
