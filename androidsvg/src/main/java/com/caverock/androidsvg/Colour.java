package com.caverock.androidsvg;

public class Colour extends SvgPaint {
    public int colour;

    public static final Colour BLACK = new Colour(0xff000000);  // Black singleton - a common default value.

    Colour(int val) {
        this.colour = val;
    }

    public String toString() {
        return String.format("#%08x", colour);
    }
}
