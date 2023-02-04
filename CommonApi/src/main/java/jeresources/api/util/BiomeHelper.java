package jeresources.api.util;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;

public class BiomeHelper {
    public static List<Biome> getAllBiomes() {
        List<Biome> biomes = new ArrayList<>();
        VanillaRegistries.createLookup().lookupOrThrow(Registries.BIOME).listElements().map(Holder.Reference::value).forEach(biomes::add);
        return biomes;
    }

    public static Biome getBiome(ResourceKey<Biome> key) {
        return VanillaRegistries.createLookup().lookupOrThrow(Registries.BIOME).getOrThrow(key).value();
    }

    public static List<Biome> getBiomes(ResourceKey<Biome> category) {
        List<Biome> biomes = new ArrayList<>();
        VanillaRegistries.createLookup().lookupOrThrow(Registries.BIOME).listElements().forEach(
            biome_entry -> {
                if (biome_entry.key().equals(category)) {
                    biomes.add(biome_entry.value());
                }
            }
        );
        return biomes;
    }
}
