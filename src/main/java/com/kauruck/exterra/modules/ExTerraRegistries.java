package com.kauruck.exterra.modules;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.matter.Matter;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class ExTerraRegistries {

    public static Supplier<IForgeRegistry<Matter>> MATTER;

    public static void makeRegistries(){
        MATTER = RegistryManger.MATTER_REGISTRY.makeRegistry("matter", () -> new RegistryBuilder<Matter>()
                .setType(Matter.class)
                .setName(ExTerra.getResource("matter"))
        );
    }
}
