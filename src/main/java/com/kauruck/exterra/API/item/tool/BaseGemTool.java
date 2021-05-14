package com.kauruck.exterra.API.item.tool;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.kauruck.exterra.ExTerraRegistries;
import com.kauruck.exterra.crafting.IUpgradable;
import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.API.gem.Gem;
import com.kauruck.exterra.item.tools.GemShovel;
import com.kauruck.exterra.modules.ExTerraTools;
import com.kauruck.exterra.util.ToolHelper;
import net.minecraft.block.BlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Optional;

/**
 * Base tool for the Gem
 *
 * Extend this class
 *
 * @author Kauruck
 */
public class BaseGemTool extends Item implements IUpgradable {

    public static final String TAG_ORIGINAL_ITEM = "org";
    public static final String TAG_GEM_ITEM = "gem";


    public BaseGemTool() {
        super(new Item.Properties().defaultMaxDamage(1).group(ExTerraTools.TOOLS_TAB));
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        Item base = getBase(stack);
        Gem gem = getGemType(stack);
        if(base == null)
            return 1;
        if(!ToolHelper.isValidTool(base.getDefaultInstance(), state))
            return 1;
        if(gem == null)
            return base.getDestroySpeed(stack, state);
        return gem.getMiningSpeedModifier() + base.getDestroySpeed(stack, state);
    }



    @Override
    public ItemStack getDefaultInstance() {
        return this.forTool(Items.BUCKET.getDefaultInstance(), ExTerraRegistries.GEM_REGISTRY.get().getValue(ExTerra.getResource("ruby")));
    }


    /**
     * Get the gem for a stack
     * @param stack The stack
     * @return The gem
     */
    public Gem getGemType(ItemStack stack){
        CompoundNBT nbt;
        if(!stack.hasTag())
            return null;
        nbt = stack.getTag();
        if(nbt == null)
            return null;
        if(!nbt.contains(BaseGemTool.TAG_GEM_ITEM))
            return null;
        String gem = nbt.getString(BaseGemTool.TAG_GEM_ITEM);
        return ExTerraRegistries.GEM_REGISTRY.get().getValue(new ResourceLocation(gem));
    }

    /**
     * Create ItemStack for a tool
     * @param tool The tool
     * @param gem The gem to apply
     * @return The new tool
     */
    public ItemStack forTool(ItemStack tool, Gem gem){
        ItemStack out = new ItemStack(this, 1);
        CompoundNBT nbt;
        if(tool.hasTag())
            nbt =  tool.getTag();
        else
            nbt = new CompoundNBT();
        nbt.putString(BaseGemTool.TAG_ORIGINAL_ITEM, tool.getItem().getRegistryName().toString());
        nbt.putString(BaseGemTool.TAG_GEM_ITEM, gem.getRegistryName().toString());
        out.setTag(nbt);
        return out;
    }

