package jeresources.api.distributions;

import jeresources.api.utils.DistributionHelpers;

public class DistributionCustom extends DistributionBase
{

    public DistributionCustom(float[] distribution)
    {
        super(distribution);
        this.bestHeight = DistributionHelpers.calculateMeanLevel(this.getDistribution(), distribution.length / 2);
    }

    public DistributionCustom(float[] distribution, int bestHeight)
    {
        super(distribution);
        this.bestHeight = bestHeight;
    }

}
