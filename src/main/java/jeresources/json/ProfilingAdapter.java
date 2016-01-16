package jeresources.json;

import com.google.gson.stream.JsonWriter;
import jeresources.config.ConfigHandler;
import net.minecraft.block.Block;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class ProfilingAdapter
{
    public static void write(final Map<Block, Float[]> data)
    {
        try
        {
            JsonWriter writer = new JsonWriter(new FileWriter(new File(ConfigHandler.getConfigDir(), "blocks.json")));
            writer.setIndent("\t");
            writer.beginArray();
            for (Map.Entry<Block, Float[]> entry : data.entrySet())
            {
                writer.beginObject();
                writer.name("block").value(entry.getKey().getRegistryName());
                StringBuilder sb = new StringBuilder();
                sb.append(0).append(",").append(entry.getValue()[0]).append(";");
                for (int i = 1; i < entry.getValue().length; i++)
                    if (entry.getValue()[i-1].compareTo(entry.getValue()[i]) != 0)
                        sb.append(i).append(",").append(entry.getValue()[i]).append(";");
                writer.name("distrib").value(sb.toString());
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
