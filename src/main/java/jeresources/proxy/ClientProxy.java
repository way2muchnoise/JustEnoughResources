package jeresources.proxy;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
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
        FMLCommonHandler.instance().bus().register(new NetworkEventHelper());
    }
}
