package com.kauruck.exterra.networking;

import net.minecraftforge.api.distmarker.Dist;

public enum BlockEntityPropertySide {
    Server,
    Synced,
    Requestable;

    @Override
    public String toString() {
        return switch (this){
            case Synced -> "synced";
            case Server -> "server";
            case Requestable -> "requestable";
        };
    }
}
