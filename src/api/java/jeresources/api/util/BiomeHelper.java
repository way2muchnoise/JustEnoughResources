package jeresources.api.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class BiomeHelper {
    public static List<Biome> getAllBiomes() {
        List<Biome> biomes = new ArrayList<>();
        ForgeRegistries.BIOMES.forEach(biomes::add);
        return biomes;
    }

    public static Biome getBiome(ResourceKey<Biome> key) {
        return ForgeRegistries.BIOMES.getValue(key.location());
    }

    public static List<Biome> getBiomes(Biomes category) {
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
