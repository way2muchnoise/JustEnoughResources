package jeresources.jei.mob;

import jeresources.api.utils.DropItem;
import jeresources.config.Settings;
import jeresources.entries.MobEntry;
import jeresources.jei.mob.JEIMobCategory;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MobWrapper extends TemplateRecipeHandler.CachedRecipe
{
    public MobEntry mob;
    public int set, lastSet;
    private long cycleAt;

    public MobWrapper(MobEntry mob)
    {
        this.mob = mob;
        this.set = 0;
        this.lastSet = (mob.getDrops().length / (Settings.ITEMS_PER_COLUMN + 1));
        cycleAt = -1;
    }

    public EntityLivingBase getMob()
    {
        return this.mob.getEntity();
    }

    @Override
    public PositionedStack getResult()
    {
        if (mob.getDrops().length == 0) return null;
        return new PositionedStack(mob.getDrops()[set * Settings.ITEMS_PER_COLUMN].item, JEIMobCategory.X_FIRST_ITEM, JEIMobCategory.Y_FIRST_ITEM);
    }

    @Override
    public List<PositionedStack> getOtherStacks()
    {
        List<PositionedStack> list = new ArrayList<PositionedStack>();
        int y = JEIMobCategory.Y_FIRST_ITEM;
        for (int i = set * Settings.ITEMS_PER_COLUMN; i < set * Settings.ITEMS_PER_COLUMN + Settings.ITEMS_PER_COLUMN; i++)
        {
            if (i >= mob.getDrops().length) break;
            list.add(new PositionedStack(mob.getDrops()[i].item, JEIMobCategory.X_FIRST_ITEM, y));
            y += JEIMobCategory.SPACING_Y;
        }
        if (list.size() > 0) list.remove(0);
        return list;
    }

    public void cycleOutputs(long tick, int recipe)
    {
        if (cycleAt == -1 || recipe != JEIMobCategory.lastRecipe)
        {
            JEIMobCategory.lastRecipe = recipe;
            cycleAt = tick + JEIMobCategory.CYCLE_TIME;
            return;
        }

        if (tick >= cycleAt)
        {
            if (++set > lastSet) set = 0;
            cycleAt += JEIMobCategory.CYCLE_TIME;
        }
    }

    public List<String> getToolTip(ItemStack stack)
    {
        for (DropItem item : mob.getDrops())
        {
            if (item.item.isItemEqual(stack)) return item.conditionals;
        }
        return new ArrayList<String>();
    }
}
