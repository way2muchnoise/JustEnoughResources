package jeresources.utils;

import net.minecraft.util.WeightedRandomChestContent;

import java.util.Arrays;

public class WeightedRandomChestContentHelper
{
    /**
     * Sorts WeightedRandomChestContents based on dropChance
     * Uses merge sorting algorithm
     *
     * @param contents
     * @return
     */
    public static WeightedRandomChestContent[] sort(WeightedRandomChestContent[] contents)
    {
        if (contents.length <= 1) return contents;

        int split = contents.length / 2;
        WeightedRandomChestContent[] left = subArray(contents, 0, split);
        WeightedRandomChestContent[] right = subArray(contents, split, contents.length);

        left = sort(left);
        right = sort(right);

        return merge(left, right);
    }

    private static float getAverageChance(WeightedRandomChestContent chestContent)
    {
        return (float) (chestContent.maxStackSize + chestContent.minStackSize) / 2 * chestContent.itemWeight;
    }

    private static WeightedRandomChestContent[] merge(WeightedRandomChestContent[] left, WeightedRandomChestContent[] right)
    {
        int length = left.length + right.length;
        WeightedRandomChestContent[] merged = new WeightedRandomChestContent[length];

        int i = 0;
        int li = 0;
        int ri = 0;
        while (i < length)
        {
            if ((li < left.length) && (ri < right.length))
            {
                if (getAverageChance(left[li]) >= getAverageChance(right[ri]))
                {
                    merged[i] = left[li];
                    i++;
                    li++;
                } else
                {
                    merged[i] = right[ri];
                    i++;
                    ri++;
                }
            } else
            {
                if (li >= left.length)
                {
                    while (ri < right.length)
                    {
                        merged[i] = right[ri];
                        i++;
                        ri++;
                    }
                }
                if (ri >= right.length)
                {
                    while (li < left.length)
                    {
                        merged[i] = left[li];
                        i++;
                        li++;
                    }
                }
            }
        }

        return merged;
    }

    private static WeightedRandomChestContent[] subArray(WeightedRandomChestContent[] array, int begin, int end)
    {
        return Arrays.copyOfRange(array, begin, end);
    }
}
