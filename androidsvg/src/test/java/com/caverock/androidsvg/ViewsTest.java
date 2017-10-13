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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import static com.google.common.truth.Truth.assertThat;

@Config(manifest = Config.NONE,
        sdk = 16)
@RunWith(RobolectricTestRunner.class)
public class ViewsTest {
    @Test
    public void getViewList() throws SVGParseException {
        String test = "<?xml version=\"1.0\" standalone=\"no\"?>\n" +
                "<svg xmlns=\"http://www.w3.org/2000/svg\">" +
                "  <view id=\"normalView\" viewBox=\"0 0 100 100\"/>" +
                "  <view id=\"halfView\"   viewBox=\"0 0 200 200\"/>" +
                "  <view id=\"doubleView\" viewBox=\"0 0  50  50\"/>" +
                "</svg>";
        SVG svg = SVG.getFromString(test);

        Set<String> views = svg.getViewList();
        assertThat(new TreeSet<>(views))
                .isEqualTo(new TreeSet<>(Arrays.asList(
                        "normalView",
                        "halfView",
                        "doubleView")));
    }
}
