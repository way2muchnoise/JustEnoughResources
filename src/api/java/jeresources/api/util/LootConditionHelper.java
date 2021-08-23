package jeresources.api.util;

import jeresources.api.conditionals.Conditional;
import jeresources.api.drop.LootDrop;
import net.minecraft.world.level.storage.loot.predicates.*;

public class LootConditionHelper {
    public static void applyCondition(LootItemCondition condition, LootDrop lootDrop) {
        if (condition instanceof LootItemKilledByPlayerCondition) {
            lootDrop.addConditional(Conditional.playerKill);
        } else if (condition instanceof LootItemRandomChanceCondition) {
            lootDrop.chance = ((LootItemRandomChanceCondition) condition).probability;
        } else if (condition instanceof LootItemRandomChanceWithLootingCondition) {
            lootDrop.chance = ((LootItemRandomChanceWithLootingCondition) condition).percent;
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
