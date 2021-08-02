package jeresources.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import jeresources.api.distributions.DistributionBase;
import jeresources.api.distributions.DistributionCustom;
import jeresources.api.distributions.DistributionHelpers;
import jeresources.api.drop.LootDrop;
import jeresources.api.restrictions.DimensionRestriction;
import jeresources.api.restrictions.Restriction;
import jeresources.entry.WorldGenEntry;
import jeresources.registry.WorldGenRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Dimension;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class WorldGenAdapter {
    private static final String worldGenFileName = "world-gen.json";

    public static File getWorldGenFile() {
        return FMLPaths.CONFIGDIR.get().resolve(worldGenFileName).toFile();
    }

    public static boolean hasWorldGenDIYData() {
        return getWorldGenFile().exists();
    }

    public static boolean readDIYData() {
        JsonParser parser = new JsonParser();
        try {
            JsonElement base = parser.parse(new FileReader(getWorldGenFile()));
            if (!base.isJsonArray() || base.getAsJsonArray().size() == 0) return false;
            JsonArray array = base.getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                JsonObject obj = array.get(i).getAsJsonObject();

                JsonElement element = obj.get("mod"); // use of "mod": "modID"
                if (element != null)
                    if (!ModList.get().isLoaded(element.getAsString())) // when modID is not loaded skip item
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

                Block blockBlock = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockParts[0], blockParts[1]));
                if (blockBlock == null || Item.byBlock(blockBlock) == null) continue;
                //int oreMeta = blockParts.length == 3 ? Integer.parseInt(blockParts[2]) : 0;
                ItemStack blockStack = new ItemStack(blockBlock, 1, new CompoundNBT());
                List<DistributionHelpers.OrePoint> points = new ArrayList<>();
                for (String point : distrib.split(";")) {
                    String[] split = point.split(",");
                    if (split.length == 2)
                        points.add(new DistributionHelpers.OrePoint(Integer.parseInt(split[0]), Float.parseFloat(split[1])));
                }
                DistributionBase distribution = new DistributionCustom(DistributionHelpers.getDistributionFromPoints(points.toArray(new DistributionHelpers.OrePoint[points.size()])));

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
                        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(stackStrings[0], stackStrings[1]));
                        if (item == null)
                            continue;

                        ItemStack itemStack = new ItemStack(item);
                        if (stackStrings.length >= 3) {
                            itemStack.setDamageValue(Integer.valueOf(stackStrings[2]));
                        }

                        if (stackStrings.length == 4) {
                            try {
                                itemStack.setTag(JsonToNBT.parseTag(stackStrings[3]));
                            } catch (CommandSyntaxException e) {
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

                WorldGenRegistry.getInstance().registerEntry(new WorldGenEntry(blockStack, distribution, getRestriction(dim), silktouch, dropList.toArray(new LootDrop[dropList.size()])));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        map.clear();
        return true;
    }

    private static Map<RegistryKey<World>, Restriction> map = new HashMap<>();

    private static Restriction getRestriction(String dim) {
        RegistryKey<World> worldRegistryKey = RegistryKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(dim));
        return map.computeIfAbsent(worldRegistryKey, k -> new Restriction(new DimensionRestriction(worldRegistryKey)));
    }
}
