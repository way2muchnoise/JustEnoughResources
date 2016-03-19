package jeresources.api.conditionals;

import jeresources.api.render.TextModifier;
import net.minecraft.util.text.translation.I18n;

import java.util.LinkedHashMap;
import java.util.Map;

public class Conditional
{
    public static final Map<Conditional, Conditional> reverse = new LinkedHashMap<Conditional, Conditional>();

    public static final Conditional magmaCream = new Conditional("jer.magmaCream.text", TextModifier.darkRed);
    public static final Conditional slimeBall = new Conditional("jer.slimeBall.text", TextModifier.lightGreen);
    public static final Conditional rareDrop = new Conditional("jer.rareDrop.text", TextModifier.purple);
    public static final Conditional silkTouch = new Conditional("jer.worldgen.silkTouch", TextModifier.darkCyan);
    public static final Conditional equipmentDrop = new Conditional("jer.equipmentDrop.text", TextModifier.lightCyan);

    public static final Conditional burning = new Conditional("jer.burning.text", TextModifier.lightRed);
    public static final Conditional notBurning = new Conditional("jer.notBurning.text", burning);
    public static final Conditional wet = new Conditional("jer.wet.text", TextModifier.lightCyan);
    public static final Conditional notWet = new Conditional("jer.notWet.text", wet);
    public static final Conditional hasPotion = new Conditional("jer.hasPotion.text", TextModifier.pink);
    public static final Conditional hasNoPotion = new Conditional("jer.hasNoPotion.text", hasPotion);
    public static final Conditional beyond = new Conditional("jer.beyond.text", TextModifier.darkGreen);
    public static final Conditional nearer = new Conditional("jer.nearer.text", beyond);
    public static final Conditional raining = new Conditional("jer.raining.text", TextModifier.lightGrey);
    public static final Conditional dry = new Conditional("jer.dry.text", raining);
    public static final Conditional thundering = new Conditional("jer.thundering.text", TextModifier.darkGrey);
    public static final Conditional notThundering = new Conditional("jer.notThundering.text", thundering);
    public static final Conditional moonPhase = new Conditional("jer.moonPhase.text");
    public static final Conditional notMoonPhase = new Conditional("jer.notMoonPhase.text", moonPhase);
    public static final Conditional pastTime = new Conditional("jer.pastTime.text", TextModifier.lilac);
    public static final Conditional beforeTime = new Conditional("jer.beforeTime.text", pastTime);
    public static final Conditional pastWorldTime = new Conditional("jer.pastWorldTime.text", TextModifier.purple);
    public static final Conditional beforeWorldTime = new Conditional("jer.beforeWorldTime.text", pastWorldTime);
    public static final Conditional pastWorldDifficulty = new Conditional("jer.pastWorldDifficulty.text", TextModifier.orange);
    public static final Conditional beforeWorldDifficulty = new Conditional("jer.beforeWorldDifficulty.text", pastWorldDifficulty);
    public static final Conditional gameDifficulty = new Conditional("jer.gameDifficulty.text", TextModifier.orange);
    public static final Conditional notGameDifficulty = new Conditional("jer.notGameDifficulty.text", gameDifficulty);
    public static final Conditional inDimension = new Conditional("jer.inDimension.text", TextModifier.yellow);
    public static final Conditional notInDimension = new Conditional("jer.notInDimension.text", inDimension);
    public static final Conditional inBiome = new Conditional("jer.inBiome.text", TextModifier.orange);
    public static final Conditional notInBiome = new Conditional("jer.notInBiome.text", inBiome);
    public static final Conditional onBlock = new Conditional("jer.onBlock.text", TextModifier.lightRed);
    public static final Conditional notOnBlock = new Conditional("jer.notOnBlock.text", onBlock);
    public static final Conditional below = new Conditional("jer.below.text", TextModifier.darkGreen);
    public static final Conditional above = new Conditional("jer.above.text", below);
    public static final Conditional playerOnline = new Conditional("jer.playerOnline.text", TextModifier.bold);
    public static final Conditional playerOffline = new Conditional("jer.playerOffline.text", playerOnline);
    public static final Conditional playerKill = new Conditional("jer.playerKill.text");
    public static final Conditional notPlayerKill = new Conditional("jer.notPlayerKill.text", playerKill);
    public static final Conditional aboveLooting = new Conditional("jer.aboveLooting.text", TextModifier.darkBlue);
    public static final Conditional belowLooting = new Conditional("jer.belowLooting.text", aboveLooting);
    public static final Conditional killedBy = new Conditional("jer.killedBy.text", TextModifier.darkRed);
    public static final Conditional notKilledBy = new Conditional("jer.notKilledBy.text", killedBy);

    protected String text;
    protected String colour = "";


    public Conditional()
    {
    }

    public Conditional(String text, TextModifier... textModifiers)
    {
        this.text = text;
        for (TextModifier textModifier : textModifiers)
            colour += textModifier.toString();
    }

    public Conditional(String text, Conditional opposite)
    {
        this(text);
        this.colour = opposite.colour;
        reverse.put(opposite, this);
        reverse.put(this, opposite);
    }

    @Override
    public String toString()
    {
        return colour + I18n.translateToLocal(text);
    }
}
