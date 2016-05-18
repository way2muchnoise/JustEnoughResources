package jeresources.util;

import net.minecraft.world.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class BiomeHelper
{
    public static List<Biome> getAllBiomes()
    {
        List<Biome> biomes = new ArrayList<>();
        Biome.REGISTRY.forEach(biomes::add);
        return biomes;
    }
}
