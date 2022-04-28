package com.kauruck.exterra.items;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.geometry.Shape;
import com.kauruck.exterra.blocks.RitualStone;
import com.kauruck.exterra.geometry.ShapeCollection;
import com.kauruck.exterra.modules.ExTerraShared;
import com.kauruck.exterra.modules.ExTerraTags;
import com.kauruck.exterra.util.NBTUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class RitualMap extends Item {

    public static final String TAG_SELECT_MODE = "select_mode";
    public static final String TAG_SHAPES = "shapes";
    public static final String TAG_CURRENT_SHAPE = "current_shape";
    public static final String TAG_CENTER = "center_block";

    public RitualMap() {
        super(ExTerraShared.DEFAULT_PROPERTIES_ITEM);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        if(pContext.getPlayer() == null)
            return InteractionResult.FAIL;
        InteractionResult interactionResult = InteractionResult.PASS;
        if(!pContext.getLevel().isClientSide()) {
            CompoundTag tag =  pContext.getItemInHand().getTag();
            if(tag == null)
                tag = new CompoundTag();

            ShapeCollection shapes = new ShapeCollection(tag.getCompound(TAG_SHAPES));
            int currentShapeIndex = tag.getInt(TAG_CURRENT_SHAPE);

            BlockState clicked = pContext.getLevel().getBlockState(pContext.getClickedPos());

            if(clicked.is(ExTerraTags.RITUAL_TIER_I) && tag.contains(TAG_CENTER)) {
                BlockPos center = NBTUtil.blockPosFromNBT(tag.getCompound(TAG_CENTER));
                Shape current = null;
                if (tag.getBoolean(TAG_SELECT_MODE) && shapes.getShapes().size() > currentShapeIndex) {
                    current = shapes.getShapes().get(currentShapeIndex);
                } else if (pContext.getPlayer().isCrouching()) {
                    tag.putBoolean(TAG_SELECT_MODE, true);
                    current = new Shape();
                    shapes.getShapes().add(current);
                }

                //Convert into position relative to the center
                BlockPos blockPosToAdd = pContext.getClickedPos().subtract(center);

                if(current != null) {
                    if (!current.containsPoint(blockPosToAdd)) {
                        current.addPoint(blockPosToAdd);
                        currentShapeIndex = shapes.getShapes().indexOf(current);
                        interactionResult = InteractionResult.SUCCESS;
                    } else {
                        //Close the Shape, test weather it is valid
                        if(current.end()) {
                            currentShapeIndex = shapes.getShapes().size();
                            tag.putBoolean(TAG_SELECT_MODE, false);
                            interactionResult = InteractionResult.SUCCESS;
                        }
                        else{
                            pContext.getPlayer().sendMessage(new TextComponent("Invalid Shape"), Util.NIL_UUID);
                            //Remove invalid shape
                            shapes.getShapes().remove(currentShapeIndex);
                            currentShapeIndex = shapes.getShapes().size();
                            interactionResult = InteractionResult.FAIL;
                        }
                    }
                }
            }
            else if(clicked.getBlock() instanceof RitualStone && pContext.getPlayer().isCrouching()){
                CompoundTag centerPos = NBTUtil.blockPosToNBT(pContext.getClickedPos());
                tag.put(TAG_CENTER, centerPos);
                interactionResult = InteractionResult.SUCCESS;
            }

            tag.put(TAG_SHAPES, shapes.toNBT());
            tag.putInt(TAG_CURRENT_SHAPE, currentShapeIndex);

            pContext.getItemInHand().setTag(tag);
        }

        return interactionResult;
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        CompoundTag tag = pStack.getTag();
        if(tag != null) {
            ShapeCollection shapes = new ShapeCollection(tag.getCompound(TAG_SHAPES));
            for (Shape shape : shapes.getShapes())
                pTooltipComponents.add(new TextComponent(shape.toString()));
        }
    }
}
