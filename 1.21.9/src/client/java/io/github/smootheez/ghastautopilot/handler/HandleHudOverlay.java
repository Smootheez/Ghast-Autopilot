package io.github.smootheez.ghastautopilot.handler;

import io.github.smootheez.ghastautopilot.config.*;
import io.github.smootheez.ghastautopilot.util.*;
import io.github.smootheez.smoothiezapi.config.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.rendering.v1.hud.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.*;

@Environment(EnvType.CLIENT)
public class HandleHudOverlay implements HudElement {
    private static final ResourceLocation AUTOPILOT_ACTIVE_ICON =
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/autopilot_active.png");
    private static final GhastAutopilotConfig CONFIG = ConfigManager.getConfig(GhastAutopilotConfig.class);

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (Boolean.FALSE.equals(CONFIG.getAutopilotIcon().getValue()) || !GhastAutopilotUtil.isAutoPilot()) return;
        var iconSize = 16;

        guiGraphics.blit(
                RenderPipelines.GUI_TEXTURED, AUTOPILOT_ACTIVE_ICON, 5, 5, 0, 0, iconSize, iconSize, iconSize, iconSize);
    }
}
