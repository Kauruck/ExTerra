package com.kauruck.exterra.serializers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.kauruck.exterra.api.matter.Matter;
import com.kauruck.exterra.api.matter.MatterStack;
import com.kauruck.exterra.modules.ExTerraRegistries;
import com.kauruck.exterra.util.FriendlyByteBufferHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class MatterStackSerializer {
    public static MatterStack fromJson(JsonObject json){
        if(!json.has("matter")){
            throw new JsonParseException("No matter for stack given");
        }
        ResourceLocation matterLoc = ResourceLocation.tryParse(json.get("matter").getAsString());
        Matter matter = ExTerraRegistries.MATTER.get(matterLoc);

        if(!json.has("amount")){
            throw new JsonParseException("No amount for stack given");
        }
        int amount = json.get("amount").getAsInt();
        return new MatterStack(matter, amount);
    }

    public static JsonObject toJson(MatterStack stack){
        JsonObject out = new JsonObject();
        out.addProperty("matter", ExTerraRegistries.MATTER.getKey(stack.getMatter()).toString());
        out.addProperty("amount", stack.getAmount());
        return out;
    }

    public static void writeNetwork(FriendlyByteBuf buff, MatterStack stack){
        if (stack.isEmpty()) {
            buff.writeBoolean(false);
        } else {
            buff.writeBoolean(true);
            Matter matter = stack.getMatter();
            buff.writeVarInt(ExTerraRegistries.MATTER.getId(matter));
            buff.writeByte(stack.getAmount());
            // Maybe NBT
            /*CompoundTag compoundtag = null;
            if (item.isDamageable(pStack) || item.shouldOverrideMultiplayerNbt()) {
                compoundtag = limitedTag ? pStack.getShareTag() : pStack.getTag();
            }

            this.writeNbt(compoundtag);*/
        }
    }

    public static MatterStack readNetwork(FriendlyByteBuf buf) {
        if (!buf.readBoolean()) {
            return MatterStack.EMPTY;
        } else {
            int id = buf.readVarInt();
            Matter matter = (ExTerraRegistries.MATTER.byId(id));
            int i = buf.readByte();
            MatterStack stack = new MatterStack(matter, i);
            //TODO NBT?
            // itemstack.readShareTag(buf.readNbt());
            return stack;
        }
    }
}
