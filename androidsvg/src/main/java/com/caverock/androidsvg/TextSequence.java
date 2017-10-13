package com.caverock.androidsvg;

class TextSequence extends SvgObject implements TextChild {
    String text;

    private TextRoot textRoot;

    TextSequence(String text) {
        this.text = text;
    }

    public String toString() {
        return this.getClass().getSimpleName() + " '" + text + "'";
    }

    @Override
    public void setTextRoot(TextRoot obj) {
        this.textRoot = obj;
    }

    @Override
    public TextRoot getTextRoot() {
        return this.textRoot;
    }
}
