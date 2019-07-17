package jeresources.api.conditionals;


import net.minecraft.client.resources.I18n;

public class LightLevel {
    public static LightLevel any = new LightLevel(-1, Relative.above);
    public static LightLevel bat = new LightLevel(4);
    public static LightLevel hostile = new LightLevel(8);
    public static LightLevel blaze = new LightLevel(12);

    int lightLevel;
    Relative relative;

    /**
     * @param level    the level of light
     * @param relative an {@link LightLevel.Relative}
     */
    LightLevel(int level, Relative relative) {
        this.lightLevel = level;
        this.relative = relative;
    }

    /**
     * @param level the maximum level light the mob can spawn (the {@link LightLevel.Relative} will be below)
     */
    LightLevel(int level) {
        this(level, Relative.below);
    }

    @Override
    public String toString() {
        String base = I18n.format("jer.lightLevel");
        if (lightLevel < 0) return base + ": " + I18n.format("jer.any");
        return base + ": " + relative.toString() + " " + lightLevel;
    }

    /**
     * The {@link LightLevel.Relative} enum holding an above and below entry
     */
    public enum Relative {
        above("Above"),
        below("Below");
        String text;

        Relative(String string) {
            this.text = string;
        }

        @Override
        public String toString() {
            return text;
        }
    }

}
