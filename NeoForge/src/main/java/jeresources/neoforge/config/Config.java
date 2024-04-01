package jeresources.neoforge.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import jeresources.reference.Reference;
import jeresources.util.LogHelper;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.nio.file.Path;

public class Config {
    public static Config instance = new Config();

    private Config() {

    }

    public static final ModConfigSpec COMMON = ConfigValues.build();

    public void loadConfig(ModConfigSpec spec, Path path) {
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
