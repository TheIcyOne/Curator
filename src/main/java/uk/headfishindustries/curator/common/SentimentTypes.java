package uk.headfishindustries.curator.common;

import net.minecraft.resources.Identifier;
import uk.headfishindustries.curator.neoforge.TheCurator;

public class SentimentTypes {
    public static final Identifier MOVEMENT = TCIdentifier("movement");
    public static final Identifier DEFENCE = TCIdentifier("defence");
    public static final Identifier ATTACK = TCIdentifier("attack");
    public static final Identifier MINING = TCIdentifier("mining");


    static Identifier TCIdentifier(String path){
        return Identifier.fromNamespaceAndPath(TheCurator.MODID, path);
    }
}
