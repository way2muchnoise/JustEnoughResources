package jeresources.api.render;

public enum TextModifier
{
    black("\u00A70"),
    darkBlue("\u00A71"),
    darkGreen("\u00A72"),
    darkCyan("\u00A73"),
    darkRed("\u00A74"),
    purple("\u00A75"),
    orange("\u00A76"),
    lightGrey("\u00A77"),
    darkGrey("\u00A78"),
    lilac("\u00A79"),
    lightGreen("\u00A7a"),
    lightCyan("\u00A7b"),
    lightRed("\u00A7c"),
    pink("\u00A7d"),
    yellow("\u00A7e"),
    white("\u00A7f"),
    obfuscated("\u00A7k"),
    bold("\u00A7l"),
    strikethrough("\u00A7m"),
    underline("\u00A7n"),
    italic("\u00A7o"),
    reset("\u00A7r");

    private String prefix;

    TextModifier(String prefix)
    {
        this.prefix = prefix;
    }

    @Override
    public String toString()
    {
        return prefix;
    }
}
