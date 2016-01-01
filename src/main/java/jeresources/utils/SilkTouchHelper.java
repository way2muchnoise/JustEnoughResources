package jeresources.utils;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class SilkTouchHelper
{
    public static enum SilkTouchClasses
    {
        metallurgy("com.teammetallurgy.metallurgy.metals.MetalBlock"),
        electriOre("Reika.ElectriCraft.Blocks.BlockElectriOre"),
        fluoriteOre("Reika.ReactorCraft.Blocks.BlockFluoriteOre"),
        reactorOre("Reika.ReactorCraft.Blocks.BlockReactorOre"),
        thaumcraftOre("thaumcraft.common.blocks.BlockCustomOre"),
        BROre("erogenousbeef.bigreactors.common.block.BlockBROre"),
        forestryOre("forestry.core.gadgets.BlockResource"),
        quartzOre("appeng.block.solids.OreQuartz"),
        chargedQuartzOre("appeng.block.solids.OreQuartzCharged");

        public String className;

        private SilkTouchClasses(String className)
        {
            this.className = className;
        }

        public String getClassNameToString()
        {
            return "class " + className;
        }
    }

    public static boolean isOreBlock(ItemStack itemStack)
    {
        Block block = Block.getBlockFromItem(itemStack.getItem());
        if (block == Blocks.diamond_ore || block == Blocks.lapis_ore || block == Blocks.redstone_ore || block == Blocks.lit_redstone_ore || block == Blocks.coal_block)
            return true;
        for (SilkTouchClasses silkTouchClass : SilkTouchClasses.values())
            if (block.getClass().toString().equals(silkTouchClass.getClassNameToString())) return true;
        return false;
    }
}
