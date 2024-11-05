package com.kauruck.exterra.api.exceptions;

import net.minecraft.resources.ResourceLocation;

public class NoCodecException extends RuntimeException {
    public NoCodecException(ResourceLocation codecLocation) {
        super("No codec for " + codecLocation);
    }
}
