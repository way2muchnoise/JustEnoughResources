package jeresources.entries;

import jeresources.api.messages.RegisterDungeonMessage;
import jeresources.utils.ReflectionHelper;
import jeresources.utils.WeightedRandomChestContentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandomChestContent;
import net.minecraftforge.common.ChestGenHooks;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DungeonEntry
{
    private Map<ItemStack, Float> chestDrops = new LinkedHashMap<ItemStack, Float>();
    private String name;
    private int maxStacks, minStacks;

    public DungeonEntry(String name, ChestGenHooks chestGenHooks)
    {
        int totalWeight = 0;
        List<WeightedRandomChestContent> content = ReflectionHelper.getPrivateValue(ChestGenHooks.class, chestGenHooks, "contents");
        for (WeightedRandomChestContent chestItem : content)
            totalWeight += chestItem.itemWeight;
        for (WeightedRandomChestContent chestItem : WeightedRandomChestContentHelper.sort(content.toArray(new WeightedRandomChestContent[content.size()])))
            chestDrops.put(chestItem.theItemId, (float) (chestItem.maxStackSize + chestItem.minStackSize) / 2 * (float) chestItem.itemWeight / totalWeight);
        this.name = name;
        this.minStacks = chestGenHooks.getMin();
        this.maxStacks = chestGenHooks.getMax();
    }

    public DungeonEntry(RegisterDungeonMessage message)
    {
        chestDrops = message.getChestDrops();
        name = message.getName();
        maxStacks = message.getMaxStacks();
        minStacks = message.getMinStacks();
    }

    public boolean containsItem(ItemStack itemStack)
    {
        for (ItemStack item : chestDrops.keySet())
            if (item.isItemEqual(itemStack)) return true;
        return false;
    }

    public String getName()
    {
        return name;
    }

    public Map<ItemStack, Float> getChestDrops()
    {
        return chestDrops;
    }

    public ItemStack[] getItemStacks()
    {
        return chestDrops.keySet().toArray(new ItemStack[chestDrops.size()]);
    }

    public Float getChange(ItemStack itemStack)
    {
        return chestDrops.get(itemStack);
    }

    public int getMaxStacks()
    {
        return maxStacks;
    }

    public int getMinStacks()
    {
        return minStacks;
    }
}
