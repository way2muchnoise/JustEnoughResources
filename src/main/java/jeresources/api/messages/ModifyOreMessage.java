package jeresources.api.messages;

import jeresources.api.messages.utils.MessageHelper;
import jeresources.api.messages.utils.MessageKeys;
import jeresources.api.utils.DropItem;
import jeresources.api.utils.Priority;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ModifyOreMessage extends ModifyMessage
{
    private ItemStack ore;
    private DropItem[] addDrops = new DropItem[0];
    private DropItem[] removeDrops = new DropItem[0];

    public ModifyOreMessage(ItemStack ore, DropItem... drops)
    {
        this(ore, true, drops);
    }

    public ModifyOreMessage(ItemStack ore, Priority priority, DropItem... drops)
    {
        this(ore, true, priority, drops);
    }

    public ModifyOreMessage(ItemStack ore, boolean add, DropItem... drops)
    {
        this(ore, add, Priority.SECOND, drops);
    }

    public ModifyOreMessage(ItemStack ore, boolean add, Priority priority, DropItem... drops)
    {
        super(priority, add);
        this.ore = ore;
        if (add)
            this.addDrops = drops;
        else
            this.removeDrops = drops;
    }

    public ModifyOreMessage(ItemStack ore, DropItem[] removeDrops, DropItem[] addDrops)
    {
        this(ore, removeDrops, addDrops, Priority.SECOND, Priority.FIRST);
    }

    public ModifyOreMessage(ItemStack ore, DropItem[] removeDrops, DropItem[] addDrops, Priority removePriority, Priority addPriority)
    {
        super(addPriority, removePriority);
        this.ore = ore;
        this.removeDrops = removeDrops;
        this.addDrops = addDrops;
    }

    public ModifyOreMessage(NBTTagCompound tagCompound)
    {
        super(tagCompound);
        this.ore = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag(MessageKeys.stack));
        this.addDrops = MessageHelper.getDropItems(tagCompound, MessageKeys.addDrops);
        this.removeDrops = MessageHelper.getDropItems(tagCompound, MessageKeys.removeDrops);
    }

    public ItemStack getOre()
    {
        return ore;
    }

    public DropItem[] getRemoveDrops()
    {
        return removeDrops;
    }

    public DropItem[] getAddDrops()
    {
        return addDrops;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        tagCompound.setTag(MessageKeys.stack, ore.writeToNBT(new NBTTagCompound()));
        tagCompound.setTag(MessageKeys.addDrops, MessageHelper.getDropItemList(addDrops));
        tagCompound.setTag(MessageKeys.removeDrops, MessageHelper.getDropItemList(removeDrops));
        return tagCompound;
    }

    @Override
    public boolean hasAdd()
    {
        return addDrops.length > 0;
    }

    @Override
    public boolean hasRemove()
    {
        return removeDrops.length > 0;
    }

    @Override
    public boolean isValid()
    {
        return ore != null && (hasAdd() || hasRemove());
    }
}
