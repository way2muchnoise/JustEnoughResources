package jeresources.registry;

import jeresources.entries.DungeonEntry;
import jeresources.utils.ReflectionHelper;
import jeresources.utils.TranslationHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ChestGenHooks;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DungeonRegistry
{
    private Map<String, DungeonEntry> registry = new LinkedHashMap<String, DungeonEntry>();
    public static Map<String, String> categoryToLocalKeyMap = new LinkedHashMap<String, String>();
    private static DungeonRegistry instance = null;

    public static DungeonRegistry getInstance()
    {
        if (instance == null)
            return instance = new DungeonRegistry();
        return instance;
    }

    public DungeonRegistry()
    {
        addCategoryMapping("mineshaftCorridor", "jer.dungeon.mineshaftCorridor");
        addCategoryMapping("pyramidDesertyChest", "jer.dungeon.pyramidDesertyChest");
        addCategoryMapping("pyramidJungleChest", "jer.dungeon.pyramidJungleChest");
        addCategoryMapping("pyramidJungleDispenser", "jer.dungeon.pyramidJungleDispenser");
        addCategoryMapping("strongholdCorridor", "jer.dungeon.strongholdCorridor");
        addCategoryMapping("strongholdLibrary", "jer.dungeon.strongholdLibrary");
        addCategoryMapping("strongholdCrossing", "jer.dungeon.strongholdCrossing");
        addCategoryMapping("villageBlacksmith", "jer.dungeon.villageBlacksmith");
        addCategoryMapping("bonusChest", "jer.dungeon.bonusChest");
        addCategoryMapping("dungeonChest", "jer.dungeon.dungeonChest");
        addCategoryMapping("netherFortress", "jer.dungeon.netherFortress");
    }

    public static boolean addCategoryMapping(String category, String name)
    {
        if (!categoryToLocalKeyMap.containsKey(category))
        {
            categoryToLocalKeyMap.put(category, name);
            return true;
        }
        return false;
    }

    public boolean registerChestHook(String name, ChestGenHooks chestGenHooks)
    {
        if (!registry.containsKey(name))
        {
            registry.put(name, new DungeonEntry(name, chestGenHooks));
            return true;
        }
        return false;
    }

    public boolean registerChestHook(ChestGenHooks chestGenHooks)
    {
        String name = ReflectionHelper.getPrivateValue(ChestGenHooks.class, chestGenHooks, "category");
        if (categoryToLocalKeyMap.containsKey(name))
            return registerChestHook(categoryToLocalKeyMap.get(name), chestGenHooks);
        return registerChestHook(name, chestGenHooks);
    }

    public void registerDungeonEntry(DungeonEntry entry)
    {
        String name = entry.getName();
        if (registry.containsKey(name)) return;
        registry.put(name, entry);
    }

    public List<DungeonEntry> getDungeons(ItemStack item)
    {
        List<DungeonEntry> list = new ArrayList<DungeonEntry>();
        for (DungeonEntry entry : registry.values())
            if (entry.containsItem(item)) list.add(entry);
        return list;
    }

    public List<DungeonEntry> getDungeons()
    {
        return new ArrayList<DungeonEntry>(registry.values());
    }

    public String getNumStacks(DungeonEntry entry)
    {
        int max = entry.getMaxStacks();
        int min = entry.getMinStacks();
        if (min == max) return String.format(TranslationHelper.translateToLocal("jer.stacks"), max);
        return String.format(TranslationHelper.translateToLocal("jer.stacks"), min + " - " + max);
    }

    public void clear()
    {
        instance = new DungeonRegistry();
    }
}
