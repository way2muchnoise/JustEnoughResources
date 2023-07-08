package jeresources.api.util;

import jeresources.api.conditionals.Conditional;
import jeresources.api.conditionals.ICustomLootFunction;
import jeresources.api.drop.LootDrop;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootDataManager;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.BinomialDistributionGenerator;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.stream.IntStream;

public class LootFunctionHelper {
    public static final LootContext randContext = new RandomLootContext();
    private static final int STATISTICAL_TEST = 100; // Values tested to determine min and max

    public static void applyFunction(LootItemFunction lootFunction, LootDrop lootDrop) {
        if (lootFunction instanceof SetItemCountFunction) {
            lootDrop.minDrop = getMin(((SetItemCountFunction) lootFunction).value);
            if (lootDrop.minDrop < 0) lootDrop.minDrop = 0;
            lootDrop.item.setCount(Math.max(lootDrop.minDrop, 1));
            lootDrop.maxDrop = getMax(((SetItemCountFunction) lootFunction).value);
        } else if (lootFunction instanceof SetItemDamageFunction) {
            ((SetItemDamageFunction) lootFunction).run(lootDrop.item, randContext);
        } else if (lootFunction instanceof EnchantRandomlyFunction || lootFunction instanceof EnchantWithLevelsFunction) {
            lootDrop.enchanted = true;
        } else if (lootFunction instanceof SmeltItemFunction) {
            // TODO figure out how to create a client side LootContext
            //lootDrop.smeltedItem = lootFunction.apply(lootDrop.item, new Random(), null);
            //if (ItemStack.isSame(lootDrop.item, lootDrop.smeltedItem)) {
            //    lootDrop.smeltedItem = null;
            //}
        } else if (lootFunction instanceof LootingEnchantFunction) {
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

    public static int getMin(NumberProvider randomRange) {
        if (randomRange instanceof ConstantValue) {
            return randomRange.getInt(randContext);
        } else if (randomRange instanceof UniformGenerator) {
            return Mth.floor(((UniformGenerator) randomRange).min.getInt(randContext));
        } else if (randomRange instanceof BinomialDistributionGenerator) {
            return 0;
        } else {
            // Test a 100 values
            return IntStream.iterate(0, i -> randomRange.getInt(randContext)).limit(STATISTICAL_TEST).min().orElse(0);
        }
    }

    public static int getMax(NumberProvider randomRange) {
        if (randomRange instanceof ConstantValue) {
            return randomRange.getInt(randContext);
        } else if (randomRange instanceof UniformGenerator) {
            return Mth.floor(((UniformGenerator) randomRange).max.getInt(randContext));
        } else if (randomRange instanceof BinomialDistributionGenerator) {
            return ((BinomialDistributionGenerator) randomRange).n.getInt(randContext);
        } else {
            // Test a 100 values
            return IntStream.iterate(0, i -> randomRange.getInt(randContext)).limit(STATISTICAL_TEST).max().orElse(0);
        }
    }

    public static class RandomLootContext extends LootContext {
        public RandomLootContext(){
            //LootParams, RandomSource , LootDataResolver
            super(new LootParams.Builder(null).create(LootContextParamSets.EMPTY), RandomSource.create(), new LootDataManager());
        }
    }
}
