package com.caverock.androidsvg;

class TRef extends TextContainer implements TextChild {
    String href;

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
