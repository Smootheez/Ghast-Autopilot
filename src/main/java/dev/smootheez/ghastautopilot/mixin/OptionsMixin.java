package dev.smootheez.ghastautopilot.mixin;

import dev.smootheez.ghastautopilot.registry.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import org.apache.commons.lang3.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.io.*;

@Environment(EnvType.CLIENT)
@Mixin(Options.class)
public abstract class OptionsMixin {
    @Mutable
    @Shadow @Final public KeyMapping[] keyMappings;

    @Inject(method = "<init>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Options;load()V"
    ))
    private void addCustomKeybind(Minecraft minecraft, File file, CallbackInfo ci) {
        this.keyMappings = ArrayUtils.addAll(
                this.keyMappings,
                KeyMappingRegistrar.START_AUTOPILOT
        );
    }
}
