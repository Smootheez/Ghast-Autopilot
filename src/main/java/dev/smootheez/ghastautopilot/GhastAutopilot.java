package dev.smootheez.ghastautopilot;

import net.fabricmc.api.*;

@Environment(EnvType.CLIENT)
public class GhastAutopilot implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Constants.LOGGER.info("Ghast Autopilot initialized");
    }
}
