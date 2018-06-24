package jeresources.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jeresources.api.distributions.DistributionBase;
import jeresources.api.distributions.DistributionCustom;
import jeresources.api.distributions.DistributionHelpers;
import jeresources.api.distributions.DistributionSquare;
import jeresources.api.distributions.DistributionTriangular;
import jeresources.api.distributions.DistributionUnderWater;
import jeresources.api.drop.LootDrop;
import jeresources.api.restrictions.BiomeRestriction;
import jeresources.api.restrictions.DimensionRestriction;
import jeresources.api.restrictions.Restriction;
import jeresources.config.ConfigHandler;
import jeresources.entry.WorldGenEntry;
import jeresources.registry.WorldGenRegistry;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.Loader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import org.apache.commons.lang3.ArrayUtils;

public class WorldGenAdapter {
    public static boolean hasWorldGenDIYData() {
        return ConfigHandler.getWorldGenFile().exists();
    }

    public static boolean readDIYData() {
        JsonParser parser = new JsonParser();
        try {
            JsonElement base = parser.parse(new FileReader(ConfigHandler.getWorldGenFile()));
            if (!base.isJsonArray() || base.getAsJsonArray().size() == 0) return false;
            JsonArray array = base.getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                JsonObject obj = array.get(i).getAsJsonObject();

                JsonElement element = obj.get("mod"); // use of "mod": "modID"
                if (element != null)
                    if (!Loader.isModLoaded(element.getAsString())) // when modID is not loaded skip item
                        continue;

                String block = obj.get("block").getAsString();
                JsonObject distribObject = obj.get("distrib").getAsJsonObject();
                if (distribObject == null)
                    continue;

                String distribType = distribObject.get("type").getAsString();
                String distrib = distribObject.get("value").getAsString();
                JsonElement silk = obj.get("silktouch");
                boolean silktouch = silk != null && silk.getAsBoolean();

                JsonElement dimElement = obj.get("dim");
                String dim = dimElement != null ? dimElement.getAsString() : "";

                JsonArray biomesArray = obj.get("biomes").getAsJsonArray();
                Biome[] biomes = {};
                for (JsonElement biomeElement : biomesArray) {
                	int biomeId;
                	try {
                		biomeId = biomeElement.getAsInt();
                	} catch (Exception e) {
                		break;
                	}
                	biomes = ArrayUtils.add(biomes, Biome.getBiome(biomeId));
                }

                String[] blockParts = block.split(":");

                Block blockBlock = Block.REGISTRY.getObject(new ResourceLocation(blockParts[0], blockParts[1]));
                if (blockBlock == null || Item.getItemFromBlock(blockBlock) == null) continue;
                int oreMeta = blockParts.length == 3 ? Integer.parseInt(blockParts[2]) : 0;
                ItemStack blockStack = new ItemStack(blockBlock, 1, oreMeta);

                DistributionBase distribution = new DistributionSquare(0, 0, 0, 255);
                switch (distribType.toLowerCase()) {
	                case "points":
		                List<DistributionHelpers.OrePoint> points = new ArrayList<>();
		                for (String point : distrib.split(";")) {
		                    String[] split = point.split(",");
		                    if (split.length == 2)
		                        points.add(new DistributionHelpers.OrePoint(Integer.parseInt(split[0]), Float.parseFloat(split[1])));
		                }
		                distribution = new DistributionCustom(DistributionHelpers.getDistributionFromPoints(points.toArray(new DistributionHelpers.OrePoint[points.size()])));
		                break;
	                case "square":
		                String[] squareVals = distrib.split(",");
		                switch (squareVals.length) {
		                	case 3:
		                		try {
			                		distribution = new DistributionSquare(Integer.parseInt(squareVals[0]), Integer.parseInt(squareVals[1]), Float.parseFloat(squareVals[2]));
		                		} catch (NumberFormatException e) {
		                			continue;
		                		}
		                		break;
		                	case 4:
		                		try {
			                		distribution = new DistributionSquare(Integer.parseInt(squareVals[0]), Integer.parseInt(squareVals[1]), Integer.parseInt(squareVals[2]), Integer.parseInt(squareVals[3]));
		                		} catch (NumberFormatException e) {
		                			continue;
		                		}
		                		break;
		                	case 5:
		                		try {
			                		distribution = new DistributionSquare(Integer.parseInt(squareVals[0]), Integer.parseInt(squareVals[1]), Integer.parseInt(squareVals[2]), Integer.parseInt(squareVals[3]), Float.parseFloat(squareVals[4]));
		                		} catch (NumberFormatException e) {
		                			continue;
		                		}
		                		break;
		                	default:
		                		continue;
		                }
		                break;
	                case "triangular":
	                	String[] triangularVals = distrib.split(",");
	                	if (triangularVals.length != 3)
	                		continue;
	                	try {
	                		distribution = new DistributionTriangular(Integer.parseInt(triangularVals[0]), Integer.parseInt(triangularVals[1]), Float.parseFloat((triangularVals[2])));
	                	} catch (NumberFormatException e) {
	                		continue;
	                	}
	                	break;
	                case "underwater":
	                	String[] underwaterVals = distrib.split(",");
	                	if (underwaterVals.length != 1)
	                		continue;
	                	try {
	                		distribution = new DistributionUnderWater(Float.parseFloat((underwaterVals[0])));
	                	} catch (NumberFormatException e) {
	                		continue;
	                	}
	                	break;
		            default:
		            	continue;
                }

                JsonElement dropsListElement = obj.get("dropsList");
                List<LootDrop> dropList = new LinkedList<>();
                if (dropsListElement != null) {
                    JsonArray drops = dropsListElement.getAsJsonArray();
                    for (JsonElement dropElement : drops) {
                        JsonObject drop = dropElement.getAsJsonObject();
                        JsonElement itemStackElement = drop.get("itemStack");
                        if (itemStackElement.isJsonNull()) continue;
                        String itemStackString = itemStackElement.getAsString();
                        String[] stackStrings = itemStackString.split(":", 4);
                        Item item = Item.REGISTRY.getObject(new ResourceLocation(stackStrings[0], stackStrings[1]));
                        if (item == null)
                            continue;

                        ItemStack itemStack = new ItemStack(item);
                        if (stackStrings.length >= 3) {
                            itemStack.setItemDamage(Integer.valueOf(stackStrings[2]));
                        }

                        if (stackStrings.length == 4) {
                            try {
                                itemStack.setTagCompound(JsonToNBT.getTagFromJson(stackStrings[3]));
                            } catch (NBTException e) {
                                e.printStackTrace();
                            }
                        }

                        JsonElement fortuneElement = drop.get("fortunes");
                        if (fortuneElement != null) {
                            JsonObject fortunes = fortuneElement.getAsJsonObject();
                            for (Map.Entry<String, JsonElement> fortuneValue : fortunes.entrySet()) {
                                int fortuneLevel = Integer.valueOf(fortuneValue.getKey());
                                float dropAmount = fortuneValue.getValue().getAsFloat();
                                dropList.add(new LootDrop(itemStack, dropAmount, fortuneLevel));
                            }
                        }
                    }
                }

                if ((blockStack.isEmpty() || blockStack.getItem() == Items.AIR) && dropList.size() > 0) {
                    // Some blocks don't have an item equivalent so we use the first drop
                    blockStack = dropList.get(0).item.copy();
                    blockStack.setCount(1);
                }

                WorldGenRegistry.getInstance().registerEntry(new WorldGenEntry(blockStack, distribution, getRestriction(biomes, dim), silktouch, dropList.toArray(new LootDrop[dropList.size()])));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        map.clear();
        return true;
    }

    private static Map<String, Restriction> map = new HashMap<>();

    private static Restriction getRestriction(Biome[] biomes, String dim) {
    	if (biomes.length > 0)
    		return new Restriction(new BiomeRestriction(biomes), new DimensionRestriction(dim));
		return map.computeIfAbsent(dim, k -> new Restriction(new DimensionRestriction(dim)));
    }
}
