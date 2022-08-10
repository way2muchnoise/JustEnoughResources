package jeresources.fabric.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import jeresources.config.Settings;
import jeresources.util.LogHelper;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ConfigFileHandler {
    public static void readFromConfig() {
        JsonObject root = new JsonObject();
        Gson gson = new Gson();
        try (FileReader file = new FileReader(getConfigFile())) {
            root = JsonParser.parseReader(file).getAsJsonObject();
        } catch (IOException e) {
            LogHelper.error(e.getMessage());
        }

        if (root.has("itemsPerColumn")) {
            Settings.ITEMS_PER_COLUMN = root.get("itemsPerColumn").getAsInt();
        }
        if (root.has("itemsPerRow")) {
            Settings.ITEMS_PER_ROW = root.get("itemsPerRow").getAsInt();
        }
        if (root.has("diyData")) {
            Settings.useDIYdata = root.get("diyData").getAsBoolean();
        }
        if (root.has("showDevData")) {
            Settings.showDevData = root.get("showDevData").getAsBoolean();
        }
        if (root.has("enchantsBlacklist")) {
            Settings.excludedEnchants = gson.fromJson(root.getAsJsonArray("enchantsBlacklist"), String[].class);
        }
        if (root.has("hiddenTabs")) {
            Settings.hiddenCategories = gson.fromJson(root.getAsJsonArray("hiddenTabs"), String[].class);
        }
        if (root.has("dimensionsBlacklist")) {
            Settings.excludedDimensions = Arrays.asList(gson.fromJson(root.getAsJsonArray("dimensionsBlacklist"), Integer[].class));
        }
        if (root.has("disableLootManagerReloading")) {
            Settings.disableLootManagerReloading = root.get("disableLootManagerReloading").getAsBoolean();
        }

    }

    public static void writeToConfig() {
        JsonObject root = new JsonObject();
        Gson gson = new Gson();

        root.addProperty("itemsPerColumn", Settings.ITEMS_PER_COLUMN);
        root.addProperty("itemsPerRow", Settings.ITEMS_PER_ROW);
        root.addProperty("diyData", Settings.useDIYdata);
        root.addProperty("showDevData", Settings.showDevData);
        root.add("enchantsBlacklist", gson.toJsonTree(Settings.excludedEnchants));
        root.add("hiddenTabs", gson.toJsonTree(Settings.hiddenCategories));
        root.add("dimensionsBlacklist", gson.toJsonTree(Settings.excludedDimensions));
        root.addProperty("disableLootManagerReloading", Settings.disableLootManagerReloading);

        try (FileWriter file = new FileWriter(getConfigFile())) {
            file.write(new GsonBuilder().setPrettyPrinting().create().toJson(root));
            file.flush();
        } catch (IOException e) {
            LogHelper.error(e.getMessage());
        }
    }

    public static File getConfigFile() {
        return FabricLoader.getInstance().getConfigDir().resolve("jeresources.json").toFile();
    }
}
