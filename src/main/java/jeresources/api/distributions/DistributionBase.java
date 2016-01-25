package jeresources.api.distributions;

public abstract class DistributionBase
{
    private float[] distribution;
    protected int bestHeight;

    public DistributionBase(float[] distribution)
    {
        this.distribution = distribution;
    }

    public float[] getDistribution()
    {
        return distribution;
    }

    public int getBestHeight()
    {
        return bestHeight;
    }
}
