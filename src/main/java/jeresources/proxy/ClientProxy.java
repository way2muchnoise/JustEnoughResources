package jeresources.proxy;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import jeresources.utils.NetworkEventHelper;
import net.minecraft.world.World;

public class ClientProxy extends CommonProxy
{
    @Override
    public World getClientWorld()
    {
        return FMLClientHandler.instance().getClient().theWorld;
    }

    @Override
    public void registerEvents()
    {
        super.registerEvents();
        MinecraftForge.EVENT_BUS.register(new NetworkEventHelper());
    }
}
