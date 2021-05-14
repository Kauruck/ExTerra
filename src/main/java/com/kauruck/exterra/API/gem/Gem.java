package com.kauruck.exterra.API.gem;

import com.google.gson.*;
import com.kauruck.exterra.API.tooltype.Tool;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.lang.reflect.Type;

/**
 * The gem.
 * This represents a gem with all its stats and attributes.
 *
 *  It needs to be registered to the GemRegistry:
 * @see com.kauruck.exterra.ExTerraRegistries
 *
 *  @author Kauruck
 */
public class Gem extends ForgeRegistryEntry<Gem>{

    private float miningSpeedModifier;
    private int itemEnchantantebilityModifier;
    private float attackSpeedModifier;
    private float attackDamageModifier;
    private int maxDamageModifier;
    private int priority;

    /**
     * Create a new Gem form the properties
     * @param properties The properties
     */
    public Gem(Properties properties){
        this.miningSpeedModifier = properties.miningSpeedModifier;
        this.itemEnchantantebilityModifier = properties.itemEnchantantebilityModifier;;
        this.attackSpeedModifier = properties.attackSpeedModifier;
        this.attackDamageModifier = properties.attackDamageModifier;
        this.maxDamageModifier = properties.maxDamageModifier;
        this.priority = properties.priority;
    }

    /**
     * Get the translation key for the gem
     * @return The translation key
     */
    public String getTranslationKey() {
        return "gem." + this.getRegistryName().getNamespace() + "." + this.getRegistryName().getPath();
    }

    /**
     * Load the stats form properties.
     *
     * It only loads if the property is higher or equal
     * @param properties The new properties
     */
    public void fromProperties(Properties properties){
        //Do not update, we are set from a higher priority
        if(properties.priority < this.priority)
            return;
        this.miningSpeedModifier = properties.miningSpeedModifier;
        this.itemEnchantantebilityModifier = properties.itemEnchantantebilityModifier;;
        this.attackSpeedModifier = properties.attackSpeedModifier;
        this.attackDamageModifier = properties.attackDamageModifier;
        this.maxDamageModifier = properties.maxDamageModifier;
    }


    /**
     * Get the speed modifier for mining
     * @return The speed modifier for mining
     */
    public float getMiningSpeedModifier(){
        return miningSpeedModifier;
    }

    /**
     * Get the enchantebility modifier
     * @return The encahtebility modifier
     */
    public int getItemEnchantebilityModifier(){
        return itemEnchantantebilityModifier;
    }

    /**
     * Get the speed modifier for attacking
     * @return The speed modifier for attacking
     */
    public float getAttackSpeedModifier(){
        return attackSpeedModifier;
    }

    /**
     * Get the attack damage modifier
     * @return The attack damage modifier
     */
    public float getAttackDamageModifier(){
        return attackDamageModifier;
    }

    /**
     * Get the max damage modifier
     * @return The max damage modifier
     */
    public int getMaxDamageModifier(){
        return maxDamageModifier;
    }

    /**
     * Get the resource location for the texture of the gem
     * @return The resource location
     */
    public ResourceLocation getTexture(){
        return new ResourceLocation(this.getRegistryName().getNamespace(), "gems/" + this.getRegistryName().getPath());
    }


    /**
     * Properties for the gem
     */
    public static class Properties{

        private float miningSpeedModifier = 0;
        private int itemEnchantantebilityModifier = 0;
        private float attackSpeedModifier = 0;
        private float attackDamageModifier = 0;
        private int maxDamageModifier = 0;
        private int priority = -1;


        /**
         * Set the mining speed modifier
         * @param value The value
         * @return this
         */
        public Properties setMiningSpeedModifier(float value){
            this.miningSpeedModifier = value;
            return this;
        }

        /**
         * Set the priority for loading the property
         * @param priority The value
         * @return this
         */
        public Properties setPriority(int priority) {
            this.priority = priority;
            return this;
        }

        /**
         * Set the enchantebility modifier
         * @param itemEnchantantebilityModifier The value
         * @return this
         */
        public Properties setItemEnchantebilityModifier(int itemEnchantantebilityModifier) {
            this.itemEnchantantebilityModifier = itemEnchantantebilityModifier;
            return this;
        }

        /**
         * Set the attack speed modifier
         * @param attackSpeedModifier The
         * @return this
         */
        public Properties setAttackSpeedModifier(float attackSpeedModifier) {
            this.attackSpeedModifier = attackSpeedModifier;
            return this;
        }

        /**
         * Set the attack damage modifier
         * @param attackDamageModifier The value
         * @return this
         */
        public Properties setAttackDamageModifier(float attackDamageModifier) {
            this.attackDamageModifier = attackDamageModifier;
            return this;
        }

        /**
         * Set the max damage modifier
         * @param maxDamageModifier The value
         * @return this
         */
        public Properties setMaxDamageModifier(int maxDamageModifier) {
            this.maxDamageModifier = maxDamageModifier;
            return this;
        }
    }

    /**
     * Serializer for the properties
     */
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
