package uk.headfishindustries.curator.neoforge.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import uk.headfishindustries.curator.neoforge.blockentity.DisplayPedestalBlockEntity;

public class DisplayPedestalRenderer implements BlockEntityRenderer<DisplayPedestalBlockEntity, DisplayPedestalRenderState> {
    private final ItemModelResolver itemModelResolver;

    public DisplayPedestalRenderer(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public DisplayPedestalRenderState createRenderState() {
        return new DisplayPedestalRenderState();
    }

    @Override
    public void extractRenderState(DisplayPedestalBlockEntity blockEntity, DisplayPedestalRenderState state,
                                   float partialTicks, Vec3 cameraPosition,
                                   ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);

        ItemStack stack = blockEntity.getDisplayedItem();
        if (!stack.isEmpty()) {
            this.itemModelResolver.updateForTopItem(
                    state.item,
                    stack,
                    ItemDisplayContext.FIXED,
                    blockEntity.getLevel(),
                    null,
                    0
            );
        } else {
            state.item.clear();
        }
    }

    @Override
    public void submit(DisplayPedestalRenderState state, PoseStack poseStack,
                       SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        if (state.item.isEmpty()) {
            return;
        }

        poseStack.pushPose();

        // Position centered on top of the pedestal
        poseStack.translate(0.5D, 0.55D, 0.5D);
        poseStack.scale(0.5F, 0.5F, 0.5F);

        state.item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);

        poseStack.popPose();
    }
}