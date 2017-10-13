package com.caverock.androidsvg.utils;

/**
 * RM -- Adapter from Robolectric 3.1 to Robolectric 3.3
 */
public class Shadow {
    public static Object extract(Object instance) {
        return org.robolectric.internal.ShadowExtractor.extract(instance);
    }
}
