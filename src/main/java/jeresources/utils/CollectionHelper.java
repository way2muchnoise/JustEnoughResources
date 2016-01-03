package jeresources.utils;

import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.List;

public class CollectionHelper
{

    public static List<ItemStack> create(ItemStack... itemStacks)
    {
        return Arrays.asList(itemStacks);
    }
}
