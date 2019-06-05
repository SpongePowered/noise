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

public final class Noise {
    private static final int X_NOISE_GEN = 1619;
    private static final int Y_NOISE_GEN = 31337;
    private static final int Z_NOISE_GEN = 6971;
    private static final int SEED_NOISE_GEN = 1013;
    private static final int SHIFT_NOISE_GEN = 8;

    private Noise() {
    }

    /**
     * Generates a gradient-coherent-noise value from the coordinates of a three-dimensional input value.
     *
     * @param x The @a x coordinate of the input value.
     * @param y The @a y coordinate of the input value.
     * @param z The @a z coordinate of the input value.
     * @param seed The random number seed.
     * @param quality The quality of the coherent-noise.
     * @return The generated gradient-coherent-noise value.
     * <p/>
     * The return value ranges from 0 to 1.
     * <p/>
     * For an explanation of the difference between <i>gradient</i> noise and <i>value</i> noise, see the comments for the GradientNoise3D() function.
     */
    public static double gradientCoherentNoise3D(double x, double y, double z, int seed, NoiseQuality quality) {

        // Create a unit-length cube aligned along an integer boundary.  This cube
        // surrounds the input point.

        int x0 = ((x > 0.0) ? (int) x : (int) x - 1);
        int x1 = x0 + 1;

        int y0 = ((y > 0.0) ? (int) y : (int) y - 1);
        int y1 = y0 + 1;

        int z0 = ((z > 0.0) ? (int) z : (int) z - 1);
        int z1 = z0 + 1;

        // Map the difference between the coordinates of the input value and the
        // coordinates of the cube's outer-lower-left vertex onto an S-curve.
        double xs, ys, zs;
        if (quality == NoiseQuality.FAST) {
            xs = (x - (double) x0);
            ys = (y - (double) y0);
            zs = (z - (double) z0);
        } else if (quality == NoiseQuality.STANDARD) {
            xs = Utils.sCurve3(x - (double) x0);
            ys = Utils.sCurve3(y - (double) y0);
            zs = Utils.sCurve3(z - (double) z0);
        } else {

            xs = Utils.sCurve5(x - (double) x0);
            ys = Utils.sCurve5(y - (double) y0);
            zs = Utils.sCurve5(z - (double) z0);
        }

        // Now calculate the noise values at each vertex of the cube.  To generate
        // the coherent-noise value at the input point, interpolate these eight
        // noise values using the S-curve value as the interpolant (trilinear
        // interpolation.)
        double n0, n1, ix0, ix1, iy0, iy1;
        n0 = gradientNoise3D(x, y, z, x0, y0, z0, seed);
        n1 = gradientNoise3D(x, y, z, x1, y0, z0, seed);
        ix0 = Utils.linearInterp(n0, n1, xs);

        n0 = gradientNoise3D(x, y, z, x0, y1, z0, seed);
        n1 = gradientNoise3D(x, y, z, x1, y1, z0, seed);
        ix1 = Utils.linearInterp(n0, n1, xs);
        iy0 = Utils.linearInterp(ix0, ix1, ys);
        n0 = gradientNoise3D(x, y, z, x0, y0, z1, seed);
        n1 = gradientNoise3D(x, y, z, x1, y0, z1, seed);
        ix0 = Utils.linearInterp(n0, n1, xs);
        n0 = gradientNoise3D(x, y, z, x0, y1, z1, seed);
        n1 = gradientNoise3D(x, y, z, x1, y1, z1, seed);
        ix1 = Utils.linearInterp(n0, n1, xs);
        iy1 = Utils.linearInterp(ix0, ix1, ys);
        return Utils.linearInterp(iy0, iy1, zs);
    }

    /**
     * Generates a gradient-noise value from the coordinates of a three-dimensional input value and the integer coordinates of a nearby three-dimensional value.
     *
     * @param fx The floating-point @a x coordinate of the input value.
     * @param fy The floating-point @a y coordinate of the input value.
     * @param fz The floating-point @a z coordinate of the input value.
     * @param ix The integer @a x coordinate of a nearby value.
     * @param iy The integer @a y coordinate of a nearby value.
     * @param iz The integer @a z coordinate of a nearby value.
     * @param seed The random number seed.
     * @return The generated gradient-noise value.
     * <p/>
     * The difference between fx and ix must be less than or equal to one. The difference between @a fy and @a iy must be less than or equal to one. The difference between @a fz and @a iz must be less
     * than or equal to one.
     * <p/>
     * A <i>gradient</i>-noise function generates better-quality noise than a <i>value</i>-noise function. Most noise modules use gradient noise for this reason, although it takes much longer to
     * calculate.
     * <p/>
     * The return value ranges from 0 to 1.
     * <p/>
     * This function generates a gradient-noise value by performing the following steps: - It first calculates a random normalized vector based on the nearby integer value passed to this function. -
     * It then calculates a new value by adding this vector to the nearby integer value passed to this function. - It then calculates the dot product of the above-generated value and the
     * floating-point input value passed to this function.
     * <p/>
     * A noise function differs from a random-number generator because it always returns the same output value if the same input value is passed to it.
     */
    public static double gradientNoise3D(double fx, double fy, double fz, int ix, int iy, int iz, int seed) {
        // Randomly generate a gradient vector given the integer coordinates of the
        // input value.  This implementation generates a random number and uses it
        // as an index into a normalized-vector lookup table.
        int vectorIndex = (X_NOISE_GEN * ix + Y_NOISE_GEN * iy + Z_NOISE_GEN * iz + SEED_NOISE_GEN * seed);
        vectorIndex ^= (vectorIndex >> SHIFT_NOISE_GEN);
        vectorIndex &= 0xff;

        double xvGradient = Utils.RANDOM_VECTORS[(vectorIndex << 2)];
        double yvGradient = Utils.RANDOM_VECTORS[(vectorIndex << 2) + 1];
        double zvGradient = Utils.RANDOM_VECTORS[(vectorIndex << 2) + 2];

        // Set up us another vector equal to the distance between the two vectors
        // passed to this function.
        double xvPoint = (fx - ix);
        double yvPoint = (fy - iy);
        double zvPoint = (fz - iz);

        // Now compute the dot product of the gradient vector with the distance
        // vector.  The resulting value is gradient noise.  Apply a scaling and
        // offset value so that this noise value ranges from 0 to 1.
        return ((xvGradient * xvPoint) + (yvGradient * yvPoint) + (zvGradient * zvPoint)) + 0.5;
    }

