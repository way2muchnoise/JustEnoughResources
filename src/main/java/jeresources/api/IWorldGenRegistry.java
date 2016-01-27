package jeresources.api;

import jeresources.api.distributions.DistributionBase;
import jeresources.api.drop.DropItem;
import jeresources.api.restrictions.Restriction;
import net.minecraft.item.ItemStack;

/**
 * Use to register world generation and block drops
 * Note: This info wil only be used if there is no DIY data present
 */
public interface IWorldGenRegistry
{
    void register(ItemStack block, DistributionBase distribution, DropItem... drops);
    void register(ItemStack block, DistributionBase distribution, Restriction restriction, DropItem... drops);
    void register(ItemStack block, DistributionBase distribution, boolean silktouch, DropItem... drops);
    void register(ItemStack block, DistributionBase distribution, Restriction restriction, boolean silktouch, DropItem... drops);

    void registerDrops(ItemStack block, DropItem... drops);
}
