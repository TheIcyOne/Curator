package uk.headfishindustries.curator.neoforge;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import uk.headfishindustries.curator.common.SentimentData;

public class ItemStackAdapter {

        public static SentimentData getOrInitialize(ItemStack stack) {
            if (stack.isEmpty()) return SentimentData.EMPTY;

            SentimentData existing = stack.get(ModDataComponents.SENTIMENT.get());
            if (existing != null) {
                return existing;
            }

            // Initialize component silently in the background
            SentimentData newData = SentimentData.EMPTY;
            stack.set(ModDataComponents.SENTIMENT.get(), newData);

            return newData;
        }

        /**
         * Called when placed on a pedestal. Reveals the sentiment data to the player.
         */
        public static SentimentData revealSentimentOnDisplay(ItemStack stack) {
            if (stack.isEmpty()) return SentimentData.EMPTY;

            SentimentData data = getOrInitialize(stack);
            if (!data.isRevealed()) {
                data = data.withRevealed(true);
                stack.set(ModDataComponents.SENTIMENT.get(), data);
            }
            return data;
        }

        public static int getOrCalculateVs(ItemStack stack) {
            if (stack.isEmpty()) return 0;
            SentimentData data = getOrInitialize(stack);
            return data.calculateVs();
        }
}
