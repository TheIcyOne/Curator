package uk.headfishindustries.curator.neoforge.blockentity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import uk.headfishindustries.curator.neoforge.TheCurator;
import uk.headfishindustries.curator.neoforge.block.ModBlocks;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, TheCurator.MODID);

    public static final Supplier<BlockEntityType<DisplayPedestalBlockEntity>> DISPLAY_PEDESTAL = BLOCK_ENTITIES.register(
            "display_pedestal",
            () -> new BlockEntityType<>(
                    DisplayPedestalBlockEntity::new,
                    false,
                    ModBlocks.DISPLAY_PEDESTAL.get()
            )
    );

}
