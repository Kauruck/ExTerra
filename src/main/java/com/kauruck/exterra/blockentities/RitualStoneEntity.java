package com.kauruck.exterra.blockentities;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.exceptions.UnexpectedBehaviorException;
import com.kauruck.exterra.fx.ParticleHelper;
import com.kauruck.exterra.fx.VectorHelper;
import com.kauruck.exterra.geometry.Shape;
import com.kauruck.exterra.modules.ExTerraCore;
import com.kauruck.exterra.modules.NetworkInbuilt;
import com.kauruck.exterra.networking.BaseBlockEntity;
import com.kauruck.exterra.networking.BlockEntityProperty;
import com.kauruck.exterra.networks.matter.Grid;
import com.kauruck.exterra.networks.matter.GridScanner;
import com.kauruck.exterra.networks.matter.MatterNetwork;
import com.kauruck.exterra.networks.matter.Wire;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3f;

import java.util.*;

import static com.kauruck.exterra.fx.MathHelper.clamp;

import static com.kauruck.exterra.networking.BlockEntityPropertySide.*;
public class RitualStoneEntity extends BaseBlockEntity {

    private final BlockEntityProperty<List<Shape>> shapes = createProperty(Requestable, "shapes", new ArrayList<>(), ExTerraCore.PROPERTY_SHAPE_LIST.get());
    private final BlockEntityProperty<MatterNetwork> matterNetwork = createProperty(Requestable, "network", new MatterNetwork(), ExTerraCore.PROPERTY_MATTER_NETWORK.get());
    public static final int SIZE = 5;
    private final BlockEntityProperty<Boolean> broken = createProperty(Synced, "broken", false, NetworkInbuilt.PROPERTY_BOOLEAN.get());
    private final BlockEntityProperty<Map<BlockPos, Block>> trackingBlock = createProperty(Server, "tracking_blocks", new HashMap<>(), ExTerraCore.PROPERTY_MULTIBLOCK_ELEMENTS.get());

    private int validationCooldown = 0;

    public RitualStoneEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(ExTerraCore.RITUAL_STONE_ENTITY.get(), pWorldPosition, pBlockState);
    }


    private void validateMultiblock(){
        for(BlockPos pos : trackingBlock.get().keySet()){
            if(this.getLevel().getBlockState(pos).getBlock() != trackingBlock.get().get(pos)){
                this.broken.set(true);
                this.broken.markChanged();
                this.setChanged();
                return;
            }
        }
        this.broken.set(false);
        this.broken.markChanged();
        this.setChanged();
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
                        ParticleHelper.emitParticlesOnLine(clientLevel, VectorHelper.blockPosToVector(start),
                                VectorHelper.blockPosToVector(end), new Vector3f(r / 255f, g / 255f, b / 255f), 1F);
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

    public void animationTick(ClientLevel clientLevel, RandomSource random){
        matterNetwork.get().animationsTick(clientLevel, random);
    }

    public void serverTick(){
        if (validationCooldown == 0) {
            validateMultiblock();
            validationCooldown = 20;
        } else {
            validationCooldown--;
        }
        if(!broken.get()) {
            try {
                if (!matterNetwork.get().isLinked()) {
                    this.matterNetwork.get().setShapeGetter(this::getShapes);
                    matterNetwork.get().link(this.getLevel());
                } else { // Wait for network to link
                    matterNetwork.get().serverTick();
                    matterNetwork.markChanged();
                }
            }catch (RuntimeException e){
                //Something went wrong in the network
                this.broken.set(true);
                this.broken.markChanged();
                ExTerra.LOGGER.debug("Ritual Stone Block: Network Exception", e);
            }
        }
        this.setChanged();
    }

    public List<Shape> getShapes() {
        return shapes.get();
    }

    public boolean isBroken(){
        return this.broken.get();
    }

    private void addBlocksToTracking(List<BlockPos> poses) {
        for (BlockPos current : poses) {
            if (!this.trackingBlock.get().containsKey(current))
                this.trackingBlock.get().put(current, level.getBlockState(current).getBlock());
        }
        this.trackingBlock.markChanged();
    }


    public void buildRitual(List<Shape> shapes){
        trackingBlock.get().clear();
        this.shapes.set(shapes);
        this.setChanged();
        this.addBlocksToTracking(shapes.stream().flatMap(s -> s.getActualPositions(this.getBlockPos()).stream()).toList());
        Grid grid = GridScanner.ScanGrid(this.getBlockPos(), SIZE, this.getLevel());
        matterNetwork.set(new MatterNetwork());
        List<BlockPos> vertices = GridScanner.scanGridForMembers(grid);

        this.addBlocksToTracking(vertices);
        try {
            matterNetwork.get().addRangeVertices(vertices, level);
        } catch (UnexpectedBehaviorException e) {
            throw new RuntimeException(e);
        }
        HashSet<Wire> wires = GridScanner.scanGridForWires(grid);
        this.addBlocksToTracking(wires.stream().flatMap(w -> w.getPositions().stream().map(Pair::getFirst)).toList());
        ExTerra.LOGGER.info(wires);
        this.matterNetwork.get().setShapeGetter(this::getShapes);
        this.matterNetwork.get().addRangeEdge(wires);
        this.broken.set(false);
        this.broken.markChanged();
        this.shapes.markChanged();
        this.setChanged();
    }
}
