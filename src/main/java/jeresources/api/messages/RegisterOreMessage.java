package jeresources.api.messages;

import jeresources.api.distributions.DistributionBase;
import jeresources.api.messages.utils.MessageHelper;
import jeresources.api.messages.utils.MessageKeys;
import jeresources.api.utils.ColorHelper;
import jeresources.api.utils.Priority;
import jeresources.api.utils.restrictions.Restriction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RegisterOreMessage extends RegistryMessage
{
    private ItemStack ore;
    private ItemStack[] drops;
    private boolean needSilkTouch;
    private DistributionBase distribution;
    private int colour;
    private Restriction restriction;

    public RegisterOreMessage(ItemStack ore, DistributionBase distribution, ItemStack... drops)
    {
        this(ore, distribution, ColorHelper.BLACK, Restriction.OVERWORLD_LIKE, false, drops);
    }

    public RegisterOreMessage(ItemStack ore, DistributionBase distribution, boolean needSilkTouch, ItemStack... drops)
    {
        this(ore, distribution, ColorHelper.BLACK, Restriction.OVERWORLD_LIKE, needSilkTouch, drops);
    }

    public RegisterOreMessage(ItemStack ore, DistributionBase distribution, int colour, ItemStack... drops)
    {
        this(ore, distribution, colour, Restriction.OVERWORLD_LIKE, false, drops);
    }

    public RegisterOreMessage(ItemStack ore, DistributionBase distribution, int colour,Restriction restriction, boolean needSilkTouch, ItemStack... drops)
    {
        this(ore,distribution,colour,restriction, needSilkTouch,Priority.FIRST,drops);
    }

    public RegisterOreMessage(ItemStack ore, DistributionBase distribution, int colour, boolean needSilkTouch, Priority priority, ItemStack... drops)
    {
        this(ore,distribution,colour,Restriction.OVERWORLD_LIKE,needSilkTouch,priority,drops);
    }

    public RegisterOreMessage(ItemStack ore, DistributionBase distribution, Restriction restriction, ItemStack... drops)
    {
        this(ore, distribution, ColorHelper.BLACK, restriction, false, drops);
    }

    public RegisterOreMessage(ItemStack ore, DistributionBase distribution,Restriction restriction, boolean needSilkTouch, ItemStack... drops)
    {
        this(ore, distribution, ColorHelper.BLACK, restriction, needSilkTouch, drops);
    }

    public RegisterOreMessage(ItemStack ore, DistributionBase distribution,Restriction restriction, int colour, ItemStack... drops)
    {
        this(ore, distribution, colour, restriction, false, drops);
    }

    public RegisterOreMessage(ItemStack ore, DistributionBase distribution, Restriction restriction,int colour, boolean needSilkTouch, Priority priority, ItemStack... drops)
    {
        this(ore,distribution,colour,restriction,needSilkTouch,priority,drops);
    }

    public RegisterOreMessage(ItemStack ore, DistributionBase distribution, int colour, Restriction restriction, boolean needSilkTouch, Priority priority, ItemStack... drops)
    {
        super(priority,true);
        this.ore = ore;
        this.drops = drops;
        this.needSilkTouch = needSilkTouch;
        this.distribution = distribution;
        this.colour = colour;
        this.restriction = restriction;
    }



    public RegisterOreMessage(NBTTagCompound tagCompound)
    {
        super(tagCompound);
        this.ore = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag(MessageKeys.stack));
        this.drops = MessageHelper.getItemStacks(tagCompound, MessageKeys.addDrops);
        this.needSilkTouch = tagCompound.getBoolean(MessageKeys.silkTouch);
        this.distribution = MessageHelper.getDistribution(tagCompound);
        this.colour = tagCompound.hasKey(MessageKeys.colour) ? tagCompound.getInteger(MessageKeys.colour) : ColorHelper.BLACK;
        this.restriction = new Restriction(tagCompound.getCompoundTag(MessageKeys.restriction));
    }

    public ItemStack getOre()
    {
        return this.ore;
    }

    public DistributionBase getDistribution()
    {
        return distribution;
    }

    public boolean needSilkTouch()
    {
        return this.needSilkTouch;
    }

    public int getColour()
    {
        return colour;
    }

    public ItemStack[] getDrops()
    {
        return drops;
    }

    public Restriction getRestriction()
    {
        return restriction;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        tagCompound.setTag(MessageKeys.stack, ore.writeToNBT(new NBTTagCompound()));
        tagCompound.setTag(MessageKeys.addDrops, MessageHelper.getItemStackList(drops));
        distribution.writeToNBT(tagCompound);
        tagCompound.setBoolean(MessageKeys.silkTouch, needSilkTouch);
        tagCompound.setInteger(MessageKeys.colour, colour);
        tagCompound.setTag(MessageKeys.restriction,restriction.writeToNBT());
        return tagCompound;
    }

    @Override
    public boolean isValid()
    {
        return ore != null && distribution.getDistribution().length == 256;
    }
}
