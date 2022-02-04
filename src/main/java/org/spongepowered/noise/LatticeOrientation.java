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

import org.spongepowered.noise.module.source.RidgedMultiSimplex;

/**
 * A lattice orientation for use with Simplex-type noise.
 *
 * @see Noise#simplexStyleGradientCoherentNoise3D(double, double, double, int, LatticeOrientation, NoiseQualitySimplex)
 * @see RidgedMultiSimplex
 */
public enum LatticeOrientation {

    /**
     * Generates the simplex-style coherent noise with the classic lattice orientation.
     *
     * <p>Might be better for texturing 3D models, but less good for generating
     * terrain which has a vertical direction that works differently than the
     * two horizontal directions. There may be subtle diagonal artifacts,
     * similar to classic Simplex.</p>
     */
    CLASSIC,
    /**
     * Generates simplex-style noise with Y pointing up the main diagonal on the noise lattice.
     *
     * <p>If used properly, this can produce better results than CLASSIC, when
     * generating 3D worlds with vertical direction that works differently than
     * the two horizontal directions.</p>
     *
     * <p>See the following recommended usage patterns:</p>
     * <ul>
     * <li>If Y is vertical and X/Z are horizontal, call {@code noise(x, Y, z)}</li>
     * <li>If Z is vertical and X/Y are horizontal, call {@code noise(x, Z, y)}
     * or use mode {@link #XY_BEFORE_Z}.</li>
     * <li>If T is time and X/Y or X/Z are horizontal, call {@code noise(x, T, y)}
     * or {@code noise(x, T, z)}, or use mode {@link #XY_BEFORE_Z}.</li>
     * <li>If only two coordinates are needed for a 2D noise plane, call
     * {@code noise(x, 0, y)} or {@code noise(x, 0, z)},
     * or use mode {@link #XY_BEFORE_Z}</li>
     * </ul>
     */
    XZ_BEFORE_Y,
    /**
     * Generates simplex-style noise with Z pointing up the main diagonal on
     * the noise lattice.
     *
     * <p>If used properly, this can produce better results than CLASSIC,
     * when generating 3D worlds with vertical direction that works differently
     * than the two horizontal directions.</p>
     *
     * <p>See the following recommended usage patterns:</p>
     * <ul>
     * <li>If Y is vertical and X/Z are horizontal, call {@code noise(x, z, Y)}
     * or use mode {@link #XZ_BEFORE_Y}.</li>
     * <li>If Z is vertical and X/Y are horizontal, call {@code noise(x, y, Z)}</li>
     * <li>If T is time and X/Y or X/Z are horizontal, call
     * {@code noise(x, y, T)} or {@code noise(x, z, T)}, or use
     * mode {@link #XZ_BEFORE_Y}.</li>
     * <li>If only two coordinates are needed for a 2D noise plane, call
     * {@code noise(x, y, 0)} or {@code noise(x, z, 0)}, or use
     * mode {@link #XZ_BEFORE_Y}.</li>
     * </ul>
     */
    XY_BEFORE_Z

}
