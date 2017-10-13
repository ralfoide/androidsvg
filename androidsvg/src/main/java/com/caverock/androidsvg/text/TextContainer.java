package com.caverock.androidsvg.text;

import com.caverock.androidsvg.SvgConditionalContainer;
import com.caverock.androidsvg.SvgObject;
import org.xml.sax.SAXException;

public class TextContainer extends SvgConditionalContainer {
    @Override
    public void addChild(SvgObject elem) throws SAXException {
        if (elem instanceof TextChild) {
            children.add(elem);
        } else {
            throw new SAXException("Text content elements cannot contain " + elem + " elements.");
        }
    }
}
