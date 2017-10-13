package com.caverock.androidsvg.tag;

import com.caverock.androidsvg.SvgContainer;
import com.caverock.androidsvg.SvgElementBase;
import com.caverock.androidsvg.SvgObject;
import org.xml.sax.SAXException;

import java.util.Collections;
import java.util.List;

public class Stop extends SvgElementBase implements SvgContainer {
    public Float offset;

    // Dummy container methods. Stop is officially a container, but we
    // are not interested in any of its possible child elements.
    @Override
    public List<SvgObject> getChildren() {
        return Collections.emptyList();
    }

    @Override
    public void addChild(SvgObject elem) throws SAXException { /* do nothing */ }
}
