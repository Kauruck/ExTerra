package com.kauruck.exterra.item;

import com.kauruck.exterra.API.gem.IGemAction;
import com.kauruck.exterra.gems.gemactions.RubyAction;
import com.kauruck.exterra.gems.gemactions.SapphireAction;
import com.kauruck.exterra.gems.gemactions.TourmalineAction;
import com.kauruck.exterra.gems.gemactions.ZirconAction;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public enum GemTypeOld {
    Ruby("ruby", 1561,8.0F,3.0F,10, 15, 33, new int[]{3, 6, 8, 3}, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2, 0.0F, new RubyAction()),   //Fire Autosmelt
    Sapphire("sapphire", 1561,8.0F,3.0F,10, 15,33, new int[]{3, 6, 8, 3}, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2, 0.0F, new SapphireAction()),   //Water Aquainfity
    Tourmaline("tourmaline", 2031, 7.0F,5.0F,10, 15, 37, new int[]{3, 6, 8, 3}, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 3, 1.0F, new TourmalineAction()),   //Earth Heavy
    Zircon("zircon", 1561, 12.0F,3.0F,10, 15,33, new int[]{3, 6, 8, 3}, SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, 2, 0.0F, new ZirconAction());   //Wind Swift

    public static int indexOf(GemTypeOld gemTypeOld){
        int i = 0;
        for(GemTypeOld current : GemTypeOld.values()){
            if(current == gemTypeOld)
                return i;
            i++;
        }
        return  -1;
    }

    public static GemTypeOld fromItem(Item item){
        for(int i = 0; i < GemTypeOld.values().length; i++){
            /*if(item == ModItems.GEMS[i])
                return GemTypeOld.values()[i];*/
        }
        return null;
    }

    public static boolean isGem(Item item){
        return GemTypeOld.fromItem(item) != null;
    }


    private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};

    private final String name;
    private final int uses;
    private final float eff;
    private final float dmg;
    private final int lev;
    private final int enc;
    //private final LazyValue<Ingredient> rep;
    private final  int durArmor;
    private final int[] dra;
    private final SoundEvent se;
    private final float tou;
    private final float kbr;
    private final IGemAction gemAction;

    private GemTypeOld(String name, int uses, float eff, float dmg, int lev, int enc, int durArmor, int[] dra, SoundEvent se, float tou, float kbr, IGemAction gemAction){
        this.name = name;
        this.dmg = dmg;
        this.eff = eff;
        this.enc = enc;
        this.lev = lev;
        //this.rep = new LazyValue<Ingredient>(() -> {return Ingredient.fromItems(ModItems.GEMS[GemTypeOld.indexOf(this)]);});
        this.uses = uses;
        this.durArmor = durArmor;
        this.dra = dra;
        this.se = se;
        this.tou = tou;
        this.kbr = kbr;
        this.gemAction = gemAction;
    }

}
