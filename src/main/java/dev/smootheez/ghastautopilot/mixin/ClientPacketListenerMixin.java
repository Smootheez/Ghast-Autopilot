package dev.smootheez.ghastautopilot.mixin;

import com.mojang.brigadier.*;
import dev.smootheez.ghastautopilot.*;
import dev.smootheez.ghastautopilot.command.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.network.protocol.game.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Shadow public abstract CommandDispatcher<ClientSuggestionProvider> getCommands();

    @Shadow public abstract ClientSuggestionProvider getSuggestionsProvider();

    @Inject(method = "handleCommands", at = @At("RETURN"))
    private void onHandleCommands(ClientboundCommandsPacket clientboundCommandsPacket, CallbackInfo ci) {
        CommandDispatcher<ClientSuggestionProvider> dispatcher = this.getCommands();
        AutopilotCommand.register(dispatcher);
    }

    @Inject(method = "sendUnattendedCommand", at = @At("HEAD"), cancellable = true)
    private void sendUnattendedCommand(String string, Screen screen, CallbackInfo ci) {
        if (this.tryExecuteClientCommand(string)) {
            ci.cancel();
        }
    }

    @Inject(method = "sendCommand", at = @At("HEAD"), cancellable = true)
    private void sendCommand(String string, CallbackInfo ci) {
        if (tryExecuteClientCommand(string)) {
            ci.cancel();
        }
    }

    @Unique
    private boolean tryExecuteClientCommand(String command) {
        if (!command.startsWith("ghastautopilot")) {
            return false; // Not our command, do nothing.
        }

        Constants.LOGGER.info("Intercepted client-side command: /{}", command);
        try {
            CommandDispatcher<ClientSuggestionProvider> dispatcher = this.getCommands();
            ClientSuggestionProvider source = this.getSuggestionsProvider();

            dispatcher.execute(command, source);

        } catch (Exception e) {
            Constants.LOGGER.error("Failed to execute client-side command '/{}'", command, e);
        }
        return true;
    }
}
