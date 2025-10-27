package io.github.smootheez.ghastautopilot.util;

import net.minecraft.world.phys.*;

public final class GhastAutopilotUtil {
    private GhastAutopilotUtil() {}

    private static Vec3 vec3;
    private static boolean autoPilot = false;

    public static boolean isAutoPilot() {
        return autoPilot;
    }

    public static void setAutoPilot(boolean autoPilot) {
        GhastAutopilotUtil.autoPilot = autoPilot;
    }

    public static void clearVec3() {
        if (vec3 == null) return;
        DebugMode.sendLoggerInfo("Clearing Vec3");
        GhastAutopilotUtil.vec3 = null;
    }

    public static Vec3 getVec3() {
        return vec3;
    }

    public static void setVec3(Vec3 vec3) {
        DebugMode.sendLoggerInfo("Setting Vec3: " + vec3.x + ", " + vec3.z);
        GhastAutopilotUtil.vec3 = vec3;
    }
}
