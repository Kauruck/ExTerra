package com.kauruck.exterra.client;

import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IDynamicBakedModel;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class ConnectedTextureModel implements IDynamicBakedModel {

    private static final FaceBakery BAKERY = new FaceBakery();

    //Why 2 idk
    public static final float BLOCK_UV_DISTANCE = 2f;

    private final Map<Direction, TextureAtlasSprite> textures;

    public ConnectedTextureModel(Map<Direction, TextureAtlasSprite> textures) {
        this.textures = textures;
    }

    @Nonnull
    @Override
    public IModelData getModelData(@Nonnull BlockAndTintGetter world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nonnull IModelData tileData) {
        ConnectedModelData modelData = new ConnectedModelData();
        for(Direction current : Direction.values()){
            modelData.setSide(current, generateSideDataForSide(current, pos,state,world));
        }
        return modelData;
    }

    private ConnectedModelSideData generateSideDataForSide(Direction side, BlockPos pos, BlockState state, BlockAndTintGetter world){

        ConnectedModelSideData data = new ConnectedModelSideData();

        if(side == Direction.UP){

            BlockPos east = pos.east();
            BlockPos west = pos.west();
            BlockPos north = pos.north();
            BlockPos south = pos.south();

            BlockPos east_north = east.north();
            BlockPos west_north = west.north();
            BlockPos east_south = east.south();
            BlockPos west_south = west.south();

            data.down = world.getBlockState(south).getBlock() == state.getBlock();
            data.up = world.getBlockState(north).getBlock() == state.getBlock();
            data.right = world.getBlockState(east).getBlock() == state.getBlock();
            data.left = world.getBlockState(west).getBlock() == state.getBlock();

            data.down_right = world.getBlockState(east_south).getBlock() == state.getBlock();
            data.up_right = world.getBlockState(east_north).getBlock() == state.getBlock();
            data.down_left = world.getBlockState(west_south).getBlock() == state.getBlock();
            data.up_left = world.getBlockState(west_north).getBlock() == state.getBlock();


        }else if(side == Direction.DOWN){

            BlockPos east = pos.east();
            BlockPos west = pos.west();
            BlockPos north = pos.north();
            BlockPos south = pos.south();

            BlockPos east_north = east.north();
            BlockPos west_north = west.north();
            BlockPos east_south = east.south();
            BlockPos west_south = west.south();

            data.up = world.getBlockState(south).getBlock() == state.getBlock();
            data.down = world.getBlockState(north).getBlock() == state.getBlock();
            data.right = world.getBlockState(east).getBlock() == state.getBlock();
            data.left = world.getBlockState(west).getBlock() == state.getBlock();

            data.up_right = world.getBlockState(east_south).getBlock() == state.getBlock();
            data.down_right = world.getBlockState(east_north).getBlock() == state.getBlock();
            data.up_left = world.getBlockState(west_south).getBlock() == state.getBlock();
            data.down_left = world.getBlockState(west_north).getBlock() == state.getBlock();


        } else if(side == Direction.NORTH){

            BlockPos east = pos.east();
            BlockPos west = pos.west();
            BlockPos up = pos.above();
            BlockPos down = pos.below();

            BlockPos east_up = east.above();
            BlockPos west_up = west.above();
            BlockPos east_down= east.below();
            BlockPos west_down = west.below();

            data.up = world.getBlockState(up).getBlock() == state.getBlock();
            data.down = world.getBlockState(down).getBlock() == state.getBlock();
            data.left = world.getBlockState(east).getBlock() == state.getBlock();
            data.right = world.getBlockState(west).getBlock() == state.getBlock();

            data.up_left = world.getBlockState(east_up).getBlock() == state.getBlock();
            data.down_left = world.getBlockState(east_down).getBlock() == state.getBlock();
            data.up_right = world.getBlockState(west_up).getBlock() == state.getBlock();
            data.down_right = world.getBlockState(west_down).getBlock() == state.getBlock();

        }else if(side == Direction.SOUTH){

            BlockPos east = pos.east();
            BlockPos west = pos.west();
            BlockPos up = pos.above();
            BlockPos down = pos.below();

            BlockPos east_up = east.above();
            BlockPos west_up = west.above();
            BlockPos east_down= east.below();
            BlockPos west_down = west.below();

            data.up = world.getBlockState(up).getBlock() == state.getBlock();
            data.down = world.getBlockState(down).getBlock() == state.getBlock();
            data.right = world.getBlockState(east).getBlock() == state.getBlock();
            data.left = world.getBlockState(west).getBlock() == state.getBlock();

            data.up_left = world.getBlockState(west_up).getBlock() == state.getBlock();
            data.down_left = world.getBlockState(west_down).getBlock() == state.getBlock();
            data.up_right = world.getBlockState(east_up).getBlock() == state.getBlock();
            data.down_right = world.getBlockState(east_down).getBlock() == state.getBlock();

        } else if(side == Direction.WEST){

            BlockPos north = pos.north();
            BlockPos south = pos.south();
            BlockPos up = pos.above();
            BlockPos down = pos.below();

            BlockPos north_up = north.above();
            BlockPos south_up = south.above();
            BlockPos north_down= north.below();
            BlockPos south_down = south.below();

            data.up = world.getBlockState(up).getBlock() == state.getBlock();
            data.down = world.getBlockState(down).getBlock() == state.getBlock();
            data.left = world.getBlockState(north).getBlock() == state.getBlock();
            data.right = world.getBlockState(south).getBlock() == state.getBlock();

            data.up_left = world.getBlockState(north_up).getBlock() == state.getBlock();
            data.down_left = world.getBlockState(north_down).getBlock() == state.getBlock();
            data.up_right = world.getBlockState(south_up).getBlock() == state.getBlock();
            data.down_right = world.getBlockState(south_down).getBlock() == state.getBlock();

        }else if(side == Direction.EAST){

            BlockPos north = pos.north();
            BlockPos south = pos.south();
            BlockPos up = pos.above();
            BlockPos down = pos.below();

            BlockPos north_up = north.above();
            BlockPos south_up = south.above();
            BlockPos north_down= north.below();
            BlockPos south_down = south.below();

            data.up = world.getBlockState(up).getBlock() == state.getBlock();
            data.down = world.getBlockState(down).getBlock() == state.getBlock();
            data.right = world.getBlockState(north).getBlock() == state.getBlock();
            data.left = world.getBlockState(south).getBlock() == state.getBlock();

            data.up_left = world.getBlockState(south_up).getBlock() == state.getBlock();
            data.down_left = world.getBlockState(south_down).getBlock() == state.getBlock();
            data.up_right = world.getBlockState(north_up).getBlock() == state.getBlock();
            data.down_right = world.getBlockState(north_down).getBlock() == state.getBlock();

        }

        return data;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        if(side == null)
            return Collections.emptyList();

        return Collections.singletonList(this.createQuadForSide(side,extraData));
    }

    protected BakedQuad createQuadForSide(Direction side, IModelData data){
        BlockElementFace face = new BlockElementFace(side, 0, "", new BlockFaceUV(this.getUV(side, data), 0));
        BakedQuad quad = BAKERY.bakeQuad(new Vector3f(0,0,0), new Vector3f(16,16,16), face, getTexture(side), side, BlockModelRotation.X0_Y0, null, true, null);
        return quad;
    }

    protected TextureAtlasSprite getTexture(Direction side){
        return textures.get(side);
    }

    protected float[] getUV(Direction side, IModelData modelData){
        if(!(modelData instanceof ConnectedModelData))
            return getUVForPosition(0,0);
        ConnectedModelSideData data = ((ConnectedModelData) modelData).getSide(side);

        if(!data.up && !data.down && !data.left && !data.right)
            return getUVForPosition(0,0);

        if(!data.up && data.down && !data.left && !data.right)
            return getUVForPosition(0,1);

        if(data.up && data.down && !data.left && !data.right)
            return getUVForPosition(0,2);

        if(data.up && !data.down && !data.left && !data.right)
            return getUVForPosition(0,3);

        if(!data.up && data.down && !data.left && data.right && data.down_right)
            return getUVForPosition(1,0);

        if(data.up && data.down && !data.left && data.right && data.up_right && data.down_right)
            return getUVForPosition(1,1);

        if(data.up && !data.down && !data.left && data.right && data.up_right)
            return getUVForPosition(1,2);

        if(!data.up && !data.down && !data.left && data.right)
            return getUVForPosition(1,3);

        if(!data.up && data.down && data.left && data.right && data.down_left && data.down_right)
            return getUVForPosition(2,0);

        if(data.up && data.down && data.left && data.right && data.up_right && data.up_left && data.down_left && data.down_right)
            return getUVForPosition(2,1);

        if(data.up && !data.down && data.left && data.right && data.up_left && data.up_right)
            return getUVForPosition(2,2);

        if(!data.up && !data.down && data.left && data.right)
            return getUVForPosition(2,3);

        if(!data.up && data.down && data.left && !data.right && data.down_left)
            return getUVForPosition(3,0);

        if(data.up && data.down && data.left && !data.right && data.up_left && data.down_left)
            return getUVForPosition(3,1);

        if(data.up && !data.down && data.left && !data.right && data.up_left)
            return getUVForPosition(3,2);

        if(!data.up && !data.down && data.left && !data.right)
            return getUVForPosition(3,3);

        if(!data.up && data.down && !data.left && data.right && !data.down_right)
            return getUVForPosition(4,0);

        if(data.up && !data.down && !data.left && data.right && !data.up_right)
            return getUVForPosition(4,1);

        if(!data.up && data.down && data.left && !data.right && !data.down_left)
            return getUVForPosition(5,0);

        if(data.up && !data.down && data.left && !data.right && !data.up_left)
            return getUVForPosition(5,1);

        if(data.up && data.down && data.left && data.right && !data.down_left && !data.down_right && !data.up_right && !data.up_left)
            return getUVForPosition(4,2);

        if(data.up && !data.down && data.left && data.right && !data.up_left && !data.up_right)
            return getUVForPosition(6,0);

        if(data.up && data.down && !data.left && data.right && !data.down_right && !data.up_right)
            return getUVForPosition(6,1);

        if(!data.up && data.down && data.left && data.right && !data.down_left && !data.down_right)
            return getUVForPosition(6,2);

        if(data.up && data.down && data.left && !data.right && !data.up_left && !data.down_left)
            return getUVForPosition(6,3);


        if(data.up && data.down && data.left && data.right && data.up_left && !data.up_right && data.down_left && data.down_right)
            return getUVForPosition(0,4);

        if(data.up && data.down && data.left && data.right && data.up_left && data.up_right && data.down_left && !data.down_right)
            return getUVForPosition(1,4);

        if(data.up && data.down && data.left && data.right && data.up_left && data.up_right && !data.down_left && data.down_right)
            return getUVForPosition(2,4);

        if(data.up && data.down && data.left && data.right && !data.up_left && data.up_right && data.down_left && data.down_right)
            return getUVForPosition(3,4);


        if(data.up && data.down && data.left && data.right && !data.up_left && !data.up_right && data.down_left && data.down_right)
            return getUVForPosition(0,5);

        if(data.up && data.down && data.left && data.right && data.up_left && !data.up_right && data.down_left && !data.down_right)
            return getUVForPosition(1,5);

        if(data.up && data.down && data.left && data.right && data.up_left && data.up_right && !data.down_left && !data.down_right)
            return getUVForPosition(2,5);

        if(data.up && data.down && data.left && data.right && !data.up_left && data.up_right && !data.down_left && data.down_right)
            return getUVForPosition(3,5);


        if(data.up && data.down && data.left && data.right && !data.up_left && !data.up_right && data.down_left && !data.down_right)
            return getUVForPosition(0,6);

        if(data.up && data.down && data.left && data.right && data.up_left && !data.up_right && !data.down_left && !data.down_right)
            return getUVForPosition(1,6);

        if(data.up && data.down && data.left && data.right && !data.up_left && data.up_right && !data.down_left && !data.down_right)
            return getUVForPosition(2,6);

        if(data.up && data.down && data.left && data.right && !data.up_left && !data.up_right && !data.down_left && data.down_right)
            return getUVForPosition(3,6);


        if(data.up && data.down && data.left && data.right && data.up_left && !data.up_right && !data.down_left && data.down_right)
            return getUVForPosition(0,7);

        if(data.up && data.down && data.left && data.right && !data.up_left && data.up_right && data.down_left && !data.down_right)
            return getUVForPosition(1,7);


        if(data.up && data.down && !data.left && data.right && !data.up_right && data.down_right)
            return getUVForPosition(7,0);

        if(data.up && data.down && !data.left && data.right && data.up_right && !data.down_right)
            return getUVForPosition(7,1);

        if(!data.up && data.down && data.left && data.right && !data.down_left && data.down_right)
            return getUVForPosition(7,2);

        if(!data.up && data.down && data.left && data.right && data.down_left && !data.down_right)
            return getUVForPosition(7,3);


        if(data.up && data.down && data.left && !data.right && !data.up_left && data.down_left)
            return getUVForPosition(7,4);

        if(data.up && data.down && data.left && !data.right && data.up_left && !data.down_left)
            return getUVForPosition(7,5);

        if(data.up && !data.down && data.left && data.right && !data.up_left && data.up_right)
            return getUVForPosition(7,6);

        if(data.up && !data.down && data.left && data.right && data.up_left && !data.up_right)
            return getUVForPosition(7,7);

        return getUVForPosition(0,0);
    }

    @Override
    public ItemTransforms getTransforms() {
        return Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(Blocks.STONE.getRegistryName(), "")).getTransforms();
    }

    private float[] getUVForPosition(int x, int y){
        return new float[]{BLOCK_UV_DISTANCE * x,BLOCK_UV_DISTANCE * y, BLOCK_UV_DISTANCE + BLOCK_UV_DISTANCE * x, BLOCK_UV_DISTANCE + BLOCK_UV_DISTANCE * y};
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean usesBlockLight() {
        return true;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.getTexture(Direction.NORTH);
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    public static class ConnectedModelData implements IModelData {

        public Map<Direction, ConnectedModelSideData> sideDatas = new HashMap<>();

        public void setSide(Direction side, ConnectedModelSideData sideData){
            this.sideDatas.put(side, sideData);
        }

        public ConnectedModelSideData getSide(Direction side){
            return this.sideDatas.get(side);
        }

        @Override
        public boolean hasProperty(ModelProperty<?> prop) {
            return false;
        }

        @Nullable
        @Override
        public <T> T getData(ModelProperty<T> prop) {
            return null;
        }

        @Nullable
        @Override
        public <T> T setData(ModelProperty<T> prop, T data) {
            return null;
        }
    }

    public static class ConnectedModelSideData {
        public boolean left;
        public boolean right;
        public boolean up;
        public boolean up_left;
        public boolean up_right;
        public boolean down;
        public boolean down_left;
        public boolean down_right;

    }
}
