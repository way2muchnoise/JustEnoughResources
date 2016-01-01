package jeresources.api.messages;

import jeresources.api.messages.utils.MessageKeys;
import jeresources.api.utils.Priority;
import net.minecraft.nbt.NBTTagCompound;

public abstract class ModifyMessage extends Message
{
    private final Priority addPriority;
    private final Priority removePriority;

    public ModifyMessage(Priority addPriority, Priority removePriority)
    {
        this.addPriority = addPriority;
        this.removePriority = removePriority;
    }

    public ModifyMessage(Priority priority, boolean add)
    {
        if (add)
        {
            addPriority = priority;
            removePriority = Priority.FIRST;
        } else
        {
            addPriority = Priority.FIRST;
            removePriority = priority;
        }
    }

    public Priority getAddPriority()
    {
        return addPriority;
    }

    public Priority getRemovePriority()
    {
        return removePriority;
    }

    public abstract boolean hasAdd();

    public abstract boolean hasRemove();

    public ModifyMessage(NBTTagCompound tagCompound)
    {
        addPriority = Priority.getPriority(tagCompound.getInteger(MessageKeys.addPriority));
        removePriority = Priority.getPriority(tagCompound.getInteger(MessageKeys.removePriority));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setInteger(MessageKeys.addPriority, addPriority.ordinal());
        tagCompound.setInteger(MessageKeys.removePriority, removePriority.ordinal());
        return tagCompound;
    }
}
