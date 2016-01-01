package jeresources.api.utils;

import net.minecraft.util.StatCollector;

public class LightLevel
{
    public static LightLevel any = new LightLevel(-1, Relative.above);
    public static LightLevel bat = new LightLevel(4);
    public static LightLevel hostile = new LightLevel(8);
    public static LightLevel blaze = new LightLevel(12);

    int lightLevel;
    Relative relative;

    /**
     * @param level    the level of light
     * @param relative an {@link jeresources.api.utils.LightLevel.Relative}
     */
    LightLevel(int level, Relative relative)
    {
        this.lightLevel = level;
        this.relative = relative;
    }

    public static LightLevel decodeLightLevel(String string)
    {
        if (string == null || string.equals("") || !string.contains(":")) return any;
        String[] splitString = string.split(":");

        if (splitString.length != 2) return any;
        int level;
        try
        {
            level = Integer.valueOf(splitString[0]);
        } catch (Exception e)
        {
            return any;
        }
        if (level < 0 || level > 15) return any;
        return new LightLevel(level, splitString[1].equals("a") ? Relative.above : Relative.below);
    }

    public String encode()
    {
        return this.lightLevel + ":" + (relative == Relative.above ? "a" : "b");
    }

    /**
     * @param level    the level of light
     * @param relative the relative positive is above, negative is below. Zero will also be below.
     */
    LightLevel(int level, int relative)
    {
        this.lightLevel = level;
        this.relative = relative > 0 ? Relative.above : Relative.below;
    }

    /**
     * @param level the maximum level light the mob can spawn (the {@link jeresources.api.utils.LightLevel.Relative} will be below)
     */
    LightLevel(int level)
    {
        this(level, Relative.below);
    }

    @Override
    public String toString()
    {
        String base = StatCollector.translateToLocal("ner.lightLevel");
        if (lightLevel < 0) return base + ": " + StatCollector.translateToLocal("ner.any");;
        return base + ": " + relative.toString() + " " + lightLevel;
    }

    /**
     * The {@link jeresources.api.utils.LightLevel.Relative} enum holding an above and below entry
     */
    public enum Relative
    {
        above("Above"),
        below("Below");
        String text;

        Relative(String string)
        {
            this.text = string;
        }

        @Override
        public String toString()
        {
            return text;
        }
    }

}
