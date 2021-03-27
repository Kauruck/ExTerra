package com.kauruck.exterra.Item;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.Init.ModItems;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public enum GemType  implements IItemTier, IArmorMaterial {
    Ruby("ruby", 1561,8.0F,3.0F,10, 15, 33, new int[]{3, 6, 8, 3}, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2, 0.0F),   //Fire Autosmelt
    Sapphire("sapphire", 1561,8.0F,3.0F,10, 15,33, new int[]{3, 6, 8, 3}, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2, 0.0F),   //Water Aquainfity
    Tourmaline("tourmaline", 2031, 7.0F,5.0F,10, 15, 37, new int[]{3, 6, 8, 3}, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3, 1.0F),   //Earth Heavy
    Zircon("zircon", 1561, 12.0F,3.0F,10, 15,33, new int[]{3, 6, 8, 3}, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2, 0.0F);   //Wind Swift

    public static int indexOf(GemType gemType){
        int i = 0;
        for(GemType current : GemType.values()){
            if(current == gemType)
                return i;
            i++;
        }
        return  -1;
    }


    private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};

    private final String name;
    private final int uses;
    private final float eff;
    private final float dmg;
    private final int lev;
    private final int enc;
    private final LazyValue<Ingredient> rep;
    private final  int durArmor;
    private final int[] dra;
    private final SoundEvent se;
    private final float tou;
    private final float kbr;

    private GemType(String name, int uses, float eff, float dmg, int lev, int enc, int durArmor, int[] dra, SoundEvent se, float tou,  float kbr){
        this.name = name;
        this.dmg = dmg;
        this.eff = eff;
        this.enc = enc;
        this.lev = lev;
        this.rep = new LazyValue<Ingredient>(() -> {return Ingredient.fromItems(ModItems.GEMS[GemType.indexOf(this)]);});
        this.uses = uses;
        this.durArmor = durArmor;
        this.dra = dra;
        this.se = se;
        this.tou = tou;
        this.kbr = kbr;
    }

    public String getRegistryName() {
        return name;
    }

    @Override
    public float getToughness() {
        return this.tou;
    }

    @Override
    public float getKnockbackResistance() {
        return this.kbr;
    }

    @Override
    public int getMaxUses() {
        return uses;
    }

    @Override
    public float getEfficiency() {
        return eff;
    }

    @Override
    public float getAttackDamage() {
        return dmg;
    }

    @Override
    public int getHarvestLevel() {
        return lev;
    }

    @Override
    public int getDurability(EquipmentSlotType slotIn) {
        return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * this.durArmor;
    }

    @Override
    public int getDamageReductionAmount(EquipmentSlotType slotIn) {
        return this.dra[slotIn.getIndex()];
    }

    @Override
    public int getEnchantability() {
        return enc;
    }

    @Override
    public SoundEvent getSoundEvent() {
        return se;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return rep.getValue();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getName() {
        return ExTerra.MOD_ID + ":" + name;
    }
}
