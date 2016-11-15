package jeresources.api.render;

public class ColourHelper {
    public static final int BLACK = -16777216;
    public static final int BLUE = -16776961;
    public static final int CYAN = -16711681;
    public static final int DRKGRAY = -12303292;
    public static final int GRAY = -7829368;
    public static final int GREEN = -16711936;
    public static final int LTGRAY = -3355444;
    public static final int MAGENTA = -65281;
    public static final int RED = -65536;
    public static final int TRANSPARENT = 0;
    public static final int WHITE = -1;
    public static final int YELLOW = -256;

    public static float getRed(int color) {
        return (color & 255) / 255.0F;
    }

    public static float getGreen(int color) {
        return ((color >> 8) & 255) / 255.0F;
    }

    public static float getBlue(int color) {
        return ((color >> 16) & 255) / 255.0F;
    }

    public static float getAlpa(int color) {
        return ((color >> 24) & 255) / 255.0F;
    }
}
