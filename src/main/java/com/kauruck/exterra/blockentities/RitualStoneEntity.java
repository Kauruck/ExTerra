package com.kauruck.exterra.blockentities;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.fx.ParticleHelper;
import com.kauruck.exterra.fx.VectorHelper;
import com.kauruck.exterra.geometry.Shape;
import com.kauruck.exterra.api.rituals.IRitualProvider;
import com.kauruck.exterra.geometry.ShapeCollection;
import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.modules.ExTerraTags;
import com.kauruck.exterra.util.NBTUtil;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.kauruck.exterra.fx.MathHelper.clamp;

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
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("shapes", new ShapeCollection(shapes).toNBT());
        tag.put("shapesPositions", NBTUtil.blockPosListToNBT(shapesPos));
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
                //TODO Send packet
                this.setChanged();
            }
        }
    }


    public void infoToPlayer(Player player){
        player.sendSystemMessage(Component.literal(Integer.toString(shapes.size())));
        for(Shape current : shapes){
            player.sendSystemMessage(Component.literal(current.toString()));
        }
    }

    public void clientTick(ClientLevel clientLevel){
        if(Minecraft.getInstance().player.getMainHandItem().getItem() == ExTerraCore.DEBUG_LENS.get()) {
            int i = 0;
            int r = 100;
            int g = 100;
            int b = 100;
            int step = (int) (155 * (1f/this.getShapes().size()));
            //Spawn Particles
            for (Shape current : shapes) {
                List<BlockPos> worldPos = current.getActualPositions(this.getBlockPos());
                for (int index = 0; index < worldPos.size(); index++) {
                    BlockPos start = worldPos.get(index);
                    BlockPos end;
                    if (index < worldPos.size() - 1) {
                        end = worldPos.get(index + 1);
                    } else {
                        end = worldPos.get(0);
                    }
                    ParticleHelper.emitParticlesOnLine(clientLevel, VectorHelper.blockPosToVector(start), VectorHelper.blockPosToVector(end), new Vector3f(r/255f, g/255f, b/255f), 1F);
                }
                switch (i%3){
                    case 0: r += step;
                    case 1: g += step;
                    case 2: b += step;
                }
                r = clamp(r, 0, 255);
                g = clamp(g, 0, 255);
                b = clamp(b, 0, 255);
                i++;
            }
        }
    }

    //TODO Just leave this hear, as we do need it for the dust lines
    public void animationTick(ClientLevel clientLevel, RandomSource random){

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

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag out = super.getUpdateTag();
        out.put("shapes", new ShapeCollection(this.shapes).toNBT());
        return out;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        ShapeCollection collection = new ShapeCollection(tag.getCompound("shapes"));
        this.shapes = collection.getShapes();
    }

    public List<IRitualProvider> getRitualProviders() {
        return ritualProviders;
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void setShapes(ShapeCollection shapes){
        this.shapes = shapes.getShapes();
        this.setChanged();
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
