package io.github.smootheez.ghastautopilot.config;

import io.github.smootheez.ghastautopilot.util.*;
import io.github.smootheez.smoothiezapi.api.*;
import io.github.smootheez.smoothiezapi.config.option.*;

@Config(name = Constants.MOD_ID, autoGui = true)
public class GhastAutopilotConfig implements ConfigApi {
    private final BooleanOption debugMode = new BooleanOption("debug_mode", false);

    public BooleanOption getDebugMode() {
        return debugMode;
    }
}
