package uk.headfishindustries.curator.common;

import java.util.Comparator;
import java.util.List;

public class MuseumRenownCalculator {

    private static final double DIVERSITY_COEFFICIENT = 0.05; // 5% bonus per unique item type

    /**
     * Pure Java record representing an item displayed in a vault case.
     */
    public record DisplayedEntry(int sentimentValue, String itemTypeId) {}

    /**
     * Calculates total vault renown using a diminishing sum and diversity multiplier.
     */
    public static double calculateVaultRenown(List<DisplayedEntry> entries) {
        if (entries == null || entries.isEmpty()) return 0.0;

        // 1. Sort sentiments in descending order
        List<Integer> sortedSentiments = entries.stream()
                .map(DisplayedEntry::sentimentValue)
                .sorted(Comparator.reverseOrder())
                .toList();

        // 2. Diminishing sum: 1/1 * #1 + 1/2 * #2 + 1/3 * #3...
        double baseRenown = 0.0;
        for (int i = 0; i < sortedSentiments.size(); i++) {
            baseRenown += (double) sortedSentiments.get(i) / (i + 1);
        }

        // 3. Unique item type count for diversity bonus
        long uniqueCount = entries.stream()
                .map(DisplayedEntry::itemTypeId)
                .distinct()
                .count();

        double diversityMultiplier = 1.0 + (uniqueCount * DIVERSITY_COEFFICIENT);

        return baseRenown * diversityMultiplier;
    }
}