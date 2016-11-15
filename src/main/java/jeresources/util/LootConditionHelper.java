package jeresources.util;

import jeresources.api.conditionals.Conditional;
import jeresources.api.conditionals.ICustomEntityProperty;
import jeresources.api.drop.LootDrop;
import net.minecraft.world.storage.loot.conditions.*;
import net.minecraft.world.storage.loot.properties.EntityOnFire;
import net.minecraft.world.storage.loot.properties.EntityProperty;

public class LootConditionHelper {
    public static void applyCondition(LootCondition condition, LootDrop lootDrop) {
        if (condition instanceof KilledByPlayer) {
            lootDrop.addConditional(Conditional.playerKill);
        } else if (condition instanceof RandomChance) {
            lootDrop.chance = ((RandomChance) condition).chance;
        } else if (condition instanceof RandomChanceWithLooting) {
            lootDrop.chance = ((RandomChanceWithLooting) condition).chance;
            lootDrop.addConditional(Conditional.affectedByLooting);
        } else if (condition instanceof EntityHasProperty) {
            for (EntityProperty property : ((EntityHasProperty) condition).properties) {
                if (property instanceof ICustomEntityProperty)
                    ((ICustomEntityProperty) property).applyProperty(lootDrop);
                else if (property instanceof EntityOnFire)
                    lootDrop.addConditional(Conditional.burning);
            }
        }
    }
}
