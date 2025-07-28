package dev.smootheez.ghastautopilot.command;

import com.mojang.brigadier.*;
import dev.smootheez.ghastautopilot.handler.*;
import net.minecraft.client.*;
import net.minecraft.client.player.*;
import net.minecraft.commands.*;
import net.minecraft.commands.arguments.coordinates.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.phys.*;

public class AutopilotCommand {
    private static final String SET_DESTINATION = "setdestination";
    private static final String REMOVE_DESTINATION = "removedestination";

    private AutopilotCommand() {}
    public static void register(CommandDispatcher<CommandSourceStack> commandDispatcher) {
        commandDispatcher.register(
                Commands.literal("ghastautopilot")
                        .then(Commands.literal(SET_DESTINATION)
                                .then(Commands.argument(SET_DESTINATION, Vec3Argument.vec3())
                                        .executes(s -> runSetDestinationCommand(
                                                s.getSource(),
                                                Vec3Argument.getCoordinates(s, SET_DESTINATION)
                                        ))
                                )
                        )
                        .then(Commands.literal(REMOVE_DESTINATION)
                                .executes(s -> runRemoveDestinationCommand(s.getSource()))
                        ));
    }

    private static int runSetDestinationCommand(CommandSourceStack source, Coordinates coordinates) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        if (player == null || !(player.getVehicle() instanceof HappyGhast)) {
            if (player != null) {
                source.sendFailure(Component.translatable("commands.ghastautopilot.error.notRidingHappyGhast"));
            }
            return 0;
        }

        Vec3 vec = coordinates.getPosition(source); // Make sure this works in client context
        MinecraftHandler.setDestination(vec);

        source.sendSuccess(
                () -> Component.translatable("commands.ghastautopilot.setdestination.success", (int) vec.x, (int) vec.z),
                false
        );

        return 1;
    }


    private static int runRemoveDestinationCommand(CommandSourceStack source) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;

        if (player == null || !(player.getVehicle() instanceof HappyGhast)) {
            if (player != null) {
                source.sendFailure(Component.translatable("commands.ghastautopilot.error.notRidingHappyGhast"));
            }
            return 0;
        }

        if (MinecraftHandler.getDestination() == null) {
            source.sendFailure(Component.translatable("commands.ghastautopilot.removedestination.failure"));
            return 0;
        }

        MinecraftHandler.clearDestination();
        source.sendSuccess(
                () -> Component.translatable("commands.ghastautopilot.removedestination.success"),
                false
        );

        return 1;
    }
}
