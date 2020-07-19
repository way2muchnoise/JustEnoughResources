package jeresources.util;

import jeresources.api.conditionals.Conditional;
import jeresources.api.drop.LootDrop;
import net.minecraft.loot.conditions.*;

public class LootConditionHelper {
    public static void applyCondition(ILootCondition condition, LootDrop lootDrop) {
        if (condition instanceof KilledByPlayer) {
            lootDrop.addConditional(Conditional.playerKill);
        } else if (condition instanceof RandomChance) {
            lootDrop.chance = ((RandomChance) condition).chance;
        } else if (condition instanceof RandomChanceWithLooting) {
            lootDrop.chance = ((RandomChanceWithLooting) condition).chance;
            lootDrop.addConditional(Conditional.affectedByLooting);
        } else if (condition instanceof EntityHasProperty) {
            /*
            for (EntityProperty property : ((EntityHasProperty) condition).properties) {
                if (property instanceof Fire)
                    lootDrop.addConditional(Conditional.burning);
            }
            */
        }
    }
}
