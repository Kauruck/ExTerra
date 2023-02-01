package com.kauruck.exterra.items;

import com.kauruck.exterra.fx.ParticleHelper;
import com.kauruck.exterra.fx.VectorHelper;
import com.kauruck.exterra.geometry.Shape;
import com.kauruck.exterra.geometry.ShapeCollection;
import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.modules.ExTerraShared;
import com.kauruck.exterra.util.NBTUtil;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

import static com.kauruck.exterra.fx.MathHelper.clamp;

public class RitualLensItem extends Item {

    public static final String TAG_VIEW_CENTER = "center";
    public RitualLensItem() {
        super(ExTerraShared.DEFAULT_PROPERTIES_ITEM);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        BlockState clicked = pContext.getLevel().getBlockState(pContext.getClickedPos());
        if(clicked.getBlock() == ExTerraCore.RITUAL_STONE.get() && pContext.getPlayer().isCrouching()){
            CompoundTag tag = pContext.getItemInHand().getOrCreateTag();
            tag.put(TAG_VIEW_CENTER, NBTUtil.blockPosToNBT(pContext.getClickedPos()));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pLevel.isClientSide) {
            if (pEntity instanceof Player player) {
                ItemStack mainHand = player.getMainHandItem();
                if (player.getOffhandItem().getItem() == this && mainHand.getItem() == ExTerraCore.RITUAL_MAP.get()) {
                    CompoundTag data = mainHand.getTag();
                    CompoundTag centerTag = pStack.getTag();
                    if (data == null || centerTag == null || !centerTag.contains(TAG_VIEW_CENTER))
                        return;
                    ShapeCollection shapeCollection = new ShapeCollection(data.getCompound(RitualMap.TAG_SHAPES));
                    List<Shape> shapes = shapeCollection.getShapes();
                    BlockPos center = NBTUtil.blockPosFromNBT(centerTag.getCompound(TAG_VIEW_CENTER));
                    int currentShape = data.getInt(RitualMap.TAG_CURRENT_SHAPE);
                    int i = 0;
                    int r = 100;
                    int g = 100;
                    int b = 100;
                    int step = (int) (155 * (1f / shapes.size()));
                    //Spawn Particles
                    for (Shape current : shapes) {
                        List<BlockPos> worldPos = current.getActualPositions(center);
                        int shape_pos_index = 0;
                        for (int index = 0; index < worldPos.size(); index++) {
                            BlockPos start = worldPos.get(index);
                            BlockPos end;
                            if (index < worldPos.size() - 1) {
                                end = worldPos.get(index + 1);
                            } else {
                                end = worldPos.get(0);
                            }
                            if(shape_pos_index == 0){

                            }
                            if(i == currentShape) {
                                ParticleHelper.emitParticlesOnLine((ClientLevel) pLevel, VectorHelper.blockPosToVector(start), VectorHelper.blockPosToVector(end), new Vector3f(1, 0, 0), 1F);
                                if(shape_pos_index == 0)
                                    ParticleHelper.emitParticlePoint((ClientLevel) pLevel, VectorHelper.blockPosToVector(start), new Vector3f(1, 0,0), 1f);
                            }
                            else {
                                ParticleHelper.emitParticlesOnLine((ClientLevel) pLevel, VectorHelper.blockPosToVector(start), VectorHelper.blockPosToVector(end), new Vector3f(r / 255f, g / 255f, b / 255f), 1F);
                            }
                            shape_pos_index ++;
                        }
                        switch (i % 3) {
                            case 0:
                                r += step;
                            case 1:
                                g += step;
                            case 2:
                                b += step;
                        }
                        r = clamp(r, 0, 255);
                        g = clamp(g, 0, 255);
                        b = clamp(b, 0, 255);
                        i++;
                    }
                }
            }
        }
    }
}
