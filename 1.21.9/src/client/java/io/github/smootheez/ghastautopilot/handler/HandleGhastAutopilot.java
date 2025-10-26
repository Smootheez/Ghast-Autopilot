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
import net.minecraft.world.phys.*;

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
        if (!isRidingHappyGhast) {
            GhastAutopilotUtil.clearVec3();
            autoPilot = false;
        }

        // --- Determine whether we *should* press forward key ---
        boolean shouldForceKey = isRidingHappyGhast && autoPilot;

        // --- Only update key state when it actually changes ---
        if (shouldForceKey != isForwardKeyForced) {
            minecraft.options.keyUp.setDown(shouldForceKey);
            isForwardKeyForced = shouldForceKey;

            DebugMode.sendLoggerInfo((shouldForceKey ? "Forcing" : "Releasing") + " forward key");
        }

        handleLookAt(player); //TODO: Fix the set down key suddenly false after set destination command while the autopilot is true

        DebugMode.sendLoggerInfo("Autopilot: " + (autoPilot ? "Enabled" : "Disabled"));
    }

    private void handleLookAt(LocalPlayer player) {
        Vec3 destination = GhastAutopilotUtil.getVec3();
        if (Boolean.FALSE.equals(CONFIG.getEnableLookAt().getValue())
                || destination == null
                || !autoPilot) return;

        Vec3 eyePos = player.getEyePosition(1.0F);

        double dx = destination.x - eyePos.x;
        double dz = destination.z - eyePos.z;

        // --- Compute Target Rotations ---
        float targetYaw = (float) (Math.toDegrees(Math.atan2(dz, dx)) - 90.0F);
        float targetPitch = 0.0F; // we want level flight (no up/down look)

        // --- Current Rotations ---
        float currentYaw = player.getYRot();
        float currentPitch = player.getXRot();

        // --- Smoothly interpolate both angles ---
        final float SMOOTH_FACTOR = CONFIG.getSmoothFactor().getValue().floatValue(); // smaller = smoother/slower
        float newYaw = lerpAngle(currentYaw, targetYaw, SMOOTH_FACTOR);
        float newPitch = lerp(currentPitch, targetPitch, SMOOTH_FACTOR);

        // --- Apply Rotations ---
        player.setYRot(newYaw);
        player.setXRot(newPitch);
    }

    private static float lerp(float from, float to, float amount) {
        return from + (to - from) * amount;
    }

    private static float lerpAngle(float from, float to, float amount) {
        float delta = wrapDegrees(to - from);
        return from + delta * amount;
    }

    private static float wrapDegrees(float angle) {
        angle %= 360.0F;
        if (angle >= 180.0F) angle -= 360.0F;
        if (angle < -180.0F) angle += 360.0F;
        return angle;
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
