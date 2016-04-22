package jeresources.util;

import net.minecraft.world.biome.BiomeGenBase;

import java.util.ArrayList;
import java.util.List;

public class BiomeHelper
{
    public static List<BiomeGenBase> getAllBiomes()
    {
        List<BiomeGenBase> biomes = new ArrayList<>();
        for (BiomeGenBase biomeGenBase : BiomeGenBase.REGISTRY) biomes.add(biomeGenBase);
        return biomes;
    }
}
