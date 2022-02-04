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
import org.spongepowered.noise.NoiseQuality;
import org.spongepowered.noise.Utils;
import org.spongepowered.noise.module.NoiseModule;

/**
 * Noise module that outputs three-dimensional "billowy" noise.
 *
 * <p>This noise module generates "billowy" noise suitable for clouds and
 * rocks.</p>
 *
 * <p>This noise module is nearly identical to {@link Perlin} except this noise
 * module modifies each octave with an absolute-value function. See the
 * documentation of {@link Perlin} for more information.</p>
 *
 * @sourceModules 0
 */
public class Billow extends NoiseModule {

    /**
     * Default frequency for the {@link Billow} noise module.
     */
    public static final double DEFAULT_BILLOW_FREQUENCY = 1.0;

    /**
     * Default lacunarity for the {@link Billow} noise module.
     */
    public static final double DEFAULT_BILLOW_LACUNARITY = 2.0;

    /**
     * Default number of octaves for the {@link Billow} noise module.
     */
    public static final int DEFAULT_BILLOW_OCTAVE_COUNT = 6;

    /**
     * Default persistence value for the {@link Billow} noise module.
     */
    public static final double DEFAULT_BILLOW_PERSISTENCE = 0.5;

    /**
     * Default noise quality for the {@link Billow} noise module.
     */
    public static final NoiseQuality DEFAULT_BILLOW_QUALITY = NoiseQuality.STANDARD;

    /**
     * Default noise seed for the {@link Billow} noise module.
     */
    public static final int DEFAULT_BILLOW_SEED = 0;

    /**
     * Maximum number of octaves for the {@link Billow} noise module.
     */
    public static final int BILLOW_MAX_OCTAVE = 30;

    private double frequency = Billow.DEFAULT_BILLOW_FREQUENCY;
    private double lacunarity = Billow.DEFAULT_BILLOW_LACUNARITY;
    private NoiseQuality quality = Billow.DEFAULT_BILLOW_QUALITY;
    private double persistence = Billow.DEFAULT_BILLOW_PERSISTENCE;
    private int seed = Billow.DEFAULT_BILLOW_SEED;
    private int octaveCount = Billow.DEFAULT_BILLOW_OCTAVE_COUNT;

    /**
     * Create a new {@link Billow} module, with all fields initialized to their
     * defaults.
     */
    public Billow() {
        super(0);
    }

    /**
     * Returns the number of octaves that generate the billowy noise.
     *
     * <p>The number of octaves controls the amount of detail in the
     * billowy noise.</p>
     *
     * @return the number of octaves that generate the billowy noise.
     * @see #DEFAULT_BILLOW_OCTAVE_COUNT
     */
    public int getOctaveCount() {
        return this.octaveCount;
    }

    /**
     * Sets the number of octaves that generate the billowy noise.
     *
     * <p>The number of octaves controls the amount of detail in the
     * billowy noise.</p>
     *
     * <p>The larger the number of octaves, the more time required to calculate
     * the billowy-noise value.</p>
     *
     * @param octaveCount the number of octaves, from 1 to {@link #BILLOW_MAX_OCTAVE}
     * @throws IllegalArgumentException if the octave count is out of bounds.
     */
    public void setOctaveCount(final int octaveCount) {
        if (octaveCount < 1 || octaveCount > Billow.BILLOW_MAX_OCTAVE) {
            throw new IllegalArgumentException("octaveCount must be between 1 and BILLOW_MAX_OCTAVE: " + Billow.BILLOW_MAX_OCTAVE);
        }
        this.octaveCount = octaveCount;
    }

    /**
     * Get the frequency of the first octave.
     *
     * @return the frequency of the first octave
     * @see #DEFAULT_BILLOW_FREQUENCY
     */
    public double getFrequency() {
        return this.frequency;
    }

    /**
     * Sets the frequency of the first octave.
     *
     * @param frequency the frequency of the first octave
     */
    public void setFrequency(final double frequency) {
        this.frequency = frequency;
    }

    /**
     * Get the lacunarity of the billowy noise.
     *
     * <p>The lacunarity is the frequency multiplier between
     * successive octaves.</p>
     *
     * @return the lacunarity of the billowy noise
     * @see #DEFAULT_BILLOW_LACUNARITY
     */
    public double getLacunarity() {
        return this.lacunarity;
    }

    /**
     * Set the lacunarity of the billowy noise.
     *
     * <p>The lacunarity is the frequency multiplier between
     * successive octaves.</p>
     *
     * <p>For best results, set the lacunarity to a number between {@code 1.5}
     * and {@code 3.5}.</p>
     *
     * @param lacunarity the lacunarity of the billowy noise
     */
    public void setLacunarity(final double lacunarity) {
        this.lacunarity = lacunarity;
    }

    /**
     * Get the quality of the billowy noise.
     *
     * <p>See {@link NoiseQuality} for definitions of the various
     * coherent-noise qualities.</p>
     *
     * @return the quality of the billowy noise
     * @see #DEFAULT_BILLOW_QUALITY
     */
    public NoiseQuality getQuality() {
        return this.quality;
    }

    /**
     * Sets the quality of the billowy noise.
     *
     * <p>See {@link NoiseQuality} for definitions of the various
     * coherent-noise qualities.</p>
     *
     * @param quality the quality of the billowy noise
     */
    public void setQuality(final NoiseQuality quality) {
        this.quality = quality;
    }

    /**
     * Get the persistence value of the billowy noise.
     *
     * <p>The persistence value controls the roughness of the billowy noise.</p>
     *
     * @return the persistence value of the billowy noise
     * @see #DEFAULT_BILLOW_PERSISTENCE
     */
    public double getPersistence() {
        return this.persistence;
    }

    /**
     * Sets the persistence value of the billowy noise.
     *
     * @param persistence the persistence value of the billowy noise.
     *
     * <p>The persistence value controls the roughness of the billowy noise.</p>
     *
     * <p>For best results, set the persistence value to a number between
     * {@code 0.0} and {@code 1.0}.</p>
     */
    public void setPersistence(final double persistence) {
        this.persistence = persistence;
    }

    /**
     * Gets the seed used by the billowy-noise function.
     *
     * @return the seed value
     * @see #DEFAULT_BILLOW_SEED
     */
    public int getSeed() {
        return this.seed;
    }

    /**
     * Sets the seed value used by the billowy-noise function.
     *
     * @param seed the seed value
     */
    public void setSeed(final int seed) {
        this.seed = seed;
    }

    @Override
    public double getValue(final double x, final double y, final double z) {
        double z1 = z;
        double y1 = y;
        double x1 = x;
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
            signal = Noise.gradientCoherentNoise3D(nx, ny, nz, seed, this.quality) * 2 - 1;
            signal = Math.abs(signal);
            value += signal * curPersistence;

            // Prepare the next octave.
            x1 *= this.lacunarity;
            y1 *= this.lacunarity;
            z1 *= this.lacunarity;
            curPersistence *= this.persistence;
        }
        value += 0.25;

        return value;
    }
}
