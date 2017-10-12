package com.caverock.androidsvg;

/** RM -- Adapter from Robolectric 3.1 to Robolectric 3.3 */
class Shadow {
   public static Object extract(Object instance) {
      return org.robolectric.internal.ShadowExtractor.extract(instance);
   }
}
