package jeresources.forge.event;

import jeresources.profiling.ProfileCommand;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class Commands {
    @SubscribeEvent
    public void registerCommand(RegisterCommandsEvent event) {
        new ProfileCommand().register(event.getDispatcher());
    }
}
