package com.caverock.androidsvg;

class PaintReference extends SvgPaint {
    String href;
    SvgPaint fallback;

    PaintReference(String href, SvgPaint fallback) {
        this.href = href;
        this.fallback = fallback;
    }

    public String toString() {
        return href + " " + fallback;
    }
}
