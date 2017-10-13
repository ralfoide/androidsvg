package com.caverock.androidsvg.tag;

import com.caverock.androidsvg.Length;
import com.caverock.androidsvg.NotDirectlyRendered;
import com.caverock.androidsvg.SvgViewBoxContainer;

public class Marker extends SvgViewBoxContainer implements NotDirectlyRendered {
    public boolean markerUnitsAreUser;
    public Length refX;
    public Length refY;
    public Length markerWidth;
    public Length markerHeight;
    public Float orient;
}
