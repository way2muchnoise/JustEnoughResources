package jeresources.api.conditionals;

import jeresources.api.drop.LootDrop;
import net.minecraft.world.storage.loot.properties.EntityProperty;

public interface ICustomEntityProperty extends EntityProperty
{
    /**
     * Interact with the {@link LootDrop} to accommodate your {@link EntityProperty}
     * @param drop the {@link LootDrop} to change
     */
    void applyProperty(LootDrop drop);
}
