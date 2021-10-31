package com.kauruck.exterra.client;

import com.kauruck.exterra.ExTerra;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.client.model.generators.loaders.DynamicBucketModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ConnectedTextureLoader<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

    public static <T extends ModelBuilder<T>> ConnectedTextureLoader<T> begin(T parent, ExistingFileHelper existingFileHelper)
    {
        return new ConnectedTextureLoader<>(parent, existingFileHelper);
    }

    public ConnectedTextureLoader(T parent, ExistingFileHelper existingFileHelper){
        super(ExTerra.getResource("connected_textures"), parent, existingFileHelper);
    }
}
