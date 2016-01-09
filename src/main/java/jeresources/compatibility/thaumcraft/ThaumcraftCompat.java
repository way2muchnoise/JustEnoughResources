package jeresources.compatibility.thaumcraft;

import jeresources.api.distributions.DistributionCustom;
import jeresources.api.distributions.DistributionSquare;
import jeresources.api.messages.ModifyOreMessage;
import jeresources.api.messages.RegisterOreMessage;
import jeresources.api.utils.*;
import jeresources.api.utils.conditionals.Conditional;
import jeresources.compatibility.CompatBase;
import jeresources.entries.MobEntry;
import jeresources.registry.MessageRegistry;
import jeresources.utils.ModList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.registry.GameRegistry;
import thaumcraft.common.config.Config;
import thaumcraft.common.entities.monster.EntityBrainyZombie;
import thaumcraft.common.entities.monster.EntityMindSpider;
import thaumcraft.common.entities.monster.EntityPech;
import thaumcraft.common.entities.monster.EntityWisp;
import thaumcraft.common.entities.monster.cult.EntityCultistCleric;
import thaumcraft.common.entities.monster.cult.EntityCultistKnight;
import thaumcraft.common.entities.monster.tainted.*;

public class ThaumcraftCompat extends CompatBase
{

    @Override
    protected void init()
    {
        registerThaumcraftOres();
        registerThaumcraftMobs();
    }

    @Optional.Method(modid = ModList.Names.THAUMCRAFT)
    private void registerThaumcraftOres()
    {
        ItemStack amber = new ItemStack(GameRegistry.findBlock(ModList.Names.THAUMCRAFT, "ore_amber"));
        ItemStack amberDrop = new ItemStack(GameRegistry.findItem(ModList.Names.THAUMCRAFT, "amber"));
        MessageRegistry.addMessage(new ModifyOreMessage(amber, Priority.FIRST, amberDrop));
        if (Config.genCinnibar) genCinnabar();
        if (Config.genAmber) genAmber();
    }

