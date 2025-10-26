package io.github.smootheez.ghastautopilot.config;

import io.github.smootheez.ghastautopilot.util.*;
import io.github.smootheez.smoothiezapi.api.*;
import io.github.smootheez.smoothiezapi.config.option.*;

@Config(name = Constants.MOD_ID, autoGui = true)
public class GhastAutopilotConfig implements ConfigApi {
    private final BooleanOption debugMode = new BooleanOption("debug_mode", false);

    private final BooleanOption displayToggleNotification = new BooleanOption("display_toggle_notification", true);
    private final BooleanOption enableGhastAutopilot = new BooleanOption("enable_ghast_autopilot", true);
    private final BooleanOption enableLookAt = new BooleanOption("enable_look_at", true);

    private final DoubleOption smoothFactor = new DoubleOption("smooth_factor", 0.12, 0.01, 0.5);

    public BooleanOption getDebugMode() {
        return debugMode;
    }

    public BooleanOption getDisplayToggleNotification() {
        return displayToggleNotification;
    }

    public BooleanOption getEnableGhastAutopilot() {
        return enableGhastAutopilot;
    }

    public BooleanOption getEnableLookAt() {
        return enableLookAt;
    }

    public DoubleOption getSmoothFactor() {
        return smoothFactor;
    }
}
