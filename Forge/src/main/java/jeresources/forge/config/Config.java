package jeresources.forge.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import jeresources.reference.Reference;
import jeresources.util.LogHelper;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.nio.file.Path;

public class Config {
    public static Config instance = new Config();

    private Config() {

    }

    public static final ForgeConfigSpec COMMON = ConfigValues.build();

    public void loadConfig(ForgeConfigSpec spec, Path path) {
        LogHelper.debug("Loading config file {}", path);

        final CommentedFileConfig configData = CommentedFileConfig.builder(path)
            .sync()
            .autosave()
            .writingMode(WritingMode.REPLACE)
            .build();

        LogHelper.debug("Built TOML config for {}", path.toString());
        configData.load();
        LogHelper.debug("Loaded TOML config file {}", path.toString());
        spec.setConfig(configData);
        ConfigValues.pushChanges();
    }

    @SubscribeEvent
    public void onLoad(final ModConfigEvent.Loading configEvent) {
        LogHelper.debug("Loaded {} config file {}", Reference.ID, configEvent.getConfig().getFileName());
        ConfigValues.pushChanges();
    }

    @SubscribeEvent
    public void onFileChange(final ModConfigEvent.Reloading configEvent) {
        LogHelper.debug("Reloaded {} config file {}", Reference.ID, configEvent.getConfig().getFileName());
        ConfigValues.pushChanges();
    }
}
