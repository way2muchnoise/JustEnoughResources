package jeresources.api.messages;

import jeresources.api.messages.utils.MessageHelper;
import jeresources.api.messages.utils.MessageKeys;
import jeresources.api.utils.DropItem;
import jeresources.api.utils.LightLevel;
import jeresources.api.utils.Priority;
import jeresources.utils.ReflectionHelper;
import net.minecraft.nbt.NBTTagCompound;

public class RegisterMobMessage extends RegistryMessage
{
    private Class mobClass;
    private LightLevel lightLevel;
    private DropItem[] drops;

    public RegisterMobMessage(Class clazz)
    {
        this(clazz, LightLevel.any);
    }

    public RegisterMobMessage(Class clazz, LightLevel level)
    {
        this(clazz, level, new DropItem[0]);
    }

    public RegisterMobMessage(Class clazz, LightLevel level, DropItem[] drops)
    {
        this(Priority.FIRST,clazz,level,drops);
    }

    public RegisterMobMessage(Priority priority, Class clazz, LightLevel level, DropItem[] drops)
    {
        super(priority,true);
        this.mobClass = clazz;
        this.lightLevel = level;
        this.drops = drops;
    }

    public RegisterMobMessage(NBTTagCompound tagCompound)
    {
        super(tagCompound);
        this.mobClass = ReflectionHelper.findClass(tagCompound.getString(MessageKeys.name));
        this.lightLevel = LightLevel.decodeLightLevel(tagCompound.getString(MessageKeys.lightLevel));
        this.drops = MessageHelper.getDropItems(tagCompound, MessageKeys.addDrops);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        tagCompound.setString(MessageKeys.name, mobClass.getName());
        tagCompound.setString(MessageKeys.lightLevel, lightLevel.encode());
        tagCompound.setTag(MessageKeys.addDrops, MessageHelper.getDropItemList(drops));
        return tagCompound;
    }

    @Override
    public boolean isValid()
    {
        return mobClass != null;
    }

    public Class getMobClass()
    {
        return mobClass;
    }

    public LightLevel getLightLevel()
    {
        return lightLevel;
    }

    public DropItem[] getDrops()
    {
        return drops;
    }
}
