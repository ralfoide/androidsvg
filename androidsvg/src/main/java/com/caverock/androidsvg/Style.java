package com.caverock.androidsvg;

import java.util.List;

class Style implements Cloneable {
    // Which properties have been explicitly specified by this element
    long specifiedFlags = 0;

    SvgPaint fill;
    FillRule fillRule;
    Float fillOpacity;

    SvgPaint stroke;
    Float strokeOpacity;
    Length strokeWidth;
    LineCaps strokeLineCap;
    LineJoin strokeLineJoin;
    Float strokeMiterLimit;
    Length[] strokeDashArray;
    Length strokeDashOffset;

    Float opacity; // master opacity of both stroke and fill

    Colour color;

    List<String> fontFamily;
    Length fontSize;
    Integer fontWeight;
    FontStyle fontStyle;
    TextDecoration textDecoration;
    TextDirection direction;

    TextAnchor textAnchor;

    Boolean overflow;  // true if overflow visible
    CSSClipRect clip;

    String markerStart;
    String markerMid;
    String markerEnd;

    Boolean display;    // true if we should display
    Boolean visibility; // true if visible

    SvgPaint stopColor;
    Float stopOpacity;

    String clipPath;
    FillRule clipRule;

    String mask;

    SvgPaint solidColor;
    Float solidOpacity;

    SvgPaint viewportFill;
    Float viewportFillOpacity;

    VectorEffect vectorEffect;

    RenderQuality imageRendering;


    static final int FONT_WEIGHT_NORMAL = 400;
    static final int FONT_WEIGHT_BOLD = 700;
    static final int FONT_WEIGHT_LIGHTER = -1;
    static final int FONT_WEIGHT_BOLDER = +1;


    public enum FillRule {
        NonZero,
        EvenOdd
    }

    public enum LineCaps {
        Butt,
        Round,
        Square
    }

    public enum LineJoin {
        Miter,
        Round,
        Bevel
    }

    public enum FontStyle {
        Normal,
        Italic,
        Oblique
    }

    public enum TextAnchor {
        Start,
        Middle,
        End
    }

    public enum TextDecoration {
        None,
        Underline,
        Overline,
        LineThrough,
        Blink
    }

    public enum TextDirection {
        LTR,
        RTL
    }

    public enum VectorEffect {
        None,
        NonScalingStroke
    }

    public enum RenderQuality {
        auto,
        optimizeQuality,
        optimizeSpeed
    }

    static Style getDefaultStyle() {
        Style def = new Style();
        def.specifiedFlags = SVG.SPECIFIED_ALL;
        //def.inheritFlags = 0;
        def.fill = Colour.BLACK;
        def.fillRule = FillRule.NonZero;
        def.fillOpacity = 1f;
        def.stroke = null;         // none
        def.strokeOpacity = 1f;
        def.strokeWidth = new Length(1f);
        def.strokeLineCap = LineCaps.Butt;
        def.strokeLineJoin = LineJoin.Miter;
        def.strokeMiterLimit = 4f;
        def.strokeDashArray = null;
        def.strokeDashOffset = new Length(0f);
        def.opacity = 1f;
        def.color = Colour.BLACK; // currentColor defaults to black
        def.fontFamily = null;
        def.fontSize = new Length(12, Unit.pt);
        def.fontWeight = FONT_WEIGHT_NORMAL;
        def.fontStyle = FontStyle.Normal;
        def.textDecoration = TextDecoration.None;
        def.direction = TextDirection.LTR;
        def.textAnchor = TextAnchor.Start;
        def.overflow = true;  // Overflow shown/visible for root, but not for other elements (see section 14.3.3).
        def.clip = null;
        def.markerStart = null;
        def.markerMid = null;
        def.markerEnd = null;
        def.display = Boolean.TRUE;
        def.visibility = Boolean.TRUE;
        def.stopColor = Colour.BLACK;
        def.stopOpacity = 1f;
        def.clipPath = null;
        def.clipRule = FillRule.NonZero;
        def.mask = null;
        def.solidColor = null;
        def.solidOpacity = 1f;
        def.viewportFill = null;
        def.viewportFillOpacity = 1f;
        def.vectorEffect = VectorEffect.None;
        def.imageRendering = RenderQuality.auto;
        return def;
    }


    // Called on the state.style object to reset the properties that don't inherit
    // from the parent style.
    void resetNonInheritingProperties(boolean isRootSVG) {
        this.display = Boolean.TRUE;
        this.overflow = isRootSVG ? Boolean.TRUE : Boolean.FALSE;
        this.clip = null;
        this.clipPath = null;
        this.opacity = 1f;
        this.stopColor = Colour.BLACK;
        this.stopOpacity = 1f;
        this.mask = null;
        this.solidColor = null;
        this.solidOpacity = 1f;
        this.viewportFill = null;
        this.viewportFillOpacity = 1f;
        this.vectorEffect = VectorEffect.None;
    }


    @Override
    protected Object clone() throws CloneNotSupportedException {
        Style obj = (Style) super.clone();
        if (strokeDashArray != null) {
            obj.strokeDashArray = strokeDashArray.clone();
        }
        return obj;
    }
}
