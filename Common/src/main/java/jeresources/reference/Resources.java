package jeresources.reference;

import jeresources.jei.BackgroundDrawable;
import net.minecraft.resources.ResourceLocation;

public final class Resources {
    public static final class Gui {
        public static final class Jei {
            public static final BackgroundDrawable MOB = new BackgroundDrawable(Textures.Gui.Jei.MOB, 163, 120);
            public static final BackgroundDrawable WORLD_GEN = new BackgroundDrawable(Textures.Gui.Jei.WORLD_GEN, 156, 80);
            public static final BackgroundDrawable DUNGEON = new BackgroundDrawable(Textures.Gui.Jei.DUNGEON, 163, 120);
            public static final BackgroundDrawable PLANT = new BackgroundDrawable(Textures.Gui.Jei.PLANT, 165, 120);
            public static final BackgroundDrawable ENCHANTMENT = new BackgroundDrawable(Textures.Gui.Jei.ENCHANTMENT, 163, 120);
            public static final BackgroundDrawable VILLAGER = new BackgroundDrawable(Textures.Gui.Jei.VILLAGER, 163, 120);
            public static final BackgroundDrawable VILLAGER_ARROW = new BackgroundDrawable(Textures.Gui.Jei.VILLAGER, 0, 120, 20, 20);
            public static final BackgroundDrawable VILLAGER_SLOT = new BackgroundDrawable(Textures.Gui.Jei.VILLAGER, 22, 120, 18, 18);
            public static final ResourceLocation TABS = ResourceLocation.fromNamespaceAndPath(Reference.ID, Textures.Gui.Jei.TABS);
        }
    }

    public static final class Vanilla {
        public static final ResourceLocation FONT = ResourceLocation.withDefaultNamespace("textures/font/ascii.png");
        public static final ResourceLocation CHEST = ResourceLocation.withDefaultNamespace("textures/entity/chest/normal.png");
    }
}
