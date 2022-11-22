package com.kauruck.exterra.fx;

public class MathHelper {

    //From StackOverflow: https://stackoverflow.com/questions/16656651/does-java-have-a-clamp-function
    //Why does java not have this?
    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
}
