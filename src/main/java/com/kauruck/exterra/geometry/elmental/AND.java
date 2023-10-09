package com.kauruck.exterra.geometry.elmental;


import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.geometry.ElementalOperation;
import com.kauruck.exterra.api.geometry.GeometricRule;
import com.kauruck.exterra.api.geometry.IGeometricTest;
import com.kauruck.exterra.commands.ExTerraCommands;
import com.kauruck.exterra.geometry.BlockPosHolder;
import net.minecraft.resources.ResourceLocation;


public class AND extends ElementalOperation {

    @Override
    public boolean test(BlockPosHolder positions) {
        for(IGeometricTest currentTest : this.parts){
            BlockPosHolder subHolder;
            if(currentTest instanceof GeometricRule){
                subHolder = positions.subHolder(currentTest.expectedBlockPosNames());
            }else{
                subHolder = positions;
            }

            if(!currentTest.test(subHolder))
                return false;
        }

        return true;
    }

    @Override
    public ResourceLocation getName() {
        return ExTerra.getResource("and");
    }

}
