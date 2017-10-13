package com.caverock.androidsvg.text;

public class TRef extends TextContainer implements TextChild {
    public String href;

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
