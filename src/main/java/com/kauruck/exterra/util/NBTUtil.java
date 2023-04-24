package com.kauruck.exterra.util;

import com.google.common.collect.Sets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class NBTUtil {

    public static CompoundTag compoundTagArrayToCompoundTag(CompoundTag[] tags){
        CompoundTag out = new CompoundTag();
        out.putInt("length", tags.length);
        for(int i = 0; i < tags.length; i++){
            out.put(Integer.toString(i), tags[i]);
        }
        return out;
    }

    public static CompoundTag blockPosMapToNBT(Map<BlockPos, Block> map){
        CompoundTag out = new CompoundTag();
        int i = 0;
        for(BlockPos key : map.keySet()){
            CompoundTag keyNBT = NBTUtil.blockPosToNBT(key);
            String resourceString = ForgeRegistries.BLOCKS.getKey(map.get(key)).toString();
            CompoundTag entry = new CompoundTag();
            entry.put("key", keyNBT);
            entry.putString("value", resourceString);
            out.put(Integer.toString(i), entry);
            i++;
        }
        out.putInt("length", i);
        return out;
    }

    public static Map<BlockPos, Block> blockPosBlockMapFromNBT(CompoundTag tag){
        int length = tag.getInt("Length");
        Map<BlockPos, Block> out = new HashMap<>();
        for(int i = 0; i < length; i++){
            CompoundTag entry = tag.getCompound(Integer.toString(i));
            BlockPos pos = NBTUtil.blockPosFromNBT(entry.getCompound("key"));
            Block block = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(entry.getString("value")));
            out.put(pos, block);
        }
        return out;
    }

    public static CompoundTag[] compoundTagToCompoundTagArray(CompoundTag tag){
        int length = tag.getInt("length");
        CompoundTag[] tags = new CompoundTag[length];
        for(int i = 0; i < length; i++){
            tags[i] = tag.getCompound(Integer.toString(i));
        }
        return tags;
    }


    public static  CompoundTag LevelToNBT(Level level){
        ResourceKey<Level> key = level.dimension();
        CompoundTag tag = new CompoundTag();
        tag.putString("location_namespace", key.location().getNamespace());
        tag.putString("location_path", key.location().getPath());
        tag.putString("registry_namespace", key.registry().getNamespace());
        tag.putString("registry_path", key.registry().getPath());
        return tag;
    }

    public static Level levelFromNBT(CompoundTag tag){
        String locationNamespace = tag.getString("location_namespace");
        String locationPath = tag.getString("location_path");
        String registryNamespace = tag.getString("registry_namespace");
        String registryPath = tag.getString("registry_path");
        ResourceLocation location = new ResourceLocation(locationNamespace, locationPath);
        ResourceLocation registry = new ResourceLocation(registryNamespace, registryPath);
        ResourceKey<? extends Registry<Level>> registryKey = ResourceKey.createRegistryKey(registry);
        ResourceKey<Level> key = ResourceKey.create(registryKey, location);
        return ServerLifecycleHooks.getCurrentServer().getLevel(key);
    }

    public static CompoundTag blockPosToNBT(BlockPos blockPos){
        CompoundTag tag = new CompoundTag();
        tag.putInt("x", blockPos.getX());
        tag.putInt("y", blockPos.getY());
        tag.putInt("z", blockPos.getZ());
        return tag;
    }

    public static BlockPos blockPosFromNBT(CompoundTag nbt){
        int x = nbt.getInt("x");
        int y = nbt.getInt("y");
        int z = nbt.getInt("z");

        return new BlockPos(x,y,z);
    }

    public static CompoundTag blockPosListToNBT(List<BlockPos> list){
        CompoundTag tag = new CompoundTag();
        tag.putInt("size", list.size());
        for(int i = 0; i < list.size(); i++){
            tag.put(Integer.toString(i), blockPosToNBT(list.get(i)));
        }
        return tag;
    }

    public static List<BlockPos> blockPosListFromNBT(CompoundTag tag){
        List<BlockPos> out = new ArrayList<>();
        int size = tag.getInt("size");
        for(int i = 0; i < size; i++){
            out.add(blockPosFromNBT(tag.getCompound(Integer.toString(i))));
        }
        return out;
    }

    public static CompoundTag vec3ToNBT(Vec3 vec3){
        CompoundTag out = new CompoundTag();
        out.putDouble("x", vec3.x);
        out.putDouble("y", vec3.y);
        out.putDouble("z", vec3.z);
        return out;
    }

    public static Vec3 vec3FromNBT(CompoundTag tag){
        double x = tag.getDouble("x");
        double y = tag.getDouble("y");
        double z = tag.getDouble("z");
        return new Vec3(x,y,z);
    }

    public static CompoundTagCollector toSingleCompoundTag(){
        return new CompoundTagCollector();
    }

    public static Stream<CompoundTag> fromSingleCompoundTag(CompoundTag tag){
        List<CompoundTag> tags = new ArrayList<>();
        for(String key : tag.getAllKeys()){
            tags.add(tag.getCompound(key));
        }
        return tags.stream();
    }

    public static class CompoundTagCollector implements Collector<Tag, CompoundTag, CompoundTag> {

        private static final Random RANDOM = new Random();

        @Override
        public Supplier<CompoundTag> supplier() {
            return CompoundTag::new;
        }

        @Override
        public BiConsumer<CompoundTag, Tag> accumulator() {
            return (acc, tag) -> acc.put(Integer.toString(tag.hashCode()), tag);
        }

        @Override
        public BinaryOperator<CompoundTag> combiner() {
            return (a, b) -> {
                b.getAllKeys()
                    .forEach(current -> a.put(getNextUniqueKey(a, current), b.get(current)));
                return a;
            };
        }

        @Override
        public Function<CompoundTag, CompoundTag> finisher() {
            return (a) -> a;
        }

        @Override
        public Set<Characteristics> characteristics() {
            return Sets.immutableEnumSet(Characteristics.CONCURRENT, Characteristics.UNORDERED, Characteristics.IDENTITY_FINISH);
        }

        private String getNextUniqueKey(CompoundTag testTag, String hashKey){
            int hash = Integer.parseInt(hashKey);
            while (testTag.contains(Integer.toString(hash))){
                hash += RANDOM.nextInt(20);
            }
            return Integer.toString(hash);
        }
    }
}
