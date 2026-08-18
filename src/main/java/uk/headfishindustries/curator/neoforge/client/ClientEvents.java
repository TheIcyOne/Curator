package uk.headfishindustries.curator.neoforge.client;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.AddAttributeTooltipsEvent;
import uk.headfishindustries.curator.common.SentimentData;
import uk.headfishindustries.curator.neoforge.ModDataComponents;
import uk.headfishindustries.curator.neoforge.TheCurator;
import uk.headfishindustries.curator.neoforge.blockentity.ModBlockEntities;
import uk.headfishindustries.curator.neoforge.client.render.blockentity.DisplayPedestalRenderer;

import java.util.logging.Logger;

@EventBusSubscriber(modid = TheCurator.MODID, value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        TheCurator.LOGGER.info("Registering entity renderers.");
        event.registerBlockEntityRenderer(
                ModBlockEntities.DISPLAY_PEDESTAL.get(),
                DisplayPedestalRenderer::new
        );
    }

    @SubscribeEvent
    public static void handleItemTooltip(AddAttributeTooltipsEvent event){
        ItemStack stack = event.getStack();
        if (!stack.isEmpty()) {

            SentimentData sentimentData = stack.get(ModDataComponents.SENTIMENT.get());
            if (sentimentData == null || !sentimentData.isRevealed()) return;

            Component sentimentLine = Component.translatable("curator.tooltip.sentimentvalue", sentimentData.calculateVs()).withColor(TextColor.LIGHT_PURPLE);

            event.addTooltipLines(sentimentLine);
        }

    }
}