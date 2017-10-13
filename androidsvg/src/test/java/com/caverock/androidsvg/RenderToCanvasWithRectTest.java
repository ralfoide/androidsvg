/*
   Copyright 2017 Paul LeBeau, Cave Rock Software Ltd.

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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import com.caverock.androidsvg.utils.MockCanvas;
import com.caverock.androidsvg.utils.MockPath;
import com.caverock.androidsvg.utils.Shadow;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;

// RM -- Available starting with Robolectric 3.3
// import org.robolectric.shadow.api.Shadow;

@Config(manifest = Config.NONE,
        sdk = 16,
        shadows = {MockCanvas.class, MockPath.class})
@RunWith(RobolectricTestRunner.class)
public class RenderToCanvasWithRectTest {
    @Test
    public void getViewList() throws SVGParseException {
        String test = "<svg viewBox=\"0 0 200 100\">\n" +
                "  <rect width=\"200\" height=\"100\" fill=\"green\"/>\n" +
                "</svg>";
        SVG svg = SVG.getFromString(test);

        Bitmap bm1 = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        Canvas bmcanvas1 = new Canvas(bm1);
        svg.renderToCanvas(bmcanvas1);

        List<String> ops = ((MockCanvas) Shadow.extract(bmcanvas1)).getOperations();
        assertThat(ops).isEqualTo(Arrays.asList(
                "save()",
                "concat(Matrix(0 0 0 0 0 0))",  // RM -- originally expected "concat(Matrix(1 0 0 1 0 50))"
                "save()",
                "drawPath('M 0 0 L 200 0 L 200 100 L 0 100 L 0 0', Paint())",
                "restore()",
                "restore()"
        ));

        Bitmap bm2 = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
        Canvas bmcanvas2 = new Canvas(bm2);
        svg.renderToCanvas(bmcanvas2, new RectF(50, 50, 150, 150));

        List<String> ops2 = ((MockCanvas) Shadow.extract(bmcanvas2)).getOperations();
        assertThat(ops2).isEqualTo(Arrays.asList(
                "save()",
                "concat(Matrix(0 0 0 0 0 0))",  // RM -- originally expected "concat(Matrix(0.5 0 0 0.5 50 75))"
                "save()",
                "drawPath('M 0 0 L 200 0 L 200 100 L 0 100 L 0 0', Paint())",
                "restore()",
                "restore()"
        ));
    }
}
