package dev.smootheez.ghastautopilot.command;

import com.mojang.brigadier.*;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.*;
import com.mojang.brigadier.context.*;
import dev.smootheez.ghastautopilot.handler.*;
import net.minecraft.*;
import net.minecraft.client.*;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.player.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.phys.*;

public class AutopilotCommand {
    private static final String SET_DESTINATION = "setdestination";
    private static final String REMOVE_DESTINATION = "removedestination";

    private AutopilotCommand() {
    }

    public static void register(CommandDispatcher<ClientSuggestionProvider> commandDispatcher) {
        commandDispatcher.register(
                LiteralArgumentBuilder.<ClientSuggestionProvider>literal("ghastautopilot")
                        .then(LiteralArgumentBuilder.<ClientSuggestionProvider>literal(SET_DESTINATION)
                                .then(RequiredArgumentBuilder.<ClientSuggestionProvider, Integer>argument("x", IntegerArgumentType.integer())
                                        .then(RequiredArgumentBuilder.<ClientSuggestionProvider, Integer>argument("z", IntegerArgumentType.integer())
                                                .executes(AutopilotCommand::runSetDestinationCommand))))
                        .then(LiteralArgumentBuilder.<ClientSuggestionProvider>literal(REMOVE_DESTINATION)
                                .executes(AutopilotCommand::runRemoveDestinationCommand))
        );
    }

    private static int runSetDestinationCommand(CommandContext<ClientSuggestionProvider> context) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        if (player == null || !(player.getVehicle() instanceof HappyGhast)) {
            if (player != null)
                player.displayClientMessage(Component.translatable("commands.ghastautopilot.error.notRiding").withStyle(ChatFormatting.RED), false);
            return 0;
        }

        Integer x = context.getArgument("x", Integer.class);
        Integer z = context.getArgument("z", Integer.class);

        Vec3 destination = new Vec3(x, 0, z);
        MinecraftHandler.setDestination(destination);

        player.displayClientMessage(Component.translatable("commands.ghastautopilot.setdestination.success", x, z).withStyle(ChatFormatting.GREEN), false);
        return 1;
    }

    private static int runRemoveDestinationCommand(CommandContext<ClientSuggestionProvider> context) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        if (player == null || !(player.getVehicle() instanceof HappyGhast)) {
            if (player != null)
                player.displayClientMessage(Component.translatable("commands.ghastautopilot.error.notRiding").withStyle(ChatFormatting.RED), false);
            return 0;
        }

        if (MinecraftHandler.getDestination() == null) {
            player.displayClientMessage(Component.translatable("commands.ghastautopilot.removedestination.failure").withStyle(ChatFormatting.RED), false);
        }

        MinecraftHandler.clearDestination();
        player.displayClientMessage(Component.translatable("commands.ghastautopilot.removedestination.success").withStyle(ChatFormatting.GREEN), false);
        return 1;
    }
}