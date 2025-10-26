package io.github.smootheez.ghastautopilot.registry;

import com.mojang.blaze3d.platform.*;
import io.github.smootheez.ghastautopilot.util.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.keybinding.v1.*;
import net.minecraft.client.*;
import net.minecraft.resources.*;

@Environment(EnvType.CLIENT)
public final class KeyMappingRegistry {
    private KeyMappingRegistry() {}

    private static final KeyMapping.Category GHAST_AUTOPILOT_CATEGORY = KeyMapping.Category.register(
            ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "ghast_autopilot")
    );

    public static final KeyMapping TOGGLE_AUTOPILOT = new KeyMapping(
            "key." + Constants.MOD_ID + ".toggle_autopilot",
            InputConstants.KEY_B,
            GHAST_AUTOPILOT_CATEGORY
    );

    public static void registerKeyMappings() {
        Constants.LOGGER.info("Registering Key Mappings...");
        KeyBindingHelper.registerKeyBinding(TOGGLE_AUTOPILOT);
    }
}
