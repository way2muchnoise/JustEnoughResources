package jeresources.api.distributions;

public class DistributionTriangular extends DistributionBase {
    /**
     * @param midY      top of the triangular distribution
     * @param range     length of the sides
     * @param maxChance chance at the top
     */
    public DistributionTriangular(int midY, int range, float maxChance) {
        super(DistributionHelpers.getTriangularDistribution(midY, range, maxChance));
        this.bestHeight = midY;
    }

    /**
     * @param veinCount the amount of veins per chunk
     * @param veinSize  the amount of blocks per vein
     * @param midY      top of the triangular distribution
     * @param range     length of the sides
     */
    public DistributionTriangular(int veinCount, int veinSize, int midY, int range) {
        super(DistributionHelpers.getTriangularDistribution(midY, range, DistributionHelpers.calculateChance(veinCount, veinSize, midY - range, midY + range)));
        this.bestHeight = midY;
    }
}
