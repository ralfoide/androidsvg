package com.caverock.androidsvg;

import org.xml.sax.SAXException;

import java.util.List;

public interface SvgContainer {
    List<SvgObject> getChildren();

    void addChild(SvgObject elem) throws SAXException;
}
