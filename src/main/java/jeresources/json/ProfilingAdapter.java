package jeresources.json;

import com.google.common.collect.Sets;
import com.google.gson.stream.JsonWriter;
import jeresources.util.DimensionHelper;
import jeresources.util.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProfilingAdapter {

    public static class DimensionData {
        public Map<String, Float[]> distribution = new HashMap<>();
        public Map<String, Boolean> silkTouchMap = new HashMap<>();
        public Map<String, Map<String, Map<Integer, Float>>> dropsMap = new HashMap<>();
    }

    public static void write(final Map<RegistryKey<World>, DimensionData> allDimensionData) {

        File oldWorldGenFile = WorldGenAdapter.getWorldGenFile();
        if (oldWorldGenFile.exists()) {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
            boolean renamed = oldWorldGenFile.renameTo(FMLPaths.CONFIGDIR.get().resolve("world-gen-old-" + dateFormat.format(date) + ".json").toFile());
            if (!renamed) {
                LogHelper.warn("Could not rename old world-gen file. Aborting.");
                return;
            }
        }

        try {
            JsonWriter writer = new JsonWriter(new FileWriter(WorldGenAdapter.getWorldGenFile()));
            writer.setIndent("\t");
            writer.beginArray();

            for (RegistryKey<World> worldRegistryKey : allDimensionData.keySet()) {
                DimensionData dimensionData = allDimensionData.get(worldRegistryKey);

                Set<String> blockKeys = Sets.union(dimensionData.distribution.keySet(), dimensionData.dropsMap.keySet());

                for (String blockKey : blockKeys) {
                    writer.beginObject();
                    writer.name("block").value(blockKey);

                    Float[] distribution = dimensionData.distribution.get(blockKey);
                    if (distribution != null && distribution.length > 0) {
                        StringBuilder sb = new StringBuilder();
                        int lastPrint = 0;
                        sb.append(0).append(",").append(distribution[0]).append(";");
                        for (int i = 1; i < distribution.length; i++) {
                            if (distribution[i - 1].compareTo(distribution[i]) != 0) {
                                if (lastPrint != i - 1)
                                    sb.append(i - 1).append(",").append(distribution[i - 1]).append(";");
                                sb.append(i).append(",").append(distribution[i]).append(";");
                                lastPrint = i;
                            }
                        }
                        writer.name("distrib").value(sb.toString());
                    }

                    Map<String, Map<Integer, Float>> drops = dimensionData.dropsMap.get(blockKey);

                    Boolean canSilkTouch = dimensionData.silkTouchMap.get(blockKey);
                    if (canSilkTouch != null) {
                        if (drops != null && !drops.isEmpty()) {
                            if (canSilkTouch && drops.containsKey(blockKey)) {
                                drops.remove(blockKey);
                                canSilkTouch = false;
                            }
                        }
                        writer.name("silktouch").value(canSilkTouch);
                    }

                    if (drops != null && !drops.isEmpty()) {
                        writer.name("dropsList");
                        writer.beginArray();
                        {
                            for (Map.Entry<String, Map<Integer, Float>> dropEntry : drops.entrySet()) {
                                writer.beginObject();
                                {
                                    writer.name("itemStack").value(dropEntry.getKey());
                                    writer.name("fortunes");
                                    writer.beginObject();
                                    {
                                        for (Map.Entry<Integer, Float> fortuneEntry : dropEntry.getValue().entrySet()) {
                                            writer.name(String.valueOf(fortuneEntry.getKey())).value(fortuneEntry.getValue());
                                        }
                                    }
                                    writer.endObject();
                                }
                                writer.endObject();
                            }
                        }
                        writer.endArray();
                    }

                    writer.name("dim").value(worldRegistryKey.getLocation().toString());
                    writer.endObject();
                }
            }
            writer.endArray();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
