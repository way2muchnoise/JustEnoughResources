package jeresources.util;

import jeresources.entry.VillagerEntry;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

import java.util.List;

public class VillagersHelper
{
    public static void initRegistry(jeresources.registry.VillagerRegistry reg)
    {
        int professionId = 0;
        for (VillagerRegistry.VillagerProfession profession : VillagerRegistry.instance().getRegistry())
        {
            for (VillagerRegistry.VillagerCareer career : getCareers(profession))
            {
                reg.addVillagerEntry(new VillagerEntry(
                        career.getName(),
                        professionId,
                        getId(career),
                        getTrades(career)
                ));
            }
            professionId++;
        }
    }

    private static List<VillagerRegistry.VillagerCareer> getCareers(VillagerRegistry.VillagerProfession profession)
    {
        return ReflectionHelper.getPrivateValue(VillagerRegistry.VillagerProfession.class, profession, "careers");
    }

    private static List<List<EntityVillager.ITradeList>> getTrades(VillagerRegistry.VillagerCareer career)
    {
        return ReflectionHelper.getPrivateValue(VillagerRegistry.VillagerCareer.class, career, "trades");
    }

    private static int getId(VillagerRegistry.VillagerCareer career)
    {
        return ReflectionHelper.getPrivateValue(VillagerRegistry.VillagerCareer.class, career, "id");
    }
}
