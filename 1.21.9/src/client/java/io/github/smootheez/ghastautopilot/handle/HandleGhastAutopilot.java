package io.github.smootheez.ghastautopilot.handle;

import io.github.smootheez.ghastautopilot.registry.*;
import io.github.smootheez.ghastautopilot.util.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
import net.minecraft.client.*;
import net.minecraft.client.player.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.animal.*;

@Environment(EnvType.CLIENT)
public class HandleGhastAutopilot implements ClientTickEvents.EndTick {
    private boolean autoPilot = false;

    @Override
    public void onEndTick(Minecraft minecraft) {
        LocalPlayer player = minecraft.player;
        if (player == null) return;

        boolean isRidingHappyGhast = player.getVehicle() instanceof HappyGhast;

        shouldContinueAutopilot(isRidingHappyGhast); // check if the autopilot should continue or not

        if (KeyMappingRegistry.TOGGLE_AUTOPILOT.consumeClick()) {
            if (isRidingHappyGhast) {
                autoPilot = !autoPilot;
                sendSuccessMessage(player);
            } else sendFailMessage(player);
        }

        minecraft.options.keyUp.setDown(autoPilot);
        DebugMode.sendLoggerInfo("Autopilot: " + (autoPilot ? "Enabled" : "Disabled"));
    }

    private static void sendFailMessage(LocalPlayer player) {
        player.displayClientMessage(
                Component.translatable("fail." + Constants.MOD_ID + ".toggle_autopilot"),
                true
        );
    }

    private void sendSuccessMessage(LocalPlayer player) {
        if (!autoPilot) return;
        player.displayClientMessage(
                Component.translatable("success." + Constants.MOD_ID + ".toggle_autopilot"),
                true
        );
    }

    private void shouldContinueAutopilot(boolean isRidingHappyGhast) {
        if (autoPilot && isRidingHappyGhast) return;
        autoPilot = false; // when the autopilot still active and user stop riding happy ghast, disable it
    }
}
