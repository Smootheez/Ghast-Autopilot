package io.github.smootheez.ghastautopilot;

import io.github.smootheez.ghastautopilot.util.*;
import net.fabricmc.api.*;

@Environment(EnvType.CLIENT)
public class GhastAutopilotClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Constants.LOGGER.info("Initializing Client " + Constants.MOD_NAME + "(" + Constants.MOD_ID + ")...");
    }
}
