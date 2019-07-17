package jeresources.util;

import jeresources.api.conditionals.Conditional;
import jeresources.api.conditionals.ICustomLootFunction;
import jeresources.api.drop.LootDrop;
import jeresources.compatibility.CompatBase;
import jeresources.profiling.DummyWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.profiler.Profiler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.minecraft.world.storage.WorldSavedDataStorage;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraft.world.storage.loot.functions.*;

import javax.annotation.Nullable;
import java.util.Random;

public class LootFunctionHelper {
    public static void applyFunction(LootFunction lootFunction, LootDrop lootDrop) {
        if (lootFunction instanceof SetCount) {
            lootDrop.minDrop = MathHelper.floor(((SetCount) lootFunction).countRange.getMin());
            if (lootDrop.minDrop < 0) lootDrop.minDrop = 0;
            lootDrop.item.setCount(lootDrop.minDrop < 1 ? 1 : lootDrop.minDrop);
            lootDrop.maxDrop = MathHelper.floor(((SetCount) lootFunction).countRange.getMax());
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
                lootDrop.item = lootFunction.apply(lootDrop.item, new Random(), null);
            } catch (NullPointerException ignored) {
            }
        }
    }
}
