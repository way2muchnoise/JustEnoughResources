package jeresources.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import jeresources.JEResources;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;

import java.nio.file.Path;

public class Config {
    public static Config instance = new Config();

    private Config() {

    }

    public static final ForgeConfigSpec COMMON = ConfigValues.build();

    public void loadConfig(ForgeConfigSpec spec, Path path) {
        JEResources.LOGGER.debug("Loading config file {}", path);

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
            .sync()
            .autosave()
            .writingMode(WritingMode.REPLACE)
            .build();

        JEResources.LOGGER.debug("Built TOML config for {}", path.toString());
        configData.load();
        JEResources.LOGGER.debug("Loaded TOML config file {}", path.toString());
        spec.setConfig(configData);
        ConfigValues.pushChanges();
    }

    @SubscribeEvent
    public void onLoad(final ModConfig.Loading configEvent) {
        JEResources.LOGGER.debug("Loaded {} config file {}", JEResources.ID, configEvent.getConfig().getFileName());
        ConfigValues.pushChanges();
    }

    @SubscribeEvent
    public void onFileChange(final ModConfig.Reloading configEvent) {
        JEResources.LOGGER.debug("Reloaded {} config file {}", JEResources.ID, configEvent.getConfig().getFileName());
        ConfigValues.pushChanges();
    }
}
