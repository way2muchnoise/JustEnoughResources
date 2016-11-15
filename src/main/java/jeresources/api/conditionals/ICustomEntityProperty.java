package jeresources.api.conditionals;

import jeresources.api.drop.LootDrop;
import net.minecraft.world.storage.loot.properties.EntityProperty;

/**
 * Should be implemented on a {@link EntityProperty}
 * when a table has use that {@link EntityProperty}
 * you'll be granted access to the {@link LootDrop}
 * <p>
 * It is advised to add {@link Conditional}s,
 * but you can also change stack sizes or other data.
 */
public interface ICustomEntityProperty extends EntityProperty {
    /**
     * Interact with the {@link LootDrop} to accommodate your {@link EntityProperty}
     *
     * @param drop the {@link LootDrop} to change
     */
    void applyProperty(LootDrop drop);
}
