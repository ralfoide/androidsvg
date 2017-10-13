package com.caverock.androidsvg;

class TextPath extends TextContainer implements TextChild {
    String href;
    Length startOffset;

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
