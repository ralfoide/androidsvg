package com.caverock.androidsvg;

import android.graphics.Matrix;

/** An SVG element that can contain other elements. */
class Group extends SvgConditionalContainer implements HasTransform {
    Matrix transform;

    @Override
    public void setTransform(Matrix transform) {
        this.transform = transform;
    }
}
