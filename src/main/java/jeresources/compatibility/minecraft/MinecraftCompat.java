package jeresources.compatibility.minecraft;

import jeresources.api.conditionals.Conditional;
import jeresources.api.conditionals.LightLevel;
import jeresources.api.distributions.DistributionSquare;
import jeresources.api.distributions.DistributionTriangular;
import jeresources.api.distributions.DistributionUnderWater;
import jeresources.api.drop.DropItem;
import jeresources.api.drop.PlantDrop;
import jeresources.api.restrictions.BiomeRestriction;
import jeresources.api.restrictions.BlockRestriction;
import jeresources.api.restrictions.DimensionRestriction;
import jeresources.api.restrictions.Restriction;
import jeresources.compatibility.CompatBase;
import jeresources.entries.MobEntry;
import jeresources.entries.PlantEntry;
import jeresources.entries.WorldGenEntry;
import jeresources.registry.DungeonRegistry;
import jeresources.utils.MobHelper;
import jeresources.utils.ReflectionHelper;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.IPlantable;

import java.util.HashMap;

public class MinecraftCompat extends CompatBase
{
    @Override
    protected void init(boolean worldGen)
    {
        registerVanillaMobs();
        registerDungeonLoot();
        if (worldGen)
            registerOres();
        registerVanillaPlants();
    }

