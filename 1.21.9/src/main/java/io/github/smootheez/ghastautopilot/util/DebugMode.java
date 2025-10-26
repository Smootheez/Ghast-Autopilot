package io.github.smootheez.ghastautopilot.util;

import io.github.smootheez.ghastautopilot.config.*;
import io.github.smootheez.smoothiezapi.config.*;

public final class DebugMode {
    private DebugMode() {}

    public static void sendLoggerInfo(String msg) {
        if (Boolean.FALSE.equals(ConfigManager.getConfig(GhastAutopilotConfig.class).getDebugMode().getValue())) return;
        Constants.LOGGER.info(msg);
    }
}
