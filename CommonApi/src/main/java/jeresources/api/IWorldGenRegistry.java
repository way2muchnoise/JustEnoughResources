package jeresources.api;

import jeresources.api.distributions.DistributionBase;
import jeresources.api.drop.LootDrop;
import jeresources.api.restrictions.Restriction;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

/**
 * Use to register world generation and block drops
 * Note: This info wil only be used if there is no DIY data present
 */
public interface IWorldGenRegistry {
    /**
     * Register a {@link ItemStack} to be shown in the WorldGen View
     *
     * @param block             the {@link net.minecraft.world.level.block.Block} as an {@link ItemStack}
     * @param deepSlateBlock    the deepslate version of the {@link net.minecraft.world.level.block.Block} as an {@link ItemStack}
     * @param distribution      the {@link DistributionBase} use {@link jeresources.api.distributions.DistributionHelpers} to create it
     * @param restriction       any {@link Restriction}s
     * @param silktouch         true if this block can only be harvested with silktouch
     * @param drops             the list of possible {@link LootDrop}s this has
     */
    void register(@Nonnull ItemStack block, @Nonnull ItemStack deepSlateBlock, DistributionBase distribution, Restriction restriction, boolean silktouch, LootDrop... drops);
    void register(@Nonnull ItemStack block, DistributionBase distribution, Restriction restriction, boolean silktouch, LootDrop... drops);
    void register(@Nonnull ItemStack block, @Nonnull ItemStack deepSlateBlock, DistributionBase distribution, Restriction restriction, LootDrop... drops);
    void register(@Nonnull ItemStack block, DistributionBase distribution, Restriction restriction, LootDrop... drops);
    void register(@Nonnull ItemStack block, @Nonnull ItemStack deepSlateBlock, DistributionBase distribution, boolean silktouch, LootDrop... drops);
    void register(@Nonnull ItemStack block, DistributionBase distribution, boolean silktouch, LootDrop... drops);
    void register(@Nonnull ItemStack block, @Nonnull ItemStack deepSlateBlock, DistributionBase distribution, LootDrop... drops);
    void register(@Nonnull ItemStack block, DistributionBase distribution, LootDrop... drops);

    /**
     * Register extra drops to existing blocks
     *
     * @param block the {@link net.minecraft.world.level.block.Block} as an {@link ItemStack}
     * @param drops the list of possible {@link LootDrop}s that should be added
     */
    void registerDrops(@Nonnull ItemStack block, LootDrop... drops);
}
