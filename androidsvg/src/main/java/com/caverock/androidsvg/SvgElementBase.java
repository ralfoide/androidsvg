package com.caverock.androidsvg;

import java.util.List;

/**
 * Any object in the tree that corresponds to an SVG element
 */
public class SvgElementBase extends SvgObject {
    public String id = null;
    public Boolean spacePreserve = null;
    /**
     * Style defined by explicit style attributes in the element (eg. fill="black")
     */
    public Style baseStyle = null;
    /**
     * style expressed in a 'style' attribute (eg. style="fill:black")
     */
    public Style style = null;
    /**
     * contents of the 'class' attribute
     */
    public List<String> classNames = null;
}
