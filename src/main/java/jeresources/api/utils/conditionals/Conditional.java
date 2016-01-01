package jeresources.api.utils.conditionals;

import jeresources.api.utils.Modifier;
import net.minecraft.util.StatCollector;

import java.util.LinkedHashMap;
import java.util.Map;

public class Conditional
{
    public static final Map<Conditional, Conditional> reverse = new LinkedHashMap<Conditional, Conditional>();

    public static final Conditional magmaCream = new Conditional("ner.magmaCream.text", Modifier.darkRed);
    public static final Conditional slimeBall = new Conditional("ner.slimeBall.text", Modifier.lightGreen);
    public static final Conditional rareDrop = new Conditional("ner.rareDrop.text", Modifier.purple);
    public static final Conditional silkTouch = new Conditional("ner.ore.silkTouch", Modifier.darkCyan);
    public static final Conditional equipmentDrop = new Conditional("ner.equipmentDrop.text", Modifier.lightCyan);

    public static final Conditional burning = new Conditional("ner.burning.text", Modifier.lightRed);
    public static final Conditional notBurning = new Conditional("ner.notBurning.text", burning);
    public static final Conditional wet = new Conditional("ner.wet.text", Modifier.lightCyan);
    public static final Conditional notWet = new Conditional("ner.notWet.text", wet);
    public static final Conditional hasPotion = new Conditional("ner.hasPotion.text", Modifier.pink);
    public static final Conditional hasNoPotion = new Conditional("ner.hasNoPotion.text", hasPotion);
    public static final Conditional beyond = new Conditional("ner.beyond.text", Modifier.darkGreen);
    public static final Conditional nearer = new Conditional("ner.nearer.text", beyond);
    public static final Conditional raining = new Conditional("ner.raining.text", Modifier.lightGrey);
    public static final Conditional dry = new Conditional("ner.dry.text", raining);
    public static final Conditional thundering = new Conditional("ner.thundering.text", Modifier.darkGrey);
    public static final Conditional notThundering = new Conditional("ner.notThundering.text", thundering);
    public static final Conditional moonPhase = new Conditional("ner.moonPhase.text");
    public static final Conditional notMoonPhase = new Conditional("ner.notMoonPhase.text", moonPhase);
    public static final Conditional pastTime = new Conditional("ner.pastTime.text", Modifier.lilac);
    public static final Conditional beforeTime = new Conditional("ner.beforeTime.text", pastTime);
    public static final Conditional pastWorldTime = new Conditional("ner.pastWorldTime.text", Modifier.purple);
    public static final Conditional beforeWorldTime = new Conditional("ner.beforeWorldTime.text", pastWorldTime);
    public static final Conditional pastWorldDifficulty = new Conditional("ner.pastWorldDifficulty.text", Modifier.orange);
    public static final Conditional beforeWorldDifficulty = new Conditional("ner.beforeWorldDifficulty.text", pastWorldDifficulty);
    public static final Conditional gameDifficulty = new Conditional("ner.gameDifficulty.text", Modifier.orange);
    public static final Conditional notGameDifficulty = new Conditional("ner.notGameDifficulty.text", gameDifficulty);
    public static final Conditional inDimension = new Conditional("ner.inDimension.text", Modifier.yellow);
    public static final Conditional notInDimension = new Conditional("ner.notInDimension.text", inDimension);
    public static final Conditional inBiome = new Conditional("ner.inBiome.text", Modifier.orange);
    public static final Conditional notInBiome = new Conditional("ner.notInBiome.text", inBiome);
    public static final Conditional onBlock = new Conditional("ner.onBlock.text", Modifier.lightRed);
    public static final Conditional notOnBlock = new Conditional("ner.notOnBlock.text", onBlock);
    public static final Conditional below = new Conditional("ner.below.text", Modifier.darkGreen);
    public static final Conditional above = new Conditional("ner.above.text", below);
    public static final Conditional playerOnline = new Conditional("ner.playerOnline.text", Modifier.bold);
    public static final Conditional playerOffline = new Conditional("ner.playerOffline.text", playerOnline);
    public static final Conditional playerKill = new Conditional("ner.playerKill.text");
    public static final Conditional notPlayerKill = new Conditional("ner.notPlayerKill.text", playerKill);
    public static final Conditional aboveLooting = new Conditional("ner.aboveLooting.text", Modifier.darkBlue);
    public static final Conditional belowLooting = new Conditional("ner.belowLooting.text", aboveLooting);
    public static final Conditional killedBy = new Conditional("ner.killedBy.text", Modifier.darkRed);
    public static final Conditional notKilledBy = new Conditional("ner.notKilledBy.text", killedBy);

    protected String text;
    protected String colour = "";


    public Conditional()
    {
    }

    public Conditional(String text, Modifier... modifiers)
    {
        this.text = text;
        for (Modifier modifier : modifiers)
            colour += modifier.toString();
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
        return colour + StatCollector.translateToLocal(text);
    }
}
