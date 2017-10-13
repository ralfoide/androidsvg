package com.caverock.androidsvg.text;

import android.graphics.Matrix;
import com.caverock.androidsvg.HasTransform;

public class Text extends TextPositionedContainer implements TextRoot, HasTransform {
    public Matrix transform;

    @Override
    public void setTransform(Matrix transform) {
        this.transform = transform;
    }
}
