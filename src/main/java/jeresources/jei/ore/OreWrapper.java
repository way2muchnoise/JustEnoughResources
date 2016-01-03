package jeresources.jei.ore;

import jeresources.entries.OreMatchEntry;
import jeresources.jei.ore.JEIOreCategory;
import net.minecraft.item.ItemStack;

import java.util.List;

public class OreWrapper extends TemplateRecipeHandler.CachedRecipe
{
    private OreMatchEntry oreMatchEntry;
    private List<ItemStack> oresAndDrops;

    public OreWrapper(OreMatchEntry oreMatchEntry)
    {
        this.oreMatchEntry = oreMatchEntry;
        this.oresAndDrops = oreMatchEntry.getOresAndDrops();
    }

    @Override
    public PositionedStack getResult()
    {
        int index = (cycleticks / JEIOreCategory.CYCLE_TIME) % this.oresAndDrops.size();
        return new PositionedStack(this.oresAndDrops.get(index), JEIOreCategory.X_ITEM, JEIOreCategory.Y_ITEM);
    }

    public int getLineColor()
    {
        return this.oreMatchEntry.getColour();
    }

    public List<String> getRestrictions()
    {
        return this.oreMatchEntry.getRestrictions();
    }

    public boolean contains(ItemStack itemStack)
    {
        for (ItemStack listStack : this.oresAndDrops)
            if (listStack.isItemEqual(itemStack)) return true;
        return false;
    }
}
