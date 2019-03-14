package jeresources.profiling;

import jeresources.config.ConfigHandler;
import net.minecraft.block.state.IBlockState;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

public class ProfilingBlacklist {

    private List<String> blacklist;

    public ProfilingBlacklist() {
        blacklist = new LinkedList<>();
        File scanBlacklistFile = ConfigHandler.getScanBlacklistFile();
        if (scanBlacklistFile.exists()) {
            try {
                blacklist = Files.readAllLines(scanBlacklistFile.toPath());
            } catch (IOException ignored) {

            }
        }
    }

    public boolean contains(IBlockState blockState) {
        final String blockString = blockState.toString();
        return blacklist.stream().anyMatch((blockString::startsWith));
    }
}
