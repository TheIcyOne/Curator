package uk.headfishindustries.curator.neoforge.attachment;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import uk.headfishindustries.curator.neoforge.TheCurator;

import java.util.function.Supplier;

public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, TheCurator.MODID);

    public static final Supplier<AttachmentType<MuseumRenownData>> RENOWN_DATA = ATTACHMENTS.register(
            "renown_data",
            () -> AttachmentType.builder((Supplier<MuseumRenownData>) MuseumRenownData::new)
                    .serialize(MuseumRenownData.CODEC)
                    .copyOnDeath()
                    .build()
    );
}
