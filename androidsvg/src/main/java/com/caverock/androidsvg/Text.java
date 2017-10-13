package com.caverock.androidsvg;

import android.graphics.Matrix;

class Text extends TextPositionedContainer implements TextRoot, HasTransform {
    Matrix transform;

    @Override
    public void setTransform(Matrix transform) {
        this.transform = transform;
    }
}
