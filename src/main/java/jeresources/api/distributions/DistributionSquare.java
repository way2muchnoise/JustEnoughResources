package jeresources.api.distributions;

public class DistributionSquare extends DistributionBase
{
    /**
     * Creates pure square distribution
     *
     * @param minY   first occurrence
     * @param maxY   last occurrence
     * @param chance chance for the distribution
     */
    public DistributionSquare(int minY, int maxY, float chance)
    {
        super(DistributionHelpers.getSquareDistribution(Math.max(minY, 0), Math.min(maxY, 255), chance));
        this.bestHeight = (minY + maxY) / 2;
    }

    /**
     * Creates rounded square distribution
     *
     * @param min0   start of the ramp
     * @param minY   end of the ramp up
     * @param maxY   start of the ramp down
     * @param max0   end of ramp down
     * @param chance the chance at the top
     */
    public DistributionSquare(int min0, int minY, int maxY, int max0, float chance)
    {
        super(DistributionHelpers.getRoundedSquareDistribution(Math.max(min0, 0), Math.max(minY, 0), Math.min(maxY, 255), Math.min(max0, 255), chance));
        this.bestHeight = DistributionHelpers.calculateMeanLevel(this.getDistribution(), (minY + maxY) / 2);
    }

    /**
     * Creates rounded square distribution
     *
     * @param veinCount the amount of veins per chunk
     * @param veinSize  the amount of blocks per vein
     * @param minY      the lowest Y value for a vein
     * @param maxY      the highest Y value for a vein
     */
    public DistributionSquare(int veinCount, int veinSize, int minY, int maxY)
    {
        this(minY - veinSize / 2, minY, maxY, maxY + veinSize / 2, DistributionHelpers.calculateChance(veinCount, veinSize, minY, maxY));
    }
}
