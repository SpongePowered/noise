/*
 * This file is part of Noise, licensed under the MIT License (MIT).
 *
 * Copyright (c) Flow Powered <https://github.com/flow>
 * Copyright (c) SpongePowered <https://github.com/SpongePowered>
 * Copyright (c) contributors
 *
 * Original libnoise C++ library by Jason Bevins <http://libnoise.sourceforge.net>
 * jlibnoise Java port by Garrett Fleenor <https://github.com/RoyAwesome/jlibnoise>
 * Noise is re-licensed with permission from jlibnoise author.
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
package org.spongepowered.noise.module.source;

import org.spongepowered.noise.Utils;
import org.spongepowered.noise.module.Module;

public class Spheres extends Module {
    // Default frequency value for the noise::module::Spheres noise module.
    public static final double DEFAULT_SPHERES_FREQUENCY = 1.0;
    // Frequency of the concentric spheres.
    private double frequency = DEFAULT_SPHERES_FREQUENCY;

    public Spheres() {
        super(0);
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    @Override
    public int getSourceModuleCount() {
        return 0;
    }

    @Override
    public double getValue(double x, double y, double z) {
        double x1 = x;
        double y1 = y;
        double z1 = z;
        x1 *= frequency;
        y1 *= frequency;
        z1 *= frequency;

        double distFromCenter = Math.sqrt(x1 * x1 + y1 * y1 + z1 * z1);
        double distFromSmallerSphere = distFromCenter - Utils.floor(distFromCenter);
        double distFromLargerSphere = 1.0 - distFromSmallerSphere;
        double nearestDist = Math.min(distFromSmallerSphere, distFromLargerSphere);
        return 1.0 - (nearestDist * 2.0); // Puts it in the 0 to 1 range.
    }
}
