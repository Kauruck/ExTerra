package com.kauruck.exterra.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BlockEntityClientUpdateHandler {

    public static boolean handleUpdate(CompoundTag tag, BlockPos targetPos){
        Level level = Minecraft.getInstance().player.getLevel();
        if(level.hasChunkAt(targetPos)){
            BlockEntity entity = level.getBlockEntity(targetPos);
            if(entity instanceof BaseBlockEntity baseEntity){
                baseEntity.handelClientUpdateTag(tag);
            }
        }
        return true;
    }


}