    private void registerVanillaMobs()
    {
        //Iron Golem
        DropItem ironIngot = new DropItem(Items.iron_ingot, 3, 5);
        DropItem poppy = new DropItem(new ItemStack(Blocks.red_flower), 0, 2);
        registerMob(new MobEntry(new EntityIronGolem(world), LightLevel.any, ironIngot, poppy));

        //Snow golem
        DropItem snowball = new DropItem(Items.snowball, 0, 15);
        registerMob(new MobEntry(new EntitySnowman(world), LightLevel.any, snowball));

        //Cow
        DropItem leather = new DropItem(Items.leather, 0, 2);
        DropItem beef = new DropItem(Items.beef, 1, 3);
        DropItem steak = new DropItem(Items.cooked_beef, 1, 3, Conditional.burning);
        registerMob(new MobEntry(new EntityCow(world), LightLevel.any, 1, 3, leather, beef, steak));

        //Mooshroom
        registerMob(new MobEntry(new EntityMooshroom(world), LightLevel.any, 1, 3, leather, beef, steak));

        //Chicken
        DropItem feather = new DropItem(Items.feather, 0, 2);
        DropItem chicken = new DropItem(Items.chicken, 1, 1);
        DropItem cookedchicken = new DropItem(Items.cooked_chicken, 1, 1, Conditional.burning);
        registerMob(new MobEntry(new EntityChicken(world), LightLevel.any, 1, 3, feather, chicken, cookedchicken));

        //Pig
        DropItem pork = new DropItem(Items.porkchop, 1, 3);
        DropItem cookedpork = new DropItem(Items.cooked_porkchop, 1, 3, Conditional.burning);
        registerMob(new MobEntry(new EntityPig(world), LightLevel.any, 1, 3, pork, cookedpork));

        //Sheep
        DropItem wool = new DropItem(new ItemStack(Blocks.wool), 1, 1);
        registerMob(new MobEntry(new EntitySheep(world), LightLevel.any, 1, 3, wool));

        //Wither
        DropItem star = new DropItem(Items.nether_star, 1, 1);
        registerMob(new MobEntry(new EntityWither(world), LightLevel.any, 50, star));

        //End Dragon
        DropItem egg = new DropItem(new ItemStack(Blocks.dragon_egg), 1, 1);
        registerMob(new MobEntry(new EntityDragon(world), LightLevel.any, egg));
        registerMobRenderHook(EntityDragon.class, RenderHooks.ENDER_DRAGON);

        //Enderman
        DropItem pearl = new DropItem(Items.ender_pearl, 0, 1);
        registerMob(new MobEntry(new EntityEnderman(world), LightLevel.hostile, pearl));

        //Zombie
        DropItem rottenFlesh = new DropItem(Items.rotten_flesh, 0, 2);
        ironIngot = new DropItem(Items.iron_ingot, 1, 1, 0.008F, Conditional.playerKill, Conditional.rareDrop);
        DropItem potato = new DropItem(Items.potato, 1, 1, 0.008F, Conditional.playerKill, Conditional.rareDrop);
        DropItem carrot = new DropItem(Items.carrot, 1, 1, 0.008F, Conditional.playerKill, Conditional.rareDrop);
        registerMob(new MobEntry(new EntityZombie(world), LightLevel.hostile, rottenFlesh, ironIngot, potato, carrot));

        //Zombie Pigman
        DropItem goldNugget = new DropItem(Items.gold_nugget, 0, 1);
        DropItem goldIngot = new DropItem(Items.gold_ingot, 0, 1, 0.025F, Conditional.playerKill, Conditional.rareDrop);
        registerMob(new MobEntry(new EntityPigZombie(world), LightLevel.any, new String[]{"Nether"}, rottenFlesh, goldNugget, goldIngot));

        //Skeleton
        DropItem bone = new DropItem(Items.bone, 0, 2);
        DropItem arrow = new DropItem(Items.arrow, 0, 2);
        registerMob(new MobEntry(new EntitySkeleton(world), LightLevel.hostile, bone, arrow));

        //Wither Skeleton
        DropItem coal = new DropItem(Items.coal, 0, 1, 0.33F);
        DropItem skull = new DropItem(new ItemStack(Items.skull, 1, 1), 1, 1, 0.025F, Conditional.playerKill, Conditional.rareDrop);
        EntitySkeleton witherSkeleton = new EntitySkeleton(world);
        witherSkeleton.setSkeletonType(1);
        registerMob(new MobEntry(witherSkeleton, LightLevel.hostile, new String[]{"Nether Fortress"}, bone, coal, skull));

        //Creeper
        DropItem gunpowder = new DropItem(Items.gunpowder, 0, 2);
        registerMob(new MobEntry(new EntityCreeper(world), LightLevel.hostile, gunpowder));

        //Ghast
        DropItem tear = new DropItem(Items.ghast_tear, 0, 1);
        registerMob(new MobEntry(new EntityGhast(world), LightLevel.hostile, new String[]{"Nether"}, gunpowder, tear));

        //Witches
        DropItem bottle = new DropItem(Items.glass_bottle, 0, 6);
        DropItem glowstone = new DropItem(Items.glowstone_dust, 0, 6);
        gunpowder = new DropItem(Items.gunpowder, 0, 6);
        DropItem redstone = new DropItem(Items.redstone, 0, 6);
        DropItem spider = new DropItem(Items.spider_eye, 0, 6);
        DropItem stick = new DropItem(Items.stick, 0, 6);
        DropItem sugar = new DropItem(Items.sugar, 0, 6);
        registerMob(new MobEntry(new EntityWitch(world), LightLevel.hostile, bottle, glowstone, gunpowder, redstone, spider, stick, sugar));

        //Slimes
        DropItem slimeball = new DropItem(Items.slime_ball, 0, 2, Conditional.slimeBall);
        registerMob(new MobEntry(MobHelper.setSlimeSize(new EntitySlime(world), 1), LightLevel.hostile, slimeball));

        //Magma Cube
        DropItem magma = new DropItem(Items.magma_cream, 0, 1, Conditional.magmaCream);
        registerMob(new MobEntry(MobHelper.setSlimeSize(new EntityMagmaCube(world), 1), LightLevel.hostile, new String[]{"Nether"}, magma));

        //Blaze
        DropItem blazeRod = new DropItem(Items.blaze_rod, 0, 1, Conditional.playerKill);
        registerMob(new MobEntry(new EntityBlaze(world), LightLevel.blaze, new String[]{"Nether Fortress"}, blazeRod));

        //Silverfish
        registerMob(new MobEntry(new EntitySilverfish(world), LightLevel.hostile));

        //Bats
        registerMob(new MobEntry(new EntityBat(world), LightLevel.hostile));
        registerMobRenderHook(EntityBat.class, RenderHooks.BAT);

        //Spider
        DropItem string = new DropItem(Items.string, 0, 2);
        spider = new DropItem(Items.spider_eye, 1, 1, 0.33F, Conditional.playerKill);
        registerMob(new MobEntry(new EntitySpider(world), LightLevel.hostile, string, spider));

        //Cave Spider
        registerMob(new MobEntry(new EntityCaveSpider(world), LightLevel.hostile, string, spider));

        //Squid
        DropItem ink = new DropItem(Items.dye, 0, 1, 3);
        registerMob(new MobEntry(new EntitySquid(world), LightLevel.any, new String[]{"In water"}, ink));
        registerMobRenderHook(EntitySquid.class, RenderHooks.SQUID);

        //Rabbit
        DropItem rabbitHide = new DropItem(Items.rabbit_hide, 0, 1);
        DropItem rawRabbit = new DropItem(Items.rabbit, 0, 1, Conditional.notBurning);
        DropItem cookedRabbit = new DropItem(Items.cooked_rabbit, 0, 1, Conditional.burning);
        DropItem rabbitFoot = new DropItem(new ItemStack(Items.rabbit_foot), 0.025F);
        registerMob(new MobEntry(new EntityRabbit(world), LightLevel.any, 1, 3, rabbitHide, rawRabbit, rabbitFoot, cookedRabbit));

        //Guardian
        DropItem prismarineCrystal = new DropItem(Items.prismarine_crystals, 0, 1);
        DropItem prismarineShard = new DropItem(Items.prismarine_shard, 0, 2);
        DropItem rawFish = new DropItem(Items.fish, 0, 1);
        DropItem sponge = new DropItem(new ItemStack(Blocks.sponge, 1, 1), 1, 1, Conditional.playerKill);
        DropItem rareFish = new DropItem(new ItemStack(Items.fish), 0.025F);
        DropItem rareSalmon = new DropItem(new ItemStack(Items.fish, 1, 1), 0.025F);
        DropItem rarePuffer = new DropItem(new ItemStack(Items.fish, 1, 3), 0.025F);
        DropItem rareClown = new DropItem(new ItemStack(Items.fish, 1, 2), 0.025F);
        EntityGuardian elderGuardian = new EntityGuardian(world);
        elderGuardian.setElder();
        registerMob(new MobEntry(new EntityGuardian(world), LightLevel.any, new String[]{"Ocean monument"}, prismarineCrystal, prismarineShard, rawFish, sponge, rareFish, rareSalmon, rarePuffer, rareClown));
        registerMob(new MobEntry(elderGuardian, LightLevel.any, new String[]{"Ocean monument"}, prismarineCrystal, prismarineShard, rawFish, sponge, rareFish, rareSalmon, rarePuffer, rareClown));
        registerMobRenderHook(EntityGuardian.class, RenderHooks.ELDER_GUARDIAN);

    }

