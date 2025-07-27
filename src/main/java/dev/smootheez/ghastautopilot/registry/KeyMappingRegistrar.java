package dev.smootheez.ghastautopilot.registry;

import com.mojang.blaze3d.platform.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;

@Environment(EnvType.CLIENT)
public class KeyMappingRegistrar {
    private static final String KEYBIND_CATEGORY = "key.categories.ghastautopilot";
    private KeyMappingRegistrar() {
    }

    public static final KeyMapping START_AUTOPILOT = new KeyMapping(
            "key.ghastautopilot.startAutopilot",
            InputConstants.Type.KEYSYM,
            InputConstants.KEY_B,
            KEYBIND_CATEGORY
    );
}
