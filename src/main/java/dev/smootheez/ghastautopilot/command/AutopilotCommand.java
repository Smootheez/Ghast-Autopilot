package dev.smootheez.ghastautopilot.command;

import com.mojang.brigadier.*;
import dev.smootheez.ghastautopilot.handler.*;
import net.minecraft.client.*;
import net.minecraft.commands.*;
import net.minecraft.commands.arguments.coordinates.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.phys.*;

public class AutopilotCommand {
    private static final String SET_DESTINATION = "setdestination";
    private static final String REMOVE_DESTINATION = "removedestination";
    private static final MinecraftHandler handler = new MinecraftHandler(Minecraft.getInstance());

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
        Vec3 vec = coordinates.getPosition(source);
        handler.setDestination(vec);

        source.sendSuccess(
                () -> Component.translatable("commands.ghastautopilot.setdestination.success", (int) vec.x, (int) vec.y, (int) vec.z),
                true
        );

        return 1;
    }

    private static int runRemoveDestinationCommand(CommandSourceStack source) {
        handler.clearDestination();

        source.sendSuccess(
                () -> Component.translatable("commands.ghastautopilot.removedestination.success"),
                true
        );

        return 1;
    }

}
