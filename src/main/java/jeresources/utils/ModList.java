package jeresources.utils;

import jeresources.compatibility.CompatBase;
import jeresources.compatibility.minecraft.MinecraftCompat;
import jeresources.compatibility.thaumcraft.ThaumcraftCompat;
import net.minecraftforge.fml.common.Loader;

public enum ModList
{
    minecraft(MinecraftCompat.class),
    thaumcraft(Names.THAUMCRAFT, ThaumcraftCompat.class),
    /*cofhcore(Names.COFHCORE, CoFHCompat.class),
    metallurgy(Names.METALLURGY, MetallurgyCompat.class),
    netherores(Names.NETHERORES, NetherOresCompat.class),
    bigreactors(Names.BIGREACTORS, BigReactorsCompat.class),
    ae2(Names.APPLIEDENERGISTICS, AE2Compat.class),
    electricraft(Names.ELECTRICRAFT, ElectriCraftCompat.class),
    reactorcraft(Names.REACTORCRAFT, ReactorCraftCompat.class),
    forestry(Names.FORESTRY, ForestryCompat.class),
    ticon(Names.TICON, TiConCompat.class),
    denseores(Names.DENSEORES),
    mystcraft(Names.MYSTCRAFT),
    ic2(Names.IC2, IC2Compat.class),
    mobproperties(Names.MOBPROPERTIES, MobPropertiesCompat.class),
    reliquary(Names.RELIQUARY, ReliquaryCompat.class),
    bluepower(Names.BLUEPOWER, BluePowerCompat.class)*/;

    private String name;
    private Class compat;
    private boolean isLoaded;

    ModList(Class compat)
    {
        name = "minecraft";
        this.compat = compat;
        isLoaded = true;
    }

    ModList(String name)
    {
        this(name, null);
    }

    ModList(String name, Class compat)
    {
        this.name = name;
        this.compat = compat;
        this.isLoaded = Loader.isModLoaded(this.name);
    }

    public boolean isLoaded()
    {
        return isLoaded;
    }

    public Class compatClass()
    {
        return compat;
    }

    @Override
    public String toString()
    {
        return name;
    }

    public boolean initialise()
    {
        return compat != null && CompatBase.load(this);
    }

    public class Names
    {
        public static final String COFHCORE = "CoFHCore";
        public static final String METALLURGY = "Metallurgy";
        public static final String APPLIEDENERGISTICS = "appliedenergistics2";
        public static final String BIGREACTORS = "BigReactors";
        public static final String FORESTRY = "Forestry";
        public static final String NETHERORES = "NetherOres";
        public static final String ELECTRICRAFT = "ElectriCraft";
        public static final String REACTORCRAFT = "ReactorCraft";
        public static final String THAUMCRAFT = "Thaumcraft";
        public static final String TICON = "TConstruct";
        public static final String DENSEORES = "denseores";
        public static final String MYSTCRAFT = "Mystcraft";
        public static final String IC2= "IC2";
        public static final String MOBPROPERTIES = "MobProperties";
        public static final String RELIQUARY = "xreliquary";
        public static final String BLUEPOWER = "bluepower";
        public static final String JEI = "JEI";
    }
}
