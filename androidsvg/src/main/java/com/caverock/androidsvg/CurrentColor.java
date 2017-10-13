package com.caverock.androidsvg;

/** Special version of Colour that indicates use of 'currentColor' keyword */
class CurrentColor extends SvgPaint {
    private static CurrentColor instance = new CurrentColor();

    private CurrentColor() {
    }

    static CurrentColor getInstance() {
        return instance;
    }
}
