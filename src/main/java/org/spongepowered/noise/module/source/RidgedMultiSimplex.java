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

import org.spongepowered.noise.LatticeOrientation;
import org.spongepowered.noise.Noise;
import org.spongepowered.noise.NoiseQualitySimplex;
import org.spongepowered.noise.Utils;
import org.spongepowered.noise.module.NoiseModule;

/**
 * Generates ridged Simplex-style noise.
 *
 * <p>The base Simplex uses a different formula but produces a similar
 * appearance to classic Simplex.</p>
 *
 * <p>The default lattice orientation is {@link LatticeOrientation#XZ_BEFORE_Y}. See
 * {@link org.spongepowered.noise.LatticeOrientation} for recommended usage.</p>
 *
 * @sourceModules 0
 */
public class RidgedMultiSimplex extends NoiseModule {

    /**
     * Default frequency for the {@link RidgedMultiSimplex} noise module.
     */
    public static final double DEFAULT_RIDGED_FREQUENCY = 1.0;

    /**
     * Default lacunarity for the {@link RidgedMultiSimplex} noise module.
     */
    public static final double DEFAULT_RIDGED_LACUNARITY = 2.0;

    /**
     * Default number of octaves for the {@link RidgedMultiSimplex} noise module.
     */
    public static final int DEFAULT_RIDGED_OCTAVE_COUNT = 6;

    /**
     * Default lattice orientation for the {@link RidgedMultiSimplex} noise module.
     */
    public static final LatticeOrientation DEFAULT_SIMPLEX_ORIENTATION = LatticeOrientation.XZ_BEFORE_Y;

    /**
     * Default noise quality for the {@link RidgedMultiSimplex} noise module.
     */
    public static final NoiseQualitySimplex DEFAULT_RIDGED_QUALITY = NoiseQualitySimplex.SMOOTH;

    /**
     * Default noise seed for the {@link RidgedMultiSimplex} noise module.
     */
    public static final int DEFAULT_RIDGED_SEED = 0;

    /**
     * Maximum number of octaves for the {@link RidgedMultiSimplex} noise module.
     */
    public static final int RIDGED_MAX_OCTAVE = 30;

    // Frequency of the first octave.
    private double frequency = RidgedMultiSimplex.DEFAULT_RIDGED_FREQUENCY;
    // Frequency multiplier between successive octaves.
    private double lacunarity = RidgedMultiSimplex.DEFAULT_RIDGED_LACUNARITY;
    // Lattice Orientation of the Simplex-style noise.
    private LatticeOrientation latticeOrientation = RidgedMultiSimplex.DEFAULT_SIMPLEX_ORIENTATION;
    // Quality of the ridged-multifractal noise.
    private NoiseQualitySimplex noiseQuality = RidgedMultiSimplex.DEFAULT_RIDGED_QUALITY;
    // Total number of octaves that generate the ridged-multifractal noise.
    private int octaveCount = RidgedMultiSimplex.DEFAULT_RIDGED_OCTAVE_COUNT;
    // Contains the spectral weights for each octave.
    private double[] spectralWeights;
    // Seed value used by the ridged-multfractal-noise function.
    private int seed = RidgedMultiSimplex.DEFAULT_RIDGED_SEED;

    public RidgedMultiSimplex() {
        super(0);
        this.calcSpectralWeights();
    }

    /**
     * Get the frequency of the first octave.
     *
     * @return the frequency of the first octave
     * @see #DEFAULT_RIDGED_FREQUENCY
     */
    public double frequency() {
        return this.frequency;
    }

    /**
     * Set the frequency of the first octave.
     *
     * @param frequency the frequency of the first octave
     */
    public void setFrequency(final double frequency) {
        this.frequency = frequency;
    }

    /**
     * Get the lacunarity of the ridged-multifractal noise.
     *
     * @return the lacunarity of the ridged-multifractal noise.
     * @see #DEFAULT_RIDGED_LACUNARITY
     */
    public double lacunarity() {
        return this.lacunarity;
    }

    /**
     * Sets the lacunarity of the ridged-multifractal noise.
     *
     * <p>The lacunarity is the frequency multiplier between
     * successive octaves.</p>
     *
     * <p>For best results, set the lacunarity to a number between
     * 1.5 and 3.5.</p>
     *
     * @param lacunarity the lacunarity of the ridged-multifractal noise
     */
    public void setLacunarity(final double lacunarity) {
        this.lacunarity = lacunarity;
    }

    /**
     * Get the lattice orientation for the Simplex noise.
     *
     * @return the lattice orientation
     * @see #DEFAULT_SIMPLEX_ORIENTATION
     */
    public LatticeOrientation latticeOrientation() {
        return this.latticeOrientation;
    }

    /**
     * Set the lattice orientation for the Simplex noise.
     *
     * @param latticeOrientation the lattice orientation
     */
    public void setLatticeOrientation(final LatticeOrientation latticeOrientation) {
        this.latticeOrientation = latticeOrientation;
    }

    /**
     * Get the quality of the ridged-multifractal noise.
     *
     * <p>See {@link NoiseQualitySimplex} for definitions of the various
     * coherent-noise qualities.</p>
     *
     * @return the quality of the ridged-multifractal noise
     * @see #DEFAULT_RIDGED_QUALITY
     */
    public NoiseQualitySimplex noiseQuality() {
        return this.noiseQuality;
    }

