package com.caverock.androidsvg;

import android.graphics.Matrix;

class Pattern extends SvgViewBoxContainer implements NotDirectlyRendered {
    Boolean patternUnitsAreUser;
    Boolean patternContentUnitsAreUser;
    Matrix patternTransform;
    Length x;
    Length y;
    Length width;
    Length height;
    String href;
}
