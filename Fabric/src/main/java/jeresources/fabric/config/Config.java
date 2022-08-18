package jeresources.fabric.config;

import jeresources.config.Settings;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class Config {
    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
            .setParentScreen(parent)
            .setTitle(Component.translatable("title.jeresources.config"));

        builder.setSavingRunnable(Config::saveAndReload);

        ConfigCategory mainCategory = builder.getOrCreateCategory(Component.translatable("category.jeresources.general"));
        ConfigValues.build(mainCategory, builder.entryBuilder());

        return builder.build();
    }

    public static void saveAndReload() {
        ConfigFileHandler.writeToConfig();
        Settings.reload();
    }
}
