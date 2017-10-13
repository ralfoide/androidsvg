package com.caverock.androidsvg;

class Colour extends SvgPaint {
    int colour;

    static final Colour BLACK = new Colour(0xff000000);  // Black singleton - a common default value.

    Colour(int val) {
        this.colour = val;
    }

    public String toString() {
        return String.format("#%08x", colour);
    }
}
