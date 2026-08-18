package uk.headfishindustries.curator.neoforge.event;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import uk.headfishindustries.curator.common.SentimentData;
import uk.headfishindustries.curator.common.SentimentTypes;
import uk.headfishindustries.curator.neoforge.ModDataComponents;
import uk.headfishindustries.curator.neoforge.TheCurator;


@EventBusSubscriber(modid = TheCurator.MODID)
    public class SentimentEventHandler {
    @SubscribeEvent
    public static void onAttackEvent(AttackEntityEvent event) {
        Player player = event.getEntity();

        if (player.level().isClientSide() || event.isCanceled()) {
            return;
        }

        ItemStack stack = player.getWeaponItem();
        if (!stack.isEmpty()) {
            stack.update(
                    ModDataComponents.SENTIMENT.get(),
                    SentimentData.EMPTY,
                    data -> data.addSentiment(SentimentTypes.ATTACK, 1) // Returns updated copy
            );

        }
    }
}
