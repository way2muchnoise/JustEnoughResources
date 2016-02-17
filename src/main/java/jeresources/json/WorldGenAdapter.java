package jeresources.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jeresources.api.distributions.DistributionBase;
import jeresources.api.distributions.DistributionCustom;
import jeresources.api.distributions.DistributionHelpers;
import jeresources.api.drop.DropItem;
import jeresources.api.restrictions.Restriction;
import jeresources.config.ConfigHandler;
import jeresources.entries.WorldGenEntry;
import jeresources.registry.WorldGenRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class WorldGenAdapter
{
    public static boolean hasWorldGenDIYData()
    {
        return new File(ConfigHandler.getConfigDir(), "world-gen.json").exists();
    }

    public static boolean readDIYData()
    {
        JsonParser parser = new JsonParser();
        try
        {
            JsonElement base = parser.parse(new FileReader(new File(ConfigHandler.getConfigDir(), "world-gen.json")));
            if (!base.isJsonArray() || base.getAsJsonArray().size() == 0) return false;
            JsonArray array = base.getAsJsonArray();
            for (int i = 0; i < array.size(); i++)
            {
                JsonObject obj = array.get(i).getAsJsonObject();

                JsonElement element = obj.get("mod"); // use of "mod": "modID"
                if (element != null)
                    if (!Loader.isModLoaded(element.getAsString())) // when modID is not loaded skip item
                        continue;

                String block = obj.get("block").getAsString();
                JsonElement distribElement = obj.get("distrib");
                if (distribElement == null)
                    continue;

                String distrib = distribElement.getAsString();
                JsonElement dropsElement = obj.get("drops");
                String drops = dropsElement != null ? dropsElement.getAsString() : "";
                JsonElement silk = obj.get("silktouch");
                boolean silktouch = silk != null && silk.getAsBoolean();

                JsonElement dimElement = obj.get("dim");
                String dim = dimElement != null ? dimElement.getAsString() : "";

                String[] blockParts = block.split(":");

                Block blockBlock = GameRegistry.findBlock(blockParts[0], blockParts[1]);
                if (blockBlock == null || Item.getItemFromBlock(blockBlock) == null) continue;
                int oreMeta = blockParts.length == 3 ? Integer.parseInt(blockParts[2]) : 0;
                ItemStack blockStack = new ItemStack(blockBlock, 1, oreMeta);
                List<DistributionHelpers.OrePoint> points = new ArrayList<>();
                for (String point : distrib.split(";"))
                {
                    String[] split = point.split(",");
                    if (split.length == 2)
                        points.add(new DistributionHelpers.OrePoint(Integer.parseInt(split[0]), Float.parseFloat(split[1])));
                }
                DistributionBase distribution = new DistributionCustom(DistributionHelpers.getDistributionFromPoints(points.toArray(new DistributionHelpers.OrePoint[points.size()])));

                List<DropItem> dropList = new ArrayList<>();
                if (!drops.isEmpty())
                {
                    for (String drop : drops.split(","))
                    {
                        String[] dropSplit = drop.split(":");
                        Item item = GameRegistry.findItem(dropSplit[0], dropSplit[1]);
                        if (item == null) continue;

                        int meta = 0;
                        float averageAmount = 1;
                        int fortuneLevel = 0;
                        if (dropSplit.length >= 3)
                        {
                            meta = Integer.parseInt(dropSplit[2]);
                            if (dropSplit.length >= 4)
                            {
                                averageAmount = Float.parseFloat(dropSplit[3]);
                                if (dropSplit.length >= 5)
                                    fortuneLevel = Integer.parseInt(dropSplit[4]);
                            }
                        }
                        dropList.add(new DropItem(new ItemStack(item, 1, meta), averageAmount, fortuneLevel));
                    }
                }

                WorldGenRegistry.getInstance().registerEntry(new WorldGenEntry(blockStack, distribution, getRestriction(dim), silktouch, dropList.toArray(new DropItem[dropList.size()])));
            }
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return true;
    }

    private static Restriction getRestriction(String dim)
    {
        switch (dim.toLowerCase())
        {
            case "overworld":
                return Restriction.OVERWORLD;
            case "nether":
                return Restriction.NETHER;
            case "the end":
                return Restriction.END;
            default:
                return Restriction.OVERWORLD_LIKE;
        }
    }
}
