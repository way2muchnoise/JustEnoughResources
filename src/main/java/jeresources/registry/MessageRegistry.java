package jeresources.registry;

import jeresources.api.messages.*;
import jeresources.api.messages.utils.MessageKeys;
import jeresources.api.utils.Priority;
import jeresources.entries.DungeonEntry;
import jeresources.entries.MobEntry;
import net.minecraft.nbt.NBTTagCompound;

import java.util.LinkedHashSet;
import java.util.Set;

public class MessageRegistry
{
    private static Set<Message> registerMessages = new LinkedHashSet<Message>();
    private static Set<ModifyMessage> modifyMessages = new LinkedHashSet<ModifyMessage>();
    private static Set<RegistryMessage> registryMessages = new LinkedHashSet<RegistryMessage>();
    
    public static void clear()
    {
        registerMessages = new LinkedHashSet<Message>();
        modifyMessages = new LinkedHashSet<ModifyMessage>();
        registryMessages = new LinkedHashSet<RegistryMessage>();
    }

    private static void getModifyMessages(Priority priority, Set<ModifyMessage> add, Set<ModifyMessage> remove)
    {
        for (ModifyMessage message : modifyMessages)
        {
            if (message.hasAdd() && message.getAddPriority() == priority) add.add(message);
            if (message.hasRemove() && message.getRemovePriority() == priority) remove.add(message);
        }
    }

    private static void getRegistryMessages(Priority priority, Set<RegistryMessage> add, Set<RegistryMessage> remove)
    {
        for (RegistryMessage message : registryMessages)
        {
            if (message.getAddPriority() == priority) add.add(message);
            if (message.getRemovePriority() == priority) remove.add(message);
        }
    }

    public static void addMessage(Message message)
    {
        if (message == null || !message.isValid()) return;
        if (message instanceof ModifyMessage) modifyMessages.add((ModifyMessage) message);
        else if (message instanceof RegistryMessage) registryMessages.add((RegistryMessage) message);
    }

    public static void registerMessage(String key, NBTTagCompound tagCompound)
    {
        Message message = null;
        if (key.equals(MessageKeys.registerMob)) message = new RegisterMobMessage(tagCompound);
        else if (key.equals(MessageKeys.registerOre)) message = new RegisterOreMessage(tagCompound);
        else if (key.equals(MessageKeys.registerDungeon)) message = new RegisterDungeonMessage(tagCompound);
        else if (key.equals(MessageKeys.modifyMob)) message = new ModifyMobMessage(tagCompound);
        else if (key.equals(MessageKeys.modifyOre)) message = new ModifyOreMessage(tagCompound);
        else if (key.equals(MessageKeys.modifyPlant)) message = new ModifyPlantMessage(tagCompound);
        else if (key.equals(MessageKeys.removeMob)) message = new RemoveMobMessage(tagCompound);
        addMessage(message);
    }

    public static void processMessages()
    {
        for (Priority priority : Priority.values())
        {
            Set<RegistryMessage> addMessages = new LinkedHashSet<RegistryMessage>();
            Set<RegistryMessage> removeMessages = new LinkedHashSet<RegistryMessage>();
            getRegistryMessages(priority, addMessages, removeMessages);
            for (RegistryMessage message : addMessages)
            {
                if (message instanceof RegisterOreMessage)
                    OreRegistry.registerOre((RegisterOreMessage) message);
                else if (message instanceof RegisterMobMessage)
                    MobRegistry.getInstance().registerMob(new MobEntry((RegisterMobMessage) message));
                else if (message instanceof RegisterDungeonMessage)
                    DungeonRegistry.getInstance().registerDungeonEntry(new DungeonEntry((RegisterDungeonMessage) message));
            }
            for (RegistryMessage message : removeMessages)
            {
                if (message instanceof RemoveMobMessage) MobRegistry.getInstance().removeMob((RemoveMobMessage) message);
            }
        }

        for (Priority priority : Priority.values())
        {
            Set<ModifyMessage> addMessages = new LinkedHashSet<ModifyMessage>();
            Set<ModifyMessage> removeMessages = new LinkedHashSet<ModifyMessage>();
            getModifyMessages(priority, addMessages, removeMessages);
            for (ModifyMessage addMessage : addMessages)
            {
                if (addMessage instanceof ModifyMobMessage)
                    MobRegistry.getInstance().addMobDrops((ModifyMobMessage) addMessage);
                else if (addMessage instanceof ModifyOreMessage)
                    OreRegistry.addDrops((ModifyOreMessage) addMessage);
            }
            for (ModifyMessage removeMessage : removeMessages)
            {
                if (removeMessage instanceof ModifyMobMessage)
                    MobRegistry.getInstance().removeMobDrops((ModifyMobMessage) removeMessage);
                else if (removeMessage instanceof ModifyOreMessage)
                    OreRegistry.removeDrops((ModifyOreMessage) removeMessage);
            }
        }
    }
}
