package jeresources.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import org.apache.commons.lang3.text.WordUtils;

public class MobHelper {
    public static String getExpandedName(LivingEntity entity) {
        String expandedName = entity.getName().getString();

        if (entity instanceof Sheep sheep) {
            String colorNameKey = String.format("color.minecraft.%s", sheep.getColor().getName());
            String localizedColorName = TranslationHelper.translateAndFormat(colorNameKey);
            expandedName = String.format("%s (%s)", expandedName, localizedColorName);
        }

        return WordUtils.capitalize(expandedName);
    }
}
