package com.caverock.androidsvg.tag;

import com.caverock.androidsvg.Length;
import com.caverock.androidsvg.NotDirectlyRendered;
import com.caverock.androidsvg.SvgConditionalContainer;

public class Mask extends SvgConditionalContainer implements NotDirectlyRendered {
    public Boolean maskUnitsAreUser;
    public Boolean maskContentUnitsAreUser;
    public Length x;
    public Length y;
    public Length width;
    public Length height;
}
