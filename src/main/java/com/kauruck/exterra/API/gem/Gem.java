package com.kauruck.exterra.API.gem;

import com.google.gson.*;
import com.kauruck.exterra.API.tooltype.Tool;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.lang.reflect.Type;

public class Gem extends ForgeRegistryEntry<Gem>{

    private float miningSpeedModifier;
    private int itemEnchantantebilityModifier;
    private float attackSpeedModifier;
    private float attackDamageModifier;
    private int maxDamageModifier;
    private int priority;

    public Gem(Properties properties){
        this.miningSpeedModifier = properties.miningSpeedModifier;
        this.itemEnchantantebilityModifier = properties.itemEnchantantebilityModifier;;
        this.attackSpeedModifier = properties.attackSpeedModifier;
        this.attackDamageModifier = properties.attackDamageModifier;
        this.maxDamageModifier = properties.maxDamageModifier;
        this.priority = properties.priority;
    }

    public String getTranslationKey() {
        return "gem." + this.getRegistryName().getNamespace() + "." + this.getRegistryName().getPath();
    }

    public void fromProperties(Properties properties){
        //Do not update, we are set from a higher priority
        if(properties.priority <= this.priority)
            return;
        this.miningSpeedModifier = properties.miningSpeedModifier;
        this.itemEnchantantebilityModifier = properties.itemEnchantantebilityModifier;;
        this.attackSpeedModifier = properties.attackSpeedModifier;
        this.attackDamageModifier = properties.attackDamageModifier;
        this.maxDamageModifier = properties.maxDamageModifier;
    }


    public float getMiningSpeedModifier(){
        return miningSpeedModifier;
    }

    public int getItemEnchantabilityModifier(){
        return itemEnchantantebilityModifier;
    }

    public float getAttackSpeedModifier(){
        return attackSpeedModifier;
    }

    public float getAttackDamageModifier(){
        return attackDamageModifier;
    }

    public Item getTool(Tool toolTag){
        return null;
    }

    public ResourceLocation getTexture(){
        return new ResourceLocation(this.getRegistryName().getNamespace(), "gems/" + this.getRegistryName().getPath());
    }


    public int getMaxDamageModifier(){
        return maxDamageModifier;
    }




    public static class Properties{

        private float miningSpeedModifier = 0;
        private int itemEnchantantebilityModifier = 0;
        private float attackSpeedModifier = 0;
        private float attackDamageModifier = 0;
        private int maxDamageModifier = 0;
        private int priority = -1;


        public Properties setMiningSpeedModifier(float value){
            this.miningSpeedModifier = value;
            return this;
        }

        public Properties setPriority(int priority) {
            this.priority = priority;
            return this;
        }

        public Properties setItemEnchantebilityModifier(int itemEnchantantebilityModifier) {
            this.itemEnchantantebilityModifier = itemEnchantantebilityModifier;
            return this;
        }

        public Properties setAttackSpeedModifier(float attackSpeedModifier) {
            this.attackSpeedModifier = attackSpeedModifier;
            return this;
        }

        public Properties setAttackDamageModifier(float attackDamageModifier) {
            this.attackDamageModifier = attackDamageModifier;
            return this;
        }

        public Properties setMaxDamageModifier(int maxDamageModifier) {
            this.maxDamageModifier = maxDamageModifier;
            return this;
        }
    }

    public static class PropertiesSerializer implements JsonDeserializer<Properties>, JsonSerializer<Properties>{

        @Override
        public Properties deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            Properties out = new Properties();
            if(json.isJsonObject()){
                JsonObject obj = json.getAsJsonObject();
                if(obj.has("priority")){
                    out.setPriority(obj.get("priority").getAsInt());
                }
                if(obj.has("miningSpeedModifier")){
                    out.setMiningSpeedModifier(obj.get("miningSpeedModifier").getAsFloat());
                }
                if(obj.has("attackSpeedModifier")){
                    out.setAttackSpeedModifier(obj.get("attackSpeedModifier").getAsFloat());
                }
                if(obj.has("attackDamageModifier")){
                    out.setAttackDamageModifier(obj.get("attackDamageModifier").getAsFloat());
                }
                if(obj.has("maxDamageModifier")){
                    out.setMaxDamageModifier(obj.get("maxDamageModifier").getAsInt());
                }
                if(obj.has("enchantebilityModifier")){
                    out.setItemEnchantebilityModifier(obj.get("enchantebilityModifier").getAsInt());
                }

            }

            return out;
        }

        @Override
        public JsonElement serialize(Properties src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.add("priority", new JsonPrimitive(src.priority));
            obj.add("miningSpeedModifier", new JsonPrimitive(src.miningSpeedModifier));
            obj.add("attackSpeedModifier", new JsonPrimitive(src.attackSpeedModifier));
            obj.add("attackDamageModifier", new JsonPrimitive(src.attackDamageModifier));
            obj.add("maxDamageModifier", new JsonPrimitive(src.maxDamageModifier));
            obj.add("enchantebilityModifier", new JsonPrimitive(src.itemEnchantantebilityModifier));
            return obj;
        }
    }


}
