package com.caverock.androidsvg.tag;

import android.graphics.Matrix;
import com.caverock.androidsvg.HasTransform;
import com.caverock.androidsvg.SvgConditionalContainer;

/** An SVG element that can contain other elements. */
public class Group extends SvgConditionalContainer implements HasTransform {
    public Matrix transform;

    @Override
    public void setTransform(Matrix transform) {
        this.transform = transform;
    }
}
