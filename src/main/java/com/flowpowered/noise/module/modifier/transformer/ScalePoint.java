/*
 * This file is part of Flow Noise, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013 Flow Powered <https://flowpowered.com/>
 * Original libnoise C++ library by Jason Bevins <http://libnoise.sourceforge.net/>
 * jlibnoise Java port by Garrett Fleenor <https://github.com/RoyAwesome/jlibnoise>
 * Flow Noise is re-licensed with permission from jlibnoise author.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.flowpowered.noise.module.modifier.transformer;

import com.flowpowered.noise.module.Module;
import com.flowpowered.noise.module.modifier.Modifier;


public class ScalePoint extends Modifier {
    // Scaling factor applied to the @a x coordinate of the input value.
    private final double xScale;
    // Scaling factor applied to the @a y coordinate of the input value.
    private final double yScale;
    // Scaling factor applied to the @a z coordinate of the input value.
    private final double zScale;

    public ScalePoint(Module source, double xScale, double yScale, double zScale) {
        super(source);
        this.xScale = xScale;
        this.yScale = yScale;
        this.zScale = zScale;
    }

    public double getXScale() {
        return xScale;
    }

    public double getYScale() {
        return yScale;
    }

    public double getZScale() {
        return zScale;
    }

    @Override
    public double getValue(double x, double y, double z) {
        return source.getValue(x * xScale, y * yScale, z * zScale);
    }

}
