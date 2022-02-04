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

import org.spongepowered.noise.Noise;
import org.spongepowered.noise.LatticeOrientation;
import org.spongepowered.noise.NoiseQualitySimplex;
import org.spongepowered.noise.Utils;
import org.spongepowered.noise.module.NoiseModule;

/**
 * Generates summed octave Simplex-style noise.
 *
 * <p>The base Simplex uses a different formula but produces a similar
 * appearance to classic Simplex.</p>
 * <p>Default lattice orientation is {@link LatticeOrientation#XZ_BEFORE_Y}. See
 * {@link LatticeOrientation} for recommended usage.</p>
 *
 * @sourceModules 0
 */
public class Simplex extends NoiseModule {

    /**
     * Default frequency for the {@link Simplex} noise module.
     */
    public static final double DEFAULT_SIMPLEX_FREQUENCY = 1.0;

    /**
     * Default lacunarity for the {@link Simplex} noise module.
     */
    public static final double DEFAULT_SIMPLEX_LACUNARITY = 2.0;

    /**
     * Default number of octaves for the {@link Simplex} noise module.
     */
    public static final int DEFAULT_SIMPLEX_OCTAVE_COUNT = 6;

    /**
     * Default persistence value for the {@link Simplex} noise module.
     */
    public static final double DEFAULT_SIMPLEX_PERSISTENCE = 0.5;

    /**
     * Default lattice orientation for the {@link Simplex} noise module.
     */
    public static final LatticeOrientation DEFAULT_SIMPLEX_ORIENTATION = LatticeOrientation.XZ_BEFORE_Y;

    /**
     * Default noise quality for the {@link Simplex} noise module.
     */
    public static final NoiseQualitySimplex DEFAULT_SIMPLEX_QUALITY = NoiseQualitySimplex.STANDARD;

    /**
     * Default noise seed for the {@link Simplex} noise module.
     */
    public static final int DEFAULT_SIMPLEX_SEED = 0;

    /**
     * Maximum number of octaves for the {@link Simplex} noise module.
     */
    public static final int SIMPLEX_MAX_OCTAVE = 30;
    // Frequency of the first octave.
    private double frequency = Simplex.DEFAULT_SIMPLEX_FREQUENCY;
    // Frequency multiplier between successive octaves.
    private double lacunarity = Simplex.DEFAULT_SIMPLEX_LACUNARITY;
    // Lattice Orientation of the Simplex-style noise.
    private LatticeOrientation latticeOrientation = Simplex.DEFAULT_SIMPLEX_ORIENTATION;
    // Quality of the Simplex-style noise.
    private NoiseQualitySimplex noiseQuality = Simplex.DEFAULT_SIMPLEX_QUALITY;
    // Total number of octaves that generate the Simplex-style noise.
    private int octaveCount = Simplex.DEFAULT_SIMPLEX_OCTAVE_COUNT;
    // Persistence of the Simplex-style noise.
    private double persistence = Simplex.DEFAULT_SIMPLEX_PERSISTENCE;
    // Seed value used by the Simplex-style noise function.
    private int seed = Simplex.DEFAULT_SIMPLEX_SEED;

    public Simplex() {
        super(0);
    }

