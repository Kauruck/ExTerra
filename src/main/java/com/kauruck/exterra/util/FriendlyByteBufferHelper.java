package com.kauruck.exterra.util;

import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.modules.ExTerraRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistry;

public class FriendlyByteBufferHelper {

    public static void writeMatter(FriendlyByteBuf buff, MatterStack stack){
        if (stack.isEmpty()) {
            buff.writeBoolean(false);
        } else {
            buff.writeBoolean(true);
            Matter matter = stack.getMatter();
            buff.writeVarInt(((ForgeRegistry<Matter>)ExTerraRegistries.MATTER.get()).getID(matter));
            buff.writeByte(stack.getAmount());
            // Maybe NBT
            /*CompoundTag compoundtag = null;
            if (item.isDamageable(pStack) || item.shouldOverrideMultiplayerNbt()) {
                compoundtag = limitedTag ? pStack.getShareTag() : pStack.getTag();
            }

            this.writeNbt(compoundtag);*/
        }
    }

    public static MatterStack readMatter(FriendlyByteBuf buf) {
        if (!buf.readBoolean()) {
            return MatterStack.EMPTY;
        } else {
            int id = buf.readVarInt();
            Matter matter = ((ForgeRegistry<Matter>) ExTerraRegistries.MATTER.get()).getValue(id);
            int i = buf.readByte();
            MatterStack stack = new MatterStack(matter, i);
            //TODO NBT?
            // itemstack.readShareTag(buf.readNbt());
            return stack;
        }
    }
}
