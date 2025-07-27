package dev.smootheez.ghastautopilot.handler;

import dev.smootheez.ghastautopilot.config.*;
import dev.smootheez.ghastautopilot.registry.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import net.minecraft.client.player.*;
import net.minecraft.network.chat.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.phys.*;

@Environment(EnvType.CLIENT)
public class MinecraftHandler {
    private final Minecraft minecraft;
    private boolean enableAutopilot = false;
    private static Vec3 destination = null;

    public static void setDestination(Vec3 newDestination) {
        destination = newDestination;
    }

    public static void clearDestination() {
        destination = null;
    }

    public MinecraftHandler(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public void handleKeybinds() {
        // Handle keybinds
    }

    public void clientTick() {
        var player = minecraft.player;
        if (player == null) return;

        boolean isRidingHappyGhast = player.getVehicle() instanceof HappyGhast;

        handleAutopilotToggle(player, isRidingHappyGhast);
        handleAutopilotInterrupt(isRidingHappyGhast);
        handleAutopilotSteering(player);
    }

    private void handleAutopilotToggle(LocalPlayer player, boolean isRidingHappyGhast) {
        boolean autopilotEnabledInConfig = GhastAutopilotConfig.ENABLE_AUTOPILOT.getValue();

        while (KeyMappingRegistrar.START_AUTOPILOT.consumeClick() && autopilotEnabledInConfig) {
            if (isRidingHappyGhast) {
                if (player.getY() <= GhastAutopilotConfig.MINIMUM_HEIGHT.getValue()) {
                    log("You are too low to use the autopilot!");
                    continue;
                }

            } else {
                log("You are not riding a happy ghast!");
            }

            toggleAutopilot();
        }
    }

    private void toggleAutopilot() {
        enableAutopilot = !enableAutopilot;
        minecraft.options.keyUp.setDown(enableAutopilot);
        log("Autopilot " + (enableAutopilot ? "enabled" : "disabled"));
    }

    private void handleAutopilotInterrupt(boolean isRidingHappyGhast) {
        if (enableAutopilot && !isRidingHappyGhast) {
            enableAutopilot = false;
            clearDestination();
            minecraft.options.keyUp.setDown(false);
            log("You are not riding a happy ghast!");
        }
    }

    private void handleAutopilotSteering(LocalPlayer player) {
        if (!enableAutopilot) return;

        minecraft.options.keyUp.setDown(true);

        if (destination != null) {
            lookAt(destination);
        }

        float currentPitch = player.getXRot();
        float targetPitch = 0f;
        float lerpSpeed = 0.1f;
        player.setXRot(lerp(currentPitch, targetPitch, lerpSpeed));
    }


    private void lookAt(Vec3 destination) {
        LocalPlayer player = minecraft.player;
        if (player == null) return;
        Vec3 eyePos = player.getEyePosition(1.0F);

        double dx = destination.x - eyePos.x;
        double dy = destination.y - eyePos.y;
        double dz = destination.z - eyePos.z;

        double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) (Mth.atan2(dz, dx) * (180F / Math.PI)) - 90F;
        float pitch = (float) -(Mth.atan2(dy, horizontalDistance) * (180F / Math.PI));

        player.setYRot(yaw);
        player.setXRot(pitch);

        player.yRotO = yaw;
        player.xRotO = pitch;

        log("Looking at " + destination);
    }


    private float lerp(float from, float to, float maxSpeed) {
        float diff = to - from;
        float speed = Math.min(Math.abs(diff) * 0.2f, maxSpeed); // scale speed based on distance
        return from + diff * speed;
    }


    private void log(String message) {
        if (minecraft.player != null)
            minecraft.player.displayClientMessage(Component.literal("[Autopilot] " + message), false);
    }
}
