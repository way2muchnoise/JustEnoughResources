package jeresources.json;

import com.google.common.collect.Sets;
import com.google.gson.stream.JsonWriter;
import jeresources.config.ConfigHandler;
import net.minecraftforge.common.DimensionManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProfilingAdapter
{

    public static class DimensionData
    {
        public Map<String, Float[]> distribution = new HashMap<>();
        public Map<String, Boolean> silkTouchMap = new HashMap<>();
        public Map<String, Map<String, Map<Integer, Float>>> dropsMap = new HashMap<>();
    }

    public static void write(final Map<Integer, DimensionData> allDimensionData)
    {
        try
        {
            JsonWriter writer = new JsonWriter(new FileWriter(new File(ConfigHandler.getConfigDir(), "world-gen-scan.json")));
            writer.setIndent("\t");
            writer.beginArray();

            for (int dim : allDimensionData.keySet())
            {
                DimensionData dimensionData = allDimensionData.get(dim);

                Set<String> blockKeys = Sets.union(dimensionData.distribution.keySet(), dimensionData.dropsMap.keySet());

                for (String blockKey : blockKeys)
                {
                    writer.beginObject();
                    writer.name("block").value(blockKey);

                    Float[] distribution = dimensionData.distribution.get(blockKey);
                    if (distribution != null && distribution.length > 0)
                    {
                        StringBuilder sb = new StringBuilder();
                        int lastPrint = 0;
                        sb.append(0).append(",").append(distribution[0]).append(";");
                        for (int i = 1; i < distribution.length; i++)
                        {
                            if (distribution[i - 1].compareTo(distribution[i]) != 0)
                            {
                                if (lastPrint != i - 1)
                                    sb.append(i - 1).append(",").append(distribution[i - 1]).append(";");
                                sb.append(i).append(",").append(distribution[i]).append(";");
                                lastPrint = i;
                            }
                        }
                        writer.name("distrib").value(sb.toString());
                    }

                    Map<String, Map<Integer, Float>> drops = dimensionData.dropsMap.get(blockKey);
                    if (drops != null && !drops.isEmpty())
                    {
                        StringBuilder dropsString = new StringBuilder();
                        for (Map.Entry<String, Map<Integer, Float>> dropEntry : drops.entrySet())
                            for (Map.Entry<Integer, Float> fortuneEntry : dropEntry.getValue().entrySet())
                                dropsString.append(dropEntry.getKey()).append(":").append(fortuneEntry.getValue()).append(":").append(fortuneEntry.getKey()).append(",");
                        writer.name("drops").value(dropsString.toString());
                    }

                    Boolean canSilkTouch = dimensionData.silkTouchMap.get(blockKey);
                    if (canSilkTouch != null)
                    {
                        writer.name("silktouch").value(canSilkTouch);
                    }

                    writer.name("dim").value(DimensionManager.getProvider(dim).getDimensionType().getName());
                    writer.endObject();
                }
            }
            writer.endArray();
            writer.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
