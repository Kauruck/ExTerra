package com.kauruck.exterra.modules;

import com.kauruck.exterra.API.gem.Gem;
import com.kauruck.exterra.API.tooltype.Tool;
import com.kauruck.exterra.ExTerraRegistries;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryBuilder;

import static com.kauruck.exterra.ExTerra.MOD_ID;

/**
 * All the registries for ExTerra
 *
 * @author Kauruck
 */
public class ExTerraModules {

    //Objects
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    //World



    //Loot
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> GLOBAL_LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, MOD_ID);


    //Gems 'n more
    public static final DeferredRegister<Gem> GEMS = DeferredRegister.create(Gem.class, MOD_ID);
    public static final DeferredRegister<Tool> TOOLS = DeferredRegister.create(Tool.class, MOD_ID);


    //UI
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, MOD_ID);


    //TileEntity
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MOD_ID);

    public static void createRegistries(){
        ExTerraRegistries.GEM_REGISTRY = GEMS.makeRegistry("gem", ExTerraModules::gemRegistryBuilder);
        ExTerraRegistries.TOOL_REGISTRY = TOOLS.makeRegistry("tool", ExTerraModules::toolRegistryBuilder);
    }

    private static RegistryBuilder<Gem> gemRegistryBuilder(){
        return new RegistryBuilder<>();
    }
    private static RegistryBuilder<Tool> toolRegistryBuilder(){
        return new RegistryBuilder<>();
    }


    public static void register(){
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(bus);
        ITEMS.register(bus);
        GLOBAL_LOOT_MODIFIER_SERIALIZERS.register(bus);
        GEMS.register(bus);
        CONTAINERS.register(bus);
        TILE_ENTITIES.register(bus);
        TOOLS.register(bus);
    }
}
