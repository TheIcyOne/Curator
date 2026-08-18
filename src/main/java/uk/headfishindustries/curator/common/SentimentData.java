package uk.headfishindustries.curator.common;

import net.minecraft.resources.Identifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Pure Java record to store and calculate Sentiment data.
 * Completely independent of NeoForge/Minecraft classes.
 */
public record SentimentData(
        Map<Identifier, Float> values,
        boolean isAncientArtifact,
        int legacyLevel,
        boolean isRevealed
) {

    public static final SentimentData EMPTY = new SentimentData(Collections.emptyMap(), false, 0, false);

    public float getSentiment(Identifier type) {
        return this.values.getOrDefault(type, 0.0f);
    }
    /**
     * Calculates the raw Sentiment Value (Vs).
     *
     * @return Calculated Sentiment Value (Vs)
     */
    public int calculateVs() {
        float weightedTotal = 0.0f;

        for (Map.Entry<Identifier, Float> entry : this.values.entrySet()) {
            float weight = SentimentWeightRegistry.getWeight(entry.getKey());
            weightedTotal += entry.getValue() * weight;
        }

        int artifactBonus = this.isAncientArtifact ? 2000 : 0;
        return Math.round(weightedTotal) + artifactBonus;
    }

    public SentimentData addSentiment(Identifier type, float amount) {
        Map<Identifier, Float> updatedMap = new HashMap<>(this.values);
        updatedMap.put(type, updatedMap.getOrDefault(type, 0.0f) + amount);

        return new SentimentData(Map.copyOf(updatedMap), this.isAncientArtifact, this.legacyLevel, this.isRevealed);
    }

    public SentimentData withRevealed(boolean revealed){
        return new SentimentData(this.values, this.isAncientArtifact, this.legacyLevel, revealed);
    }
}