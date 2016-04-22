package jeresources.api.restrictions;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class BlockRestriction
{
    public static final BlockRestriction STONE = new BlockRestriction(Blocks.STONE);
    public static final BlockRestriction NETHER = new BlockRestriction(Blocks.NETHERRACK);
    public static final BlockRestriction END = new BlockRestriction(Blocks.END_STONE);

    private Block block;
    private int metadata;

    public BlockRestriction(Block block)
    {
        this(block, 0);
    }

    public BlockRestriction(Block block, int metadata)
    {
        this.block = block;
        this.metadata = metadata;
    }

    @Override
    public int hashCode()
    {
        return block.hashCode() ^ metadata;
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
        return "Block: " + block.getUnlocalizedName() + ":" + metadata;
    }
}
