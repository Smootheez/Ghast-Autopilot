package io.github.smootheez.ghastautopilot.handler;

import io.github.smootheez.ghastautopilot.config.*;
import io.github.smootheez.ghastautopilot.config.option.*;
import io.github.smootheez.ghastautopilot.util.*;
import io.github.smootheez.smoothiezapi.config.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.rendering.v1.hud.*;
import net.minecraft.client.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.*;
import net.minecraft.resources.*;
import org.jetbrains.annotations.*;

@Environment(EnvType.CLIENT)
public class HandleHudOverlay implements HudElement {
    private static final ResourceLocation AUTOPILOT_ACTIVE_ICON =
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/gui/autopilot_active.png");
    private static final GhastAutopilotConfig CONFIG = ConfigManager.getConfig(GhastAutopilotConfig.class);
    private static final int ICON_SIZE = 16;

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (Boolean.FALSE.equals(CONFIG.getAutopilotIcon().getValue()) || !GhastAutopilotUtil.isAutoPilot()) return;

        var iconPosition = setPosition(CONFIG.getIconPosition().getValue(), guiGraphics);

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, AUTOPILOT_ACTIVE_ICON,
                iconPosition.x(), iconPosition.y(), 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
    }

    private static @NotNull ArangeIconPosition setPosition(IconPosition value, GuiGraphics guiGraphics) {
        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();

        // Small margin so the icon isn’t glued to the edges
        final int margin = 3;
        final int size = ICON_SIZE;

        int x = 0;
        int y = 0;

        switch (value) {
            case TOP_LEFT -> {
                x = margin;
                y = margin;
            }
            case TOP_MIDDLE -> {
                x = (width - size) / 2;
                y = margin;
            }
            case TOP_RIGHT -> {
                x = width - size - margin;
                y = margin;
            }
            case RIGHT_MIDDLE -> {
                x = width - size - margin;
                y = (height - size) / 2;
            }
            case BOTTOM_RIGHT -> {
                x = width - size - margin;
                y = height - size - margin;
            }
            case BOTTOM_LEFT -> {
                x = margin;
                y = height - size - margin;
            }
            case LEFT_MIDDLE -> {
                x = margin;
                y = (height - size) / 2;
            }
        }

        return new ArangeIconPosition(x, y);
    }

    record ArangeIconPosition(int x, int y) {}
}