    /**
     * Get the base tool for a stack
     * @param tool The stack
     * @return The tool
     */
    public Item getBase(ItemStack tool){
        CompoundNBT nbt;
        if(tool.hasTag())
            nbt = tool.getTag();
        else
            return Items.BUCKET;

        if(nbt.contains(BaseGemTool.TAG_ORIGINAL_ITEM)) {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt.getString(BaseGemTool.TAG_ORIGINAL_ITEM)));
            if(item == null)
                return Items.BUCKET;
            else
                return item;
        }

        return Items.BUCKET;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        Item base = getBase(toRepair);
        return base.getIsRepairable(base.getDefaultInstance(), repair);
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    /**
     * Get the attack damage for a stack
     * @param stack The stack
     * @return The attack damage
     */
    public float getAttackDamage(ItemStack stack){
        Multimap<Attribute, AttributeModifier> attributes = getParentsAttributes(stack);
        if(attributes == null || !attributes.containsKey(Attributes.ATTACK_DAMAGE))
            return 0;
        Gem gem = this.getGemType(stack);
        if(gem == null)
            return (float) attributes.get(Attributes.ATTACK_DAMAGE).stream().findFirst().get().getAmount();
        return gem.getAttackDamageModifier() + (float) attributes.get(Attributes.ATTACK_DAMAGE).stream().findFirst().get().getAmount();
    }

    /**
     * Get the attack speed for a stack
     * @param stack The stack
     * @return The attack speed
     */
    public float getAttackSpeed(ItemStack stack){
        Multimap<Attribute, AttributeModifier> attributes = getParentsAttributes(stack);
        if(attributes == null || !attributes.containsKey(Attributes.ATTACK_SPEED))
            return 0;
        Gem gem = this.getGemType(stack);
        if(gem == null)
            return (float) attributes.get(Attributes.ATTACK_SPEED).stream().findFirst().get().getAmount();
        double baseSpeed = attributes.get(Attributes.ATTACK_SPEED).stream().findFirst().get().getAmount();
        return (float) (gem.getAttackSpeedModifier() + baseSpeed);
    }

    /**
     * Get the attributes of the parent
     * @param stack The stack
     * @return The map of attributes
     */
    public Multimap<Attribute, AttributeModifier> getParentsAttributes(ItemStack stack){
        Item base = this.getBase(stack);
        if(base == null)
            return null;
        return base.getAttributeModifiers(EquipmentSlotType.MAINHAND, base.getDefaultInstance());
    }

    /**
     * Get the value of an attribute of the parent
     * @param stack The stack
     * @param attribute The attribute
     * @return The value
     */
    public Optional<Double> getPatentsAttributeValue(ItemStack stack, Attribute attribute){
        Multimap<Attribute, AttributeModifier> attributes = getParentsAttributes(stack);
        if(!attributes.containsKey(attribute))
            return Optional.empty();
        if(!attributes.get(attribute).stream().findFirst().isPresent())
            return Optional.empty();

        return Optional.of(attributes.get(attribute).stream().findFirst().get().getAmount());
    }

    /**
     * Get the parent attributes of the parent for a given stack and slot
     * @param stack The stack
     * @param slot The slot
     * @return The map of attributes
     */
    public Multimap<Attribute, AttributeModifier> getParentsAttributes(ItemStack stack, EquipmentSlotType slot){
        Item base = this.getBase(stack);
        if(base == null)
            return null;
        return base.getAttributeModifiers(slot, base.getDefaultInstance());
    }


    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        Item base = this.getBase(stack);
        if(base == null)
            return new TranslationTextComponent("error.exterra.gemtool");

        Gem gem = this.getGemType(stack);
        if(gem == null)
            return new TranslationTextComponent("error.exterra.gemtool");
            //

        String out = I18n.format(gem.getTranslationKey()) + " " + I18n.format("info.exterra.infused") + " " + I18n.format(base.getTranslationKey());
        return new StringTextComponent(out);
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot, ItemStack stack) {
        if(equipmentSlot == EquipmentSlotType.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", this.getAttackDamage(stack), AttributeModifier.Operation.ADDITION));
            float attackSpeed = this.getAttackSpeed(stack);
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
            if(attackSpeed == 0 && stack.getItem() instanceof GemShovel)
                ExTerra.LOGGER.info(stack.getDisplayName() + " attackspeed is null");
            return builder.build();
        }
        return super.getAttributeModifiers(equipmentSlot,stack);
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        //Copied from mekanism
        if(attacker instanceof PlayerEntity){
            target.attackEntityFrom(DamageSource.causePlayerDamage((PlayerEntity) attacker), getAttackDamage(stack));
        }
        else{
            target.attackEntityFrom(DamageSource.causeMobDamage(attacker), getAttackDamage(stack));
        }


        stack.damageItem(1, attacker, (p_220039_0_) -> {
            p_220039_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
        });
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, BlockState state, BlockPos pos, LivingEntity entityLiving) {
        if (!worldIn.isRemote && state.getBlockHardness(worldIn, pos) != 0.0F) {
            stack.damageItem(1, entityLiving, (p_220038_0_) -> {
                p_220038_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
            });
        }

        return true;
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        Item base = getBase(stack);
        if(base == null)
            return 1;
        Gem gem = getGemType(stack);
        if(gem == null)
            return base.getMaxDamage(base.getDefaultInstance());
        int modifier = gem.getMaxDamageModifier();
        if(modifier < 0)
            modifier = 0;
        return base.getMaxDamage(base.getDefaultInstance()) + modifier;
    }
}
