package com.kauruck.exterra.modules;

import com.kauruck.exterra.blocks.controllers.Generator;
import com.kauruck.exterra.blocks.controllers.GeneratorBlockEntity;
import com.kauruck.exterra.containers.GeneratorContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fmllegacy.RegistryObject;

import static com.kauruck.exterra.modules.RegistryManger.*;

public class ExTerraPower {

    //Generator
    public static final RegistryObject<Block> GENERATOR = BLOCK_REGISTRY.register("generator", Generator::new);
    public static final RegistryObject<BlockItem> GENERATOR_ITEM = ITEM_REGISTRY.register("generator", () -> new BlockItem(GENERATOR.get(), new Item.Properties().tab(CreativeModeTab.TAB_MISC)));
    public static final RegistryObject<BlockEntityType<GeneratorBlockEntity>> GENERATOR_BLOCK_ENTITY = BLOCK_ENTITY_REGISTRY.register("generator", () -> BlockEntityType.Builder.of(GeneratorBlockEntity::new, GENERATOR.get()).build(null));
    public static final RegistryObject<MenuType<GeneratorContainer>> GENERATOR_CONTAINER = CONTAINER_REGISTRY.register("generator", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        Level world = inv.player.getCommandSenderWorld();
        return new GeneratorContainer(windowId, world, pos, inv, inv.player);
    }));

}
