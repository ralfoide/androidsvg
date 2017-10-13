package com.caverock.androidsvg;

/** Any object that can be part of the tree */
public class SvgObject {
    public SVG document;
    public SvgContainer parent;

    public String toString() {
        return this.getClass().getSimpleName();
        //return super.toString();
    }
}
