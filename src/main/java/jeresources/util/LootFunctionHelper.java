package jeresources.util;

import jeresources.api.conditionals.Conditional;
import jeresources.api.conditionals.ICustomLootFunction;
import jeresources.api.drop.LootDrop;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.storage.loot.functions.*;

public class LootFunctionHelper {
    public static void applyFunction(LootFunction lootFunction, LootDrop lootDrop) {
        if (lootFunction instanceof SetCount) {
            lootDrop.minDrop = MathHelper.floor_float(((SetCount) lootFunction).countRange.getMin());
            if (lootDrop.minDrop < 0) lootDrop.minDrop = 0;
            lootDrop.item.stackSize = lootDrop.minDrop < 1 ? 1 : lootDrop.minDrop;
            lootDrop.maxDrop = MathHelper.floor_float(((SetCount) lootFunction).countRange.getMax());
        } else if (lootFunction instanceof SetMetadata) {
            lootDrop.item.setItemDamage(MathHelper.floor_float(((SetMetadata) lootFunction).metaRange.getMin()));
        } else if (lootFunction instanceof EnchantRandomly || lootFunction instanceof EnchantWithLevels) {
            lootDrop.enchanted = true;
        } else if (lootFunction instanceof Smelt) {
            lootDrop.smeltedItem = lootFunction.apply(lootDrop.item, null, null);
            if (ItemStack.areItemStacksEqual(lootDrop.item, lootDrop.smeltedItem))
                lootDrop.smeltedItem = null;
        } else if (lootFunction instanceof LootingEnchantBonus) {
            lootDrop.addConditional(Conditional.affectedByLooting);
        } else if (lootFunction instanceof ICustomLootFunction) {
            ((ICustomLootFunction) lootFunction).apply(lootDrop);
        } else {
            try {
                lootDrop.item = lootFunction.apply(lootDrop.item, null, null);
            } catch (NullPointerException ignored) {
            }
        }
    }
}
