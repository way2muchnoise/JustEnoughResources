package jeresources.api.messages;

import jeresources.api.messages.utils.MessageKeys;
import jeresources.api.utils.Priority;
import net.minecraft.nbt.NBTTagCompound;

public abstract class RegistryMessage extends Message
{
    private final Priority priority;
    private final boolean add;

    public RegistryMessage(Priority priority, boolean add)
    {
        this.priority = priority;
        this.add = add;
    }

    public RegistryMessage(NBTTagCompound tagCompound)
    {
        this.priority = Priority.getPriority(tagCompound.getInteger(MessageKeys.priority));
        this.add = tagCompound.getBoolean(MessageKeys.addToRegistry);
    }

    public boolean isAdd()
    {
        return add;
    }

    public Priority getAddPriority()
    {
        return add ? priority : null;
    }

    public Priority getRemovePriority()
    {
        return add ? null : priority;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setBoolean(MessageKeys.addToRegistry, add);
        tagCompound.setInteger(MessageKeys.priority, priority.ordinal());
        return null;
    }

    @Override
    public boolean isValid()
    {
        return false;
    }
}
