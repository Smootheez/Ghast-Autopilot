package dev.smootheez.ghastautopilot.mixin;

import com.mojang.brigadier.*;
import dev.smootheez.ghastautopilot.command.*;
import net.minecraft.commands.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(Commands.class)
public abstract class CommandsMixin {
    @Shadow @Final private CommandDispatcher<CommandSourceStack> dispatcher;

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(Commands.CommandSelection commandSelection, CommandBuildContext commandBuildContext, CallbackInfo ci) {
        AutopilotCommand.register(this.dispatcher);
    }
}
