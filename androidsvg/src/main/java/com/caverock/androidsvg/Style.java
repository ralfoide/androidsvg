package com.caverock.androidsvg;

import java.util.List;

public class Style implements Cloneable {
    // Which properties have been explicitly specified by this element
    public long specifiedFlags = 0;

    public SvgPaint fill;
    public FillRule fillRule;
    public Float fillOpacity;

    public SvgPaint stroke;
    public Float strokeOpacity;
    public Length strokeWidth;
    public LineCaps strokeLineCap;
    public LineJoin strokeLineJoin;
    public Float strokeMiterLimit;
    public Length[] strokeDashArray;
    public Length strokeDashOffset;

    public Float opacity; // master opacity of both stroke and fill

    public Colour color;

    public List<String> fontFamily;
    public Length fontSize;
    public Integer fontWeight;
    public FontStyle fontStyle;
    public TextDecoration textDecoration;
    public TextDirection direction;

    public TextAnchor textAnchor;

    public Boolean overflow;  // true if overflow visible
    public CSSClipRect clip;

    public String markerStart;
    public String markerMid;
    public String markerEnd;

    public Boolean display;    // true if we should display
    public Boolean visibility; // true if visible

    public SvgPaint stopColor;
    public Float stopOpacity;

    public String clipPath;
    public FillRule clipRule;

    public String mask;

    public SvgPaint solidColor;
    public Float solidOpacity;

    public SvgPaint viewportFill;
    public Float viewportFillOpacity;

    public VectorEffect vectorEffect;

    public RenderQuality imageRendering;


    public static final int FONT_WEIGHT_NORMAL = 400;
    public static final int FONT_WEIGHT_BOLD = 700;
    public static final int FONT_WEIGHT_LIGHTER = -1;
    public static final int FONT_WEIGHT_BOLDER = +1;


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

    public static Style getDefaultStyle() {
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
    public void resetNonInheritingProperties(boolean isRootSVG) {
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
    public Object clone() throws CloneNotSupportedException {
        Style obj = (Style) super.clone();
        if (strokeDashArray != null) {
            obj.strokeDashArray = strokeDashArray.clone();
        }
        return obj;
    }
}
