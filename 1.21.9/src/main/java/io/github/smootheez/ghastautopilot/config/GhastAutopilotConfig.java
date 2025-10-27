package io.github.smootheez.ghastautopilot.config;

import io.github.smootheez.ghastautopilot.config.option.*;
import io.github.smootheez.ghastautopilot.util.*;
import io.github.smootheez.smoothiezapi.api.*;
import io.github.smootheez.smoothiezapi.config.option.*;

@Config(name = Constants.MOD_ID, autoGui = true)
public class GhastAutopilotConfig implements ConfigApi {
    private final BooleanOption debugMode = new BooleanOption("debug_mode", false);

    private final BooleanOption ghastAutopilot = new BooleanOption("ghast_autopilot", true);
    private final BooleanOption displayToggleNotification = new BooleanOption("display_toggle_notification", true);
    private final BooleanOption lookAt = new BooleanOption("look_at", true);
    private final BooleanOption autopilotIcon = new BooleanOption("autopilot_icon", true);

    private final DoubleOption smoothFactor = new DoubleOption("smooth_factor", 0.12, 0.01, 0.5);

    private final EnumOption<IconPosition> iconPosition = new EnumOption<>("icon_position", IconPosition.TOP_RIGHT);

    private final DoubleOption stopThreshold = new DoubleOption("stop_threshold", 2.0, 0.0, 10.0);

    public BooleanOption getDebugMode() {
        return debugMode;
    }

    public BooleanOption getDisplayToggleNotification() {
        return displayToggleNotification;
    }

    public BooleanOption getGhastAutopilot() {
        return ghastAutopilot;
    }

    public BooleanOption getLookAt() {
        return lookAt;
    }

    public BooleanOption getAutopilotIcon() {
        return autopilotIcon;
    }

    public DoubleOption getSmoothFactor() {
        return smoothFactor;
    }

    public EnumOption<IconPosition> getIconPosition() {
        return iconPosition;
    }

    public DoubleOption getStopThreshold() {
        return stopThreshold;
    }
}
