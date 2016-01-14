package jeresources.reference;

import jeresources.jei.BackgroundDrawable;
import net.minecraft.util.ResourceLocation;

public final class Resources
{
    public static final class Gui
    {
        public static final class Jei
        {
            public static final BackgroundDrawable MOB = new BackgroundDrawable(Textures.Gui.Jei.MOB, 163, 120);
            public static final BackgroundDrawable ORE = new BackgroundDrawable(Textures.Gui.Jei.ORE, 163, 65);
            public static final BackgroundDrawable DUNGEON = new BackgroundDrawable(Textures.Gui.Jei.DUNGEON, 163, 120);
            public static final BackgroundDrawable PLANT = new BackgroundDrawable(Textures.Gui.Jei.PLANT, 165, 120);
            public static final BackgroundDrawable ENCHANTMENT = new BackgroundDrawable(Textures.Gui.Jei.ENCHANTMENT, 163, 120);
        }
    }

    public static final class Vanilla
    {
        public static final ResourceLocation FONT = new ResourceLocation("textures/font/ascii.png");
        public static final ResourceLocation CHEST = new ResourceLocation("textures/entity/chest/normal.png");
    }
}
