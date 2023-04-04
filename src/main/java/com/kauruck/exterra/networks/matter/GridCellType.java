package com.kauruck.exterra.networks.matter;

import com.kauruck.exterra.api.networks.matter.INetworkMemberBlock;
import com.kauruck.exterra.blocks.RitualStone;
import com.kauruck.exterra.modules.ExTerraTags;
import net.minecraft.world.level.block.state.BlockState;

public enum GridCellType {
    NetworkMember('M'),
    RitualStone('C'),
    Connection('#'),
    Air('_');

    private final char repr;

    GridCellType(char repr) {
        this.repr = repr;
    }

    public static GridCellType match(BlockState state) {
        if (state.getBlock() instanceof INetworkMemberBlock) {
            return NetworkMember;
        } else if (state.getBlock() instanceof com.kauruck.exterra.blocks.RitualStone) {
            return RitualStone;
        } else if (state.is(ExTerraTags.MATTER_WIRE)) {
            return Connection;
        }
        return Air;
    }


    @Override
    public String toString() {
        return String.valueOf(repr);
    }
}
