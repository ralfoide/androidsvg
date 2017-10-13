package com.caverock.androidsvg;

import org.xml.sax.SAXException;

import java.util.Collections;
import java.util.List;

class Stop extends SvgElementBase implements SvgContainer {
    Float offset;

    // Dummy container methods. Stop is officially a container, but we
    // are not interested in any of its possible child elements.
    @Override
    public List<SvgObject> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public void addChild(SvgObject elem) throws SAXException { /* do nothing */ }
}
