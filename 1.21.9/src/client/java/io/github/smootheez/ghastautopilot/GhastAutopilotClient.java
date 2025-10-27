package io.github.smootheez.ghastautopilot;

import io.github.smootheez.ghastautopilot.command.*;
import io.github.smootheez.ghastautopilot.handler.*;
import io.github.smootheez.ghastautopilot.registry.*;
import io.github.smootheez.ghastautopilot.util.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.command.v2.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
import net.fabricmc.fabric.api.client.rendering.v1.hud.*;
import net.minecraft.resources.*;

@Environment(EnvType.CLIENT)
public class GhastAutopilotClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Constants.LOGGER.info("Initializing Client " + Constants.MOD_NAME + "(" + Constants.MOD_ID + ")...");
        KeyMappingRegistry.registerKeyMappings();

        ClientCommandRegistrationCallback.EVENT.register(new GhastAutopilotCommand());
        ClientTickEvents.END_CLIENT_TICK.register(new HandleEndTick());
        HudElementRegistry.addFirst(
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "overlay"),
                new HandleHudOverlay()
        );
    }
}
