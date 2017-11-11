# AndroidSVG

## Original Project Description
AndroidSVG is a SVG parser and renderer for Android.  It has almost complete support for the static
visual elements of the SVG 1.1 and SVG 1.2 Tiny specifications (except for filters).

### License

*AndroidSVG is licensed under the [Apache License v2.0](http://www.apache.org/licenses/LICENSE-2.0)*.

[More information, including downloads and documentation, is available at the main AndroidSVG site.](http://bigbadaboom.github.io/androidsvg/)


### Find a bug?

Please file a [bug report](https://github.com/BigBadaboom/androidsvg/issues) and include as much detail as you can.
If possible, please include a sample SVG file showing the error.

If you wish to contact the author with feedback on this project, you can email me at
[androidsvgfeedback@gmail.com](mailto:androidsvgfeedback@gmail.com).

### Using AndroidSVG in your app?

If you have found AndroidSVG useful and are using it in your project, please let me know. I'd love to hear about it!


## Fork Notice & Changes

This repository is a fork from AndroidSVG used in [RTAC](ralf.alfray.com/trains/rtac).

The original source is available here: https://github.com/BigBadaboom/androidsvg

Main changes compared to the original:
 * [ffc6d59](https://github.com/ralfoide/androidsvg/commit/ffc6d599a10c05954004b357aaafc35253d97c0d)
   Added a method SVG.getAllElementIds().
 * [f5d7de0](https://github.com/ralfoide/androidsvg/commit/f5d7de07b13894878317a5ceedd5a52f5645d491)
   Fixed an SVG parser bug to parse both attributes id and xml:space
   (instead of stopping at the first one found).
 * Made a bunch of fields publicly accessible so that RTAC can update
   style information before rendering.

Rest of the changes are reformating to fit my personal style guide and
splitting the extracted the SVG inner classes, mostly inconsequential stuff.
