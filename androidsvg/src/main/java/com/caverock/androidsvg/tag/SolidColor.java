package com.caverock.androidsvg.tag;

import com.caverock.androidsvg.SvgContainer;
import com.caverock.androidsvg.SvgElementBase;
import com.caverock.androidsvg.SvgObject;
import org.xml.sax.SAXException;

import java.util.Collections;
import java.util.List;

public class SolidColor extends SvgElementBase implements SvgContainer {
    // Not needed right now. Colour is set in this.baseStyle.
    //public Length  solidColor;
    //public Length  solidOpacity;

    // Dummy container methods. Stop is officially a container, but we
    // are not interested in any of its possible child elements.
    @Override
    public List<SvgObject> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public void addChild(SvgObject elem) throws SAXException { /* do nothing */ }
}
