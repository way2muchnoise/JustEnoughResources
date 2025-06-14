package jeresources.entry;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import jeresources.compatibility.CompatBase;
import jeresources.util.VillagersHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class VillagerEntry extends AbstractVillagerEntry<Villager>{
    private final VillagerProfession profession;

    public VillagerEntry(VillagerProfession profession, Int2ObjectMap<VillagerTrades.ItemListing[]> itemListings) {
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
            this.entity = EntityType.VILLAGER.create(CompatBase.getLevel(), EntitySpawnReason.LOAD);
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
