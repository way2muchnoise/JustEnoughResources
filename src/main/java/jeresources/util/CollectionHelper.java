package jeresources.util;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CollectionHelper {
    public static List<ItemStack> create(ItemStack... itemStacks) {
        return new ArrayList<>(Arrays.asList(itemStacks));
    }

    public static List<String> create(String... strings) {
        return new ArrayList<>(Arrays.asList(strings));
    }

    public static List<ITextComponent> create(Function<String, ITextComponent> function, String... strings) {
        return new ArrayList<>(Arrays.asList(strings)).stream().map(function).collect(Collectors.toList());
    }
}
