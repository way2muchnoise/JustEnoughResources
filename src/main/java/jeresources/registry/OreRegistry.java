package jeresources.registry;

import jeresources.api.messages.ModifyOreMessage;
import jeresources.api.messages.RegisterOreMessage;
import jeresources.api.utils.DropItem;
import jeresources.api.utils.Priority;
import jeresources.entries.OreMatchEntry;
import jeresources.utils.MapKeys;
import net.minecraft.item.ItemStack;

import java.util.*;

public class OreRegistry
{
    private static Map<Integer, OreMatchEntry> matchEntryMap = new LinkedHashMap<Integer, OreMatchEntry>();
    private static Map<String, Set<Integer>> dropMap = new LinkedHashMap<String, Set<Integer>>();

    public static void registerOre(RegisterOreMessage message)
    {
        String key = MapKeys.getKey(message.getOre());
        if (key == null) return;

        if (dropMap.containsKey(key))
        {
            Set<Integer> hashCodes = dropMap.get(key);
            boolean matched = false;
            for (int hashCode : hashCodes)
            {
                if (matchEntryMap.get(hashCode).add(message))
                {
                    matched = true;
                    break;
                }
            }
            if (!matched)
            {
                int newHash = addNewOre(key, message);
                for (int hashCode : hashCodes)
                {
                    if (hashCode != newHash)
                        matchEntryMap.get(newHash).add(matchEntryMap.get(hashCode));
                }
            }
        } else
        {
            addNewOre(key, message);
        }
        DropItem[] drops = message.getDrops();
        if (drops == null || drops.length == 0) return;
        addDrops(new ModifyOreMessage(message.getOre(), Priority.FIRST, drops));
    }

    private static int addNewOre(String key, RegisterOreMessage message)
    {
        OreMatchEntry newMatch = new OreMatchEntry(message);
        int hashCode = newMatch.hashCode();
        Set<Integer> hashSet = dropMap.containsKey(key) ? dropMap.get(key) : new LinkedHashSet<Integer>();
        hashSet.add(hashCode);
        dropMap.put(key, hashSet);
        matchEntryMap.put(hashCode, newMatch);
        return hashCode;
    }

    public static List<OreMatchEntry> getOres()
    {
        List<OreMatchEntry> result = new ArrayList<OreMatchEntry>();
        Set<Integer> addedCodes = new TreeSet<Integer>();
        for (Set<Integer> hashCodes : dropMap.values())
        {
            for (int hashCode : hashCodes)
            {
                if (!addedCodes.contains(hashCode))
                {
                    addedCodes.add(hashCode);
                    result.add(matchEntryMap.get(hashCode));
                }
            }
        }
        return result;
    }

    public static boolean removeDrops(ModifyOreMessage oreMod)
    {
        if (oreMod.getRemoveDrops() == null) return true;
        String oreKey = MapKeys.getKey(oreMod.getOre());
        if (oreKey == null || !dropMap.containsKey(oreKey) || dropMap.get(oreKey).isEmpty()) return false;
        for (DropItem drop : oreMod.getRemoveDrops())
        {
            String dropKey = MapKeys.getKey(drop);
            if (dropKey == null || !dropMap.containsKey(dropKey)) continue;
            Set<Integer> hashSet = dropMap.get(oreKey);
            dropMap.get(dropKey).removeAll(hashSet);
            for (int hashCode : hashSet)
                matchEntryMap.get(hashCode).removeDrop(drop.item);
        }
        return true;
    }

    public static boolean addDrops(ModifyOreMessage oreMod)
    {
        if (oreMod.getAddDrops() == null) return true;
        String oreKey = MapKeys.getKey(oreMod.getOre());
        if (oreKey == null || !dropMap.containsKey(oreKey)) return false;
        for (DropItem drop : oreMod.getAddDrops())
        {
            String dropKey = MapKeys.getKey(drop);
            if (dropKey == null) continue;
            Set<Integer> hashSet = dropMap.containsKey(dropKey) ? dropMap.get(dropKey) : new LinkedHashSet<Integer>();
            for (int hashCode : dropMap.get(oreKey))
                matchEntryMap.get(hashCode).addDrop(drop);
            hashSet.addAll(dropMap.get(oreKey));
            dropMap.put(dropKey, hashSet);
        }
        return true;
    }
}
