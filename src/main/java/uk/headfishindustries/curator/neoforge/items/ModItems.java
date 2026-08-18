package uk.headfishindustries.curator.neoforge.items;

import net.minecraft.world.item.BlockItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import uk.headfishindustries.curator.neoforge.TheCurator;
import uk.headfishindustries.curator.neoforge.block.DisplayPedestalBlock;
import uk.headfishindustries.curator.neoforge.block.ModBlocks;

public class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(TheCurator.MODID);

    public static final DeferredItem<BlockItem> DISPLAY_PEDESTAL_ITEM =
            ITEMS.registerSimpleBlockItem(ModBlocks.DISPLAY_PEDESTAL);
}
