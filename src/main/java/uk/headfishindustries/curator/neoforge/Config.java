package uk.headfishindustries.curator.neoforge;

import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue SENTIMENTALITY_MULTIPLIER = BUILDER
            .comment("Multiplier for the total sentimentality score of all items.")
            .defineInRange("sentimentalityMulti", 100.0, 0.1, 10000.0);

    static final ModConfigSpec SPEC = BUILDER.build();

}
