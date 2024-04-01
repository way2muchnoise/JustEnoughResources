package jeresources.neoforge.event;

import jeresources.profiling.ProfileCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class Commands {
    @SubscribeEvent
    public void registerCommand(RegisterCommandsEvent event) {
        new ProfileCommand().register(event.getDispatcher());
    }
}
