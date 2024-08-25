package jeresources.api.util;

import jeresources.api.conditionals.Conditional;
import jeresources.api.drop.LootDrop;
import net.minecraft.world.level.storage.loot.predicates.*;

public class LootConditionHelper {
    public static void applyCondition(LootItemCondition condition, LootDrop lootDrop) {
        if (condition instanceof LootItemKilledByPlayerCondition) {
            lootDrop.addConditional(Conditional.playerKill);
        } else if (condition instanceof LootItemRandomChanceCondition) {
            lootDrop.chance = ((LootItemRandomChanceCondition) condition).chance().getFloat(null); // TODO check if null is OK to use
        } else if (condition instanceof LootItemRandomChanceWithEnchantedBonusCondition) {
            lootDrop.chance = ((LootItemRandomChanceWithEnchantedBonusCondition) condition).enchantedChance().calculate(1);
            lootDrop.addConditional(Conditional.affectedByLooting);
        } else if (condition instanceof LootItemBlockStatePropertyCondition) {
            /*
            for (EntityProperty property : ((EntityHasProperty) condition).properties) {
                if (property instanceof Fire)
                    lootDrop.addConditional(Conditional.burning);
            }
            */
        }
    }
}
