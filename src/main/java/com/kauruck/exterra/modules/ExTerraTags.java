package com.kauruck.exterra.modules;


import com.kauruck.exterra.ExTerra;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ExTerraTags {
    public static final TagKey<Block> RITUAL_TIER_I = BlockTags.create(ExTerra.getResource("rituals/tier_i"));
    public static final TagKey<Block> MATTER_WIRE = BlockTags.create(ExTerra.getResource("rituals/wire"));
}
