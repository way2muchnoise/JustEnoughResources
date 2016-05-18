package jeresources.api.restrictions;

import jeresources.util.BiomeHelper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BiomeRestriction
{
    public static final BiomeRestriction NONE = new BiomeRestriction();
    public static final BiomeRestriction OCEAN = new BiomeRestriction(BiomeDictionary.Type.OCEAN);
    public static final BiomeRestriction PLAINS = new BiomeRestriction(BiomeDictionary.Type.PLAINS);
    public static final BiomeRestriction FOREST = new BiomeRestriction(BiomeDictionary.Type.FOREST);
    public static final BiomeRestriction SANDY = new BiomeRestriction(BiomeDictionary.Type.FOREST);
    public static final BiomeRestriction SNOWY = new BiomeRestriction(BiomeDictionary.Type.FOREST);
    public static final BiomeRestriction HILLS = new BiomeRestriction(BiomeDictionary.Type.HILLS);
    public static final BiomeRestriction MUSHROOM = new BiomeRestriction(BiomeDictionary.Type.MUSHROOM);

    public static final BiomeRestriction HOT = new BiomeRestriction(BiomeDictionary.Type.HOT);
    public static final BiomeRestriction COLD = new BiomeRestriction(BiomeDictionary.Type.COLD);
    public static final BiomeRestriction TEMPERATE = new BiomeRestriction(Type.BLACKLIST, BiomeDictionary.Type.HOT, BiomeDictionary.Type.COLD);

    private static final int extremeHillsBiomeId = 3, extremeHillsEdgeBiomeId = 20;
    public static final BiomeRestriction EXTREME_HILLS = new BiomeRestriction(Type.WHITELIST, Biome.getBiome(extremeHillsBiomeId), Biome.getBiome(extremeHillsEdgeBiomeId));

    private List<Biome> biomes = new ArrayList<Biome>();
    private Type type;

    public BiomeRestriction()
    {
        this.type = Type.NONE;
    }

    public BiomeRestriction(Biome biome)
    {
        this(Type.WHITELIST, biome);
    }

    public BiomeRestriction(Type type, Biome biome)
    {
        this(type, biome, new Biome[0]);
    }

    public BiomeRestriction(Biome biome, Biome... moreBiomes)
    {
        this(Type.WHITELIST, biome, moreBiomes);
    }

    public BiomeRestriction(Type type, Biome biome, Biome... moreBiomes)
    {
        this.type = type;
        switch (type)
        {
            case NONE:
                break;
            case WHITELIST:
                this.biomes.add(biome);
                this.biomes.addAll(Arrays.asList(moreBiomes));
                break;
            default:
                biomes = BiomeHelper.getAllBiomes();
                biomes.remove(biome);
                biomes.removeAll(Arrays.asList(moreBiomes));
        }
    }

    public BiomeRestriction(BiomeDictionary.Type type, BiomeDictionary.Type... biomeTypes)
    {
        this(Type.WHITELIST, type, biomeTypes);
    }

    public BiomeRestriction(Type type, BiomeDictionary.Type biomeType, BiomeDictionary.Type... biomeTypes)
    {
        this.type = type;
        switch (type)
        {
            case NONE:
                break;
            case WHITELIST:
                biomes = getBiomes(biomeType, biomeTypes);
                break;
            default:
                biomes = BiomeHelper.getAllBiomes();
                biomes.removeAll(getBiomes(biomeType, biomeTypes));
        }
    }

    private ArrayList<Biome> getBiomes(BiomeDictionary.Type biomeType, BiomeDictionary.Type... biomeTypes)
    {
        ArrayList<Biome> biomes = new ArrayList<>();
        biomes.addAll(Arrays.asList(BiomeDictionary.getBiomesForType(biomeType)));
        for (int i = 1; i < biomeTypes.length; i++)
        {
            ArrayList<Biome> newBiomes = new ArrayList<>();
            for (Biome biome : BiomeDictionary.getBiomesForType(biomeTypes[i]))
            {
                if (biomes.remove(biome)) newBiomes.add(biome);
            }
            biomes = newBiomes;
        }
        return biomes;
    }

    public List<String> toStringList()
    {
        List<String> result = new ArrayList<>();
        for (Biome biome : biomes)
            if (!biome.getBiomeName().equals("")) result.add("  " + biome.getBiomeName());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof BiomeRestriction)
        {
            BiomeRestriction other = (BiomeRestriction) obj;
            return other.biomes.size() == biomes.size() && other.biomes.containsAll(biomes);
        }
        return false;
    }

    public boolean isMergeAble(BiomeRestriction other)
    {
        return other.type == Type.NONE || (this.type != Type.NONE && !biomes.isEmpty() && other.biomes.containsAll(biomes));
    }

    @Override
    public String toString()
    {
        return "Biomes: " + type + (type != Type.NONE ? " - " + biomes.size() : "");
    }

    @Override
    public int hashCode()
    {
        return type.hashCode() ^ biomes.hashCode();
    }
}
