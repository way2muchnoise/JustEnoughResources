package jeresources.api.conditionals;

import jeresources.api.drop.LootDrop;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootContext;

import java.util.Random;

public interface ICustomLootFunction
{
    /**
     * Similar to {@link net.minecraft.world.storage.loot.functions.LootFunction#apply(ItemStack, Random, LootContext)}
     * but with out all the {@link Random} and {@link LootContext}
     *
     * You only need to implement this if the the {@link net.minecraft.world.storage.loot.functions.LootFunction#apply(ItemStack, Random, LootContext)}
     * can't be called with null random and context or if you want to do a more advanced interaction with the {@link LootDrop}
     *
     * @param drop the {@link LootDrop} to apply the {@link net.minecraft.world.storage.loot.functions.LootFunction} on
     */
    void apply(LootDrop drop);
}
