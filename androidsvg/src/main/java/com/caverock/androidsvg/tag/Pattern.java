package com.caverock.androidsvg.tag;

import android.graphics.Matrix;
import com.caverock.androidsvg.Length;
import com.caverock.androidsvg.NotDirectlyRendered;
import com.caverock.androidsvg.SvgViewBoxContainer;

public class Pattern extends SvgViewBoxContainer implements NotDirectlyRendered {
    public Boolean patternUnitsAreUser;
    public Boolean patternContentUnitsAreUser;
    public Matrix patternTransform;
    public Length x;
    public Length y;
    public Length width;
    public Length height;
    public String href;
}
