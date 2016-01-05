package jeresources.compatibility.minecraft;

import jeresources.api.distributions.DistributionSquare;
import jeresources.api.distributions.DistributionTriangular;
import jeresources.api.distributions.DistributionUnderWater;
import jeresources.api.messages.ModifyOreMessage;
import jeresources.api.messages.RegisterOreMessage;
import jeresources.api.utils.DropItem;
import jeresources.api.utils.LightLevel;
import jeresources.api.utils.PlantDrop;
import jeresources.api.utils.Priority;
import jeresources.api.utils.conditionals.Conditional;
import jeresources.api.utils.restrictions.BiomeRestriction;
import jeresources.api.utils.restrictions.Restriction;
import jeresources.compatibility.CompatBase;
import jeresources.entries.MobEntry;
import jeresources.entries.PlantEntry;
import jeresources.registry.DungeonRegistry;
import jeresources.registry.MessageRegistry;
import jeresources.utils.MobHelper;
import jeresources.utils.ReflectionHelper;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.*;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ChestGenHooks;

import java.util.HashMap;

public class MinecraftCompat extends CompatBase
{

    @Override
    protected void init()
    {
        registerVanillaMobs();
        registerDungeonLoot();
        registerOres();
        registerVanillaOreDrops();
        registerVanillaPlants();
    }

    private void registerVanillaOreDrops()
    {
        registerOre(new RegisterOreMessage(new ItemStack(Blocks.clay), new DistributionUnderWater(0.0035F), new ItemStack(Items.clay_ball,4)));
        MessageRegistry.addMessage(new ModifyOreMessage(new ItemStack(Blocks.clay), Priority.FIRST, new ItemStack(Items.clay_ball, 4)));
        MessageRegistry.addMessage(new ModifyOreMessage(new ItemStack(Blocks.coal_ore), Priority.FIRST, new ItemStack(Items.coal)));
        MessageRegistry.addMessage(new ModifyOreMessage(new ItemStack(Blocks.diamond_ore), Priority.FIRST, new ItemStack(Items.diamond)));
        MessageRegistry.addMessage(new ModifyOreMessage(new ItemStack(Blocks.lapis_ore), Priority.FIRST, new ItemStack(Items.dye, 4, 4)));
        MessageRegistry.addMessage(new ModifyOreMessage(new ItemStack(Blocks.emerald_ore), Priority.FIRST, new ItemStack(Items.emerald)));
        MessageRegistry.addMessage(new ModifyOreMessage(new ItemStack(Blocks.redstone_ore), Priority.FIRST, new ItemStack(Items.redstone, 4)));
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
        registerMob(new MobEntry(new EntityCow(world), LightLevel.any, leather, beef, steak));

        //Mooshroom
        registerMob(new MobEntry(new EntityMooshroom(world), LightLevel.any, leather, beef, steak));

        //Chicken
        DropItem feather = new DropItem(Items.feather, 0, 2);
        DropItem chicken = new DropItem(Items.chicken, 1, 1);
        DropItem cookedchicken = new DropItem(Items.cooked_chicken, 1, 1, Conditional.burning);
        registerMob(new MobEntry(new EntityChicken(world), LightLevel.any, feather, chicken, cookedchicken));

        //Pig
        DropItem pork = new DropItem(Items.porkchop, 1, 3);
        DropItem cookedpork = new DropItem(Items.cooked_porkchop, 1, 3, Conditional.burning);
        registerMob(new MobEntry(new EntityPig(world), LightLevel.any, pork, cookedpork));

        //Sheep
        DropItem wool = new DropItem(new ItemStack(Blocks.wool), 1, 1);
        registerMob(new MobEntry(new EntitySheep(world), LightLevel.any, wool));

        //Wither
        DropItem star = new DropItem(Items.nether_star, 1, 1);
        registerMob(new MobEntry(new EntityWither(world), LightLevel.any, star));

        //End Dragon
        DropItem egg = new DropItem(new ItemStack(Blocks.dragon_egg), 1, 1);
        registerMob(new MobEntry(new EntityDragon(world), LightLevel.any, egg));

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

        //Spider
        DropItem string = new DropItem(Items.string, 0, 2);
        spider = new DropItem(Items.spider_eye, 1, 1, 0.33F, Conditional.playerKill);
        registerMob(new MobEntry(new EntitySpider(world), LightLevel.hostile, string, spider));

        //Cave Spider
        registerMob(new MobEntry(new EntityCaveSpider(world), LightLevel.hostile, string, spider));

        //Squid
        DropItem ink = new DropItem(Items.dye, 0, 1, 3);
        registerMob(new MobEntry(new EntitySquid(world), LightLevel.any, new String[]{"In water"}, ink));
    }

