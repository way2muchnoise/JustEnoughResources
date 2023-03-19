package jeresources.compatibility.minecraft;

public record ExperienceRange(int min, int max) {
    public static final ExperienceRange ZERO = new ExperienceRange(0, 0);

    public String getExpString() {
        if (this.max == this.min)
            return Integer.toString(this.min);
        return this.min + " - " + this.max;
    }
}