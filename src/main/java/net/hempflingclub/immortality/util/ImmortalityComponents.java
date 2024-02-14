package net.hempflingclub.immortality.util;

import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import dev.onyxstudios.cca.api.v3.item.ItemComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.item.ItemComponentInitializer;
import dev.onyxstudios.cca.api.v3.world.WorldComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.world.WorldComponentInitializer;
import net.hempflingclub.immortality.item.ImmortalityItems;
import net.minecraft.entity.LivingEntity;

public class ImmortalityComponents implements WorldComponentInitializer, EntityComponentInitializer, ItemComponentInitializer {
    @Override
    public void registerWorldComponentFactories(WorldComponentFactoryRegistry registry) {
        registry.register(IImmortalityWorldComponent.KEY, it -> new ImmortalityWorldDataImpl());
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(IImmortalityPlayerComponent.KEY, it -> new IImmortalityPlayerComponentImpl(), RespawnCopyStrategy.ALWAYS_COPY); // Soul is Immortal, not the Body
        registry.registerFor(LivingEntity.class, IImmortalityLivingEntityComponent.KEY, it -> new IImmortalityLivingEntityComponentImpl());
    }

    @Override
    public void registerItemComponentFactories(ItemComponentFactoryRegistry registry) {
        registry.register(ImmortalityItems.SoulStone, IImmortalityItemComponent.KEY, IImmortalityItemComponentImpl::new);
    }
}
