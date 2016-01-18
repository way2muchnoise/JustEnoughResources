package jeresources.api.messages;

import jeresources.api.messages.utils.MessageKeys;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.relauncher.Side;

import java.util.LinkedList;
import java.util.List;

public class SendMessage
{
    private static List<Message.Storage> storage = new LinkedList<Message.Storage>();

    public static void sendMessage(RegisterDungeonMessage message)
    {
        sendMessage(message, MessageKeys.registerDungeon);
    }

    public static void sendMessage(RegisterOreMessage message)
    {
        sendMessage(message, MessageKeys.registerOre);
    }

    public static void sendMessage(ModifyOreMessage message)
    {
        sendMessage(message, MessageKeys.modifyOre);
    }

    public static void sendMessage(RegisterMobMessage message)
    {
        sendMessage(message, MessageKeys.registerMob);
    }

    public static void sendMessage(ModifyMobMessage message)
    {
        sendMessage(message, MessageKeys.modifyMob);
    }

    public static void sendMessage(RegisterPlantMessage message)
    {
        sendMessage(message, MessageKeys.registerPlant);
    }

    public static void sendMessage(ModifyPlantMessage message)
    {
        sendMessage(message, MessageKeys.modifyPlant);
    }

    public static void sendMessage(RemoveMobMessage message)
    {
        sendMessage(message, MessageKeys.removeMob);
    }

    public static void sendMessage(RemovePlantMessage message)
    {
        sendMessage(message, MessageKeys.removePlant);
    }

    public static void sendMessage(Message message, String key)
    {
        if (message.isValid())
        {
            FMLInterModComms.sendMessage(MessageKeys.notEnoughResources, key, message.getMessage());
            if (FMLCommonHandler.instance().getSide() == Side.SERVER) storage.add(new Message.Storage(key, message));
        }
    }

    public static void readFromStorage(List<Message.Storage> storage)
    {
        for (Message.Storage stored : storage)
            sendMessage(stored.getMessage(), stored.getKey());
    }

    public static List<Message.Storage> getStorage()
    {
        return storage;
    }
}
