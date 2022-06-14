package com.kauruck.exterra.util;

public class PositionsUtil {

    public static int arrayIndexFromName(Character blockPosName){
        //Force all characters to be uppercase
        blockPosName = Character.toUpperCase(blockPosName);
        //convert it to an int
        int index = blockPosName - 65;
        //ensure it is in range
        if(index >= 90 || index < 0)
            throw new IllegalArgumentException("The argument can only be A-Z");

        return index;
    }

    public static Character blockPosNameFromIndex(int index){
        if(index >= 90 || index < 0)
            throw new IllegalArgumentException("The index can only be between 0 and 90");

        char out = (char) (index + 65);
        return Character.toUpperCase(out);
    }
}
