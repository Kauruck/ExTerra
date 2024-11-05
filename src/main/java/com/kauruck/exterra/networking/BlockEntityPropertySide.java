package com.kauruck.exterra.networking;

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
