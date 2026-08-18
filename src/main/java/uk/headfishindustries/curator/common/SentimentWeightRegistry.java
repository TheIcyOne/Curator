package uk.headfishindustries.curator.common;

import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SentimentWeightRegistry {

    private static final Map<Identifier, Float> WEIGHTS = new HashMap<>();

    static {
        WEIGHTS.put(SentimentTypes.MOVEMENT, 1.0f);
        WEIGHTS.put(SentimentTypes.DEFENCE, 1.0f);
        WEIGHTS.put(SentimentTypes.ATTACK, 1.0f);
        WEIGHTS.put(SentimentTypes.MINING, 1.0f);
    }

    public static Set<Identifier> getTypes(){
        return WEIGHTS.keySet();
    }

    public static float getWeight(Identifier type) {
        return WEIGHTS.getOrDefault(type, 1.0f);
    }

    public static void setWeight(Identifier type, float weight) {
        WEIGHTS.put(type, weight);
    }
}
