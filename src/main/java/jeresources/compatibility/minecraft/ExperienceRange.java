package jeresources.compatibility.minecraft;

public class ExperienceRange {
    public static final ExperienceRange ZERO = new ExperienceRange(0, 0);

    public final int min;
    public final int max;

    public ExperienceRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public String getExpString() {
        if (this.max == this.min)
            return Integer.toString(this.min);
        return this.min + " - " + this.max;
    }
}
