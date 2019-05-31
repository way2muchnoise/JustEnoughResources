package jeresources.entry;

import jeresources.api.drop.PlantDrop;
import jeresources.util.MapKeys;
import jeresources.util.SeedHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.IPlantable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlantEntry {
    private IPlantable plant;
    private ItemStack plantStack;
    private Map<String, PlantDrop> drops = new LinkedHashMap<>();
    private int totalWeight = 0;
    private IBlockState soil = null;

    public static PlantEntry registerGrass() {
        List<PlantDrop> seeds = SeedHelper.getSeeds();
        PlantEntry grass = new PlantEntry(new ItemStack(Blocks.TALLGRASS, 1, 1), seeds.toArray(new PlantDrop[seeds.size()]));
        grass.totalWeight *= 8;
        return grass;
    }

    public IBlockState getSoil() {
        return soil;
    }

    public void setSoil(IBlockState soil) {
        this.soil = soil;
    }

    public PlantEntry(ItemStack itemStack, IPlantable plant, PlantDrop... drops) {
        this.plantStack = itemStack;
        this.plant = plant;
        for (PlantDrop entry : drops) {
            this.totalWeight += entry.getWeight();
            String key = MapKeys.getKey(entry.getDrop());
            if (key != null) this.drops.put(key, entry);
        }
    }

    public PlantEntry(ItemStack itemStack, PlantDrop... drops) {
        this(itemStack, null, drops);
    }

    public <T extends Item & IPlantable> PlantEntry(T plant, PlantDrop... drops) {
        this(new ItemStack(plant), plant, drops);
    }

    public void add(PlantDrop entry) {
        String key = MapKeys.getKey(entry.getDrop());
        if (!this.drops.containsKey(key)) return;
        this.drops.put(key, new PlantDrop(entry.getDrop(), (this.totalWeight + entry.getWeight())));
    }

    public IPlantable getPlant() {
        return this.plant;
    }

    public ItemStack getPlantItemStack() {
        return this.plantStack;
    }

    public List<PlantDrop> getDrops() {
        return new ArrayList<>(this.drops.values());
    }

    public List<ItemStack> getLootDropStacks() {
        return getDrops().stream().map(PlantDrop::getDrop).collect(Collectors.toList());
    }

    public PlantDrop getDrop(ItemStack itemStack) {
        return this.drops.get(MapKeys.getKey(itemStack));
    }

    public int getTotalWeight() {
        return totalWeight;
    }
}
