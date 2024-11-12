package com.kauruck.exterra.api.networks.matter;

import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.state.BlockState;

public interface INetworkMemberBlock extends EntityBlock {
    /**
     * Weather a wire can connect to this side of the member
     * @param direction direction the wire wants to connect to
     * @param state state of the block
     * @param level level of this block
     * @return weather a wire can connect
     */
    boolean canConnectTo(Direction direction, BlockState state, LevelAccessor level);
}
