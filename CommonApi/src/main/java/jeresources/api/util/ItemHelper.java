package jeresources.api.util;

import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemHelper {
    @NotNull
    public static ItemStack copyStackWithSize(@NotNull ItemStack itemStack, int size)
    {
        if (size == 0) {
            return ItemStack.EMPTY;
        }
        ItemStack copy = itemStack.copy();
        copy.setCount(size);
        return copy;
    }

    public static ItemStack itemStackWithDataComponents(Item item, int size, DataComponentPatch dataComponentPatch) {
        ItemStack itemStack = new ItemStack(item, size);
        itemStack.applyComponentsAndValidate(dataComponentPatch);
        return itemStack;
    }
}
