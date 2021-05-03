package com.kauruck.exterra.modules;

import com.kauruck.exterra.API.item.tool.BaseGemTool;
import com.kauruck.exterra.API.tooltype.Tool;
import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.block.machines.GemWorkbench;
import com.kauruck.exterra.guis.machines.GemWorkbenchContainer;
import com.kauruck.exterra.guis.machines.GemWorkbenchScreen;
import com.kauruck.exterra.item.tools.*;
import com.kauruck.exterra.lootcondtitiontype.ExTerraLootConditionType;
import com.kauruck.exterra.lootmodifiers.Autosmelt;
import com.kauruck.exterra.tabs.ToolsTab;
import com.kauruck.exterra.tileentities.GemWorkbenchTileEntity;
import com.kauruck.exterra.util.ModFactory;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = ExTerra.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
@SuppressWarnings("unused")
public class ExTerraTools {

    //Tabs
    public static ItemGroup TOOLS_TAB = new ToolsTab();

    //Tool items
    public static final RegistryObject<Item> TOOL_PICKAXE_ITEM = ExTerraModules.ITEMS.register("tool_pickaxe", GemPickaxe::new);
    public static final RegistryObject<Item> TOOL_AXE_ITEM = ExTerraModules.ITEMS.register("tool_axe", GemAxe::new);
    public static final RegistryObject<Item> TOOL_SHOVEL_ITEM = ExTerraModules.ITEMS.register("tool_sword", GemSword::new);
    public static final RegistryObject<Item> TOOL_SWORD_ITEM = ExTerraModules.ITEMS.register("tool_shovel", GemShovel::new);
    public static final RegistryObject<Item> TOOL_HOE_ITEM = ExTerraModules.ITEMS.register("tool_hoe", GemHoe::new);

    //Tools
    public static final RegistryObject<Tool> TOOL_PICKAXE = ExTerraModules.TOOLS.register("tooltype_pickaxe", () -> new Tool(() -> (BaseGemTool) TOOL_PICKAXE_ITEM.get(), PickaxeItem.class));
    public static final RegistryObject<Tool> TOOL_AXE = ExTerraModules.TOOLS.register("tooltype_axe", () -> new Tool(() -> (BaseGemTool) TOOL_AXE_ITEM.get(), AxeItem.class));
    public static final RegistryObject<Tool> TOOL_SHOVEL = ExTerraModules.TOOLS.register("tooltype_shovel", () -> new Tool(() -> (BaseGemTool) TOOL_SHOVEL_ITEM.get(), ShovelItem.class));
    public static final RegistryObject<Tool> TOOL_SWORD = ExTerraModules.TOOLS.register("tooltype_sword", () -> new Tool(() -> (BaseGemTool) TOOL_SWORD_ITEM.get(), SwordItem.class));
    public static final RegistryObject<Tool> TOOL_HOE = ExTerraModules.TOOLS.register("tooltype_hoe", () -> new Tool(() -> (BaseGemTool) TOOL_HOE_ITEM.get(), HoeItem.class));

    //Machines
    public static final RegistryObject<Block> GEM_WORKBENCH = ExTerraModules.BLOCKS.register("gem_workbench", () -> new GemWorkbench(3, Material.IRON, ExTerraGems.RESOURCE_TAB, ToolType.PICKAXE, 2));
    public static final RegistryObject<TileEntityType<GemWorkbenchTileEntity>> GEM_WORKBENCH_TILE_ENTITY = ExTerraModules.TILE_ENTITIES.register("gem_workbench" , () -> ModFactory.createTileEntityType(GemWorkbenchTileEntity::new, GEM_WORKBENCH.get()));
    public static final RegistryObject<ContainerType<GemWorkbenchContainer>> GEM_WORKBENCH_CONTAINER = ExTerraModules.CONTAINERS.register("gem_workbench", () -> ModFactory.createContainerType(GemWorkbenchContainer::new, GemWorkbenchScreen::new));

    //Loot modifiers
    public static final RegistryObject<GlobalLootModifierSerializer<Autosmelt>> AUTOSMELT = ExTerraModules.GLOBAL_LOOT_MODIFIER_SERIALIZERS.register("autosmelt", Autosmelt.Serializer::new);



    //Custom loot conditions
    @SubscribeEvent
    public static void registerDataSerializer(FMLCommonSetupEvent event){
        event.enqueueWork(() -> {
            Registry.register(Registry.LOOT_CONDITION_TYPE, ExTerra.getResource("match_gem"), ExTerraLootConditionType.TOOL_CONDITION);
        });
    }



}


