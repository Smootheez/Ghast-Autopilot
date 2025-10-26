package io.github.smootheez.ghastautopilot.handle;

import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.*;
import net.minecraft.client.*;

@Environment(EnvType.CLIENT)
public class HandleGhastAutopilot implements ClientTickEvents.EndTick {
    @Override
    public void onEndTick(Minecraft minecraft) {
        // Handle end tick
    }
}
