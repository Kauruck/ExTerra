package com.kauruck.exterra.networking;

import com.kauruck.exterra.api.blockentity.NotIterableInProperty;
import com.kauruck.exterra.util.NBTUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.*;
import net.minecraftforge.api.distmarker.Dist;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class BlockEntityProperty <T>{

    private static final Map<Class, BiFunction<Object, Boolean, Tag>> TO_NBTS = new HashMap<>();
    private static final Map<Class, Function<Tag, Object>> FROM_NBTS = new HashMap<>();
    private static final Map<Class, BiFunction<Class<?>, Object[], Object>> TO_ITERABLES = new HashMap<>();

    private T data;

    private final String name;
    private final BiFunction<Object, Boolean, Tag> toNbt;
    private final Function<Tag, Object> fromNbt;

    private final BiFunction<Class<?> ,Object[], Object> toIterable;

    private final Class<T> dataClass;
    private final Class<?> innerClass;

    private final BlockEntityPropertySide side;
    private boolean shouldBeUpdated = false;
    boolean outOfSync = false;

    public static void registerPropertyType(Class clazz, BiFunction<Object, Boolean, Tag> toNbt, Function<Tag, Object> fromNbt){
        TO_NBTS.put(clazz, toNbt);
        FROM_NBTS.put(clazz, fromNbt);
    }

    public static void registerIterable(Class clazz, BiFunction<Class<?>, Object[], Object> toIterable){
        TO_ITERABLES.put(clazz, toIterable);
    }


    public BlockEntityProperty(T data, String name, Class<T> dataClass, Class<?> innerClass, BlockEntityPropertySide side){
        this.dataClass = dataClass;
        if(TO_ITERABLES.containsKey(dataClass)){
            this.toIterable = TO_ITERABLES.get(dataClass);
            this.innerClass = innerClass;

            if(TO_NBTS.containsKey(this.innerClass))
                this.toNbt = TO_NBTS.get(innerClass);
            else
                throw new UnsupportedOperationException("No toNBT function for Class {}".formatted(innerClass.getName()));

            if(FROM_NBTS.containsKey(this.innerClass))
                this.fromNbt =  FROM_NBTS.get(this.innerClass);
            else
                throw new UnsupportedOperationException("No fromNBT function for Class {}".formatted(innerClass.getName()));
        }
        else{
            this.toIterable = null;
            this.innerClass = null;
            if(TO_NBTS.containsKey(this.dataClass))
                this.toNbt =  TO_NBTS.get(this.dataClass);
            else
                throw new UnsupportedOperationException("No toNBT function for Class {}".formatted(dataClass.getName()));

            if(FROM_NBTS.containsKey(this.dataClass))
                this.fromNbt =  FROM_NBTS.get(this.dataClass);
            else
                throw new UnsupportedOperationException("No fromNBT function for Class {}".formatted(dataClass.getName()));

        }

        this.name = name;
        this.data = data;
        this.side = side;
    }

    Class<T> getDataClass() {
        return dataClass;
    }

    Class<?> getInnerClass() {
        return innerClass;
    }

    public String getName() {
        return name;
    }

    public Tag toNBT(boolean networkSync){
        if(data == null) {
            return StringTag.valueOf("\\null\\");
        }
        else if(data instanceof Iterable<?> iData && ! (data instanceof NotIterableInProperty)){
            CompoundTag out = new CompoundTag();
            int index = 0;
            for(Object current : iData ){
                out.put(Integer.toString(index), this.toNbt.apply(current, networkSync));
                index ++;
            }
            out.putInt("length", index);
            return out;
        }
        else{
            return this.toNbt.apply(this.data, networkSync);
        }
    }

    public void setFromTag(Tag tag){
        if(tag instanceof StringTag && tag.getAsString().equals("\\null\\")){
            this.data = null;
        }else if (dataClass.isArray() && tag instanceof CompoundTag ct){
            int length = ct.getInt("length");
            Object[] dataArray = new Object[length];
            for(int i = 0; i < length; i++){
                dataArray[i] = this.fromNbt.apply(ct.get(Integer.toString(i)));
            }
            this.data = (T) dataArray;
        }
        else if(data instanceof Iterable<?> iData && !(data instanceof NotIterableInProperty) &&tag instanceof CompoundTag ct){
            int length = ct.getInt("length");
            Object[] dataArray = new Object[length];
            for(int i = 0; i < length; i++){
                dataArray[i] = this.fromNbt.apply(ct.get(Integer.toString(i)));
            }
            this.data = (T) toIterable.apply(this.innerClass, dataArray);
        }
        else{
            this.data = (T) this.fromNbt.apply(tag);
        }
    }

    public void set(T value){
        if(data == value)
            return;
        this.data = value;
    }

    public T get(){
        return this.data;
    }

    void overrideData(T value){
        this.data = value;
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


    static {
        // toNBTs
        TO_NBTS.put(Boolean.class, (data, ignored) -> ByteTag.valueOf((Boolean) data));
        TO_NBTS.put(String.class, (data, ignored) -> StringTag.valueOf((String) data));
        TO_NBTS.put(Integer.class, (data, ignored) -> IntTag.valueOf((Integer) data));
        TO_NBTS.put(Float.class, (data, ignored) -> FloatTag.valueOf((Float) data));
        TO_NBTS.put(Double.class, (data, ignored) -> DoubleTag.valueOf((Double) data));
        TO_NBTS.put(Long.class, (data, ignored) -> LongTag.valueOf((Long) data));
        TO_NBTS.put(BlockPos.class, (data, ignored) -> NBTUtil.blockPosToNBT((BlockPos) data));

        //fromNBts
        FROM_NBTS.put(Boolean.class, (tag) -> ((ByteTag)tag).getAsByte() != 0);
        FROM_NBTS.put(String.class, Tag::getAsString);
        FROM_NBTS.put(Integer.class, (tag) -> ((IntTag)tag).getAsInt());
        FROM_NBTS.put(Float.class, (tag) -> ((FloatTag)tag).getAsFloat());
        FROM_NBTS.put(Double.class, (tag) -> ((DoubleTag)tag).getAsDouble());
        FROM_NBTS.put(Long.class, (tag) -> ((LongTag)tag).getAsLong());
        FROM_NBTS.put(BlockPos.class, (tag) -> NBTUtil.blockPosFromNBT((CompoundTag) tag));


        //Iterables
        TO_ITERABLES.put(ArrayList.class, BlockEntityProperty::toList);
    }
}
