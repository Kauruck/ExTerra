package com.kauruck.exterra.util;

import net.minecraft.world.phys.Vec3;

public class Colors {
    public static final Vec3 RED = new Vec3(1.0f, 0f, 0f);
    public static final Vec3 GREEN = new Vec3(0f, 1.0f, 0f);
    public static final Vec3 BLUE = new Vec3(0f, 0f, 1.0f);


    public static boolean isZero(Vec3 color){
        return color.x == 0 && color.y == 0 && color.z == 0;
    }
}
