package jeresources.api.messages;

import jeresources.api.utils.Priority;
import net.minecraft.nbt.NBTTagCompound;

public class RemovePlantMessage extends RegistryMessage
{
    public RemovePlantMessage(Priority priority)
    {
        super(priority, false);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        return tagCompound;
    }

    @Override
    public boolean isValid()
    {
        return false;
    }
}
