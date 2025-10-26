package io.github.smootheez.ghastautopilot.handler;

import io.github.smootheez.ghastautopilot.config.*;
import io.github.smootheez.ghastautopilot.registry.*;
import io.github.smootheez.ghastautopilot.util.*;
import io.github.smootheez.smoothiezapi.config.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
import net.minecraft.client.*;
import net.minecraft.client.player.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.animal.*;

@Environment(EnvType.CLIENT)
public class HandleGhastAutopilot implements ClientTickEvents.EndTick {
    private boolean autoPilot = false;
    private boolean isForwardKeyForced = false;
    private static final GhastAutopilotConfig CONFIG = ConfigManager.getConfig(GhastAutopilotConfig.class);

    @Override
    public void onEndTick(Minecraft minecraft) {
        if (Boolean.FALSE.equals(CONFIG.getEnableGhastAutopilot().getValue())) return;

        LocalPlayer player = minecraft.player;
        if (player == null) return;

        boolean isRidingHappyGhast = player.getVehicle() instanceof HappyGhast;

        // --- Handle toggling ---
        while (KeyMappingRegistry.TOGGLE_AUTOPILOT.consumeClick()) {
            if (isRidingHappyGhast) {
                autoPilot = !autoPilot;
                sendToggleMessage(player);
            } else sendFailMessage(player);
        }

        // --- Stop autopilot if dismounted ---
        if (!isRidingHappyGhast) autoPilot = false;

        // --- Determine whether we *should* press forward key ---
        boolean shouldForceKey = isRidingHappyGhast && autoPilot;

        // --- Only update key state when it actually changes ---
        if (shouldForceKey != isForwardKeyForced) {
            minecraft.options.keyUp.setDown(shouldForceKey);
            isForwardKeyForced = shouldForceKey;

            DebugMode.sendLoggerInfo((shouldForceKey ? "Forcing" : "Releasing") + " forward key");
        }

        DebugMode.sendLoggerInfo("Autopilot: " + (autoPilot ? "Enabled" : "Disabled"));
    }

    private static void sendFailMessage(LocalPlayer player) {
        displayClientMessage(player, "fail." + Constants.MOD_ID + ".toggle_autopilot");
    }

    private void sendToggleMessage(LocalPlayer player) {
        String key = autoPilot ? "enabled" : "disabled";
        displayClientMessage(player, key + "." + Constants.MOD_ID + ".toggle_autopilot");
    }

    private static void displayClientMessage(LocalPlayer player, String message) {
        if (Boolean.FALSE.equals(CONFIG.getDisplayToggleNotification().getValue())) return;
        player.displayClientMessage(Component.translatable(message), true);
    }
}
