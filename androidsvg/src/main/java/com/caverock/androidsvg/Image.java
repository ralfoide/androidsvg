package com.caverock.androidsvg;

import android.graphics.Matrix;

class Image extends SvgPreserveAspectRatioContainer implements HasTransform {
    String href;
    Length x;
    Length y;
    Length width;
    Length height;
    Matrix transform;

    @Override
    public void setTransform(Matrix transform) {
        this.transform = transform;
    }
}
