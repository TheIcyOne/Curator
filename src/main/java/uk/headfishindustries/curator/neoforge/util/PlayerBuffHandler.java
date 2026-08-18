package uk.headfishindustries.curator.neoforge.util;

import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import uk.headfishindustries.curator.common.SentimentTypes;
import uk.headfishindustries.curator.neoforge.TheCurator;
import uk.headfishindustries.curator.neoforge.attachment.ModAttachments;
import uk.headfishindustries.curator.neoforge.attachment.MuseumRenownData;

public class PlayerBuffHandler {

    private static final Identifier ATTACK_MODIFIER_ID =
            Identifier.fromNamespaceAndPath(TheCurator.MODID, "renown_attack_damage");

    public static void updatePlayerAttributes(ServerPlayer player) {
        MuseumRenownData data = player.getData(ModAttachments.RENOWN_DATA);

        updateAttack(player, data.getSentiment(SentimentTypes.ATTACK));

    }

    private static void updateAttack(ServerPlayer player, double attackRenown){
        AttributeInstance attribute = player.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attribute == null) return;

        // Remove old modifier if present
        attribute.removeModifier(ATTACK_MODIFIER_ID);

        // Apply new transient modifier if renown > 0
        if (attackRenown > 1) {
            double bonusDamage = 0.25 * Math.log10(attackRenown);
            AttributeModifier modifier = new AttributeModifier(
                    ATTACK_MODIFIER_ID,
                    bonusDamage,
                    AttributeModifier.Operation.ADD_VALUE
            );
            attribute.addTransientModifier(modifier);
        }
    }
}

