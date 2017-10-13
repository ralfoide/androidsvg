/*
   Copyright 2013 Paul LeBeau, Cave Rock Software Ltd.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package com.caverock.androidsvg;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.RectF;
import android.util.Log;

import com.caverock.androidsvg.CSSParser.Ruleset;

import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * AndroidSVG is a library for reading, parsing and rendering SVG documents on Android devices.
 * <p>
 * All interaction with AndroidSVG is via this class.
 * <p>
 * Typically, you will call one of the SVG loading and parsing classes then call the renderer,
 * passing it a canvas to draw upon.
 * <p>
 * <h4>Usage summary</h4>
 * <p>
 * <ul>
 * <li>Use one of the static {@code getFromX()} methods to read and parse the SVG file.  They will
 * return an instance of this class.
 * <li>Call one of the {@code renderToX()} methods to render the document.
 * </ul>
 * <p>
 * <h4>Usage example</h4>
 * <p>
 * <pre>
 * {@code
 * SVG  svg = SVG.getFromAsset(getContext().getAssets(), svgPath);
 * svg.registerExternalFileResolver(myResolver);
 *
 * Bitmap  newBM = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
 * Canvas  bmcanvas = new Canvas(newBM);
 * bmcanvas.drawRGB(255, 255, 255);  // Clear background to white
 *
 * svg.renderToCanvas(bmcanvas);
 * }
 * </pre>
 * <p>
 * For more detailed information on how to use this library, see the documentation at {@code http://code.google.com/p/androidsvg/}
 */
public class SVG {
    static final String TAG = "AndroidSVG";

    static final String VERSION = "1.2.3-beta-1";

    static final int DEFAULT_PICTURE_WIDTH = 512;
    static final int DEFAULT_PICTURE_HEIGHT = 512;

    static final double SQRT2 = 1.414213562373095;


    private SvgViewBox rootElement = null;

    // Metadata
    private String title = "";
    private String desc = "";

    // Resolver
    private SVGExternalFileResolver fileResolver = null;

    // DPI to use for rendering
    private float renderDPI = 96f;   // default is 96

    // CSS rules
    private Ruleset cssRules = new Ruleset();

    // Map from id attribute to element
    private Map<String, SvgElementBase> idToElementMap = new HashMap<>();


    SVG() {
    }


    /**
     * Read and parse an SVG from the given {@code InputStream}.
     *
     * @param is the input stream from which to read the file.
     * @return an SVG instance on which you can call one of the render methods.
     * @throws SVGParseException if there is an error parsing the document.
     */
    @SuppressWarnings("WeakerAccess")
    public static SVG getFromInputStream(InputStream is) throws SVGParseException {
        SVGParser parser = new SVGParser();
        return parser.parse(is);
    }


    /**
     * Read and parse an SVG from the given {@code String}.
     *
     * @param svg the String instance containing the SVG document.
     * @return an SVG instance on which you can call one of the render methods.
     * @throws SVGParseException if there is an error parsing the document.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public static SVG getFromString(String svg) throws SVGParseException {
        SVGParser parser = new SVGParser();
        return parser.parse(new ByteArrayInputStream(svg.getBytes()));
    }


    /**
     * Read and parse an SVG from the given resource location.
     *
     * @param context    the Android context of the resource.
     * @param resourceId the resource identifier of the SVG document.
     * @return an SVG instance on which you can call one of the render methods.
     * @throws SVGParseException if there is an error parsing the document.
     */
    @SuppressWarnings("WeakerAccess")
    public static SVG getFromResource(Context context, int resourceId) throws SVGParseException {
        return getFromResource(context.getResources(), resourceId);
    }


