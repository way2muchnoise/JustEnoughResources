package jeresources.api.messages;

import jeresources.api.messages.utils.MessageHelper;
import jeresources.api.messages.utils.MessageKeys;
import jeresources.api.utils.Priority;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ModifyOreMessage extends ModifyMessage
{
    private ItemStack ore;
    private ItemStack[] addDrops = new ItemStack[0];
    private ItemStack[] removeDrops = new ItemStack[0];

    public ModifyOreMessage(ItemStack ore, ItemStack... drops)
    {
        this(ore, true, drops);
    }

    public ModifyOreMessage(ItemStack ore, Priority priority, ItemStack... drops)
    {
        this(ore, true, priority, drops);
    }

    public ModifyOreMessage(ItemStack ore, boolean add, ItemStack... drops)
    {
        this(ore, add, Priority.SECOND, drops);
    }

    public ModifyOreMessage(ItemStack ore, boolean add, Priority priority, ItemStack... drops)
    {
        super(priority, add);
        this.ore = ore;
        if (add)
            this.addDrops = drops;
        else
            this.removeDrops = drops;
    }

    public ModifyOreMessage(ItemStack ore, ItemStack[] removeDrops, ItemStack[] addDrops)
    {
        this(ore, removeDrops, addDrops, Priority.SECOND, Priority.FIRST);
    }

    public ModifyOreMessage(ItemStack ore, ItemStack[] removeDrops, ItemStack[] addDrops, Priority removePriority, Priority addPriority)
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
        this.addDrops = MessageHelper.getItemStacks(tagCompound, MessageKeys.addDrops);
        this.removeDrops = MessageHelper.getItemStacks(tagCompound, MessageKeys.removeDrops);
    }

    public ItemStack getOre()
    {
        return ore;
    }

    public ItemStack[] getRemoveDrops()
    {
        return removeDrops;
    }

    public ItemStack[] getAddDrops()
    {
        return addDrops;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        tagCompound.setTag(MessageKeys.stack, ore.writeToNBT(new NBTTagCompound()));
        tagCompound.setTag(MessageKeys.addDrops, MessageHelper.getItemStackList(addDrops));
        tagCompound.setTag(MessageKeys.removeDrops, MessageHelper.getItemStackList(removeDrops));
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
