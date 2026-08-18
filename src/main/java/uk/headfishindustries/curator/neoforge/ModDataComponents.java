package uk.headfishindustries.curator.neoforge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import uk.headfishindustries.curator.common.SentimentData;

import java.util.Map;

public class ModDataComponents {

    public static final DeferredRegister<DataComponentType<?>> COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, "curator");

    private static final Codec<Map<Identifier, Float>> SENTIMENT_MAP_CODEC =
            Codec.unboundedMap(Identifier.CODEC, Codec.FLOAT);

    public static final Codec<SentimentData> SENTIMENT_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    SENTIMENT_MAP_CODEC.fieldOf("values").forGetter(SentimentData::values),
                    Codec.BOOL.fieldOf("is_ancient").forGetter(SentimentData::isAncientArtifact),
                    Codec.INT.fieldOf("legacy_level").forGetter(SentimentData::legacyLevel),
                    Codec.BOOL.fieldOf("is_revealed").forGetter(SentimentData::isRevealed)
            ).apply(instance, SentimentData::new)
    );

    // StreamCodec for syncing SentimentData over the network
    public static final StreamCodec<RegistryFriendlyByteBuf, SentimentData> SENTIMENT_STREAM_CODEC =
            ByteBufCodecs.fromCodecWithRegistries(SENTIMENT_CODEC);

    // Register custom DataComponentType
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SentimentData>> SENTIMENT =
            COMPONENTS.register("sentiment_data", () ->
                    DataComponentType.<SentimentData>builder()
                            .persistent(SENTIMENT_CODEC)
                            .networkSynchronized(SENTIMENT_STREAM_CODEC)
                            .build()
            );
}