    /**
     * Read and parse an SVG from the given resource location.
     *
     * @param resources  the set of Resources in which to locate the file.
     * @param resourceId the resource identifier of the SVG document.
     * @return an SVG instance on which you can call one of the render methods.
     * @throws SVGParseException if there is an error parsing the document.
     */
    @SuppressWarnings("WeakerAccess")
    public static SVG getFromResource(Resources resources, int resourceId) throws SVGParseException {
        SVGParser parser = new SVGParser();
        InputStream is = resources.openRawResource(resourceId);
        try {
            return parser.parse(is);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // Do nothing
            }
        }
    }


    /**
     * Read and parse an SVG from the assets folder.
     *
     * @param assetManager the AssetManager instance to use when reading the file.
     * @param filename     the filename of the SVG document within assets.
     * @return an SVG instance on which you can call one of the render methods.
     * @throws SVGParseException if there is an error parsing the document.
     * @throws IOException       if there is some IO error while reading the file.
     */
    @SuppressWarnings("WeakerAccess")
    public static SVG getFromAsset(AssetManager assetManager, String filename) throws SVGParseException, IOException {
        SVGParser parser = new SVGParser();
        InputStream is = assetManager.open(filename);
        try {
            return parser.parse(is);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                // Do nothing
            }
        }
    }


    //===============================================================================


    /**
     * Register an {@link SVGExternalFileResolver} instance that the renderer should use when resolving
     * external references such as images and fonts.
     *
     * @param fileResolver the resolver to use.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public void registerExternalFileResolver(SVGExternalFileResolver fileResolver) {
        this.fileResolver = fileResolver;
    }


    /**
     * Set the DPI (dots-per-inch) value to use when rendering.  The DPI setting is used in the
     * conversion of "physical" units - such an "pt" or "cm" - to pixel values.  The default DPI is 96.
     * <p>
     * You should not normally need to alter the DPI from the default of 96 as recommended by the SVG
     * and CSS specifications.
     *
     * @param dpi the DPI value that the renderer should use.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public void setRenderDPI(float dpi) {
        this.renderDPI = dpi;
    }


    /**
     * Get the current render DPI setting.
     *
     * @return the DPI value
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public float getRenderDPI() {
        return renderDPI;
    }


    //===============================================================================
    // SVG document rendering to a Picture object (indirect rendering)


    /**
     * Renders this SVG document to a Picture object.
     * <p>
     * An attempt will be made to determine a suitable initial viewport from the contents of the SVG file.
     * If an appropriate viewport can't be determined, a default viewport of 512x512 will be used.
     *
     * @return a Picture object suitable for later rendering using {@code Canvas.drawPicture()}
     */
    @SuppressWarnings("WeakerAccess")
    public Picture renderToPicture() {
        // Determine the initial viewport. See SVG spec section 7.2.
        Length width = rootElement.width;
        if (width != null) {
            float w = width.floatValue(this.renderDPI);
            float h;
            Box rootViewBox = rootElement.viewBox;

            if (rootViewBox != null) {
                h = w * rootViewBox.height / rootViewBox.width;
            } else {
                Length height = rootElement.height;
                if (height != null) {
                    h = height.floatValue(this.renderDPI);
                } else {
                    h = w;
                }
            }
            return renderToPicture((int) Math.ceil(w), (int) Math.ceil(h));
        } else {
            return renderToPicture(DEFAULT_PICTURE_WIDTH, DEFAULT_PICTURE_HEIGHT);
        }
    }


    /**
     * Renders this SVG document to a Picture object.
     *
     * @param widthInPixels  the width of the initial viewport
     * @param heightInPixels the height of the initial viewport
     * @return a Picture object suitable for later rendering using {@code Canvas.darwPicture()}
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public Picture renderToPicture(int widthInPixels, int heightInPixels) {
        Picture picture = new Picture();
        Canvas canvas = picture.beginRecording(widthInPixels, heightInPixels);
        Box viewPort = new Box(0f, 0f, (float) widthInPixels, (float) heightInPixels);

        SVGAndroidRenderer renderer = new SVGAndroidRenderer(canvas, this.renderDPI);

        renderer.renderDocument(this, viewPort, null, null, false);

        picture.endRecording();
        return picture;
    }


    /**
     * Renders this SVG document to a Picture object using the specified view defined in the document.
     * <p>
     * A View is an special element in a SVG document that describes a rectangular area in the document.
     * Calling this method with a {@code viewId} will result in the specified view being positioned and scaled
     * to the viewport.  In other words, use {@link #renderToPicture()} to render the whole document, or use this
     * method instead to render just a part of it.
     *
     * @param viewId         the id of a view element in the document that defines which section of the document is to be visible.
     * @param widthInPixels  the width of the initial viewport
     * @param heightInPixels the height of the initial viewport
     * @return a Picture object suitable for later rendering using {@code Canvas.drawPicture()}, or null if the viewId was not found.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public Picture renderViewToPicture(String viewId, int widthInPixels, int heightInPixels) {
        SvgObject obj = this.getElementById(viewId);
        if (obj == null) {
            return null;
        }
        if (!(obj instanceof View)) {
            return null;
        }

        View view = (View) obj;

        if (view.viewBox == null) {
            Log.w(TAG, "View element is missing a viewBox attribute.");
            return null;
        }

        Picture picture = new Picture();
        Canvas canvas = picture.beginRecording(widthInPixels, heightInPixels);
        Box viewPort = new Box(0f, 0f, (float) widthInPixels, (float) heightInPixels);

        SVGAndroidRenderer renderer = new SVGAndroidRenderer(canvas, this.renderDPI);

        renderer.renderDocument(this, viewPort, view.viewBox, view.preserveAspectRatio, false);

        picture.endRecording();
        return picture;
    }


    //===============================================================================
    // SVG document rendering to a canvas object (direct rendering)


    /**
     * Renders this SVG document to a Canvas object.  The full width and height of the canvas
     * will be used as the viewport into which the document will be rendered.
     *
     * @param canvas the canvas to which the document should be rendered.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public void renderToCanvas(Canvas canvas) {
        renderToCanvas(canvas, null);
    }


    /**
     * Renders this SVG document to a Canvas object.
     *
     * @param canvas   the canvas to which the document should be rendered.
     * @param viewPort the bounds of the area on the canvas you want the SVG rendered, or null for the whole canvas.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public void renderToCanvas(Canvas canvas, RectF viewPort) {
        Box canvasViewPort;

        if (viewPort != null) {
            canvasViewPort = Box.fromLimits(viewPort.left, viewPort.top, viewPort.right, viewPort.bottom);
        } else {
            canvasViewPort = new Box(0f, 0f, (float) canvas.getWidth(), (float) canvas.getHeight());
        }

        SVGAndroidRenderer renderer = new SVGAndroidRenderer(canvas, this.renderDPI);

        renderer.renderDocument(this, canvasViewPort, null, null, true);
    }


    /**
     * Renders this SVG document to a Canvas using the specified view defined in the document.
     * <p>
     * A View is an special element in a SVG documents that describes a rectangular area in the document.
     * Calling this method with a {@code viewId} will result in the specified view being positioned and scaled
     * to the viewport.  In other words, use {@link #renderToPicture()} to render the whole document, or use this
     * method instead to render just a part of it.
     * <p>
     * If the {@code <view>} could not be found, nothing will be drawn.
     *
     * @param viewId the id of a view element in the document that defines which section of the document is to be visible.
     * @param canvas the canvas to which the document should be rendered.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public void renderViewToCanvas(String viewId, Canvas canvas) {
        renderViewToCanvas(viewId, canvas, null);
    }


    /**
     * Renders this SVG document to a Canvas using the specified view defined in the document.
     * <p>
     * A View is an special element in a SVG documents that describes a rectangular area in the document.
     * Calling this method with a {@code viewId} will result in the specified view being positioned and scaled
     * to the viewport.  In other words, use {@link #renderToPicture()} to render the whole document, or use this
     * method instead to render just a part of it.
     * <p>
     * If the {@code <view>} could not be found, nothing will be drawn.
     *
     * @param viewId   the id of a view element in the document that defines which section of the document is to be visible.
     * @param canvas   the canvas to which the document should be rendered.
     * @param viewPort the bounds of the area on the canvas you want the SVG rendered, or null for the whole canvas.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public void renderViewToCanvas(String viewId, Canvas canvas, RectF viewPort) {
        SvgObject obj = this.getElementById(viewId);
        if (obj == null) {
            return;
        }
        if (!(obj instanceof View)) {
            return;
        }

        View view = (View) obj;

        if (view.viewBox == null) {
            Log.w(TAG, "View element is missing a viewBox attribute.");
            return;
        }

        Box canvasViewPort;

        if (viewPort != null) {
            canvasViewPort = Box.fromLimits(viewPort.left, viewPort.top, viewPort.right, viewPort.bottom);
        } else {
            canvasViewPort = new Box(0f, 0f, (float) canvas.getWidth(), (float) canvas.getHeight());
        }

        SVGAndroidRenderer renderer = new SVGAndroidRenderer(canvas, this.renderDPI);

        renderer.renderDocument(this, canvasViewPort, view.viewBox, view.preserveAspectRatio, true);
    }


    //===============================================================================
    // Other document utility API functions


    /**
     * Returns the version number of this library.
     *
     * @return the version number in string format
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public static String getVersion() {
        return VERSION;
    }


    /**
     * Returns the contents of the {@code <title>} element in the SVG document.
     *
     * @return title contents if available, otherwise an empty string.
     * @throws IllegalArgumentException if there is no current SVG document loaded.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public String getDocumentTitle() {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }

        return title;
    }


    /**
     * Returns the contents of the {@code <desc>} element in the SVG document.
     *
     * @return desc contents if available, otherwise an empty string.
     * @throws IllegalArgumentException if there is no current SVG document loaded.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public String getDocumentDescription() {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }

        return desc;
    }


    /**
     * Returns the SVG version number as provided in the root {@code <svg>} tag of the document.
     *
     * @return the version string if declared, otherwise an empty string.
     * @throws IllegalArgumentException if there is no current SVG document loaded.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public String getDocumentSVGVersion() {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }

        return rootElement.version;
    }


    /**
     * Returns a list of ids for all {@code <view>} elements in this SVG document.
     * <p>
     * The returned view ids could be used when calling and of the {@code renderViewToX()} methods.
     *
     * @return the list of id strings.
     * @throws IllegalArgumentException if there is no current SVG document loaded.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public Set<String> getViewList() {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }

        List<SvgObject> viewElems = getElementsByTagName(View.class);

        Set<String> viewIds = new HashSet<>(viewElems.size());
        for (SvgObject elem : viewElems) {
            View view = (View) elem;
            if (view.id != null) {
                viewIds.add(view.id);
            } else {
                Log.w("AndroidSVG", "getViewList(): found a <view> without an id attribute");
            }
        }
        return viewIds;
    }


    /**
     * Returns the width of the document as specified in the SVG file.
     * <p>
     * If the width in the document is specified in pixels, that value will be returned.
     * If the value is listed with a physical unit such as "cm", then the current
     * {@code RenderDPI} value will be used to convert that value to pixels. If the width
     * is missing, or in a form which can't be converted to pixels, such as "100%" for
     * example, -1 will be returned.
     *
     * @return the width in pixels, or -1 if there is no width available.
     * @throws IllegalArgumentException if there is no current SVG document loaded.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public float getDocumentWidth() {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }

        return getDocumentDimensions(this.renderDPI).width;
    }


    /**
     * Change the width of the document by altering the "width" attribute
     * of the root {@code <svg>} element.
     *
     * @param pixels The new value of width in pixels.
     * @throws IllegalArgumentException if there is no current SVG document loaded.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public void setDocumentWidth(float pixels) {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }

        this.rootElement.width = new Length(pixels);
    }


    /**
     * Change the width of the document by altering the "width" attribute
     * of the root {@code <svg>} element.
     *
     * @param value A valid SVG 'length' attribute, such as "100px" or "10cm".
     * @throws SVGParseException        if {@code value} cannot be parsed successfully.
     * @throws IllegalArgumentException if there is no current SVG document loaded.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public void setDocumentWidth(String value) throws SVGParseException {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }

        try {
            this.rootElement.width = SVGParser.parseLength(value);
        } catch (SAXException e) {
            throw new SVGParseException(e.getMessage());
        }
    }


    /**
     * Returns the height of the document as specified in the SVG file.
     * <p>
     * If the height in the document is specified in pixels, that value will be returned.
     * If the value is listed with a physical unit such as "cm", then the current
     * {@code RenderDPI} value will be used to convert that value to pixels. If the height
     * is missing, or in a form which can't be converted to pixels, such as "100%" for
     * example, -1 will be returned.
     *
     * @return the height in pixels, or -1 if there is no height available.
     * @throws IllegalArgumentException if there is no current SVG document loaded.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public float getDocumentHeight() {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }

        return getDocumentDimensions(this.renderDPI).height;
    }


    /**
     * Change the height of the document by altering the "height" attribute
     * of the root {@code <svg>} element.
     *
     * @param pixels The new value of height in pixels.
     * @throws IllegalArgumentException if there is no current SVG document loaded.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public void setDocumentHeight(float pixels) {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }

        this.rootElement.height = new Length(pixels);
    }


    /**
     * Change the height of the document by altering the "height" attribute
     * of the root {@code <svg>} element.
     *
     * @param value A valid SVG 'length' attribute, such as "100px" or "10cm".
     * @throws SVGParseException        if {@code value} cannot be parsed successfully.
     * @throws IllegalArgumentException if there is no current SVG document loaded.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public void setDocumentHeight(String value) throws SVGParseException {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }

        try {
            this.rootElement.height = SVGParser.parseLength(value);
        } catch (SAXException e) {
            throw new SVGParseException(e.getMessage());
        }
    }


    /**
     * Change the document view box by altering the "viewBox" attribute
     * of the root {@code <svg>} element.
     * <p>
     * The viewBox generally describes the bounding box dimensions of the
     * document contents.  A valid viewBox is necessary if you want the
     * document scaled to fit the canvas or viewport the document is to be
     * rendered into.
     * <p>
     * By setting a viewBox that describes only a portion of the document,
     * you can reproduce the effect of image sprites.
     *
     * @param minX   the left coordinate of the viewBox in pixels
     * @param minY   the top coordinate of the viewBox in pixels.
     * @param width  the width of the viewBox in pixels
     * @param height the height of the viewBox in pixels
     * @throws IllegalArgumentException if there is no current SVG document loaded.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public void setDocumentViewBox(float minX, float minY, float width, float height) {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }

        this.rootElement.viewBox = new Box(minX, minY, width, height);
    }


    /**
     * Returns the viewBox attribute of the current SVG document.
     *
     * @return the document's viewBox attribute as a {@code android.graphics.RectF} object, or null if not set.
     * @throws IllegalArgumentException if there is no current SVG document loaded.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public RectF getDocumentViewBox() {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }

        if (this.rootElement.viewBox == null) {
            return null;
        }

        return this.rootElement.viewBox.toRectF();
    }


    /**
     * Change the document positioning by altering the "preserveAspectRatio"
     * attribute of the root {@code <svg>} element.  See the
     * documentation for {@link PreserveAspectRatio} for more information
     * on how positioning works.
     *
     * @param preserveAspectRatio the new {@code preserveAspectRatio} setting for the root {@code <svg>} element.
     * @throws IllegalArgumentException if there is no current SVG document loaded.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public void setDocumentPreserveAspectRatio(PreserveAspectRatio preserveAspectRatio) {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }

        this.rootElement.preserveAspectRatio = preserveAspectRatio;
    }


    /**
     * Return the "preserveAspectRatio" attribute of the root {@code <svg>}
     * element in the form of an {@link PreserveAspectRatio} object.
     *
     * @return the preserveAspectRatio setting of the document's root {@code <svg>} element.
     * @throws IllegalArgumentException if there is no current SVG document loaded.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public PreserveAspectRatio getDocumentPreserveAspectRatio() {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }

        if (this.rootElement.preserveAspectRatio == null) {
            return null;
        }

        return this.rootElement.preserveAspectRatio;
    }


    /**
     * Returns the aspect ratio of the document as a width/height fraction.
     * <p>
     * If the width or height of the document are listed with a physical unit such as "cm",
     * then the current {@code renderDPI} setting will be used to convert that value to pixels.
     * <p>
     * If the width or height cannot be determined, -1 will be returned.
     *
     * @return the aspect ratio as a width/height fraction, or -1 if the ratio cannot be determined.
     * @throws IllegalArgumentException if there is no current SVG document loaded.
     */
    @SuppressWarnings({"WeakerAccess", "unused"})
    public float getDocumentAspectRatio() {
        if (this.rootElement == null) {
            throw new IllegalArgumentException("SVG document is empty");
        }

        Length w = this.rootElement.width;
        Length h = this.rootElement.height;

        // If width and height are both specified and are not percentages, aspect ratio is calculated from these (SVG1.1 sect 7.12)
        if (w != null && h != null && w.unit != Unit.percent && h.unit != Unit.percent) {
            if (w.isZero() || h.isZero()) {
                return -1f;
            }
            return w.floatValue(this.renderDPI) / h.floatValue(this.renderDPI);
        }

        // Otherwise, get the ratio from the viewBox
        if (this.rootElement.viewBox != null && this.rootElement.viewBox.width != 0f && this.rootElement.viewBox.height != 0f) {
            return this.rootElement.viewBox.width / this.rootElement.viewBox.height;
        }

        // Could not determine aspect ratio
        return -1f;
    }


    //===============================================================================


    SvgViewBox getRootElement() {
        return rootElement;
    }


    void setRootElement(SvgViewBox rootElement) {
        this.rootElement = rootElement;
    }


    SvgObject resolveIRI(String iri) {
        if (iri == null) {
            return null;
        }

        iri = cssQuotedString(iri);
        if (iri.length() > 1 && iri.startsWith("#")) {
            return getElementById(iri.substring(1));
        }
        return null;
    }


    private String cssQuotedString(String str) {
        if (str.startsWith("\"") && str.endsWith("\"")) {
            // Remove quotes and replace escaped double-quote
            str = str.substring(1, str.length() - 1).replace("\\\"", "\"");
        } else if (str.startsWith("'") && str.endsWith("'")) {
            // Remove quotes and replace escaped single-quote
            str = str.substring(1, str.length() - 1).replace("\\'", "'");
        }
        // Remove escaped newline. Replace escape seq representing newline
        return str.replace("\\\n", "").replace("\\A", "\n");
    }


    private Box getDocumentDimensions(float dpi) {
        Length w = this.rootElement.width;
        Length h = this.rootElement.height;

        if (w == null || w.isZero() || w.unit == Unit.percent || w.unit == Unit.em || w.unit == Unit.ex) {
            return new Box(-1, -1, -1, -1);
        }

        float wOut = w.floatValue(dpi);
        float hOut;

        if (h != null) {
            if (h.isZero() || h.unit == Unit.percent || h.unit == Unit.em || h.unit == Unit.ex) {
                return new Box(-1, -1, -1, -1);
            }
            hOut = h.floatValue(dpi);
        } else {
            // height is not specified. SVG spec says this is okay. If there is a viewBox, we use
            // that to calculate the height. Otherwise we set height equal to width.
            if (this.rootElement.viewBox != null) {
                hOut = (wOut * this.rootElement.viewBox.height) / this.rootElement.viewBox.width;
            } else {
                hOut = wOut;
            }
        }
        return new Box(0, 0, wOut, hOut);
    }


    //===============================================================================
    // CSS support methods


    void addCSSRules(Ruleset ruleset) {
        this.cssRules.addAll(ruleset);
    }


    List<CSSParser.Rule> getCSSRules() {
        return this.cssRules.getRules();
    }


    boolean hasCSSRules() {
        return !this.cssRules.isEmpty();
    }


    //===============================================================================
    // Object sub-types used in the SVG object tree


    static final long SPECIFIED_FILL = 1;
    static final long SPECIFIED_FILL_RULE = (1 << 1);
    static final long SPECIFIED_FILL_OPACITY = (1 << 2);
    static final long SPECIFIED_STROKE = (1 << 3);
    static final long SPECIFIED_STROKE_OPACITY = (1 << 4);
    static final long SPECIFIED_STROKE_WIDTH = (1 << 5);
    static final long SPECIFIED_STROKE_LINECAP = (1 << 6);
    static final long SPECIFIED_STROKE_LINEJOIN = (1 << 7);
    static final long SPECIFIED_STROKE_MITERLIMIT = (1 << 8);
    static final long SPECIFIED_STROKE_DASHARRAY = (1 << 9);
    static final long SPECIFIED_STROKE_DASHOFFSET = (1 << 10);
    static final long SPECIFIED_OPACITY = (1 << 11);
    static final long SPECIFIED_COLOR = (1 << 12);
    static final long SPECIFIED_FONT_FAMILY = (1 << 13);
    static final long SPECIFIED_FONT_SIZE = (1 << 14);
    static final long SPECIFIED_FONT_WEIGHT = (1 << 15);
    static final long SPECIFIED_FONT_STYLE = (1 << 16);
    static final long SPECIFIED_TEXT_DECORATION = (1 << 17);
    static final long SPECIFIED_TEXT_ANCHOR = (1 << 18);
    static final long SPECIFIED_OVERFLOW = (1 << 19);
    static final long SPECIFIED_CLIP = (1 << 20);
    static final long SPECIFIED_MARKER_START = (1 << 21);
    static final long SPECIFIED_MARKER_MID = (1 << 22);
    static final long SPECIFIED_MARKER_END = (1 << 23);
    static final long SPECIFIED_DISPLAY = (1 << 24);
    static final long SPECIFIED_VISIBILITY = (1 << 25);
    static final long SPECIFIED_STOP_COLOR = (1 << 26);
    static final long SPECIFIED_STOP_OPACITY = (1 << 27);
    static final long SPECIFIED_CLIP_PATH = (1 << 28);
    static final long SPECIFIED_CLIP_RULE = (1 << 29);
    static final long SPECIFIED_MASK = (1 << 30);
    static final long SPECIFIED_SOLID_COLOR = (1L << 31);
    static final long SPECIFIED_SOLID_OPACITY = (1L << 32);
    static final long SPECIFIED_VIEWPORT_FILL = (1L << 33);
    static final long SPECIFIED_VIEWPORT_FILL_OPACITY = (1L << 34);
    static final long SPECIFIED_VECTOR_EFFECT = (1L << 35);
    static final long SPECIFIED_DIRECTION = (1L << 36);
    static final long SPECIFIED_IMAGE_RENDERING = (1L << 37);

    static final long SPECIFIED_ALL = 0xffffffff;

   /*
   protected static final long SPECIFIED_NON_INHERITING = SPECIFIED_DISPLAY | SPECIFIED_OVERFLOW | SPECIFIED_CLIP
                                                          | SPECIFIED_CLIP_PATH | SPECIFIED_OPACITY | SPECIFIED_STOP_COLOR
                                                          | SPECIFIED_STOP_OPACITY | SPECIFIED_MASK | SPECIFIED_SOLID_COLOR
                                                          | SPECIFIED_SOLID_OPACITY | SPECIFIED_VIEWPORT_FILL
                                                          | SPECIFIED_VIEWPORT_FILL_OPACITY | SPECIFIED_VECTOR_EFFECT;
   */


    //===============================================================================
    // The objects in the SVG object tree
    //===============================================================================


    //===============================================================================
    // Protected setters for internal use


    void setTitle(String title) {
        this.title = title;
    }


    void setDesc(String desc) {
        this.desc = desc;
    }


    SVGExternalFileResolver getFileResolver() {
        return fileResolver;
    }


    //===============================================================================
    // Path definition


    private SvgObject getElementById(String id) {
        if (id == null || id.length() == 0) {
            return null;
        }
        if (id.equals(rootElement.id)) {
            return rootElement;
        }

        if (idToElementMap.containsKey(id)) {
            return idToElementMap.get(id);
        }

        // Search the object tree for a node with id property that matches 'id'
        SvgElementBase result = getElementById(rootElement, id);
        idToElementMap.put(id, result);
        return result;
    }


    private SvgElementBase getElementById(SvgContainer obj, String id) {
        SvgElementBase elem = (SvgElementBase) obj;
        if (id.equals(elem.id)) {
            return elem;
        }
        for (SvgObject child : obj.getChildren()) {
            if (!(child instanceof SvgElementBase)) {
                continue;
            }
            SvgElementBase childElem = (SvgElementBase) child;
            if (id.equals(childElem.id)) {
                return childElem;
            }
            if (child instanceof SvgContainer) {
                SvgElementBase found = getElementById((SvgContainer) child, id);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }


    @SuppressWarnings("rawtypes")
    private List<SvgObject> getElementsByTagName(Class clazz) {
        // Search the object tree for nodes with the give element class
        return getElementsByTagName(rootElement, clazz);
    }


    @SuppressWarnings("rawtypes")
    private List<SvgObject> getElementsByTagName(SvgContainer obj, Class clazz) {
        List<SvgObject> result = new ArrayList<>();

        if (obj.getClass() == clazz) {
            result.add((SvgObject) obj);
        }
        for (SvgObject child : obj.getChildren()) {
            if (child.getClass() == clazz) {
                result.add(child);
            }
            if (child instanceof SvgContainer) {
                getElementsByTagName((SvgContainer) child, clazz);
            }
        }
        return result;
    }


}
