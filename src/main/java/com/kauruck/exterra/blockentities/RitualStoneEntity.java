package com.kauruck.exterra.blockentities;

import com.kauruck.exterra.api.exceptions.UnexpectedBehaviorException;
import com.kauruck.exterra.api.networks.matter.INetworkMemberBlock;
import com.kauruck.exterra.fx.ParticleHelper;
import com.kauruck.exterra.fx.VectorHelper;
import com.kauruck.exterra.geometry.Shape;
import com.kauruck.exterra.geometry.ShapeCollection;
import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.modules.ExTerraTags;
import com.kauruck.exterra.networking.BaseBlockEntity;
import com.kauruck.exterra.networking.BlockEntityProperty;
import com.kauruck.exterra.networks.matter.MatterNetwork;
import com.kauruck.exterra.util.ShapesUtil;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.*;

import static com.kauruck.exterra.fx.MathHelper.clamp;

import static com.kauruck.exterra.networking.BlockEntityPropertySide.*;
public class RitualStoneEntity extends BaseBlockEntity {

    private final BlockEntityProperty<List<BlockPos>> toCheck = createProperty(Server,"toCheck", new ArrayList<>(),  BlockPos.class);
    private final BlockEntityProperty<List<Shape>> shapes = createProperty(Synced, "shapes", new ArrayList<>(), Shape.class);
    private final BlockEntityProperty<MatterNetwork> matterNetwork = createProperty(Server, "network", new MatterNetwork(), MatterNetwork.class);
    private final int size = 5;
    private boolean building = false;
    private final BlockEntityProperty<Boolean> broken = createProperty(Synced, "broken", false);
    private Map<BlockPos, Block> trackingBlock = new HashMap<>();


    public RitualStoneEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ExTerraCore.RITUAL_STONE_ENTITY.get(), pWorldPosition, pBlockState);
    }


    private void validateMultiblock(){
        for(BlockPos pos : trackingBlock.keySet()){
            if(this.getLevel().getBlockState(pos).getBlock() != trackingBlock.get(pos)){
                this.broken.set(true);
                this.setChanged();
                return;
            }
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
    }

    public void infoToPlayer(Player player){
        player.sendSystemMessage(Component.literal(Integer.toString(shapes.get().size())));
        for(Shape current : shapes.get()){
            player.sendSystemMessage(Component.literal(current.toString()));
        }
    }

    public void clientTick(ClientLevel clientLevel){
        if(!broken.get()) {
            if (Minecraft.getInstance().player.getMainHandItem().getItem() == ExTerraCore.RITUAL_LENS.get()) {
                int i = 0;
                int r = 100;
                int g = 100;
                int b = 100;
                int step = (int) (155 * (1f / this.getShapes().size()));
                //Spawn Particles
                for (Shape current : shapes.get()) {
                    List<BlockPos> worldPos = current.getActualPositions(this.getBlockPos());
                    for (int index = 0; index < worldPos.size(); index++) {
                        BlockPos start = worldPos.get(index);
                        BlockPos end;
                        if (index < worldPos.size() - 1) {
                            end = worldPos.get(index + 1);
                        } else {
                            end = worldPos.get(0);
                        }
                        ParticleHelper.emitParticlesOnLine(clientLevel, VectorHelper.blockPosToVector(start), VectorHelper.blockPosToVector(end), new Vector3f(r / 255f, g / 255f, b / 255f), 1F);
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

    //TODO Just leave this hear, as we do need it for the dust lines
    public void animationTick(ClientLevel clientLevel, RandomSource random){

    }

    public void serverTick(){
        if(!broken.get()) {
            validateMultiblock();
            try {
                if (!matterNetwork.get().isLinked())
                    matterNetwork.get().link();
                matterNetwork.get().serverTick();
            }catch (RuntimeException e){
                this.broken.set(true);
            }
        }
        this.setChanged();
    }

    public List<Shape> getShapes() {
        return shapes.get();
    }

    public void setShapes(ShapeCollection shapes){
        this.shapes.set(shapes.getShapes());
        this.setChanged();
    }

    public boolean isBroken(){
        return this.broken.get();
    }


    public void buildRitual() {
        trackingBlock.clear();
        if(matterNetwork.get() == null)
            matterNetwork.set(new MatterNetwork());
        matterNetwork.get().reset();
        building = true;
        BlockPos basePos = this.getBlockPos();
        for (int x = -size; x <= size; x++) {
            for (int z = -size; z <= size; z++) {
                toCheck.get().add(basePos.north(x).east(z));
            }
        }
        //Build the Multiblock
        for (BlockPos current : toCheck.get()) {
            BlockState state = this.getLevel().getBlockState(current);
            if (state.getBlock() instanceof INetworkMemberBlock) {
                try {
                    matterNetwork.get().addVertex(current, this.getLevel());
                } catch (UnexpectedBehaviorException e) {
                    throw new RuntimeException(e);
                }
                trackingBlock.put(current, state.getBlock());
                this.setChanged();
            } else if (state.is(ExTerraTags.RITUAL_TIER_I) && ShapesUtil.containsBlockPos(shapes.get(), current, this.getBlockPos())) {
                trackingBlock.put(current, state.getBlock());
            }
        }
        this.toCheck.get().clear();
        this.broken.set(false);
        this.setChanged();
    }
}
