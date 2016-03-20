package jeresources.util;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectionHelper
{
    public static List<ItemStack> create(ItemStack... itemStacks)
    {
        return new ArrayList<>(Arrays.asList(itemStacks));
    }

    public static List<String> create(String... strings)
    {
        return new ArrayList<>(Arrays.asList(strings));
    }
}
