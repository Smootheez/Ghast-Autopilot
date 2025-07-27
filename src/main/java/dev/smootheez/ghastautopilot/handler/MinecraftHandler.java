package dev.smootheez.ghastautopilot.handler;

import dev.smootheez.ghastautopilot.config.*;
import dev.smootheez.ghastautopilot.registry.*;
import net.fabricmc.api.*;
import net.minecraft.*;
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

    public static Vec3 getDestination() {
        return destination;
    }

    public static void clearDestination() {
        destination = null;
    }

    public MinecraftHandler(Minecraft minecraft) {
        this.minecraft = minecraft;
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
                    player.displayClientMessage(Component.translatable("commands.ghastautopilot.error.tooLow").withStyle(ChatFormatting.RED), true);
                    continue;
                }

                toggleAutopilot();
            } else {
                player.displayClientMessage(Component.translatable("commands.ghastautopilot.error.notRiding").withStyle(ChatFormatting.RED), true);
            }
        }
    }

    private void toggleAutopilot() {
        enableAutopilot = !enableAutopilot;
        minecraft.options.keyUp.setDown(enableAutopilot);
        if (minecraft.player != null)
            minecraft.player.displayClientMessage(Component.translatable("commands.ghastautopilot." + (enableAutopilot ? "enabled" : "disabled")).withStyle(enableAutopilot ? ChatFormatting.GREEN : ChatFormatting.YELLOW), true);
    }

    private void handleAutopilotInterrupt(boolean isRidingHappyGhast) {
        if (enableAutopilot && !isRidingHappyGhast) {
            enableAutopilot = false;
            clearDestination();
            minecraft.options.keyUp.setDown(false);
            if (minecraft.player == null) return;
            minecraft.player.displayClientMessage(Component.translatable("commands.ghastautopilot.error.notRiding").withStyle(ChatFormatting.YELLOW), true);
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
        double dz = destination.z - eyePos.z;

        double distanceSq = dx * dx + dz * dz;
        double stopThresholdSq = GhastAutopilotConfig.STOP_THRESHOLD.getValue();

        if (distanceSq < stopThresholdSq) {
            enableAutopilot = false;
            minecraft.options.keyUp.setDown(false);
            clearDestination();
            player.displayClientMessage(Component.translatable("commands.ghastautopilot.arrived").withStyle(ChatFormatting.GREEN), true);
            return;
        }

        float yaw = (float) (Mth.atan2(dz, dx) * (180F / Math.PI)) - 90F;

        player.setYRot(yaw);
        player.yRotO = yaw;
    }

    private float lerp(float from, float to, float maxSpeed) {
        float diff = to - from;
        float speed = Math.min(Math.abs(diff) * 0.2f, maxSpeed);
        return from + diff * speed;
    }
}
