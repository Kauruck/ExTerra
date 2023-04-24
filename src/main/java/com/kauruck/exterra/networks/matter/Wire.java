package com.kauruck.exterra.networks.matter;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.fx.ParticleHelper;
import com.kauruck.exterra.util.Colors;
import com.kauruck.exterra.util.NBTUtil;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.RedstoneSide;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.stream.Collectors;

import static com.kauruck.exterra.blocks.DustBlock.*;

public class Wire {

    /**
     * This **should** be ordered from terminal a to terminal b.
     */
    private final List<Tuple<BlockPos, BlockState>> positions = new ArrayList<>();
    private BlockPos terminalA;
    private BlockPos terminalB;

    public static final Map<Direction, EnumProperty<RedstoneSide>> PROPERTY_BY_DIRECTION = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, NORTH, Direction.EAST, EAST, Direction.SOUTH, SOUTH, Direction.WEST, WEST));
    private List<WireTransferInfo> infos = new ArrayList<>();

    public void appendBlock(BlockPos pos, BlockState state){
        positions.add(new Tuple<>(pos, state));
    }

    public void emitParticles(Vec3 color, ClientLevel pLevel, RandomSource pRandom, BlockPos currentPos, BlockState pState) {
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            RedstoneSide redstoneside = pState.getValue(PROPERTY_BY_DIRECTION.get(direction));
            switch (redstoneside) {
                case UP:
                    ParticleHelper.spawnParticlesAlongLine(pLevel, pRandom, currentPos, color, direction, Direction.UP, -0.5F, 0.5F);
                case SIDE:
                    ParticleHelper.spawnParticlesAlongLine(pLevel, pRandom, currentPos, color, Direction.DOWN, direction, 0.0F, 0.5F);
                    break;
                case NONE:
                default:
                    ParticleHelper.spawnParticlesAlongLine(pLevel, pRandom, currentPos, color, Direction.DOWN, direction, 0.0F, 0.3F);
            }
        }
    }

    public void animationTick(ClientLevel pLevel, RandomSource pRandom){
        float stepSize = 1f/ positions.size();
        for(int index = 0; index < positions.size(); index++){
            float percentage = index*stepSize;
            Vec3 color = new Vec3(0,0,0);
            for(WireTransferInfo info : infos){
                if(!info.flip && percentage <= info.percentage)
                    color = color.add(info.color.scale(info.strength));
                else if(info.flip && percentage >= info.percentage)
                    color = color.add(info.color.scale(info.strength));
            }
            if(!Colors.isZero(color))
                this.emitParticles(color, pLevel, pRandom, positions.get(index).getA(), positions.get(index).getB());
        }
    }

    public void serverTick(){
        //Fade out the color over time
        infos = infos.stream()
                .peek(info -> info.strength -= 0.1f)
                .filter(info -> info.strength > 0)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public void addInfo(Vec3 color){
        this.addInfo(color, 1, false);
    }

    public void addInfo(Vec3 color, float percentage){
        this.addInfo(color, percentage, false);
    }

    public void addInfo(Vec3 color, float percentage, boolean fromB){
        if(!(infos instanceof ArrayList<WireTransferInfo>))
            ExTerra.LOGGER.info("HOW");
        this.infos.add(new WireTransferInfo(color, percentage, fromB));
    }

    public void setTerminalA(BlockPos terminalA) {
        this.terminalA = terminalA;
    }

    public void setTerminalB(BlockPos terminalB) {
        this.terminalB = terminalB;
    }

    public BlockPos getTerminalA() {
        return terminalA;
    }

    public BlockPos getTerminalB() {
        return terminalB;
    }

    public CompoundTag toNBT(){
        CompoundTag out = new CompoundTag();
        int index = 0;
        for(Tuple<BlockPos, BlockState> currentPos : positions){
            out.put(index+"_pos", NBTUtil.blockPosToNBT(currentPos.getA()));
            out.put(index+"_state", NbtUtils.writeBlockState(currentPos.getB()));
            index++;
        }
        out.putInt("length", index);
        CompoundTag infoTag = infos.stream()
                .map(WireTransferInfo::toNBT)
                .collect(NBTUtil.toSingleCompoundTag());
        out.put("infos", infoTag);
        return out;
    }

    public static Wire fromNBT(CompoundTag tag){
        int length = tag.getInt("length");
        Wire out = new Wire();
        for(int i = 0; i < length; i++){
            out.appendBlock(NBTUtil.blockPosFromNBT(tag.getCompound(i+"_pos")), NbtUtils.readBlockState(tag.getCompound(i + "_state")));
        }
        List<WireTransferInfo> infos = NBTUtil.fromSingleCompoundTag((CompoundTag) tag.get("infos"))
                .map(WireTransferInfo::fromNBT)
                .collect(Collectors.toCollection(ArrayList::new));
        out.infos = infos;
        return out;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof  Wire))
            return false;
        Wire other = (Wire) obj;
        boolean terminalEquals = false;
        terminalEquals = (other.terminalA.equals(this.terminalA) && other.terminalB.equals(this.terminalB)) ||
                (other.terminalA.equals(this.terminalB) && other.terminalB.equals(this.terminalA));

        boolean lengthSame = this.positions.size() == other.positions.size();

        //TODO do we need the HashSet?
        boolean allElementsIncluded = new HashSet<>(this.positions).containsAll(other.positions);

        return terminalEquals && lengthSame && allElementsIncluded;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(WireTransferInfo info : infos){
            builder.append("C:")
                    .append(info.color)
                    .append('\n');
        }
        return builder.toString();
    }

    @Override
    public int hashCode() {
        int result = positions.hashCode();
        result = 31 * result + (terminalA != null ? terminalA.hashCode() : 0);
        result = 31 * result + (terminalB != null ? terminalB.hashCode() : 0);
        result = 31 * result + (infos != null ? infos.hashCode() : 0);
        return result;
    }

    public static class WireTransferInfo {
        private final Vec3 color;
        private final float percentage;
        private final boolean flip;
        private float strength = 1f;

        public WireTransferInfo(Vec3 color, float percentage, boolean flip) {
            this.color = color;
            this.percentage = percentage;
            this.flip = flip;
        }

        public Vec3 getColor() {
            return color;
        }

        public float getPercentage() {
            return percentage;
        }

        public boolean isFlip() {
            return flip;
        }

        public float getStrength() {
            return strength;
        }

        public void setStrength(float strength) {
            this.strength = strength;
        }

        public CompoundTag toNBT(){
            CompoundTag out = new CompoundTag();
            out.put("color", NBTUtil.vec3ToNBT(color));
            out.putBoolean("flip", flip);
            out.putFloat("strength", strength);
            return out;
        }

        public static WireTransferInfo fromNBT(CompoundTag tag){
            Vec3 color = NBTUtil.vec3FromNBT(tag.getCompound("color"));
            boolean flip = tag.getBoolean("flip");
            float strength = tag.getFloat("strength");
            return new WireTransferInfo(color, strength, flip);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            WireTransferInfo info = (WireTransferInfo) o;

            if (Float.compare(info.percentage, percentage) != 0) return false;
            if (flip != info.flip) return false;
            if (Float.compare(info.strength, strength) != 0) return false;
            return Objects.equals(color, info.color);
        }

        @Override
        public int hashCode() {
            int result = color != null ? color.hashCode() : 0;
            result = 31 * result + (percentage != +0.0f ? Float.floatToIntBits(percentage) : 0);
            result = 31 * result + (flip ? 1 : 0);
            result = 31 * result + (strength != +0.0f ? Float.floatToIntBits(strength) : 0);
            return result;
        }
    }
}
