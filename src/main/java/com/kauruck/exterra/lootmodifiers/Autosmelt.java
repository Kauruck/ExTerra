package com.kauruck.exterra.lootmodifiers;

import com.google.gson.JsonObject;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.*;

/**
 * The LootModifier for Autosmelting things.
 * It takes the Items dropped original and uses, if exiting, a furnace recipe on it.
 *
 * @author Kauruck
 */
public class Autosmelt extends LootModifier {
    public Autosmelt(ILootCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        int modL = 0;
        ItemStack tool = context.get(LootParameters.TOOL);
        if(tool != null){
            if(EnchantmentHelper.getEnchantments(tool).containsKey(Enchantments.FORTUNE)) {
                modL = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, tool);
            }
        }
        final int mod = modL;
        ArrayList<ItemStack> ret = new ArrayList<>();
        generatedLoot.forEach((stack) -> ret.add(smelt(stack, context, mod)));

        return ret;
    }

    private static ItemStack smelt(ItemStack stack, LootContext context, int mod) {
        ItemStack output =  context.getWorld().getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(stack), context.getWorld())
                .map(FurnaceRecipe::getRecipeOutput)
                .filter(itemStack -> !itemStack.isEmpty())
                .map(itemStack -> ItemHandlerHelper.copyStackWithSize(itemStack, stack.getCount() * itemStack.getCount()))
                .orElse(stack);
        float xp = context.getWorld().getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(stack), context.getWorld())
                .map(FurnaceRecipe::getExperience).orElse(0f);
        Vector3d pos = context.get(LootParameters.field_237457_g_);
        if(!context.getWorld().isRemote && pos != null) {
            summonXP(context.getWorld(), pos, xp);
        }
        Random rand = context.getRandom();
        float ch = rand.nextFloat();
        float part = 1f / (mod + 1);
        float total = 0;
        int multi = 1;
        for(;multi <= mod; multi++){
            total += part;
            if(total >= ch){
                break;
            }
        }

        output.setCount(output.getCount() * multi);

        return output;
    }

    private static void summonXP(World world, Vector3d pos, float xp){
        int i = MathHelper.floor(xp);
        float f = MathHelper.frac(xp);
        if (f != 0.0F && Math.random() < (double)f) {
            ++i;
        }

        while(i > 0) {
            int j = ExperienceOrbEntity.getXPSplit(i);
            i -= j;
            world.addEntity(new ExperienceOrbEntity(world, pos.x, pos.y, pos.z, j));
        }
    }

    public static class Serializer extends GlobalLootModifierSerializer<Autosmelt> {

        @Override
        public Autosmelt read(ResourceLocation name, JsonObject json, ILootCondition[] conditionsIn) {
            return new Autosmelt(conditionsIn);
        }

        @Override
        public JsonObject write(Autosmelt instance) {
            return makeConditions(instance.conditions);
        }
    }

}