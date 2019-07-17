package jeresources.util;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class BiomeHelper {
    public static List<Biome> getAllBiomes() {
        List<Biome> biomes = new ArrayList<>();
        ForgeRegistries.BIOMES.forEach(biomes::add);
        return biomes;
    }
}
