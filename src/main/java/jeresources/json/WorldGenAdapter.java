package jeresources.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jeresources.api.distributions.DistributionBase;
import jeresources.api.distributions.DistributionCustom;
import jeresources.api.distributions.DistributionHelpers;
import jeresources.api.drop.LootDrop;
import jeresources.api.restrictions.Restriction;
import jeresources.config.ConfigHandler;
import jeresources.entry.WorldGenEntry;
import jeresources.registry.WorldGenRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorldGenAdapter
{
    public static boolean hasWorldGenDIYData()
    {
        return ConfigHandler.getWorldGenFile().exists();
    }

    public static boolean readDIYData()
    {
        JsonParser parser = new JsonParser();
        try
        {
            JsonElement base = parser.parse(new FileReader(ConfigHandler.getWorldGenFile()));
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
                JsonElement silk = obj.get("silktouch");
                boolean silktouch = silk != null && silk.getAsBoolean();

                JsonElement dimElement = obj.get("dim");
                String dim = dimElement != null ? dimElement.getAsString() : "";

                String[] blockParts = block.split(":");

                Block blockBlock = Block.REGISTRY.getObject(new ResourceLocation(blockParts[0], blockParts[1]));
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

                JsonElement dropsListElement = obj.get("dropsList");
                List<LootDrop> dropList = new ArrayList<>();
                if (dropsListElement != null)
                {
                    JsonArray drops = dropsListElement.getAsJsonArray();
                    for (JsonElement dropElement : drops)
                    {
                        JsonObject drop = dropElement.getAsJsonObject();
                        JsonElement itemStackElement = drop.get("itemStack");
                        String itemStackString = itemStackElement.getAsString();
                        String[] stackStrings = itemStackString.split(":", 4);
                        Item item = Item.REGISTRY.getObject(new ResourceLocation(stackStrings[0], stackStrings[1]));
                        if (item == null)
                            continue;

                        ItemStack itemStack = new ItemStack(item);
                        if (stackStrings.length >= 3)
                        {
                            itemStack.setItemDamage(Integer.valueOf(stackStrings[2]));
                        }

                        if (stackStrings.length == 4)
                        {
                            try
                            {
                                itemStack.setTagCompound(JsonToNBT.getTagFromJson(stackStrings[3]));
                            }
                            catch (NBTException e)
                            {
                                e.printStackTrace();
                            }
                        }

                        JsonElement fortuneElement = drop.get("fortunes");
                        if (fortuneElement != null)
                        {
                            JsonObject fortunes = fortuneElement.getAsJsonObject();
                            for (Map.Entry<String, JsonElement> fortuneValue : fortunes.entrySet())
                            {
                                int fortuneLevel = Integer.valueOf(fortuneValue.getKey());
                                float dropAmount = fortuneValue.getValue().getAsFloat();
                                dropList.add(new LootDrop(itemStack, dropAmount, fortuneLevel));
                            }
                        }
                    }
                }

                WorldGenRegistry.getInstance().registerEntry(new WorldGenEntry(blockStack, distribution, getRestriction(dim), silktouch, dropList.toArray(new LootDrop[dropList.size()])));
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