    /**
     * Generates an integer-noise value from the coordinates of a three-dimensional input value.
     *
     * @param x The integer @a x coordinate of the input value.
     * @param y The integer @a y coordinate of the input value.
     * @param z The integer @a z coordinate of the input value.
     * @param seed A random number seed.
     * @return The generated integer-noise value.
     * <p/>
     * The return value ranges from 0 to 2147483647.
     * <p/>
     * A noise function differs from a random-number generator because it always returns the same output value if the same input value is passed to it.
     */
    public static int intValueNoise3D(int x, int y, int z, int seed) {
        // All constants are primes and must remain prime in order for this noise
        // function to work correctly.
        int n = (X_NOISE_GEN * x + Y_NOISE_GEN * y + Z_NOISE_GEN * z + SEED_NOISE_GEN * seed) & 0x7fffffff;
        n = (n >> 13) ^ n;
        return (n * (n * n * 60493 + 19990303) + 1376312589) & 0x7fffffff;
    }

    /**
     * Generates a value-coherent-noise value from the coordinates of a three-dimensional input value.
     *
     * @param x The @a x coordinate of the input value.
     * @param y The @a y coordinate of the input value.
     * @param z The @a z coordinate of the input value.
     * @param seed The random number seed.
     * @param quality The quality of the coherent-noise.
     * @return The generated value-coherent-noise value.
     * <p/>
     * The return value ranges from 0 to 1.
     * <p/>
     * For an explanation of the difference between <i>gradient</i> noise and <i>value</i> noise, see the comments for the GradientNoise3D() function.
     */
    public static double valueCoherentNoise3D(double x, double y, double z, int seed, NoiseQuality quality) {
        // Create a unit-length cube aligned along an integer boundary.  This cube
        // surrounds the input point.
        int x0 = (x > 0.0 ? (int) x : (int) x - 1);
        int x1 = x0 + 1;
        int y0 = (y > 0.0 ? (int) y : (int) y - 1);
        int y1 = y0 + 1;
        int z0 = (z > 0.0 ? (int) z : (int) z - 1);
        int z1 = z0 + 1;

        // Map the difference between the coordinates of the input value and the
        // coordinates of the cube's outer-lower-left vertex onto an S-curve.
        double xs, ys, zs;
        if (quality == NoiseQuality.FAST) {
            xs = (x - x0);
            ys = (y - y0);
            zs = (z - z0);
        } else if (quality == NoiseQuality.STANDARD) {
            xs = Utils.sCurve3(x - x0);
            ys = Utils.sCurve3(y - y0);
            zs = Utils.sCurve3(z - z0);
        } else {

            xs = Utils.sCurve5(x - x0);
            ys = Utils.sCurve5(y - y0);
            zs = Utils.sCurve5(z - z0);
        }

        // Now calculate the noise values at each vertex of the cube.  To generate
        // the coherent-noise value at the input point, interpolate these eight
        // noise values using the S-curve value as the interpolant (trilinear
        // interpolation.)
        double n0, n1, ix0, ix1, iy0, iy1;
        n0 = valueNoise3D(x0, y0, z0, seed);
        n1 = valueNoise3D(x1, y0, z0, seed);
        ix0 = Utils.linearInterp(n0, n1, xs);
        n0 = valueNoise3D(x0, y1, z0, seed);
        n1 = valueNoise3D(x1, y1, z0, seed);
        ix1 = Utils.linearInterp(n0, n1, xs);
        iy0 = Utils.linearInterp(ix0, ix1, ys);
        n0 = valueNoise3D(x0, y0, z1, seed);
        n1 = valueNoise3D(x1, y0, z1, seed);
        ix0 = Utils.linearInterp(n0, n1, xs);
        n0 = valueNoise3D(x0, y1, z1, seed);
        n1 = valueNoise3D(x1, y1, z1, seed);
        ix1 = Utils.linearInterp(n0, n1, xs);
        iy1 = Utils.linearInterp(ix0, ix1, ys);
        return Utils.linearInterp(iy0, iy1, zs);
    }

    /**
     * Generates a value-noise value from the coordinates of a three-dimensional input value.
     *
     * @param x The @a x coordinate of the input value.
     * @param y The @a y coordinate of the input value.
     * @param z The @a z coordinate of the input value.
     * @param seed A random number seed.
     * @return The generated value-noise value.
     * <p/>
     * The return value ranges from 0 to 1.
     * <p/>
     * A noise function differs from a random-number generator because it always returns the same output value if the same input value is passed to it.
     */
    public static double valueNoise3D(int x, int y, int z, int seed) {
        return intValueNoise3D(x, y, z, seed) / 2147483647.0;
    }
}
