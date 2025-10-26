package io.github.smootheez.ghastautopilot;

import io.github.smootheez.ghastautopilot.command.*;
import io.github.smootheez.ghastautopilot.handle.*;
import io.github.smootheez.ghastautopilot.registry.*;
import io.github.smootheez.ghastautopilot.util.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.command.v2.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;

@Environment(EnvType.CLIENT)
public class GhastAutopilotClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Constants.LOGGER.info("Initializing Client " + Constants.MOD_NAME + "(" + Constants.MOD_ID + ")...");
        KeyMappingRegistry.registerKeyMappings();

        ClientCommandRegistrationCallback.EVENT.register(new GhastAutopilotCommand());
        ClientTickEvents.END_CLIENT_TICK.register(new HandleGhastAutopilot());
    }
}