    /**
     * Set the quality of the ridged-multifractal noise.
     *
     * <p>See {@link NoiseQualitySimplex} for definitions of the various
     * coherent-noise qualities.</p>
     *
     * @param noiseQuality the quality of the ridged-multifractal noise
     */
    public void setNoiseQuality(final NoiseQualitySimplex noiseQuality) {
        this.noiseQuality = noiseQuality;
    }

    /**
     * Get the number of octaves that generate the ridged-multifractal noise.
     *
     * <p>The number of octaves controls the amount of detail in the
     * ridged-multifractal noise.</p>
     *
     * @return the number of octaves that generate the ridged-multifractal noise
     * @see #DEFAULT_RIDGED_OCTAVE_COUNT
     */
    public int octaveCount() {
        return this.octaveCount;
    }

    /**
     * Set the number of octaves that generate the ridged-multifractal noise.
     *
     * <p>The number of octaves must be between 1 and {@link #RIDGED_MAX_OCTAVE}</p>
     *
     * <p>The number of octaves controls the amount of detail in the
     * ridged-multifractal noise.</p>
     *
     * <p>The larger the number of octaves, the more time required to calculate
     * the ridged-multifractal noise value.</p>
     *
     * @param octaveCount the number of octaves that generate the
     *     ridged-multifractal noise
     * @throws IllegalArgumentException if the octave count is larger than
     *     {@link #RIDGED_MAX_OCTAVE}
     */
    public void setOctaveCount(final int octaveCount) {
        if (octaveCount < 1 || octaveCount > RidgedMulti.RIDGED_MAX_OCTAVE) {
            throw new IllegalArgumentException("octaveCount must be between 1 and MAX OCTAVE: " + RidgedMulti.RIDGED_MAX_OCTAVE);
        }
        this.octaveCount = octaveCount;
    }

    /**
     * Get the seed value used by the ridged-multifractal noise function.
     *
     * @return the seed value
     * @see #DEFAULT_RIDGED_SEED
     */
    public int seed() {
        return this.seed;
    }

    /**
     * Set the seed value used by the ridged-multifractal noise function.
     *
     * @param seed the seed value
     */
    public void setSeed(final int seed) {
        this.seed = seed;
    }

    private void calcSpectralWeights() {
        // This exponent parameter should be user-defined; it may be exposed in a
        // future version of libnoise.
        final double h = 1.0;

        double frequency = 1.0;
        this.spectralWeights = new double[RidgedMultiSimplex.RIDGED_MAX_OCTAVE];
        for (int i = 0; i < RidgedMultiSimplex.RIDGED_MAX_OCTAVE; i++) {
            // Compute weight for each frequency.
            this.spectralWeights[i] = Math.pow(frequency, -h);
            frequency *= this.lacunarity;
        }
    }

    /**
     * Returns the maximum value the RidgedMultiSimplex module can output in
     * its current configuration.
     *
     * @return The maximum possible value for
     * {@link RidgedMultiSimplex#get(double, double, double)} to return
     */
    public double maxValue() {
        /*
         * Each successive octave adds (1/lacunarity) ^ current_octaves to max possible output.
         * So (r = lacunarity, o = octave): Max(ridged) = 1 + 1/r + 1/(r*r) + 1/(r*r*r) + ... + (1/r^(o-1))
         * See https://www.wolframalpha.com/input/?i=sum+from+k%3D0+to+n-1+of+1%2Fx%5Ek
         */
        return (this.lacunarity() - Math.pow(this.lacunarity(), 1 - this.octaveCount())) / (this.lacunarity() - 1) / 1.6;
    }

    @Override
    public double get(final double x, final double y, final double z) {
        double x1 = x;
        double y1 = y;
        double z1 = z;
        x1 *= this.frequency;
        y1 *= this.frequency;
        z1 *= this.frequency;

        double signal;
        double value = 0.0;
        double weight = 1.0;

        // These parameters should be user-defined; they may be exposed in a
        // future version of libnoise.
        final double offset = 1.0;
        final double gain = 2.0;

        for (int curOctave = 0; curOctave < this.octaveCount; curOctave++) {

            // Make sure that these floating-point values have the same range as a 32-
            // bit integer so that we can pass them to the coherent-noise functions.
            final double nx, ny, nz;
            nx = Utils.makeInt32Range(x1);
            ny = Utils.makeInt32Range(y1);
            nz = Utils.makeInt32Range(z1);

            // Get the coherent-noise value.
            final int seed = (this.seed + curOctave) & 0x7fffffff;
            signal = Noise.simplexStyleGradientCoherentNoise3D(nx, ny, nz, seed, this.latticeOrientation, this.noiseQuality) * 2 - 1;

            // Make the ridges.
            signal = Math.abs(signal);
            signal = offset - signal;

            // Square the signal to increase the sharpness of the ridges.
            signal *= signal;

            // The weighting from the previous octave is applied to the signal.
            // Larger values have higher weights, producing sharp points along the
            // ridges.
            signal *= weight;

            // Weight successive contributions by the previous signal.
            weight = signal * gain;
            if (weight > 1.0) {
                weight = 1.0;
            }
            if (weight < 0.0) {
                weight = 0.0;
            }

            // Add the signal to the output value.
            value += (signal * this.spectralWeights[curOctave]);

            // Go to the next octave.
            x1 *= this.lacunarity;
            y1 *= this.lacunarity;
            z1 *= this.lacunarity;
        }

        return value / 1.6;
    }
}
