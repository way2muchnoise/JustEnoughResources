package jeresources.util;

import jeresources.api.conditionals.Conditional;
import jeresources.api.conditionals.ICustomLootFunction;
import jeresources.api.drop.LootDrop;
import net.minecraft.loot.BinomialRange;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.IRandomRange;
import net.minecraft.loot.RandomValueRange;
import net.minecraft.loot.functions.*;
import net.minecraft.util.math.MathHelper;

import java.util.Random;
import java.util.stream.IntStream;

public class LootFunctionHelper {
    public static void applyFunction(ILootFunction lootFunction, LootDrop lootDrop) {
        if (lootFunction instanceof SetCount) {
            lootDrop.minDrop = getMin(((SetCount) lootFunction).countRange);
            if (lootDrop.minDrop < 0) lootDrop.minDrop = 0;
            lootDrop.item.setCount(Math.max(lootDrop.minDrop, 1));
            lootDrop.maxDrop = getMax(((SetCount) lootFunction).countRange);
        } else if (lootFunction instanceof SetDamage) {
            lootDrop.item.setDamage(MathHelper.floor(((SetDamage) lootFunction).damageRange.getMin()));
        } else if (lootFunction instanceof EnchantRandomly || lootFunction instanceof EnchantWithLevels) {
            lootDrop.enchanted = true;
        } else if (lootFunction instanceof Smelt) {
            // TODO figure out how to create a client side LootContext
            //lootDrop.smeltedItem = lootFunction.apply(lootDrop.item, new Random(), null);
            //if (ItemStack.areItemStacksEqual(lootDrop.item, lootDrop.smeltedItem))
                //lootDrop.smeltedItem = null;
        } else if (lootFunction instanceof LootingEnchantBonus) {
            lootDrop.addConditional(Conditional.affectedByLooting);
        } else if (lootFunction instanceof ICustomLootFunction) {
            ((ICustomLootFunction) lootFunction).apply(lootDrop);
        } else {
            try {
                // TODO figure out how to create a client side LootContext
                lootDrop.item = lootFunction.apply(lootDrop.item, null);
            } catch (NullPointerException ignored) {
            }
        }
    }

    private static final Random rand = new Random();
    private static final int STATISTICAL_TEST = 100; // Values tested to determine min and max

    public static int getMin(IRandomRange randomRange) {
        if (randomRange instanceof ConstantRange) {
            return randomRange.generateInt(rand);
        } else if (randomRange instanceof RandomValueRange) {
            return MathHelper.floor(((RandomValueRange) randomRange).getMin());
        } else if (randomRange instanceof BinomialRange) {
            return 0;
        } else {
            // Test a 100 values
            return IntStream.iterate(0, i -> randomRange.generateInt(rand)).limit(STATISTICAL_TEST).min().orElse(0);
        }
    }

    public static int getMax(IRandomRange randomRange) {
        if (randomRange instanceof ConstantRange) {
            return randomRange.generateInt(rand);
        } else if (randomRange instanceof RandomValueRange) {
            return MathHelper.floor(((RandomValueRange) randomRange).getMax());
        } else if (randomRange instanceof BinomialRange) {
            return ((BinomialRange) randomRange).n;
        } else {
            // Test a 100 values
            return IntStream.iterate(0, i -> randomRange.generateInt(rand)).limit(STATISTICAL_TEST).max().orElse(0);
        }
    }
}
