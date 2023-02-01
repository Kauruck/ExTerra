package com.kauruck.exterra.datagenenerators;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.client.model.ConnectedTextureLoader;
import com.kauruck.exterra.modules.ExTerraCore;
import net.minecraft.core.Direction;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;


public class BlockStates extends BlockStateProvider {

    private final ExistingFileHelper existingFileHelper;
    public BlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, ExTerra.MOD_ID, exFileHelper);
        existingFileHelper = exFileHelper;
    }

    @Override
    protected void registerStatesAndModels() {
        this.simpleBlock(ExTerraCore.COMPOUND_BRICKS.get());
        this.stairsBlock((StairBlock) ExTerraCore.COMPOUND_BRICKS_STAIR.get(), ExTerra.getResource("block/compound_bricks"));
        this.slabBlock((SlabBlock) ExTerraCore.COMPOUND_BRICKS_SLAB.get(), ExTerra.getResource("block/compound_bricks"), ExTerra.getResource("block/compound_bricks"));
        this.connectedTextureGlass(ExTerraCore.COMPOUND_FRAMED_GLASS.get(), ExTerra.getResource("block/compound_framed_glass"));
        this.dustBlock(ExTerraCore.CALCITE_DUST.get(), new ResourceLocation("block/redstone_dust_dot"),new ResourceLocation("block/redstone_dust_line0") , new ResourceLocation("block/redstone_dust_line1"),new ResourceLocation("block/redstone_dust_overlay"));
        this.plateBlock(ExTerraCore.RITUAL_STONE.get(), ExTerra.getResource("block/ritual_slab_top"), new ResourceLocation("minecraft","block/smooth_stone"));
        this.plateBlock(ExTerraCore.RECEIVER_BLOCK.get(), ExTerra.getResource("block/receiver_slab_top"), new ResourceLocation("minecraft","block/smooth_stone"));
        this.plateBlock(ExTerraCore.EMITTER_BLOCK.get(), ExTerra.getResource("block/emitter_slab_top"), new ResourceLocation("minecraft", "block/smooth_stone"));
    }


    private void plateBlock(Block block, ResourceLocation topTexture, ResourceLocation slabTexture){
        BlockModelBuilder builder = this.models()
                .withExistingParent(getPathOf(block), ExTerra.getResource("block/plate_block"))
                .texture("slab", slabTexture)
                .texture("top", topTexture)
                .texture("particle", slabTexture);
        this.getVariantBuilder(block)
                .partialState()
                .setModels(new ConfiguredModel(builder));
    }

    private void connectedTextureGlass(Block block, ResourceLocation allTextures){
        BlockModelBuilder model = this.models()
                .withExistingParent(ForgeRegistries.BLOCKS.getKey(block).getPath(), "cube_all")
                .texture("all", allTextures)
                .renderType(new ResourceLocation("translucent"));
        model.customLoader(ConnectedTextureLoader::begin);
        this.getVariantBuilder(block)
                .partialState()
                .setModels(new ConfiguredModel(model));
    }


    private void registerMachineBlock(Block block, ResourceLocation side, ResourceLocation front_off, ResourceLocation front_on) {
        BlockModelBuilder modelOff= models().cube(ForgeRegistries.BLOCKS.getKey(block).getPath(),
                side, side, front_off, side, side, side);
        BlockModelBuilder modelOn = models().cube(ForgeRegistries.BLOCKS.getKey(block).getPath()+ "_powered",
                side, side, front_on, side, side, side);
        orientedBlock(block, state -> {
            if (state.getValue(BlockStateProperties.POWERED)) {
                return modelOn;
            } else {
                return modelOff;
            }
        });
    }

    private void orientedBlock(Block block, Function<BlockState, ModelFile> modelFunc) {
        getVariantBuilder(block)
                .forAllStates(state -> {
                    Direction dir = state.getValue(BlockStateProperties.FACING);
                    return ConfiguredModel.builder()
                            .modelFile(modelFunc.apply(state))
                            .rotationX(dir.getAxis() == Direction.Axis.Y ?  dir.getAxisDirection().getStep() * -90 : 0)
                            .rotationY(dir.getAxis() != Direction.Axis.Y ? ((dir.get2DDataValue() + 2) % 4) * 90 : 0)
                            .build();
                });
    }

    private String getPathOf(Block block){
        return ForgeRegistries.BLOCKS.getKey(block).getPath();
    }

    private void dustBlock(Block block, ResourceLocation dot, ResourceLocation line0,  ResourceLocation line1, ResourceLocation overlay){
        String baseName = ForgeRegistries.BLOCKS.getKey(block).getPath();
        getMultipartBuilder(block)
                .part()
                .modelFile(dotModel( baseName + "_dot", dot, overlay))
                .addModel()
                .useOr()
                .nestedGroup()
                .condition(BlockStateProperties.NORTH_REDSTONE, RedstoneSide.NONE)
                .condition(BlockStateProperties.EAST_REDSTONE, RedstoneSide.NONE)
                .condition(BlockStateProperties.SOUTH_REDSTONE, RedstoneSide.NONE)
                .condition(BlockStateProperties.WEST_REDSTONE, RedstoneSide.NONE)
                .end()
                .nestedGroup()
                .condition(BlockStateProperties.NORTH_REDSTONE, RedstoneSide.SIDE, RedstoneSide.UP)
                .condition(BlockStateProperties.EAST_REDSTONE,  RedstoneSide.SIDE, RedstoneSide.UP)
                .end()
                .nestedGroup()
                .condition(BlockStateProperties.SOUTH_REDSTONE, RedstoneSide.SIDE, RedstoneSide.UP)
                .condition(BlockStateProperties.EAST_REDSTONE,  RedstoneSide.SIDE, RedstoneSide.UP)
                .end()
                .nestedGroup()
                .condition(BlockStateProperties.SOUTH_REDSTONE, RedstoneSide.SIDE, RedstoneSide.UP)
                .condition(BlockStateProperties.WEST_REDSTONE,  RedstoneSide.SIDE, RedstoneSide.UP)
                .end()
                .nestedGroup()
                .condition(BlockStateProperties.WEST_REDSTONE, RedstoneSide.SIDE, RedstoneSide.UP)
                .condition(BlockStateProperties.NORTH_REDSTONE,  RedstoneSide.SIDE, RedstoneSide.UP)
                .end()
                .end()
                .part()
                .modelFile(models().withExistingParent(baseName + "_side0", "block/redstone_dust_side")
                        .texture("line", line0))
                .addModel()
                .condition(BlockStateProperties.NORTH_REDSTONE, RedstoneSide.UP, RedstoneSide.SIDE)
                .end()
                .part()
                .modelFile(models().withExistingParent(baseName + "_side_alt0", "block/redstone_dust_side_alt")
                        .texture("line", line0))
                .addModel()
                .condition(BlockStateProperties.SOUTH_REDSTONE, RedstoneSide.UP, RedstoneSide.SIDE)
                .end()
                .part()
                .modelFile(models().withExistingParent(baseName + "_side_alt1", "block/redstone_dust_side_alt")
                        .texture("line", line1))
                .rotationY(270)
                .addModel()
                .condition(BlockStateProperties.EAST_REDSTONE, RedstoneSide.UP, RedstoneSide.SIDE)
                .end()
                .part()
                .modelFile(models().withExistingParent(baseName + "_side1", "block/redstone_dust_side")
                        .texture("line", line1))
                .rotationY(270)
                .addModel()
                .condition(BlockStateProperties.WEST_REDSTONE, RedstoneSide.UP, RedstoneSide.SIDE)
                .end()
                .part()
                .modelFile(upModel(baseName + "_up", line0, overlay))
                .addModel()
                .condition(BlockStateProperties.NORTH_REDSTONE, RedstoneSide.UP)
                .end()
                .part()
                .modelFile(upModel(baseName + "_up", line0, overlay))
                .rotationY(90)
                .addModel()
                .condition(BlockStateProperties.EAST_REDSTONE, RedstoneSide.UP)
                .end()
                .part()
                .modelFile(upModel(baseName + "_up", line0, overlay))
                .rotationY(180)
                .addModel()
                .condition(BlockStateProperties.SOUTH_REDSTONE, RedstoneSide.UP)
                .end()
                .part()
                .modelFile(upModel(baseName + "_up", line0, overlay))
                .rotationY(270)
                .addModel()
                .condition(BlockStateProperties.WEST_REDSTONE, RedstoneSide.UP)
                .end();

    }

    private BlockModelBuilder upModel(String name, ResourceLocation line, ResourceLocation overlay){
        return models()
                .getBuilder(name)
                .ao(false)
                .texture("line", line)
                .texture("overlay", overlay)
                .texture("particle", line)
                .element()
                .from( 0, 0, 0.25f)
                .to(16, 16, 0.25f)
                .shade(false)
                .face(Direction.UP)
                .texture("#line")
                .uvs(0, 0, 16, 16)
                .tintindex(0)
                .end()
                .face(Direction.DOWN)
                .texture("#line")
                .uvs(0, 16, 16, 0)
                .tintindex(0)
                .end().end()
                .element()
                .from( 0, 0, 0.25f)
                .to(16, 16, 0.25f)
                .shade(false)
                .face(Direction.UP)
                .texture("#overlay")
                .uvs(0, 0, 16, 16)
                .tintindex(0)
                .end()
                .face(Direction.DOWN)
                .texture("#overlay")
                .uvs(0, 16, 16, 0)
                .tintindex(0)
                .end().end();
    }

    private BlockModelBuilder dotModel(String name, ResourceLocation dot, ResourceLocation overlay){
        return models()
                .getBuilder(name)
                .ao(false)
                .texture("line", dot)
                .texture("overlay", overlay)
                .texture("particle", dot)
                .element()
                .from( 0, 0.25f, 0)
                .to(16, 0.25f, 16)
                .shade(false)
                .face(Direction.UP)
                .texture("#line")
                .uvs(0, 0, 16, 16)
                .tintindex(0)
                .end()
                .face(Direction.DOWN)
                .texture("#line")
                .uvs(0, 16, 16, 0)
                .tintindex(0)
                .end().end()
                .element()
                .from( 0, 0.25f, 0)
                .to(16, 0.25f, 16)
                .shade(false)
                .face(Direction.UP)
                .texture("#overlay")
                .uvs(0, 0, 16, 16)
                .tintindex(0)
                .end()
                .face(Direction.DOWN)
                .texture("#overlay")
                .uvs(0, 16, 16, 0)
                .tintindex(0)
                .end().end();
    }
}
