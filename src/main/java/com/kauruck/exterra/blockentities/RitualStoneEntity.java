package com.kauruck.exterra.blockentities;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.geometry.Shape;
import com.kauruck.exterra.api.rituals.IRitualProvider;
import com.kauruck.exterra.geometry.ShapeCollection;
import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.modules.ExTerraTags;
import com.kauruck.exterra.util.NBTUtil;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

public class RitualStoneEntity extends BlockEntity {

    private final List<BlockPos> toCheck = new ArrayList<>();
    private final List<IRitualProvider> ritualProviders = new ArrayList<>();
    private List<Shape> shapes = new ArrayList<>();
    private List<BlockPos> shapesPos = new ArrayList<>();
    private final int size = 5;
    private boolean building = false;


    public RitualStoneEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ExTerraCore.RITUAL_STONE_ENTITY.get(), pWorldPosition, pBlockState);
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        shapes = new ShapeCollection(tag.getCompound("shapes")).getShapes();
        shapesPos = NBTUtil.blockPosListFromNBT(tag.getCompound("shapesPositions"));
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.put("shapes", new ShapeCollection(shapes).toNBT());
        tag.put("shapesPositions", NBTUtil.blockPosListToNBT(shapesPos));
        return super.save(tag);
    }

    private void validateMultiblock(){
        //Check the positions of the shapes
        //No enhanced for loop here, so that I can remove
        for(int i = 0; i < shapesPos.size(); i++){
            BlockPos current = shapesPos.get(i);
            BlockState state = this.getLevel().getBlockState(current);
            if(!state.is(ExTerraTags.RITUAL_TIER_I)){
                shapesPos.remove(current);
                ExTerra.LOGGER.info("Block at " + current + " is not a tier 1 ritual block");
                this.setChanged();
            }
        }
        validateShapes();
    }

    private void validateShapes(){
        //Check if all required positions for a shape are existing
        for(int i = 0; i < shapes.size(); i++){
            Shape current = shapes.get(i);
            boolean allPositions = true;
            for(BlockPos pos : current.getActualPositions(this.getBlockPos())){
                if (!shapesPos.contains(pos)) {
                    allPositions = false;
                    break;
                }
            }
            if(!allPositions){
                shapes.remove(current);
                ExTerra.LOGGER.info("Removing Shape");
                this.setChanged();
            }
        }
    }


    public void infoToPlayer(Player player){
        player.sendMessage(new TextComponent(Integer.toString(shapes.size())), Util.NIL_UUID);
        for(Shape current : shapes){
            player.sendMessage(new TextComponent(current.toString()), Util.NIL_UUID);
        }
    }

    public void serverTick(){
        if(!toCheck.isEmpty()){
            BlockPos current = toCheck.get(0);
            toCheck.remove(current);
            BlockState state = this.getLevel().getBlockState(current);
            if(state.getBlock() instanceof IRitualProvider){
                ritualProviders.add((IRitualProvider) state.getBlock());
                //ExTerra.LOGGER.info("Block at " + current + " has a name of " + ((IRitualProvider) state.getBlock()).getComponentName());
            }
            else if(state.is(ExTerraTags.RITUAL_TIER_I)){
                ExTerra.LOGGER.info("Block at " + current + " is a tier 1 ritual block");
                shapesPos.add(current);
            }
            if(toCheck.isEmpty()) {
                building = false;
                this.setChanged();
            }
        }
        if(!building)
            validateMultiblock();
    }

    public List<IRitualProvider> getRitualProviders() {
        return ritualProviders;
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void setShapes(ShapeCollection shapes){
        this.shapes = shapes.getShapes();
    }

    public void buildRitual(){
        if(!toCheck.isEmpty())
            return;
        shapes.clear();
        shapesPos.clear();
        ritualProviders.clear();
        building = true;
        BlockPos basePos = this.getBlockPos();
        for(int x = -size; x <= size; x++){
            for(int z = -size;z <= size; z++){
                toCheck.add(basePos.north(x).east(z));
            }
        }
    }
}
