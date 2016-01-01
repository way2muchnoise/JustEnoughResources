package jeresources.api.utils.restrictions;

import jeresources.api.messages.utils.MessageKeys;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class BlockRestriction
{
    public static final BlockRestriction STONE = new BlockRestriction(Blocks.stone);
    public static final BlockRestriction NETHER = new BlockRestriction(Blocks.netherrack);
    public static final BlockRestriction END = new BlockRestriction(Blocks.end_stone);

    private Block block;
    private int metadata;

    public BlockRestriction(Block block)
    {
        this(block,0);
    }

    public BlockRestriction(Block block, int metadata)
    {
        this.block = block;
        this.metadata = metadata;
    }

    public BlockRestriction(NBTTagCompound tagCompound)
    {
        ItemStack stack = ItemStack.loadItemStackFromNBT(tagCompound.getCompoundTag(MessageKeys.stack));
        block = Block.getBlockFromItem(stack.getItem());
        metadata = stack.getItemDamage();
    }

    @Override
    public int hashCode()
    {
         return block.hashCode() ^ metadata;
    }

    public NBTTagCompound writeToNBT()
    {
        return writeToNBT(new NBTTagCompound());
    }

    public NBTTagCompound writeToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setTag(MessageKeys.stack, new ItemStack(block,1,metadata).writeToNBT(new NBTTagCompound()));
        return tagCompound;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof BlockRestriction)
        {
            BlockRestriction other = (BlockRestriction) obj;
            return other.block == block && other.metadata == metadata;
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "Block: "+block.getUnlocalizedName() + ":" + metadata;
    }
}
