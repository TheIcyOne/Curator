package uk.headfishindustries.curator.neoforge.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.event.level.NoteBlockEvent;
import uk.headfishindustries.curator.common.SentimentData;
import uk.headfishindustries.curator.common.SentimentTypes;
import uk.headfishindustries.curator.common.SentimentWeightRegistry;
import uk.headfishindustries.curator.neoforge.TheCurator;
import uk.headfishindustries.curator.neoforge.util.PlayerBuffHandler;

import java.util.HashMap;
import java.util.Map;

public class MuseumRenownData {

    public static final MapCodec<MuseumRenownData> CODEC = Codec.unboundedMap(
            Identifier.CODEC, Codec.DOUBLE).fieldOf("scores").xmap(MuseumRenownData::new, MuseumRenownData::getScores);

    private final Map<Identifier, Double> scores;

    public MuseumRenownData(){
        this(new HashMap<>());
    }

    public MuseumRenownData(Map<Identifier, Double> scores){
        this.scores = new HashMap<>(scores);
    }

    public Map<Identifier, Double> getScores() {
        return scores;
    }

    public double getSentiment(Identifier type) {
        return scores.getOrDefault(type, 0.0);
    }

    private void setSentiment(Identifier type, double amount) {
        if (amount <= 0) {
            scores.remove(type);
        } else {
            scores.put(type, amount);
        }
    }

    public void modifySentiment(Identifier type, double amount) {
        setSentiment(type, getSentiment(type) + amount);
    }


    public static void modifyRenown(Player player, SentimentData itemSentiment){
        modifyRenown(player, itemSentiment, true);
    }

    public static void modifyRenown(Player player, SentimentData itemSentiment, boolean add){
        MuseumRenownData renownData = player.getData(ModAttachments.RENOWN_DATA);

        if (!(itemSentiment == null)){
            for (Identifier type : SentimentWeightRegistry.getTypes()) {
                renownData.modifySentiment(type, add ? itemSentiment.getSentiment(type) : -itemSentiment.getSentiment(type));
            }
            player.setData(ModAttachments.RENOWN_DATA, renownData);

            if (!player.level().isClientSide()) {
                PlayerBuffHandler.updatePlayerAttributes((ServerPlayer) player);
                TheCurator.LOGGER.debug("Attack renown set to: %s".formatted(renownData.getSentiment(SentimentTypes.ATTACK)));
            }
        }
    }

}
