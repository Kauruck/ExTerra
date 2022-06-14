package com.kauruck.exterra.util;

public class MathUtil {

    //From https://stackoverflow.com/questions/9090500/how-to-compare-that-sequence-of-doubles-are-all-approximately-equal-in-java
    public static boolean almostEqual(float a, float b, float eps){
        if(a == b)
            return true;
        if(a == 0)
            return Math.abs((b - a)/b)<eps;
        return Math.abs((a-b)/a)<eps;
    }
}
