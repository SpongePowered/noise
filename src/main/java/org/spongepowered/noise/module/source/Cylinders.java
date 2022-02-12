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
import org.spongepowered.noise.module.NoiseModule;

/**
 * Noise module that outputs concentric cylinders.
 *
 * <p>This noise module outputs concentric cylinders centered on the origin.
 * These cylinders are oriented along the {@code y} axis similar to the
 * concentric rings of a tree. Each cylinder extends infinitely along the
 * {@code y} axis.</p>
 *
 * <p>By default, the first cylinder has a radius of {@code 1.0}. Each
 * subsequent cylinder has a radius that is {@code 1.0} unit larger than the
 * previous cylinder.</p>
 *
 * <p>The output value from this noise module is determined by the distance
 * between the input value and the nearest cylinder surface. The input values
 * that are located on a cylinder surface are given the output value {@code 1.0}
 * and the input values that are equidistant from two cylinder surfaces are
 * given the output value {@code -1.0}.</p>
 *
 * <p>An application can change the frequency of the concentric cylinders.
 * Increasing the frequency reduces the distances between cylinders. To specify
 * the frequency, call the {@link #setFrequency(double)} method.</p>
 *
 * <p>This noise module, modified with some low-frequency, low-power turbulence,
 * is useful for generating wood-like textures.</p>
 *
 * @sourceModules 0
 */
public class Cylinders extends NoiseModule {

    /**
     * Default frequency value for the {@link Cylinders} noise module.
     */
    public static final double DEFAULT_CYLINDERS_FREQUENCY = 1.0;

    private double frequency = Cylinders.DEFAULT_CYLINDERS_FREQUENCY;

    public Cylinders() {
        super(0);
    }

    /**
     * Get the frequency of the concentric cylinders.
     *
     * <p>Increasing the frequency increases the density of the concentric
     * cylinders, reducing the distances between them.</p>
     *
     * @return the frequency of the concentric cylinders
     */
    public double frequency() {
        return this.frequency;
    }

    /**
     * Set the frequency of the concentric cylinders.
     *
     * <p>Increasing the frequency increases the density of the concentric
     * cylinders, reducing the distances between them.</p>
     *
     * @param frequency the frequency of the concentric cylinders
     */
    public void setFrequency(final double frequency) {
        this.frequency = frequency;
    }

    @Override
    public double get(final double x, final double y, final double z) {
        double z1 = z;
        double x1 = x;
        x1 *= this.frequency;
        z1 *= this.frequency;

        final double distFromCenter = Math.sqrt(x1 * x1 + z1 * z1);
        final double distFromSmallerSphere = distFromCenter - Utils.floor(distFromCenter);
        final double distFromLargerSphere = 1.0 - distFromSmallerSphere;
        final double nearestDist = Math.min(distFromSmallerSphere, distFromLargerSphere);
        return 1.0 - (nearestDist * 2.0); // Puts it in the 0 to 1 range.
    }
}
