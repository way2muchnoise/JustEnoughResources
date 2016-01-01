package jeresources.api.messages.utils;

import jeresources.api.distributions.DistributionCustom;
import jeresources.api.utils.DropItem;
import jeresources.api.utils.PlantDrop;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MessageHelper
{
    public static final int NBT_MULTIPLIER = 100000;

    public static NBTTagList getItemStackList(ItemStack... stacks)
    {
        NBTTagList result = new NBTTagList();
        for (ItemStack stack : stacks)
            result.appendTag(stack.writeToNBT(new NBTTagCompound()));
        return result;
    }

    public static NBTTagList getDropItemList(DropItem... dropItems)
    {
        NBTTagList result = new NBTTagList();
        for (DropItem dropItem : dropItems)
            result.appendTag(dropItem.writeToNBT());
        return result;
    }

    public static NBTTagList getPlantDropList(PlantDrop... plantDrops)
    {
        NBTTagList result = new NBTTagList();
        for (PlantDrop plantDrop : plantDrops)
            result.appendTag(plantDrop.writeToNBT());
        return result;
    }

    public static ItemStack[] getItemStacks(NBTTagCompound tagCompound, String key)
    {
        return getItemStacks(tagCompound.getTagList(key, 10));
    }

    public static ItemStack[] getItemStacks(NBTTagList list)
    {
        List<ItemStack> result = new ArrayList<ItemStack>();
        if (list != null)
        {
            for (int i = 0; i < list.tagCount(); i++)
            {
                ItemStack item = ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i));
                if (item != null) result.add(item);
            }
        }
        return result.toArray(new ItemStack[result.size()]);
    }

    public static DropItem[] getDropItems(NBTTagCompound tagCompound, String key)
    {
        return getDropItems(tagCompound.getTagList(key, 10));
    }

    public static DropItem[] getDropItems(NBTTagList list)
    {
        List<DropItem> dropItems = new ArrayList<DropItem>();
        if (list != null)
        {
            for (int i = 0; i < list.tagCount(); i++)
            {
                DropItem item = DropItem.readFromNBT(list.getCompoundTagAt(i));
                if (item != null) dropItems.add(item);
            }
        }
        return dropItems.toArray(new DropItem[dropItems.size()]);
    }

    public static PlantDrop[] getPlantDrops(NBTTagCompound tagCompound, String key)
    {
        return getPlantDrops(tagCompound.getTagList(key, 10));
    }

    public static PlantDrop[] getPlantDrops(NBTTagList list)
    {
        List<PlantDrop> plantDrops = new ArrayList<PlantDrop>();
        if (list != null)
        {
            for (int i = 0; i < list.tagCount(); i++)
            {
                PlantDrop item = PlantDrop.readFromNBT(list.getCompoundTagAt(i));
                if (item != null) plantDrops.add(item);
            }
        }
        return plantDrops.toArray(new PlantDrop[plantDrops.size()]);
    }

    public static int[] getIntArray(float[] distribution)
    {
        int[] array = new int[distribution.length];
        for (int i = 0; i < array.length; i++)
            array[i] = (int) (distribution[i] * NBT_MULTIPLIER);
        return array;
    }

    public static DistributionCustom getDistribution(NBTTagCompound tagCompound)
    {
        if (!tagCompound.hasKey(MessageKeys.distribution)) return null;
        int[] array = tagCompound.getIntArray(MessageKeys.distribution);
        float[] distribution = new float[256];
        for (int i = 0; i < array.length && i < distribution.length; i++)
            distribution[i] = (float) array[i] / NBT_MULTIPLIER;
        if (tagCompound.hasKey(MessageKeys.bestHeight))
            return new DistributionCustom(distribution, tagCompound.getInteger(MessageKeys.bestHeight));
        else
            return new DistributionCustom(distribution);
    }

    public static String getClass(Object clazz)
    {
        if (clazz instanceof String)
            return (String) clazz;
        if (clazz instanceof Class)
            return ((Class) clazz).getName();
        return "";
    }

    public static NBTTagList mapToNBTTagList(Map<ItemStack, Float> map)
    {
        NBTTagList result = new NBTTagList();
        for (Map.Entry<ItemStack,Float> entry : map.entrySet())
            result.appendTag(entryToNBTTagCompound(entry));
        return result;
    }

    private static NBTTagCompound entryToNBTTagCompound(Map.Entry<ItemStack,Float> entry)
    {
        NBTTagCompound result = new NBTTagCompound();
        entry.getKey().writeToNBT(result);
        result.setFloat(MessageKeys.chance, entry.getValue());
        return result;
    }

    public static Map<ItemStack,Float> nbtTagListToMap(NBTTagList list)
    {
        Map<ItemStack,Float> result = new LinkedHashMap<ItemStack, Float>();
        for (int i = 0;i<list.tagCount();i++)
        {
            NBTTagCompound entry = list.getCompoundTagAt(i);
            ItemStack stack = ItemStack.loadItemStackFromNBT(entry);
            float chance = entry.getFloat(MessageKeys.chance);
            if (stack!=null && !(chance<0)) result.put(stack,chance);
        }
        return result;
    }
}
