package jeresources.api.messages;

import jeresources.api.messages.utils.MessageHelper;
import jeresources.api.messages.utils.MessageKeys;
import jeresources.api.utils.Priority;
import jeresources.utils.WeightedRandomChestContentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.WeightedRandomChestContent;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RegisterDungeonMessage extends RegistryMessage
{
    private Map<ItemStack, Float> chestDrops = new LinkedHashMap<ItemStack, Float>();
    private String name;
    private int maxStacks, minStacks;

    public RegisterDungeonMessage(String name, int maxStacks, List<WeightedRandomChestContent> chestDrops)
    {
        this(name,0,maxStacks,chestDrops);
    }
    public RegisterDungeonMessage(String name, int minStacks, int maxStacks, List<WeightedRandomChestContent> chestDrops)
    {
        this(name,minStacks,maxStacks,Priority.FIRST);
        int totalWeight = 0;
        for (WeightedRandomChestContent chestItem : chestDrops)
            totalWeight += chestItem.itemWeight;
        for (WeightedRandomChestContent chestItem : WeightedRandomChestContentHelper.sort(chestDrops.toArray(new WeightedRandomChestContent[chestDrops.size()])))
            this.chestDrops.put(chestItem.theItemId, (float) (chestItem.theMaximumChanceToGenerateItem + chestItem.theMinimumChanceToGenerateItem) / 2 * (float) chestItem.itemWeight / totalWeight);
    }

    public RegisterDungeonMessage(String name, int maxStacks, Map<ItemStack,Float> chestDrops)
    {
        this(name,0,maxStacks,chestDrops);
    }

    public RegisterDungeonMessage(String name, int minStacks,int maxStacks, Map<ItemStack,Float> chestDrops)
    {
        this(name,minStacks,maxStacks,Priority.FIRST);
        this.chestDrops = chestDrops;
    }

    private RegisterDungeonMessage(String name, int minStacks, int maxStacks, Priority priority)
    {
        super(priority,true);
        this.name = name;
        this.minStacks = minStacks;
        this.maxStacks = maxStacks;
    }

    public RegisterDungeonMessage(NBTTagCompound tagCompound)
    {
        super(tagCompound);
        this.chestDrops = MessageHelper.nbtTagListToMap(tagCompound.getTagList(MessageKeys.itemList,10));
        this.name = tagCompound.getString(MessageKeys.name);
        this.minStacks = tagCompound.getInteger(MessageKeys.min);
        this.maxStacks = tagCompound.getInteger(MessageKeys.max);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        super.writeToNBT(tagCompound);
        tagCompound.setTag(MessageKeys.itemList, MessageHelper.mapToNBTTagList(chestDrops));
        tagCompound.setString(MessageKeys.name,name);
        tagCompound.setInteger(MessageKeys.min,minStacks);
        tagCompound.setInteger(MessageKeys.max,maxStacks);
        return tagCompound;
    }

    @Override
    public boolean isValid()
    {
        return !name.equals("") && maxStacks>0 && chestDrops!=null && !chestDrops.isEmpty();
    }

    public Map<ItemStack, Float> getChestDrops()
    {
        return chestDrops;
    }

    public String getName()
    {
        return name;
    }

    public int getMinStacks()
    {
        return minStacks;
    }

    public int getMaxStacks()
    {
        return maxStacks;
    }
}
