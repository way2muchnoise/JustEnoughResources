package jeresources.util;

import mezz.jei.gui.Focus;
import mezz.jei.gui.RecipeGuiLogic;
import mezz.jei.gui.RecipesGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import java.lang.reflect.Field;

public class FocusHelper
{
    public static Focus getFocus()
    {
        Focus focus = new Focus();
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (screen instanceof RecipesGui)
        {
            try
            {
                Field logicField = RecipesGui.class.getDeclaredField("logic");
                logicField.setAccessible(true);
                RecipeGuiLogic logic = (RecipeGuiLogic) logicField.get(screen);
                focus = logic.getFocus();
            } catch (IllegalAccessException | NoSuchFieldException ignore) { }
        }
        return focus;
    }
}