    @Optional.Method(modid = ModList.Names.THAUMCRAFT)
    private void registerThaumcraftMobs()
    {
        Conditional randomAspect = new Conditional("jer.randomAspect.text", Modifier.pink);
        String[] tainted = new String[]{"Tainted areas"};
        DropItem flesh = new DropItem(Items.rotten_flesh, 0, 2);
        DropItem brain = new DropItem(GameRegistry.findItem(ModList.Names.THAUMCRAFT, "brain"), 0, 1);
        if (Config.spawnAngryZombie)
            registerMob(new MobEntry(new EntityBrainyZombie(world), LightLevel.hostile, flesh, brain));

        DropItem essence = new DropItem(new ItemStack(GameRegistry.findItem(ModList.Names.THAUMCRAFT, "wispy_essence")), 1, 1, randomAspect);
        if (Config.spawnWisp) registerMob(new MobEntry(new EntityWisp(world), LightLevel.hostile, essence));

        DropItem knowledge = new DropItem(new ItemStack(GameRegistry.findItem(ModList.Names.THAUMCRAFT, "knowledge_fragment")), 1, 1, 0.025F, Conditional.playerKill, Conditional.rareDrop);
        if (Config.spawnPech) registerMob(new MobEntry(new EntityPech(world), LightLevel.any, knowledge));

        DropItem taintSlime = new DropItem(new ItemStack(GameRegistry.findItem(ModList.Names.THAUMCRAFT, "tainted")), 0, 1);
        DropItem taintTendril = new DropItem(new ItemStack(GameRegistry.findItem(ModList.Names.THAUMCRAFT, "tainted"), 1, 1), 0, 1);
        if (Config.spawnTaintacle)
        {
            registerMob(new MobEntry(new EntityTaintacle(world), LightLevel.any, tainted, taintSlime, taintTendril));
            registerMob(new MobEntry(new EntityTaintacleSmall(world), LightLevel.any, tainted));
        }
        taintSlime = new DropItem(new ItemStack(GameRegistry.findItem(ModList.Names.THAUMCRAFT, "tainted")), 0, 1, 0.166F);
        taintTendril = new DropItem(new ItemStack(GameRegistry.findItem(ModList.Names.THAUMCRAFT, "tainted"), 1, 1), 0, 1, 0.166F);
        EntityLivingBase[] taintedEntities = new EntityLivingBase[]{new EntityTaintChicken(world), new EntityTaintCow(world), new EntityTaintCreeper(world), new EntityTaintPig(world), new EntityTaintSheep(world),
                new EntityTaintSheep(world), new EntityTaintRabbit(world), new EntityTaintVillager(world)};
        for (EntityLivingBase entity : taintedEntities)
            registerMob(new MobEntry(entity, LightLevel.any, tainted, taintSlime, taintTendril));

        DropItem string = new DropItem(Items.string, 0, 2);
        DropItem spider = new DropItem(Items.spider_eye, 1, 1, 0.33F, Conditional.playerKill);
        registerMob(new MobEntry(new EntityMindSpider(world), LightLevel.hostile, string, spider));

        knowledge = new DropItem(new ItemStack(GameRegistry.findItem(ModList.Names.THAUMCRAFT, "knowledge_fragment")), 0, 1, 0.1F);
        DropItem voidSeed = new DropItem(new ItemStack(GameRegistry.findItem(ModList.Names.THAUMCRAFT, "void_seed")), 0, 1, 0.2F);
        DropItem crimsonRites = new DropItem(new ItemStack(GameRegistry.findItem(ModList.Names.THAUMCRAFT, "crimson_rites")), 1, 1, 0.025F, Conditional.playerKill, Conditional.rareDrop);
        DropItem cultHelmet = new DropItem(new ItemStack(GameRegistry.findItem(ModList.Names.THAUMCRAFT, "crimson_plate_helm")), 0, 1, 0.085F, Conditional.equipmentDrop);
        DropItem cultChest = new DropItem(new ItemStack(GameRegistry.findItem(ModList.Names.THAUMCRAFT, "crimson_plate_chest")), 0, 1, 0.085F, Conditional.equipmentDrop);
        DropItem cultLegs = new DropItem(new ItemStack(GameRegistry.findItem(ModList.Names.THAUMCRAFT, "crimson_plate_legs")), 0, 1, 0.085F, Conditional.equipmentDrop);
        DropItem cultBoots = new DropItem(new ItemStack(GameRegistry.findItem(ModList.Names.THAUMCRAFT, "crimson_boots")), 0, 1, 0.085F, Conditional.equipmentDrop);
        DropItem thaumSword = new DropItem(new ItemStack(GameRegistry.findItem(ModList.Names.THAUMCRAFT, "thaumium_sword")), 0, 1, 0.085F, Conditional.equipmentDrop);
        DropItem cultRobeHelmet = new DropItem(new ItemStack(GameRegistry.findItem(ModList.Names.THAUMCRAFT, "crimson_robe_helm")), 0, 1, 0.085F, Conditional.equipmentDrop);
        DropItem voidSword = new DropItem(new ItemStack(GameRegistry.findItem(ModList.Names.THAUMCRAFT, "void_sword")), 0, 1, 0.085F, Conditional.equipmentDrop);
        DropItem cultRobeChest = new DropItem(new ItemStack(GameRegistry.findItem(ModList.Names.THAUMCRAFT, "crimson_robe_chest")), 0, 1, 0.085F, Conditional.equipmentDrop);
        DropItem cultRobeLegs = new DropItem(new ItemStack(GameRegistry.findItem(ModList.Names.THAUMCRAFT, "crimson_praetor_legs")), 0, 1, 0.085F, Conditional.equipmentDrop);
        registerMob(new MobEntry(new EntityCultistKnight(world), LightLevel.hostile, voidSeed, knowledge, crimsonRites, cultHelmet, cultChest, cultLegs, cultBoots, thaumSword, cultRobeHelmet, voidSword));
        registerMob(new MobEntry(new EntityCultistCleric(world), LightLevel.hostile, voidSeed, knowledge, crimsonRites, cultRobeHelmet, cultRobeChest, cultRobeLegs, cultBoots));
    }

    @Optional.Method(modid = ModList.Names.THAUMCRAFT)
    private void genAmber()
    {
        int minY = 0;
        int maxY = 64;
        float maxYRange = 25;
        float numVeins = 20F;
        float chance = numVeins / ((maxY - maxYRange / 2 - minY + 1) * 256);
        float[] distribution = DistributionHelpers.getSquareDistribution(minY, maxY - (int) maxYRange, chance);
        DistributionHelpers.addDistribution(distribution, DistributionHelpers.getRampDistribution(maxY, (int) (maxY - maxYRange), chance), maxY - (int) maxYRange);
        ItemStack amber = new ItemStack(GameRegistry.findBlock(ModList.Names.THAUMCRAFT, "ore_amber"));
        registerOre(new RegisterOreMessage(amber, new DistributionCustom(distribution)));
    }

    @Optional.Method(modid = ModList.Names.THAUMCRAFT)
    private void genCinnabar()
    {
        int minY = 0;
        int maxY = 64 / 5;
        float numVeins = 18F;
        float chance = numVeins / (maxY * 256);
        ItemStack ore = new ItemStack(GameRegistry.findBlock(ModList.Names.THAUMCRAFT, "ore_cinnabar"));
        registerOre(new RegisterOreMessage(ore, new DistributionSquare(minY, maxY, chance)));
    }
}
