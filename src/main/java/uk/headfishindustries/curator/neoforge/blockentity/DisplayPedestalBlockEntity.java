package uk.headfishindustries.curator.neoforge.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;
import uk.headfishindustries.curator.common.SentimentData;
import uk.headfishindustries.curator.common.SentimentWeightRegistry;
import uk.headfishindustries.curator.neoforge.ItemStackAdapter;
import uk.headfishindustries.curator.neoforge.ModDataComponents;
import uk.headfishindustries.curator.neoforge.TheCurator;
import uk.headfishindustries.curator.neoforge.attachment.ModAttachments;
import uk.headfishindustries.curator.neoforge.attachment.MuseumRenownData;

import java.util.UUID;

public class DisplayPedestalBlockEntity extends BlockEntity {

    private final ItemStacksResourceHandler inventory = new ItemStacksResourceHandler(1) {
        @Override
        protected void onContentsChanged(int slot, ItemStack previousContents) {
            setChanged();
            syncToClient();
        }

        @Override
        public int getCapacity(int slot, ItemResource resource) {
            return 1; // Limit pedestal slot capacity to 1 item
        }
    };

    private UUID owner = null;

    public DisplayPedestalBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DISPLAY_PEDESTAL.get(), pos, state);
    }

    public void handleInteraction(Player player, InteractionHand hand) {

        if (this.owner == null){
            this.owner = player.getUUID();
        }
        else if(!this.owner.equals(player.getUUID())){
            //TODO: "Visit" bonuses
            return;
        }else {
            ItemStack heldItem = player.getItemInHand(hand);

            // Get the item currently displayed in the pedestal
            ItemResource storedResource = inventory.getResource(0);
            int storedAmount = inventory.getAmountAsInt(0);
            ItemStack storedItem = storedResource.toStack(storedAmount);

            // 1. Insert item into pedestal
            if (storedItem.isEmpty() && !heldItem.isEmpty()) {
                ItemResource resourceToInsert = ItemResource.of(heldItem);

                // Use a transaction to insert the item
                try (var transaction = Transaction.openRoot()) {
                    int inserted = inventory.insert(0, resourceToInsert, 1, transaction);

                    if (inserted > 0) {
                        // Successfully inserted - consume from player's hand
                        heldItem.shrink(1);

                        // Get the newly inserted item and initialize sentiment data
                        ItemResource displayedResource = inventory.getResource(0);
                        int displayedAmount = inventory.getAmountAsInt(0);
                        ItemStack displayedItem = displayedResource.toStack(displayedAmount);
                        ItemStackAdapter.revealSentimentOnDisplay(displayedItem);

                        // Store the modified item back in the inventory
                        inventory.set(0, ItemResource.of(displayedItem), displayedAmount);

                        SentimentData sentimentData = displayedItem.get(ModDataComponents.SENTIMENT.get());

                        MuseumRenownData.modifyRenown(player, sentimentData);

                        transaction.commit();
                        setChanged();
                        syncToClient();
                    }
                }
            }
            // 2. Extract item from pedestal
            else if (!storedItem.isEmpty() && heldItem.isEmpty()) {
                ItemResource resourceToExtract = inventory.getResource(0);

                // Use a transaction to extract the item
                try (var transaction = Transaction.openRoot()) {
                    int extracted = inventory.extract(0, resourceToExtract, 1, transaction);

                    if (extracted > 0) {
                        // Successfully extracted - give item to player
                        ItemStack extractedItem = resourceToExtract.toStack(extracted);
                        player.setItemInHand(hand, extractedItem);

                        MuseumRenownData.modifyRenown(player, extractedItem.get(ModDataComponents.SENTIMENT.get()), false);

                        transaction.commit();
                        setChanged();
                        syncToClient();
                    }
                }
            }
        }
    }



    public void dropItem() {
        ItemResource displayedResource = inventory.getResource(0);
        int displayedAmount = inventory.getAmountAsInt(0);
        
        if (displayedAmount > 0 && level != null) {
            ItemStack displayedItem = displayedResource.toStack(displayedAmount);
            Containers.dropItemStack(level, worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), displayedItem);
            
            // Extract the item from the pedestal
            try (var transaction = Transaction.openRoot()) {
                inventory.extract(0, displayedResource, displayedAmount, transaction);
                transaction.commit();
            }
        }
    }

    public ItemStack getDisplayedItem() {
        ItemResource resource = inventory.getResource(0);
        int amount = inventory.getAmountAsInt(0);
        return resource.toStack(amount);
    }

    public int getDisplayedSentiment() {
        return ItemStackAdapter.getOrCalculateVs(getDisplayedItem());
    }

    // --- NBT Serialization & Syncing ---
    @Override
    protected void saveAdditional(@NonNull ValueOutput output) {
        super.saveAdditional(output);
        output.putChild("Inventory", inventory);
        if (this.owner != null) {
            output.putString("Owner", this.owner.toString());
        }
    }

    @Override
    public void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.child("Inventory").ifPresent(inventory::deserialize);
        input.getString("Owner").ifPresent(str -> this.owner = UUID.fromString(str));
    }

    private void syncToClient() {
        if (level != null && !level.isClientSide()) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveCustomOnly(registries);
    }



    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

}