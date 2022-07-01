package jeresources.api.restrictions;

import jeresources.api.util.BiomeHelper;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class BiomeRestriction {
    public static final BiomeRestriction NO_RESTRICTION = new BiomeRestriction();
    
   public static final BiomeRestriction NONE = new BiomeRestriction(Biome.BiomeCategory.NONE);
   public static final BiomeRestriction TAIGA = new BiomeRestriction(Biome.BiomeCategory.TAIGA);
   public static final BiomeRestriction EXTREME_HILLS = new BiomeRestriction(Biome.BiomeCategory.EXTREME_HILLS);
   public static final BiomeRestriction JUNGLE = new BiomeRestriction(Biome.BiomeCategory.JUNGLE);
   public static final BiomeRestriction MESA = new BiomeRestriction(Biome.BiomeCategory.MESA);
   public static final BiomeRestriction PLAINS = new BiomeRestriction(Biome.BiomeCategory.PLAINS);
   public static final BiomeRestriction SAVANNA = new BiomeRestriction(Biome.BiomeCategory.SAVANNA);
   public static final BiomeRestriction ICY = new BiomeRestriction(Biome.BiomeCategory.ICY);
   public static final BiomeRestriction THEEND = new BiomeRestriction(Biome.BiomeCategory.THEEND);
   public static final BiomeRestriction BEACH = new BiomeRestriction(Biome.BiomeCategory.BEACH);
   public static final BiomeRestriction FOREST = new BiomeRestriction(Biome.BiomeCategory.FOREST);
   public static final BiomeRestriction OCEAN = new BiomeRestriction(Biome.BiomeCategory.OCEAN);
   public static final BiomeRestriction DESERT = new BiomeRestriction(Biome.BiomeCategory.DESERT);
   public static final BiomeRestriction RIVER = new BiomeRestriction(Biome.BiomeCategory.RIVER);
   public static final BiomeRestriction SWAMP = new BiomeRestriction(Biome.BiomeCategory.SWAMP);
   public static final BiomeRestriction MUSHROOM = new BiomeRestriction(Biome.BiomeCategory.MUSHROOM);
   public static final BiomeRestriction NETHER = new BiomeRestriction(Biome.BiomeCategory.NETHER);
   public static final BiomeRestriction DRIPSTONE_CAVES = new BiomeRestriction(Biomes.DRIPSTONE_CAVES);
   public static final BiomeRestriction BADLANDS = new BiomeRestriction(Biomes.BADLANDS);

    private List<Biome> biomes = new ArrayList<>();
    private Restriction.Type restrictionType;

    public BiomeRestriction() {
        this.restrictionType = Restriction.Type.NONE;
    }

    public BiomeRestriction(ResourceKey<Biome> biome) {
        this(BiomeHelper.getBiome(biome));
    }

    public BiomeRestriction(Biome biome) {
        this(Restriction.Type.WHITELIST, biome);
    }

    public BiomeRestriction(Restriction.Type restrictionType, Biome biome) {
        this(restrictionType, biome, new Biome[0]);
    }

    public BiomeRestriction(Biome biome, Biome... moreBiomes) {
        this(Restriction.Type.WHITELIST, biome, moreBiomes);
    }

    public BiomeRestriction(Restriction.Type restrictionType, Biome biome, Biome... moreBiomes) {
        this.restrictionType = restrictionType;
        switch (restrictionType) {
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

    public BiomeRestriction(Biomes biomeCategory, Biomes... biomeCategories) {
        this(Restriction.Type.WHITELIST, biomeCategory, biomeCategories);
    }

    public BiomeRestriction(Restriction.Type restrictionType, Biomes biomeCategory, Biomes... biomeCategories) {
        this.restrictionType = restrictionType;
        switch (restrictionType) {
            case NONE:
                break;
            case WHITELIST:
                biomes = getBiomes(biomeCategory, biomeCategories);
                break;
            default:
                biomes = BiomeHelper.getAllBiomes();
                biomes.removeAll(getBiomes(biomeCategory, biomeCategories));
        }
    }

    private ArrayList<Biome> getBiomes(Biomes biomeCategory, Biomes... biomeCategories) {
        ArrayList<Biome> biomes = new ArrayList<>();
        biomes.addAll(BiomeHelper.getBiomes(biomeCategory));
        for (int i = 1; i < biomeCategories.length; i++) {
            ArrayList<Biome> newBiomes = new ArrayList<>();
            for (Biome biome : BiomeHelper.getBiomes(biomeCategories[i])) {
                if (biomes.remove(biome)) newBiomes.add(biome); // intersection of all selected categories
            }
            biomes = newBiomes;
        }
        return biomes;
    }

    public List<String> toStringList() {
        return biomes.stream().filter(biome -> !biome.toString().equals("")).map(biome -> "  " + I18n.get("biome." + biome.getRegistryName().toString().replace(":","."))).collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BiomeRestriction) {
            BiomeRestriction other = (BiomeRestriction) obj;
            return other.biomes.size() == biomes.size() && other.biomes.containsAll(biomes);
        }
        return false;
    }

    public boolean isMergeAble(BiomeRestriction other) {
        return other.restrictionType == Restriction.Type.NONE || (this.restrictionType != Restriction.Type.NONE && !biomes.isEmpty() && other.biomes.containsAll(biomes));
    }

    @Override
    public String toString() {
        return "Biomes: " + restrictionType + (restrictionType != Restriction.Type.NONE ? " - " + biomes.size() : "");
    }

    @Override
    public int hashCode() {
        return restrictionType.hashCode() ^ biomes.hashCode();
    }
}
