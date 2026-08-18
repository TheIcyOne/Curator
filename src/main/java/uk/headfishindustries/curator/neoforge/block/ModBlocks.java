package uk.headfishindustries.curator.neoforge.block;

import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import uk.headfishindustries.curator.neoforge.TheCurator;

public class ModBlocks {

    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(TheCurator.MODID);

    public static final DeferredBlock<DisplayPedestalBlock> DISPLAY_PEDESTAL =
            BLOCKS.registerBlock("display_pedestal",
                    DisplayPedestalBlock::new
            );
}
