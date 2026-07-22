package jeresources.entry;

import jeresources.compatibility.CompatBase;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.Collections;
import java.util.List;

public class WanderingTraderEntry extends AbstractVillagerEntry<WanderingTrader> {

    public WanderingTraderEntry(List<VillagerTrade> itemListings) {
        super(Collections.singletonMap(1, itemListings));
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
            this.entity = EntityTypes.WANDERING_TRADER.create(CompatBase.getLevel(), EntitySpawnReason.LOAD);
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
