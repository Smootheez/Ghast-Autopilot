package io.github.smootheez.ghastautopilot.util;

import net.minecraft.world.phys.*;

public final class GhastAutopilotUtil {
    private GhastAutopilotUtil() {}

    private static Vec3 vec3;

    public static void clearVec3() {
        GhastAutopilotUtil.vec3 = null;
    }

    public static Vec3 getVec3() {
        return vec3;
    }

    public static void setVec3(Vec3 vec3) {
        GhastAutopilotUtil.vec3 = vec3;
    }
}
