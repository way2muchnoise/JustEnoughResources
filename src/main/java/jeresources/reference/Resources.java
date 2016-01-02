package jeresources.reference;

import jeresources.jei.BackgroundDrawable;
import net.minecraft.util.ResourceLocation;

public final class Resources
{
    public static final class Gui
    {
        public static final class Jei
        {
            public static final ResourceLocation MOB = JeiBackground.MOB.getResource();
            public static final ResourceLocation ORE = JeiBackground.ORE.getResource();
            public static final ResourceLocation DUNGEON = JeiBackground.DUNGEON.getResource();
            public static final ResourceLocation PLANT = JeiBackground.PLANT.getResource();
            public static final ResourceLocation ADV_PLANT = JeiBackground.ADV_PLANT.getResource();
            public static final ResourceLocation ENCHANTMENT = JeiBackground.ENCHANTMENT.getResource();
        }

        public static final class JeiBackground
        {
            public static final BackgroundDrawable MOB = new BackgroundDrawable(Textures.Gui.Jei.MOB);
            public static final BackgroundDrawable ORE = new BackgroundDrawable(Textures.Gui.Jei.ORE);
            public static final BackgroundDrawable DUNGEON = new BackgroundDrawable(Textures.Gui.Jei.DUNGEON, 166, 130);
            public static final BackgroundDrawable PLANT = new BackgroundDrawable(Textures.Gui.Jei.PLANT, 166, 130);
            public static final BackgroundDrawable ADV_PLANT = new BackgroundDrawable(Textures.Gui.Jei.ADV_PLANT);
            public static final BackgroundDrawable ENCHANTMENT = new BackgroundDrawable(Textures.Gui.Jei.ENCHANTMENT);
        }
    }

    public static final class Vanilla
    {
        public static final ResourceLocation FONT = new ResourceLocation("textures/font/ascii.png");
        public static final ResourceLocation CHEST = new ResourceLocation("textures/entity/chest/normal.png");
    }
}
