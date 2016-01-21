package jeresources.json;

import com.google.common.collect.Sets;
import com.google.gson.stream.JsonWriter;
import jeresources.config.ConfigHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class ProfilingAdapter
{
    public static void write(final Map<String, Float[]> distributionData, final Map<String, Boolean> silkTouchMap, Map<String, Map<String, Float>> dropsMap)
    {
        try
        {
            JsonWriter writer = new JsonWriter(new FileWriter(new File(ConfigHandler.getConfigDir(), "blocks.json")));
            writer.setIndent("\t");
            writer.beginArray();

            Set<String> blockKeys = Sets.union(distributionData.keySet(), dropsMap.keySet());

            for (String blockKey : blockKeys)
            {
                writer.beginObject();
                writer.name("ore").value(blockKey);

                Float[] distribution = distributionData.get(blockKey);
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

                Map<String, Float> drops = dropsMap.get(blockKey);
                if (drops != null && !drops.isEmpty())
                {
                    StringBuilder dropsString = new StringBuilder();
                    for (Map.Entry<String, Float> dropEntry : drops.entrySet())
                    {
                        dropsString.append(dropEntry.getKey()).append(":").append(dropEntry.getValue()).append(",");
                    }
                    writer.name("drops").value(dropsString.toString());
                }

                Boolean canSilkTouch = silkTouchMap.get(blockKey);
                if (canSilkTouch != null)
                {
                    writer.name("silktouch").value(canSilkTouch);
                }
                writer.endObject();
            }
            writer.endArray();
            writer.flush();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
