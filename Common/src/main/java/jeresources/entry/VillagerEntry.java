package jeresources.entry;

import jeresources.compatibility.CompatBase;
import jeresources.util.VillagersHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.npc.villager.Villager;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.VillagerTrade;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VillagerEntry extends AbstractVillagerEntry<Villager>{
    private final VillagerProfession profession;

    public VillagerEntry(VillagerProfession profession, Map<Integer, List<VillagerTrade>> itemListings) {
        super();
        this.profession = profession;
        // only make trades after setting profession
        addITradeLists(itemListings);
    }

    @Override
    public Component getDisplayName() {
        return this.profession.name();
    }

    public VillagerProfession getProfession() {
        return this.profession;
    }

    @Override
    public Villager getVillagerEntity() {
        if (this.entity == null) {
            /*
             * level must be a client level here.
             * Passing in a ServerLevel can allow villagers to load all kinds of things,
             * like in the `VillagerTrades.TreasureMapForEmeralds` which loads chunks!
             */
            this.entity = EntityTypes.VILLAGER.create(CompatBase.getLevel(), EntitySpawnReason.LOAD);
            assert this.entity != null;
            this.entity.setVillagerData(this.entity.getVillagerData().withProfession(BuiltInRegistries.VILLAGER_PROFESSION.wrapAsHolder(this.profession)));
            this.entity.tick();
        }
        return this.entity;
    }

    @Override
    public List<ItemStack> getPois() {
        return VillagersHelper.getPoiBlocks(this.profession.heldJobSite()).stream().map(blockstate -> new ItemStack(blockstate.getBlock())).collect(Collectors.toList());
    }

    @Override
    public boolean hasPois() {
        return !VillagersHelper.getPoiBlocks(this.profession.heldJobSite()).isEmpty();
    }

    @Override
    public boolean hasLevels() {
        return true;
    }
}
