package jeresources.api.messages;

import jeresources.api.messages.utils.MessageHelper;
import jeresources.api.messages.utils.MessageKeys;
import jeresources.api.utils.PlantDrop;
import jeresources.api.utils.Priority;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ModifyPlantMessage extends ModifyMessage
{
    private ItemStack plant;
    private PlantDrop[] addDrops = new PlantDrop[0];
    private ItemStack[] removeDrops = new ItemStack[0];

    private ModifyPlantMessage(ItemStack plant, Priority priority, boolean add)
    {
        super(priority,add);
        this.plant = plant;
    }

    private ModifyPlantMessage(ItemStack plant, Priority addPriority, Priority removePriority)
    {
        super(addPriority,removePriority);
        this.plant = plant;
    }

    public ModifyPlantMessage(PlantDrop... addDrops)
    {
        this(new ItemStack(Blocks.tallgrass),addDrops);
    }

    public ModifyPlantMessage(ItemStack plant, PlantDrop... addDrops)
    {
        this(plant,Priority.SECOND,addDrops);
    }

    public ModifyPlantMessage(ItemStack plant, Priority priority, PlantDrop... addDrops)
    {
        this(plant,priority,true,new ItemStack[0],addDrops);
    }

    public ModifyPlantMessage(ItemStack... removeDrops)
    {
        this(new ItemStack(Blocks.tallgrass),removeDrops);
    }

    public ModifyPlantMessage(ItemStack plant, ItemStack... removeDrops)
    {
        this(plant,Priority.SECOND,removeDrops);
    }

    public ModifyPlantMessage(ItemStack plant, Priority priority, ItemStack... removeDrops)
    {
        this(plant,priority,false,removeDrops,new PlantDrop[0]);
    }

    public ModifyPlantMessage(ItemStack plant, Priority priority, boolean add, ItemStack[] removeDrops, PlantDrop[] addDrops)
    {
        this(plant,priority,add);
        this.addDrops = addDrops;
        this.removeDrops = removeDrops;
    }

    public ModifyPlantMessage(ItemStack plant, ItemStack[] removeDrops, PlantDrop[] addDrops)
    {
        this(plant,removeDrops,addDrops,Priority.SECOND);
    }

    public ModifyPlantMessage(ItemStack plant, ItemStack[] removeDrops, PlantDrop[] addDrops, Priority priority)
    {
        this(plant,removeDrops,addDrops,priority,priority);
    }

    public ModifyPlantMessage(ItemStack plant, ItemStack[] removeDrops, PlantDrop[] addDrops, Priority addPriority, Priority removePriority)
    {
        this(plant,addPriority,removePriority);
        this.addDrops = addDrops;
        this.removeDrops = removeDrops;
    }

    public ModifyPlantMessage(NBTTagCompound tagCompound)
    {
        super(tagCompound);
        this.plant = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag(MessageKeys.stack));
        this.addDrops = MessageHelper.getPlantDrops(tagCompound, MessageKeys.addDrops);
        this.removeDrops = MessageHelper.getItemStacks(tagCompound, MessageKeys.removeDrops);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        tagCompound.setTag(MessageKeys.stack, plant.writeToNBT(new NBTTagCompound()));
        tagCompound.setTag(MessageKeys.addDrops,MessageHelper.getPlantDropList(addDrops));
        tagCompound.setTag(MessageKeys.removeDrops,MessageHelper.getItemStackList(removeDrops));
        return tagCompound;
    }

    @Override
    public boolean hasAdd()
    {
        return addDrops.length>0;
    }

    @Override
    public boolean hasRemove()
    {
        return removeDrops.length>0;
    }

    @Override
    public boolean isValid()
    {
        return plant!=null && (hasAdd() || hasRemove());
    }
}
