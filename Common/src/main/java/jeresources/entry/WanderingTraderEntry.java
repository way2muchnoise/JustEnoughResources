package jeresources.entry;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import jeresources.compatibility.CompatBase;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

public class WanderingTraderEntry extends AbstractVillagerEntry<WanderingTrader> {

    public WanderingTraderEntry(Int2ObjectMap<VillagerTrades.ItemListing[]> itemListings) {
        super(itemListings);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("entity.minecraft.wandering_trader");
    }

    public WanderingTrader getVillagerEntity() {
        if (this.entity == null) {
            /*
             * level must be a client level here.
             * Passing in a ServerLevel can allow villagers to load all kinds of things,
             * like in the `VillagerTrades.TreasureMapForEmeralds` which loads chunks!
             */
            this.entity = EntityType.WANDERING_TRADER.create(CompatBase.getLevel(), EntitySpawnReason.LOAD);
            assert this.entity != null;
        }
        return this.entity;
    }

    @Override
    public List<ItemStack> getPois() {
        return Collections.emptyList();
    }

    @Override
    public boolean hasPois() {
        return false;
    }

    @Override
    public boolean hasLevels() {
        return false;
    }
}
