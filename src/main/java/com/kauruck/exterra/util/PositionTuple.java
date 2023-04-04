package com.kauruck.exterra.util;

public class PositionTuple {

    int x = 0;
    int y = 0;


    public PositionTuple(int x, int y){
        this.x = x;
        this.y = y;
    }

    public static PositionTuple of(int x, int y) {
        return new PositionTuple(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof PositionTuple))
            return false;

        return ((PositionTuple) obj).x == this.x && ((PositionTuple) obj).y == this.y;
    }
}
