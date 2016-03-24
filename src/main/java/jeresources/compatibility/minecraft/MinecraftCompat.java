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
        LootHelper.getAllMobLootTables(world).entrySet().stream()
            .forEach(entry -> registerMob(new MobEntry(
                    entry.getValue(),
                    world.getLootTableManager().getLootTableFromLocation(entry.getKey()))
            ));

        registerMobRenderHook(EntityBat.class, RenderHooks.BAT);
        registerMobRenderHook(EntityDragon.class, RenderHooks.ENDER_DRAGON);
        registerMobRenderHook(EntityGuardian.class, RenderHooks.ELDER_GUARDIAN);
        registerMobRenderHook(EntitySquid.class, RenderHooks.SQUID);
    }

    private void registerDungeonLoot()
    {
        LootHelper.getAllChestLootTablesResourceLocations().stream()
            .forEach(resourceLocation -> registerDungeonEntry(new DungeonEntry(
                    resourceLocation.getResourcePath(),
                    world.getLootTableManager().getLootTableFromLocation(resourceLocation))
            ));
    }

    private void registerOres()
    {
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.lapis_ore), new DistributionTriangular(15, 15, 0.001F), true, new LootDrop(new ItemStack(Items.dye, 4, 4))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.iron_ore), new DistributionSquare(20, 8, 1, 64)));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.redstone_ore), new DistributionSquare(8, 7, 1, 16), true, new LootDrop(new ItemStack(Items.redstone, 4))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.diamond_ore), new DistributionSquare(1, 7, 1, 16), true, new LootDrop(new ItemStack(Items.diamond))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.emerald_ore), new DistributionSquare(6, 1, 4, 32), new Restriction(BiomeRestriction.EXTREME_HILLS), true, new LootDrop(new ItemStack(Items.emerald))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.gold_ore), new DistributionSquare(2, 8, 1, 32)));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.coal_ore), new DistributionSquare(20, 16, 1, 128), true, new LootDrop(new ItemStack(Items.coal))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.quartz_ore), new DistributionSquare(20, 14, 1, 126), new Restriction(BlockRestriction.NETHER, DimensionRestriction.NETHER), true, new LootDrop(new ItemStack(Items.quartz, 4))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.clay), new DistributionUnderWater(0.0035F), new LootDrop(new ItemStack(Items.clay_ball, 4))));
    }

    private void registerVanillaPlants()
    {
        // Potato
        ItemSeedFood potatoPlant = (ItemSeedFood) Items.potato;
        PlantDrop potato = new PlantDrop(new ItemStack(Items.potato), 1, 4);
        PlantDrop poisonous = new PlantDrop(new ItemStack(Items.poisonous_potato), 0.02F);
        registerPlant(new PlantEntry(potatoPlant, potato, poisonous));

        //Carrot
        ItemSeedFood carrotPlant = (ItemSeedFood) Items.carrot;
        PlantDrop carrot = new PlantDrop(new ItemStack(Items.carrot), 1, 4);
        registerPlant(new PlantEntry(carrotPlant, carrot));

        //Wheat
        ItemSeeds wheatPlant = (ItemSeeds) Items.wheat_seeds;
        PlantDrop wheat = new PlantDrop(new ItemStack(Items.wheat), 1, 1);
        PlantDrop seeds = new PlantDrop(new ItemStack(Items.wheat_seeds), 0, 3);
        registerPlant(new PlantEntry(wheatPlant, wheat, seeds));

        //Melon
        ItemSeeds melonStem = (ItemSeeds) Items.melon_seeds;
        PlantDrop melonSlice = new PlantDrop(new ItemStack(Items.melon), 3, 7);
        registerPlant(new PlantEntry(melonStem, melonSlice));

        //Pumpkin
        ItemSeeds pumpkinStem = (ItemSeeds) Items.pumpkin_seeds;
        PlantDrop pumpkin = new PlantDrop(new ItemStack(Blocks.pumpkin), 1, 1);
        registerPlant(new PlantEntry(pumpkinStem, pumpkin));
    }
}
