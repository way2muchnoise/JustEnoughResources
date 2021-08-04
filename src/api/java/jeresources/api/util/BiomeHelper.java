package jeresources.api.util;

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

    public static List<Biome> getBiomes(Biome.Category category) {
        List<Biome> biomes = new ArrayList<>();
        ForgeRegistries.BIOMES.forEach(
            biome -> {
                if (biome.getBiomeCategory().equals(category)) {
                    biomes.add(biome);
                }
            }
        );
        return biomes;
    }
}
