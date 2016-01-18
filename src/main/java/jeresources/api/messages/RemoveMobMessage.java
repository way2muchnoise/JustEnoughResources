package jeresources.api.messages;

import jeresources.api.messages.utils.MessageKeys;
import jeresources.api.utils.Priority;
import jeresources.utils.ReflectionHelper;
import net.minecraft.nbt.NBTTagCompound;

public class RemoveMobMessage extends RegistryMessage
{
    private Class filterClass;
    private boolean strict;
    private boolean witherSkeleton;

    public RemoveMobMessage(Class clazz)
    {
        this(clazz, false);
    }

    public RemoveMobMessage(Priority priority, Class clazz)
    {
        this(priority, clazz, false);
    }

    public RemoveMobMessage(Class clazz, boolean strict)
    {
        this(clazz, strict, false);
    }

    public RemoveMobMessage(Priority priority, Class clazz, boolean strict)
    {
        this(priority, clazz, strict, false);
    }

    public RemoveMobMessage(Class clazz, boolean strict, boolean witherSkeleton)
    {
        this(Priority.FIRST, clazz, strict, witherSkeleton);
    }

    public RemoveMobMessage(Priority priority, Class clazz, boolean strict, boolean witherSkeleton)
    {
        super(priority, false);
        this.filterClass = clazz;
        this.strict = strict;
        this.witherSkeleton = witherSkeleton;
    }

    public RemoveMobMessage(NBTTagCompound tagCompound)
    {
        super(tagCompound);
        this.filterClass = ReflectionHelper.findClass(tagCompound.getString(MessageKeys.name));
        this.strict = tagCompound.getBoolean(MessageKeys.strict);
        this.witherSkeleton = tagCompound.getBoolean(MessageKeys.wither);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        tagCompound.setString(MessageKeys.name, this.filterClass.getName());
        tagCompound.setBoolean(MessageKeys.strict, this.strict);
        tagCompound.setBoolean(MessageKeys.wither, this.witherSkeleton);
        return tagCompound;
    }

    @Override
    public boolean isValid()
    {
        return !this.filterClass.equals("");
    }

    public Class getFilterClass()
    {
        return filterClass;
    }

    public boolean isStrict()
    {
        return strict;
    }

    public boolean isWither()
    {
        return witherSkeleton;
    }
}
