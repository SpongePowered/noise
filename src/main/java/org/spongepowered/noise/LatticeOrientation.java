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

public enum LatticeOrientation {
    /**
     * Generates the simplex-style coherent noise with the classic lattice orientation. Might be better for texturing 3D models, but less good for generating terrain
     * which has a vertical direction that works differently than the two horizontal directions. There may be subtle diagonal artifacts, similar to classic Simplex.
     */
    CLASSIC,
    /**
     * Generates simplex-style noise with Y pointing up the main diagonal on the noise lattice. If used properly, this can produce better results than CLASSIC,
     * when generating 3D worlds with vertical direction that works differently than the two horizontal directions. See the following recommended usage patterns:
     * - If Y is vertical and X/Z are horizontal, call noise(x, Y, z)
     * - If Z is vertical and X/Y are horizontal, call noise(x, Z, y) or use mode XY_BEFORE_Z
     * - If T is time and X/Y or X/Z are horizontal, call noise(x, T, y) or noise(x, T, z), or use mode XY_BEFORE_Z
     * - If only two coordinates are needed for a 2D noise plane, call noise(x, 0, y) or noise(x, 0, z), or use mode XY_BEFORE_Z
     */
    XZ_BEFORE_Y,
    /**
     * Generates simplex-style noise with Z pointing up the main diagonal on the noise lattice. If used properly, this can produce better results than CLASSIC,
     * when generating 3D worlds with vertical direction that works differently than the two horizontal directions. See the following recommended usage patterns:
     * - If Y is vertical and X/Z are horizontal, call noise(x, z, Y) or use mode XZ_BEFORE_Y
     * - If Z is vertical and X/Y are horizontal, call noise(x, y, Z)
     * - If T is time and X/Y or X/Z are horizontal, call noise(x, y, T) or noise(x, z, T), or use mode XY_BEFORE_Z
     * - If only two coordinates are needed for a 2D noise plane, call noise(x, y, 0) or noise(x, z, 0), or use mode XZ_BEFORE_Y
     */
    XY_BEFORE_Z
}
