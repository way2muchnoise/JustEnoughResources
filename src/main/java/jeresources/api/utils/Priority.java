package jeresources.api.utils;

public enum Priority
{
    FIRST,
    SECOND,
    THIRD,
    FOURTH,
    FIFTH;

    public static Priority getPriority(int i)
    {
        if (i < 0 || i >= values().length) return FIRST;
        return values()[i];
    }

}
