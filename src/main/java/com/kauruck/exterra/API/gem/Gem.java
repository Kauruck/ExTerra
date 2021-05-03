package com.kauruck.exterra.API.gem;

import com.kauruck.exterra.API.tooltype.Tool;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class Gem extends ForgeRegistryEntry<Gem>{

    private float miningSpeedModifier;
    private int itemEnchantantebilityModifier;
    private float attackSpeedModifier;
    private float attackDamageModifier;
    private int maxDamageModifier;

    public Gem(Properties properties){
        this.miningSpeedModifier = properties.miningSpeedModifier;
        this.itemEnchantantebilityModifier = properties.itemEnchantantebilityModifier;;
        this.attackSpeedModifier = properties.attackSpeedModifier;
        this.attackDamageModifier = properties.attackDamageModifier;
        this.maxDamageModifier = properties.maxDamageModifier;
    }

    public String getTranslationKey() {
        return "gem." + this.getRegistryName().getNamespace() + "." + this.getRegistryName().getPath();
    }

    public void fromProperties(Properties properties){
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


        public Properties setMiningSpeedModifier(float value){
            this.miningSpeedModifier = value;
            return this;
        }

        public Properties setItemEnchantantebilityModifier(int itemEnchantantebilityModifier) {
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


}
