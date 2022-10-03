package net.hempflingclub.immortality;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Immortality implements ModInitializer {
    public static final String MOD_ID = "immortality";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        net.hempflingclub.immortality.item.UsableItems.registerModItems();
    }
}
