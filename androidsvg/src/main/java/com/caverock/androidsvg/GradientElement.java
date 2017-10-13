package com.caverock.androidsvg;

import android.graphics.Matrix;
import com.caverock.androidsvg.tag.Stop;
import org.xml.sax.SAXException;

import java.util.ArrayList;
import java.util.List;

class GradientElement extends SvgElementBase implements SvgContainer {
    List<SvgObject> children = new ArrayList<>();

    Boolean gradientUnitsAreUser;
    Matrix gradientTransform;
    GradientSpread spreadMethod;
    String href;

    @Override
    public List<SvgObject> getChildren() {
        return children;
    }

    @Override
    public void addChild(SvgObject elem) throws SAXException {
        if (elem instanceof Stop) {
            children.add(elem);
        } else {
            throw new SAXException("Gradient elements cannot contain " + elem + " elements.");
        }
    }
}
