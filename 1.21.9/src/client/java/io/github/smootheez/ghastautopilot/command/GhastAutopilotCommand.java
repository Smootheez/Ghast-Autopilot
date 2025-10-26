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
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;

@Environment(EnvType.CLIENT)
public class GhastAutopilotCommand implements ClientCommandRegistrationCallback {
    private static final String X_COORDINATE = "x";
    private static final String Z_COORDINATE = "z";

    private static final String COMMANDS = "commands.";
    private static final String REMOVE_TRANSLATABLE_MESSAGE = COMMANDS + Constants.MOD_ID + ".remove_destination";
    private static final String SET_TRANSLATABLE_MESSAGE = COMMANDS + Constants.MOD_ID + ".set_destination";
    private static final String GET_TRANSLATABLE_MESSAGE = COMMANDS + Constants.MOD_ID + ".get_destination";
    private static final String SUCCESS = ".success";
    private static final String FAIL = ".fail";

    @Override
    public void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandBuildContext registryAccess) {
        dispatcher.register(getCommandSourceLiteralArgumentBuilder("ghastautopilot")
                .then(getCommandSourceLiteralArgumentBuilder("setdestination")
                        .then(getIntegerRequiredArgumentBuilder(X_COORDINATE)
                                .then(getIntegerRequiredArgumentBuilder(Z_COORDINATE)
                                        .executes(GhastAutopilotCommand::runSetDestination))))
                .then(getCommandSourceLiteralArgumentBuilder("removedestination")
                        .executes(GhastAutopilotCommand::runRemoveDestination))
                .then(getCommandSourceLiteralArgumentBuilder("getdestination")
                        .executes(GhastAutopilotCommand::getDestination))
        );
    }

    private static int getDestination(CommandContext<FabricClientCommandSource> context) {
        LocalPlayer player = context.getSource().getPlayer();

        Vec3 vec3 = GhastAutopilotUtil.getVec3();

        if (vec3 == null) {
            player.displayClientMessage(
                    Component.translatable(GET_TRANSLATABLE_MESSAGE + FAIL).withStyle(ChatFormatting.RED), false
            );
            return 0;
        }

        player.displayClientMessage(
                Component.translatable(GET_TRANSLATABLE_MESSAGE + SUCCESS, vec3.x, vec3.y), false
        );
        return 1;
    }

    private static @NotNull LiteralArgumentBuilder<FabricClientCommandSource> getCommandSourceLiteralArgumentBuilder(String removedestination) {
        return LiteralArgumentBuilder.literal(removedestination);
    }

    private static @NotNull RequiredArgumentBuilder<FabricClientCommandSource, Integer> getIntegerRequiredArgumentBuilder(String coordinate) {
        return RequiredArgumentBuilder.argument(coordinate, IntegerArgumentType.integer());
    }

    private static int runRemoveDestination(CommandContext<FabricClientCommandSource> context) {
        LocalPlayer player = context.getSource().getPlayer();

        if (isRidingHappyGhast(player, REMOVE_TRANSLATABLE_MESSAGE + FAIL  + ".not_riding")) return 0;

        if (GhastAutopilotUtil.getVec3() == null) {
            player.displayClientMessage(
                    Component.translatable(REMOVE_TRANSLATABLE_MESSAGE + FAIL).withStyle(ChatFormatting.RED), false
            );
            return 0;
        }

        GhastAutopilotUtil.clearVec3();
        player.displayClientMessage(
                Component.translatable(REMOVE_TRANSLATABLE_MESSAGE + SUCCESS)
                        .withStyle(ChatFormatting.GREEN), false);
        return 1;
    }

    private static int runSetDestination(CommandContext<FabricClientCommandSource> context) {
        LocalPlayer player = context.getSource().getPlayer();

        if (isRidingHappyGhast(player, SET_TRANSLATABLE_MESSAGE + FAIL)) return 0;

        int x = IntegerArgumentType.getInteger(context, X_COORDINATE);
        int z = IntegerArgumentType.getInteger(context, Z_COORDINATE);

        Vec3 vec3 = new Vec3(x, 0, z);
        GhastAutopilotUtil.setVec3(vec3);

        player.displayClientMessage(
                Component.translatable(SET_TRANSLATABLE_MESSAGE + SUCCESS, vec3.x, vec3.z)
                        .withStyle(ChatFormatting.GREEN), false);
        return 1;
    }

    private static boolean isRidingHappyGhast(LocalPlayer player, String message) {
        if (!(player.getVehicle() instanceof HappyGhast)) {
            player.displayClientMessage(
                    Component.translatable(message).withStyle(ChatFormatting.RED), false
            );
            return true;
        }
        return false;
    }
}
