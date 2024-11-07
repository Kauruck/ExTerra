package com.kauruck.exterra.items;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.geometry.Shape;
import com.kauruck.exterra.blocks.RitualStone;
import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.modules.ExTerraShared;
import com.kauruck.exterra.modules.ExTerraTags;
import com.kauruck.exterra.util.NBTUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RitualMap extends Item {

    public RitualMap() {
        super(ExTerraShared.DEFAULT_PROPERTIES_ITEM);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if(pContext.getPlayer() == null)
            return InteractionResult.FAIL;
        InteractionResult interactionResult = InteractionResult.PASS;
        if(!pContext.getLevel().isClientSide()) {
            ItemStack itemStack = pContext.getItemInHand();

            List<Shape> shapes = itemStack.get(ExTerraCore.COMPONENT_SHAPES);
            if (shapes == null) {
                shapes = new ArrayList<>();
            } else {
                shapes = new ArrayList<>(shapes);
            }
            int currentShapeIndex = itemStack.get(ExTerraCore.COMPONENT_CURRENT_SHAPE) != null ?
                    itemStack.get(ExTerraCore.COMPONENT_CURRENT_SHAPE) : -1;

            BlockState clicked = pContext.getLevel().getBlockState(pContext.getClickedPos());

            if(clicked.is(ExTerraTags.RITUAL_TIER_I) && itemStack.has(ExTerraCore.COMPONENT_CENTER_POS)) {
                BlockPos center = itemStack.get(ExTerraCore.COMPONENT_CENTER_POS);
                Shape current = null;
                if (Boolean.TRUE.equals(itemStack.get(ExTerraCore.COMPONENT_SELECT_MODE)) && shapes.size() > currentShapeIndex) {
                    current = shapes.get(currentShapeIndex);
                } else if (pContext.getPlayer().isCrouching()) {
                    itemStack.set(ExTerraCore.COMPONENT_SELECT_MODE, true);
                    current = new Shape();
                    shapes.add(current);
                }

                //Convert into position relative to the center
                BlockPos blockPosToAdd = pContext.getClickedPos().subtract(center);

                if(current != null) {
                    if (!current.containsPoint(blockPosToAdd)) {
                        current.addPoint(blockPosToAdd);
                        currentShapeIndex = shapes.indexOf(current);
                        interactionResult = InteractionResult.SUCCESS;
                    } else {
                        //Close the Shape, test weather it is valid
                        if(current.end()) {
                            currentShapeIndex = shapes.size();
                            itemStack.set(ExTerraCore.COMPONENT_SELECT_MODE, false);
                            interactionResult = InteractionResult.SUCCESS;
                        }
                        else{
                            pContext.getPlayer().sendSystemMessage(Component.translatable("info.exterra.shape_invalid"));
                            //Remove invalid shape
                            shapes.remove(currentShapeIndex);
                            currentShapeIndex = shapes.size();
                            interactionResult = InteractionResult.FAIL;
                        }
                    }
                }
            }
            else if(clicked.getBlock() instanceof RitualStone && pContext.getPlayer().isCrouching()){
                itemStack.set(ExTerraCore.COMPONENT_CENTER_POS, pContext.getClickedPos());
                interactionResult = InteractionResult.SUCCESS;
            }

            itemStack.set(ExTerraCore.COMPONENT_SHAPES, shapes);
            itemStack.set(ExTerraCore.COMPONENT_CURRENT_SHAPE, currentShapeIndex);
        }

        return interactionResult;
    }

    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pTooltipFlag) {
        super.appendHoverText(pStack, pContext, pTooltipComponents, pTooltipFlag);
        List<Shape> shapes = pStack.get(ExTerraCore.COMPONENT_SHAPES);
        if(shapes != null) {
            for (Shape shape : shapes)
                pTooltipComponents.add(Component.literal(shape.toString()));
        }
    }

    @Override
    public boolean canAttackBlock(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer) {
        if(pState.getBlock() == ExTerraCore.RECEIVER_BLOCK.get()){
            return false;
        }
        return super.canAttackBlock(pState, pLevel, pPos, pPlayer);
    }
}
