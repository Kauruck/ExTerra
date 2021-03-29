package com.kauruck.exterra.Init;

import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.LootModifiers.Autosmelt;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;

public class ModDataProvider extends GlobalLootModifierProvider {
    public ModDataProvider(DataGenerator gen, String modid) {
        super(gen, modid);
    }

    @Override
    protected void start() {
        ResourceLocation rubyTagId = new ResourceLocation(ExTerra.MOD_ID, "gemRuby");
        ITag<Item> rubyTag = ItemTags.getCollection().get(rubyTagId);
        ILootCondition[] conditions = new ILootCondition[1];
        conditions[0] = new MatchTool(ItemPredicate.Builder.create().tag(rubyTag).build());
        add("autosmelt", ModGlobalLootModifiers.AUTOSMELT, new Autosmelt(conditions));
    }
}