    @SuppressWarnings("unchecked")
    private void registerDungeonLoot()
    {
        HashMap<String, ChestGenHooks> dungeons = (HashMap<String, ChestGenHooks>) ReflectionHelper.getObject(ChestGenHooks.class, "chestInfo", null);
        ChestGenHooks bonusChest = ChestGenHooks.getInfo(ChestGenHooks.BONUS_CHEST);
        if (dungeons != null)
            for (ChestGenHooks chestGenHook : dungeons.values())
                if (chestGenHook != bonusChest)
                    DungeonRegistry.getInstance().registerChestHook(chestGenHook);
    }

    private void registerOres()
    {
        registerOre(new RegisterOreMessage(new ItemStack(Blocks.lapis_ore), new DistributionTriangular(15, 15, 0.001F)));
        registerOre(new RegisterOreMessage(new ItemStack(Blocks.iron_ore), new DistributionSquare(20, 8, 0, 64)));
        registerOre(new RegisterOreMessage(new ItemStack(Blocks.redstone_ore), new DistributionSquare(8, 7, 0, 16)));
        registerOre(new RegisterOreMessage(new ItemStack(Blocks.diamond_ore), new DistributionSquare(1, 7, 0, 16)));
        registerOre(new RegisterOreMessage(new ItemStack(Blocks.emerald_ore), new DistributionSquare(6, 1, 4, 32), new Restriction(BiomeRestriction.EXTREME_HILLS)));
        registerOre(new RegisterOreMessage(new ItemStack(Blocks.gold_ore), new DistributionSquare(2, 8, 0, 32)));
        registerOre(new RegisterOreMessage(new ItemStack(Blocks.coal_ore), new DistributionSquare(20, 16, 0, 128)));
    }

    private void registerVanillaPlants()
    {
        // Potato TODO: seems like you can't make ItemStacks of potato plant blocks anymore
        ItemStack potatoPlant = new ItemStack(Blocks.potatoes);
        PlantDrop potato = new PlantDrop(new ItemStack(Items.potato), 1, 4);
        PlantDrop poisonous = new PlantDrop(new ItemStack(Items.poisonous_potato), 0.02F);
        registerPlant(new PlantEntry(potatoPlant, potato, poisonous));

        //Carrot
        ItemStack carrotPlant = new ItemStack(Blocks.carrots);
        PlantDrop carrot = new PlantDrop(new ItemStack(Items.carrot), 1, 4);
        registerPlant(new PlantEntry(carrotPlant, carrot));
        //Wheat
        ItemStack wheatPlant = new ItemStack(Blocks.wheat);
        PlantDrop wheat = new PlantDrop(new ItemStack(Items.wheat), 1);
        PlantDrop seeds = new PlantDrop(new ItemStack(Items.wheat_seeds), 0, 3);
        registerPlant(new PlantEntry(wheatPlant, wheat, seeds));

        //Melon
        ItemStack melonStem = new ItemStack(Blocks.melon_stem);
        PlantDrop melonSlice = new PlantDrop(new ItemStack(Items.melon), 3, 7);
        registerPlant(new PlantEntry(melonStem, melonSlice));

        //Pumpkin
        ItemStack pumpkinStem = new ItemStack(Blocks.pumpkin_stem);
        PlantDrop pumpkin = new PlantDrop(new ItemStack(Blocks.pumpkin), 1);
        registerPlant(new PlantEntry(pumpkinStem, pumpkin));
    }
}
