package jeresources.api.distributions;

public class DistributionUnderWater extends DistributionBase {

    public DistributionUnderWater(float maxChance) {
        super(DistributionHelpers.getUnderwaterDistribution(maxChance));
        this.bestHeight = 61;
    }
}
