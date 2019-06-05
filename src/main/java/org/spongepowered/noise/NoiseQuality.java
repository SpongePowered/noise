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
package org.spongepowered.noise;

public enum NoiseQuality {
    /**
     * Generates coherent noise quickly.  When a coherent-noise function with this quality setting is used to generate a bump-map image, there are noticeable "creasing" artifacts in the resulting image.
     * This is because the derivative of that function is discontinuous at integer boundaries.
     */
    FAST,
    /**
     * Generates standard-quality coherent noise. When a coherent-noise function with this quality setting is used to generate a bump-map image, there are some minor "creasing" artifacts in the resulting
     * image. This is because the second derivative of that function is discontinuous at integer boundaries.
     */
    STANDARD,
    /**
     * Generates the best-quality coherent noise. When a coherent-noise function with this quality setting is used to generate a bump-map image, there are no "creasing" artifacts in the resulting image.
     * This is because the first and second derivatives of that function are continuous at integer boundaries.
     */
    BEST
}
