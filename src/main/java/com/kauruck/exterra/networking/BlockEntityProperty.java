package com.kauruck.exterra.networking;

import com.kauruck.exterra.api.blockentity.NotIterableInProperty;
import com.kauruck.exterra.api.exceptions.NoCodecException;
import com.kauruck.exterra.modules.ExTerraRegistries;
import com.kauruck.exterra.util.NBTUtil;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;

import java.util.*;

public class BlockEntityProperty<T>{

    private T data;

    private final String name;
    private final Codec<T> codec;
    private final Codec<T> networkCodec;

    private final BlockEntityPropertySide side;
    private boolean shouldBeUpdated = false;
    boolean outOfSync = false;


    public BlockEntityProperty(T data, String name, BlockEntityCodecHolder<T> codecHolder, BlockEntityPropertySide side) {
        this.codec = codecHolder.getCodec();
        this.networkCodec = codecHolder.getNetworkCodec();
        this.name = name;
        this.data = data;
        this.side = side;
    }

    public String getName() {
        return name;
    }

    public Codec<T> getCodec() {
        return codec;
    }

    public void set(T value){
        if(data == value)
            return;
        this.data = value;
    }

    public T get(){
        return this.data;
    }



    private static <K> List<K> toList(Class<K> clazz, Object[] aData){
        List<K> out = new ArrayList<>();
        for(Object current : aData){
            out.add((K) clazz.cast(current));
        }
        return out;
    }

    /**
     * Returns weather the data exists on the Dist dist
     * @param dist The dist
     * @return Weather the data exists
     */
    private boolean isPresent(Dist dist){
        return side != BlockEntityPropertySide.Server || dist == Dist.DEDICATED_SERVER;
    }


    public BlockEntityPropertySide getSide() {
        return side;
    }

    public boolean isOutOfSync(){
        return this.outOfSync;
    }

    public void confirmSend(){
        this.shouldBeUpdated = false;
    }

    public boolean isShouldBeUpdated(){
        return shouldBeUpdated;
    }

    public void markChanged(){
        this.shouldBeUpdated  = true;
    }

    public Tag toTag(boolean isNetwork) {
        DataResult<Tag>  res;
        if (isNetwork) {
            res = this.networkCodec.encodeStart(NbtOps.INSTANCE, this.data);
        } else {
            res = this.codec.encodeStart(NbtOps.INSTANCE, this.data);
        }

        if (res.isError()) {
            throw new IllegalStateException("Error converting a block entity property to a nbt.\n " + res.error().orElse(null));
        }
        return res.result().get();
    }

    public void setFromTag(Tag tag, boolean isNetwork) {
        DataResult<Pair<T, Tag>> res;
        if (isNetwork) {
            res = this.networkCodec.decode(NbtOps.INSTANCE, tag);
        } else {
            res = this.codec.decode(NbtOps.INSTANCE, tag);
        }
        if (res.isError()) {
            throw new IllegalStateException("Error converting a block entity property from a nbt");
        }
        this.data = res.result().get().getFirst();
    }
}
