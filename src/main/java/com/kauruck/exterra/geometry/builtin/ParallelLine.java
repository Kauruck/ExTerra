package com.kauruck.exterra.geometry.builtin;

import com.google.gson.JsonElement;
import com.kauruck.exterra.ExTerra;
import com.kauruck.exterra.api.geometry.GeometricRule;
import com.kauruck.exterra.geometry.BlockPosHolder;
import com.kauruck.exterra.util.MathUtil;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ParallelLine extends GeometricRule {
    public ParallelLine() {
        super( "parallel");
    }

    public ParallelLine(Character pointA, Character pointB, Character pointC, Character pointD) {
        super( "parallel");
        this.expectedBlockPos = new Character[]{pointA, pointB, pointC, pointD};
    }

    /**
     * Test weather line A to B is parallel to C to D
     * @param positions The positions
     * @return Weather they are parallel
     */
    @Override
    public boolean test(BlockPosHolder blockPos) {
        //AB || CD
        //AB
        float A_dx = blockPos.getBlockPos('A').getX() - blockPos.getBlockPos('B').getX();
        float A_dy = blockPos.getBlockPos('A').getY() - blockPos.getBlockPos('B').getY();
        float A_dz = blockPos.getBlockPos('A').getZ() - blockPos.getBlockPos('B').getZ();
        //CD
        float C_dx = blockPos.getBlockPos('C').getX() - blockPos.getBlockPos('D').getX();
        float C_dy = blockPos.getBlockPos('C').getY() - blockPos.getBlockPos('D').getY();
        float C_dz = blockPos.getBlockPos('C').getZ() - blockPos.getBlockPos('D').getZ();
        //Parallel test
        float nx = A_dx/C_dx;
        float ny = A_dy/C_dy;
        float nz = A_dz/C_dz;
        boolean parallel = false;
        //Test weather the both of the parts are zero
        if(A_dx == 0 && C_dx == 0){
            if((A_dy == 0 && C_dy == 0) || (A_dz == 0 && C_dz == 0))
                parallel = true;
            else
            //Ignore the x component
                parallel = MathUtil.almostEqual(ny, nz, this.epsilon);
        }
        if(A_dy == 0 ||  C_dy == 0) {
            if((A_dx == 0 && C_dx == 0) || (A_dz == 0 && C_dz == 0))
                parallel = true;
            else
            //Ignore the y component
                parallel = MathUtil.almostEqual(nx, nz, this.epsilon);
        }
        if(A_dz == 0 || C_dz == 0){
            if((A_dy == 0 && C_dy == 0) || (A_dx == 0 && C_dx == 0))
                parallel =  true;
            else
            //Ignore the z component
                parallel = MathUtil.almostEqual(ny, nx, this.epsilon);
        }
        if(!parallel)
        //If no component is zero, all most be (almost) equal
            parallel =  MathUtil.almostEqual(nx, ny, this.epsilon) && MathUtil.almostEqual(nx, nz, this.epsilon);

        return parallel;
    }

    @Override
    public ResourceLocation getName() {
        return ExTerra.getResource("parallel_line");
    }

    @Override
    protected Map<String, JsonElement> getParameters() {
        return new HashMap<>();
    }

    @Override
    public void offerParameter(String parameterName, JsonElement jsonElement) {

    }
}
