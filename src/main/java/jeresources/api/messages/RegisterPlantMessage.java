package jeresources.api.messages;

import jeresources.api.messages.utils.MessageHelper;
import jeresources.api.messages.utils.MessageKeys;
import jeresources.api.utils.PlantDrop;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RegisterPlantMessage extends RegistryMessage
{
    private ItemStack plant;
    private PlantDrop[] addDrops;

    public RegisterPlantMessage(NBTTagCompound tagCompound)
    {
        super(tagCompound);
        this.plant = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag(MessageKeys.stack));
        this.addDrops = MessageHelper.getPlantDrops(tagCompound, MessageKeys.addDrops);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        tagCompound.setTag(MessageKeys.stack, plant.writeToNBT(new NBTTagCompound()));
        tagCompound.setTag(MessageKeys.addDrops, MessageHelper.getPlantDropList(addDrops));
        return tagCompound;
    }

    @Override
    public boolean isValid()
    {
        return plant != null && addDrops.length > 0;
    }
}