    /**
     * Get the frequency of the first octave.
     *
     * @return the frequency of the first octave
     * @see #DEFAULT_SIMPLEX_FREQUENCY
     */
    public double getFrequency() {
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
     * Get the lacunarity of the Simplex noise.
     *
     * <p>The lacunarity is the frequency multiplier between successive octaves.</p>
     *
     * @return the lacunarity of the Simplex noise
     * @see #DEFAULT_SIMPLEX_LACUNARITY
     */
    public double getLacunarity() {
        return this.lacunarity;
    }

    /**
     * Sets the lacunarity of the Simplex noise.
     *
     * <p>The lacunarity is the frequency multiplier between successive
     * octaves.</p>
     *
     * <p>For best results, set the lacunarity to a number between 1.5
     * and 3.5.</p>
     *
     * @param lacunarity the lacunarity of the Simplex noise
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
    public LatticeOrientation getLatticeOrientation() {
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
     * Get the quality of the Simplex noise.
     *
     * <p>See {@link NoiseQualitySimplex} for definitions of the various
     * coherent-noise qualities.</p>
     *
     * @return the quality of the Simplex noise
     * @see #DEFAULT_SIMPLEX_QUALITY
     */
    public NoiseQualitySimplex getNoiseQuality() {
        return this.noiseQuality;
    }

    /**
     * Set the quality of the Simplex noise.
     *
     * <p>See {@link NoiseQualitySimplex} for definitions of the various
     * coherent-noise qualities.</p>
     *
     * @param noiseQuality the quality of the Simplex noise
     */
    public void setNoiseQuality(final NoiseQualitySimplex noiseQuality) {
        this.noiseQuality = noiseQuality;
    }

    /**
     * Get the number of octaves that generate the Simplex noise.
     *
     * <p>The number of octaves controls the amount of detail in the
     * Simplex noise.</p>
     *
     * @return the number of octaves that generate the Simplex noise
     * @see #DEFAULT_SIMPLEX_OCTAVE_COUNT
     */
    public int getOctaveCount() {
        return this.octaveCount;
    }

    /**
     * Set the number of octaves that generate the Simplex noise.
     *
     * <p>The octave count must be between 1 and {@link #SIMPLEX_MAX_OCTAVE},
     * inclusive.</p>
     *
     * <p>The number of octaves controls the amount of detail in the
     * Simplex noise.</p>
     *
     * <p>The larger the number of octaves, the more time required to calculate
     * the Simplex-noise value.</p>
     *
     * @param octaveCount the number of octaves that generate the Simplex noise
     * @throws IllegalArgumentException if the octave count is out of bounds
     */
    public void setOctaveCount(final int octaveCount) {
        if (octaveCount < 1 || octaveCount > Simplex.SIMPLEX_MAX_OCTAVE) {
            throw new IllegalArgumentException("octaveCount must be between 1 and MAX OCTAVE: " + Simplex.SIMPLEX_MAX_OCTAVE);
        }

        this.octaveCount = octaveCount;
    }

    /**
     * Get the persistence value of the Simplex noise.
     *
     * <p>The persistence value controls the roughness of the Simplex noise.</p>
     *
     * @return the persistence value
     * @see #DEFAULT_SIMPLEX_PERSISTENCE
     */
    public double getPersistence() {
        return this.persistence;
    }

    /**
     * Sets the persistence value of the Simplex noise.
     *
     * <p>The persistence value controls the roughness of the Simplex noise.</p>
     *
     * <p>For best results, set the persistence to a number between
     * 0.0 and 1.0.</p>
     *
     * @param persistence the persistence value of the Simplex noise
     */
    public void setPersistence(final double persistence) {
        this.persistence = persistence;
    }

    /**
     * Get the seed value used by the Simplex noise function.
     *
     * @return the seed value
     * @see #DEFAULT_SIMPLEX_SEED
     */
    public int getSeed() {
        return this.seed;
    }

    /**
     * Set the seed value used by the Simplex-noise function.
     *
     * @param seed the seed value
     */
    public void setSeed(final int seed) {
        this.seed = seed;
    }
    
    /**
     * Returns the maximum value the simplex module can output in its
     * current configuration.
     *
     * @return The maximum possible value for
     *     {@link Simplex#getValue(double, double, double)} to return
     */
    public double getMaxValue() {
        /*
         * Each successive octave adds persistence ^ current_octaves to max possible output.
         * So (p = persistence, o = octave): Max(simplex) = p + p*p + p*p*p + ... + p^(o-1).
         * Using geometric series formula we can narrow it down to this:
         */
        return (Math.pow(this.getPersistence(), this.getOctaveCount()) - 1) / (this.getPersistence() - 1);
    }

    @Override
    public double getValue(final double x, final double y, final double z) {
        double x1 = x;
        double y1 = y;
        double z1 = z;
        double value = 0.0;
        double signal;
        double curPersistence = 1.0;
        double nx, ny, nz;
        int seed;

        x1 *= this.frequency;
        y1 *= this.frequency;
        z1 *= this.frequency;

        for (int curOctave = 0; curOctave < this.octaveCount; curOctave++) {

            // Make sure that these floating-point values have the same range as a 32-
            // bit integer so that we can pass them to the coherent-noise functions.
            nx = Utils.makeInt32Range(x1);
            ny = Utils.makeInt32Range(y1);
            nz = Utils.makeInt32Range(z1);

            // Get the coherent-noise value from the input value and add it to the
            // final result.
            seed = (this.seed + curOctave);
            signal = Noise.simplexStyleGradientCoherentNoise3D(nx, ny, nz, seed, this.latticeOrientation, this.noiseQuality);
            value += signal * curPersistence;

            // Prepare the next octave.
            x1 *= this.lacunarity;
            y1 *= this.lacunarity;
            z1 *= this.lacunarity;
            curPersistence *= this.persistence;
        }

        return value;
    }
}
