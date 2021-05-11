package com.kauruck.exterra.modules;

import com.kauruck.exterra.API.gem.Gem;
import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.block.BlockBase;
import com.kauruck.exterra.tabs.ResourcesTab;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;

/**
 * The module of ExTerra containing everything needed for the gems.
 *
 * @author Kauruck
 */
@SuppressWarnings("unused")
public class ExTerraGems {


    //Tabs
    public static ItemGroup RESOURCE_TAB = new ResourcesTab();

    //Gems
    public static final RegistryObject<Gem> RUBY = ExTerraModules.GEMS.register("ruby", () -> new Gem(new Gem.Properties().setAttackDamageModifier(1)));
    public static final RegistryObject<Gem> SAPPHIRE = ExTerraModules.GEMS.register("sapphire", () -> new Gem(new Gem.Properties().setAttackDamageModifier(2)));

    //Gem items
    public static final RegistryObject<Item> RUBY_GEM = ExTerraModules.ITEMS.register("ruby_gem", () -> new Item(new Item.Properties().group(RESOURCE_TAB)));
    public static final RegistryObject<Item> SAPPHIRE_GEM = ExTerraModules.ITEMS.register("sapphire_gem", () -> new Item(new Item.Properties().group(RESOURCE_TAB)));

    //Blocks for the gems
    public static final RegistryObject<Block> RUBY_BLOCK = ExTerraModules.BLOCKS.register("ruby_block", () -> new BlockBase(5,Material.IRON, ToolType.PICKAXE, 2));
    public static final RegistryObject<Item> RUBY_BLOCK_ITEM = ExTerraModules.ITEMS.register("ruby_block", () -> new BlockItem(RUBY_BLOCK.get(), new Item.Properties().group(RESOURCE_TAB)));
    public static final RegistryObject<Block> SAPPHIRE_BLOCK = ExTerraModules.BLOCKS.register("sapphire_block", () -> new BlockBase(5, Material.IRON, ToolType.PICKAXE, 2));
    public static final RegistryObject<Item> SAPPHIRE_BLOCK_ITEM = ExTerraModules.ITEMS.register("sapphire_block", () -> new BlockItem(SAPPHIRE_BLOCK.get(), new Item.Properties().group(RESOURCE_TAB)));

    //Ores for the gems
    public static final RegistryObject<Block> RUBY_ORE = ExTerraModules.BLOCKS.register("ruby_ore", () -> new BlockBase(3, Material.IRON,  ToolType.PICKAXE, 2));
    public static final RegistryObject<Item> RUBY_ORE_ITEM = ExTerraModules.ITEMS.register("ruby_ore", () -> new BlockItem(RUBY_ORE.get(), new Item.Properties().group(RESOURCE_TAB)));
    public static final RegistryObject<Block> SAPPHIRE_ORE = ExTerraModules.BLOCKS.register("sapphire_ore", () -> new BlockBase(3, Material.IRON,  ToolType.PICKAXE, 2));
    public static final RegistryObject<Item> SAPPHIRE_ORE_ITEM = ExTerraModules.ITEMS.register("sapphire_ore", () -> new BlockItem(SAPPHIRE_ORE.get(), new Item.Properties().group(RESOURCE_TAB)));


}
