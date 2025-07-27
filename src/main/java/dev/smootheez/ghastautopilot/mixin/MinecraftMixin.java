package dev.smootheez.ghastautopilot.mixin;

import dev.smootheez.ghastautopilot.handler.*;
import net.fabricmc.api.*;
import net.minecraft.client.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Unique
    private final Minecraft minecraft = (Minecraft) (Object) this;
    @Unique
    private final MinecraftHandler handler = new MinecraftHandler(this.minecraft);

    @Inject(method = "tick", at = @At("TAIL"))
    private void clientTick(CallbackInfo ci) {
        this.handler.clientTick();
    }
}
