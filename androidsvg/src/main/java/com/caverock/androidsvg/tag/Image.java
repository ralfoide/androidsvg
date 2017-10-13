package com.caverock.androidsvg.tag;

import android.graphics.Matrix;
import com.caverock.androidsvg.HasTransform;
import com.caverock.androidsvg.Length;
import com.caverock.androidsvg.SvgPreserveAspectRatioContainer;

public class Image extends SvgPreserveAspectRatioContainer implements HasTransform {
    public String href;
    public Length x;
    public Length y;
    public Length width;
    public Length height;
    public Matrix transform;

    @Override
    public void setTransform(Matrix transform) {
        this.transform = transform;
    }
}
