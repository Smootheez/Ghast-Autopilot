package io.github.smootheez.ghastautopilot.command;

import com.mojang.brigadier.*;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.*;
import com.mojang.brigadier.context.*;
import io.github.smootheez.ghastautopilot.util.*;
import net.fabricmc.api.*;
import net.fabricmc.fabric.api.client.command.v2.*;
import net.minecraft.*;
import net.minecraft.client.player.*;
import net.minecraft.commands.*;
import net.minecraft.network.chat.*;

@Environment(EnvType.CLIENT)
public class GhastAutopilotCommand implements ClientCommandRegistrationCallback {
    private static final String X_COORDINATE = "x";
    private static final String Z_COORDINATE = "z";

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext registryAccess) {
        dispatcher.register(LiteralArgumentBuilder.<FabricClientCommandSource>literal("ghastautopilot")
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("setdestination")
                        .then(RequiredArgumentBuilder.<FabricClientCommandSource, Integer>argument(X_COORDINATE, IntegerArgumentType.integer())
                                .then(RequiredArgumentBuilder.<FabricClientCommandSource, Integer>argument(Z_COORDINATE, IntegerArgumentType.integer())
                                        .executes(GhastAutopilotCommand::runSetDestination))))
                .then(LiteralArgumentBuilder.<FabricClientCommandSource>literal("removedestination")
                        .executes(GhastAutopilotCommand::runRemoveDestination))
        );
    }

    private static int runRemoveDestination(CommandContext<FabricClientCommandSource> context) {
        LocalPlayer player = context.getSource().getPlayer();

        player.displayClientMessage(
                Component.translatable("commands." + Constants.MOD_ID + ".remove_destination.success")
                        .withStyle(ChatFormatting.GREEN), false);
        return 1;
    }

    private static int runSetDestination(CommandContext<FabricClientCommandSource> context) {
        LocalPlayer player = context.getSource().getPlayer();

        int x = IntegerArgumentType.getInteger(context, X_COORDINATE);
        int z = IntegerArgumentType.getInteger(context, Z_COORDINATE);

        player.displayClientMessage(
                Component.translatable("commands." + Constants.MOD_ID + ".set_destination.success", x, z)
                        .withStyle(ChatFormatting.GREEN), false);
        return 1;
    }
}
