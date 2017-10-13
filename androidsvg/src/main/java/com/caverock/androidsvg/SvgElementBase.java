package com.caverock.androidsvg;

import java.util.List;

/** Any object in the tree that corresponds to an SVG element */
class SvgElementBase extends SvgObject {
    String id = null;
    Boolean spacePreserve = null;
    Style baseStyle = null;   // style defined by explicit style attributes in the element (eg. fill="black")
    Style style = null;       // style expressed in a 'style' attribute (eg. style="fill:black")
    List<String> classNames = null;  // contents of the 'class' attribute
}
