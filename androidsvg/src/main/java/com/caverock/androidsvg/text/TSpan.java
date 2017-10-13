package com.caverock.androidsvg.text;

public class TSpan extends TextPositionedContainer implements TextChild {
    private TextRoot textRoot;

    @Override
    public void setTextRoot(TextRoot obj) {
        this.textRoot = obj;
    }

    @Override
    public TextRoot getTextRoot() {
        return this.textRoot;
    }
}
