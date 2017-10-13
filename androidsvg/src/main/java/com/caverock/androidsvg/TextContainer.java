package com.caverock.androidsvg;

import org.xml.sax.SAXException;

class TextContainer extends SvgConditionalContainer {
    @Override
    public void addChild(SvgObject elem) throws SAXException {
        if (elem instanceof TextChild) {
            children.add(elem);
        } else {
            throw new SAXException("Text content elements cannot contain " + elem + " elements.");
        }
    }
}