    @SuppressWarnings("unchecked")
    private void registerDungeonLoot()
    {
        HashMap<String, ChestGenHooks> dungeons = ReflectionHelper.getPrivateValue(ChestGenHooks.class, null, "chestInfo");
        ChestGenHooks bonusChest = ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST);
        if (dungeons != null)
            for (ChestGenHooks chestGenHook : dungeons.values())
                if (chestGenHook != bonusChest)
                    DungeonRegistry.getInstance().registerChestHook(chestGenHook);
    }

    private void registerOres()
    {
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.lapis_ore), new DistributionTriangular(15, 15, 0.001F), true, new DropItem(new ItemStack(Items.dye, 4, 4))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.iron_ore), new DistributionSquare(20, 8, 1, 64)));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.redstone_ore), new DistributionSquare(8, 7, 1, 16), true, new DropItem(new ItemStack(Items.redstone, 4))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.diamond_ore), new DistributionSquare(1, 7, 1, 16), true, new DropItem(new ItemStack(Items.diamond))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.emerald_ore), new DistributionSquare(6, 1, 4, 32), new Restriction(BiomeRestriction.EXTREME_HILLS), true, new DropItem(new ItemStack(Items.emerald))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.gold_ore), new DistributionSquare(2, 8, 1, 32)));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.coal_ore), new DistributionSquare(20, 16, 1, 128), true, new DropItem(new ItemStack(Items.coal))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.quartz_ore), new DistributionSquare(20, 14, 1, 126), new Restriction(BlockRestriction.NETHER, DimensionRestriction.NETHER), true, new DropItem(new ItemStack(Items.quartz, 4))));
        registerWorldGen(new WorldGenEntry(new ItemStack(Blocks.clay), new DistributionUnderWater(0.0035F), new DropItem(new ItemStack(Items.clay_ball, 4))));
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
