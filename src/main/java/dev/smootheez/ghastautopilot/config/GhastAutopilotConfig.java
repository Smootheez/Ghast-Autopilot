package dev.smootheez.ghastautopilot.config;

import dev.smootheez.ghastautopilot.*;
import dev.smootheez.scl.api.*;
import dev.smootheez.scl.config.*;
import net.fabricmc.api.*;

@Environment(EnvType.CLIENT)
@Config(name = Constants.MOD_ID, gui = true)
public class GhastAutopilotConfig {
    public static final ConfigOption<Integer> MINIMUM_HEIGHT = ConfigOption.create("minimumHeight", 120, 32, 320);
    public static final ConfigOption<Double> STOP_THRESHOLD = ConfigOption.create("stopThreshold", 2.0, 0.0, 10.0).asSlider();
    public static final ConfigOption<Boolean> ENABLE_AUTOPILOT = ConfigOption.create("enableAutopilot", true);
}
