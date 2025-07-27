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
    private Vec3 destination = null;

    public void setDestination(Vec3 destination) {
        this.destination = destination;
    }

    public void clearDestination() {
        this.destination = null;
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

        boolean autopilotValue = GhastAutopilotConfig.ENABLE_AUTOPILOT.getValue();
        while (KeyMappingRegistrar.START_AUTOPILOT.consumeClick() && autopilotValue) {
            if (isRidingHappyGhast) {
                if (minecraft.player.getY() <= GhastAutopilotConfig.MINIMUM_HEIGHT.getValue()) {
                    log("You are too low to use the autopilot!");
                    continue;
                }

                enableAutopilot = !enableAutopilot;

                // Fix: set W key state when toggling autopilot
                minecraft.options.keyUp.setDown(enableAutopilot);

                log("Autopilot " + (enableAutopilot ? "enabled" : "disabled"));
            } else {
                log("You are not riding a happy ghast!");
            }
        }

        // If autopilot is on, but we stopped riding the ghast, disable it
        if (enableAutopilot && !isRidingHappyGhast) {
            enableAutopilot = false;
            minecraft.options.keyUp.setDown(false);
            log("You are not riding a happy ghast!");
        }

        // Only set keyUp if autopilot is still valid
        if (enableAutopilot) {
            minecraft.options.keyUp.setDown(true);

            destination = new Vec3(100, player.getY(), 100);
            lookAt(destination);

            float currentPitch = player.getXRot();
            float targetPitch = 0f;
            float lerpSpeed = 0.1f;

            player.setXRot(lerp(currentPitch, targetPitch, lerpSpeed));
        }
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
