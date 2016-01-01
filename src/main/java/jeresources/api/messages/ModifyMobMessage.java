package jeresources.api.messages;

import jeresources.api.messages.utils.MessageHelper;
import jeresources.api.messages.utils.MessageKeys;
import jeresources.api.utils.DropItem;
import jeresources.api.utils.Priority;
import jeresources.utils.ReflectionHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ModifyMobMessage extends ModifyMessage
{
    private Class clazz;
    private DropItem[] addDrops = new DropItem[0];
    private ItemStack[] removeDrops = new ItemStack[0];
    private boolean strict;
    private boolean witherSkeleton;

    public ModifyMobMessage(Class clazz, DropItem... addDrops)
    {
        this(clazz, false, false, addDrops);
    }

    public ModifyMobMessage(Class clazz, boolean strict, DropItem... addDrops)
    {
        this(clazz, strict, false, addDrops);
    }

    public ModifyMobMessage(Class clazz, boolean strict, boolean witherSkeleton, DropItem... addDrops)
    {
        this(clazz, strict, witherSkeleton, Priority.SECOND, addDrops);
    }

    public ModifyMobMessage(Class clazz, Priority priority, DropItem... addDrops)
    {
        this(clazz, false, false, priority, addDrops);
    }

    public ModifyMobMessage(Class clazz, boolean strict, Priority priority, DropItem... addDrops)
    {
        this(clazz, strict, false, priority, addDrops);
    }

    public ModifyMobMessage(Class clazz, boolean strict, boolean witherSkeleton, Priority priority, DropItem... addDrops)
    {
        this(clazz, strict, witherSkeleton, addDrops, new ItemStack[0], priority);
    }

    public ModifyMobMessage(Class clazz, ItemStack... removeDrops)
    {
        this(clazz, false, false, removeDrops);
    }

    public ModifyMobMessage(Class clazz, boolean strict, ItemStack... removeDrops)
    {
        this(clazz, strict, false, removeDrops);
    }

    public ModifyMobMessage(Class clazz, boolean strict, boolean witherSkeleton, ItemStack... removeDrops)
    {
        this(clazz, strict, witherSkeleton, Priority.SECOND, removeDrops);
    }

    public ModifyMobMessage(Class clazz, Priority priority, ItemStack... removeDrops)
    {
        this(clazz, false, false, priority, removeDrops);
    }

    public ModifyMobMessage(Class clazz, boolean strict, Priority priority, ItemStack... removeDrops)
    {
        this(clazz, strict, false, priority, removeDrops);
    }

    public ModifyMobMessage(Class clazz, boolean strict, boolean witherSkeleton, Priority priority, ItemStack... removeDrops)
    {
        this(clazz, strict, witherSkeleton, new DropItem[0], removeDrops, priority);
    }

    public ModifyMobMessage(Class clazz, DropItem[] addDrops, ItemStack[] removeDrops)
    {
        this(clazz, addDrops, removeDrops, Priority.SECOND);
    }

    public ModifyMobMessage(Class clazz, boolean strict, DropItem[] addDrops, ItemStack[] removeDrops)
    {
        this(clazz, strict, addDrops, removeDrops, Priority.SECOND);
    }

    public ModifyMobMessage(Class clazz, boolean strict, boolean witherSkeleton, DropItem[] addDrops, ItemStack[] removeDrops)
    {
        this(clazz, strict, witherSkeleton, addDrops, removeDrops, Priority.SECOND);
    }

    public ModifyMobMessage(Class clazz, DropItem[] addDrops, ItemStack[] removeDrops, Priority priority)
    {
        this(clazz, false, addDrops, removeDrops, priority);
    }

    public ModifyMobMessage(Class clazz, boolean strict, DropItem[] addDrops, ItemStack[] removeDrops, Priority priority)
    {
        this(clazz, strict, false, addDrops, removeDrops, priority);
    }

    public ModifyMobMessage(Class clazz, boolean strict, boolean witherSkeleton, DropItem[] addDrops, ItemStack[] removeDrops, Priority priority)
    {
        this(clazz, strict, witherSkeleton, addDrops, removeDrops, priority, priority);
    }

    public ModifyMobMessage(Class clazz, boolean strict, boolean witherSkeleton, DropItem[] addDrops, ItemStack[] removeDrops, Priority addPriority, Priority removePriority)
    {
        super(addPriority, removePriority);
        initialize(clazz, strict, witherSkeleton, addDrops, removeDrops);
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

    public Class getFilterClass()
    {
        return clazz;
    }

    public DropItem[] getAddDrops()
    {
        return addDrops;
    }

    public ItemStack[] getRemoveDrops()
    {
        return removeDrops;
    }

    public boolean isWither()
    {
        return witherSkeleton;
    }

    public boolean isStrict()
    {
        return strict;
    }

    public ModifyMobMessage(NBTTagCompound tagCompound)
    {
        super(tagCompound);
        initialize(ReflectionHelper.findClass(tagCompound.getString(MessageKeys.name)), tagCompound.getBoolean(MessageKeys.strict), tagCompound.getBoolean(MessageKeys.wither), MessageHelper.getDropItems(tagCompound, MessageKeys.addDrops), MessageHelper.getItemStacks(tagCompound, MessageKeys.removeDrops));
    }

    private void initialize(Class clazz, boolean strict, boolean witherSkeleton, DropItem[] addDrops, ItemStack[] removeDrops)
    {
        this.clazz = clazz;
        this.strict = strict;
        this.witherSkeleton = witherSkeleton;
        this.addDrops = addDrops;
        this.removeDrops = removeDrops;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        tagCompound.setString(MessageKeys.name, this.clazz.getName());
        tagCompound.setBoolean(MessageKeys.strict, this.strict);
        tagCompound.setBoolean(MessageKeys.wither, this.witherSkeleton);
        tagCompound.setTag(MessageKeys.addDrops, MessageHelper.getDropItemList(addDrops));
        tagCompound.setTag(MessageKeys.removeDrops, MessageHelper.getItemStackList(removeDrops));
        return tagCompound;
    }

    public void setWither(boolean wither)
    {
        this.witherSkeleton = wither;
    }

    @Override
    public boolean isValid()
    {
        return clazz != null && (hasAdd() || hasRemove());
    }


}