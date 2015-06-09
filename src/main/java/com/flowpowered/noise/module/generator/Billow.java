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
package com.flowpowered.noise.module.generator;

import com.flowpowered.noise.Noise;
import com.flowpowered.noise.NoiseQuality;
import com.flowpowered.noise.Utils;
import com.flowpowered.noise.module.Module;

/**
 * Represents a generator module that generates "billowy" noise suitable for clouds and rocks.
 *
 * <p>It works by adding several octaves of noise (generated separately) together. Several values
 * are applied iteratively to each successive octave. The {@link #lacunarity} determines how
 * quickly the frequency increases or decreases for each successive octave, while the
 * {@link #persistence} determines the increase or decrease in amplitude.</p>
 *
 * <p>A higher {@link #noiseQuality noise quality} value causes less artifacts in the generated
 * values, but takes more time to generate. Because the outputs of noise functions are somewhat
 * similar to sinusoids and have a "periodicity", the {@link #frequency} value determines about how
 * many "cycles" there are per unit length (1.0).</p>
 *
 * <p>The seed is nearly identical to the concept of a :random number seed"</p>
 *
 * <p>The "billow" module comes from libnoise's
 * <a href="http://libnoise.sourceforge.net/docs/classnoise_1_1module_1_1Billow.html">Billow</a>.
 * </p>
 */
public class Billow extends Generator {

    /**
     * The default octave count.
     */
    public static final int DEFAULT_OCTAVE_COUNT = 6;

    /**
     * The maximum count of octaves.
     */
    public static final int MAX_OCTAVE_COUNT = 30;

    /**
     * The default frequency.
     */
    public static final double DEFAULT_FREQUENCY = 1.0;

    /**
     * The default lacunarity.
     */
    public static final double DEFAULT_LACUNARITY = 2.0;

    /**
     * The default persistence.
     */
    public static final double DEFAULT_PERSISTENCE = 0.5;

    /**
     * The default noise quality.
     */
    public static final NoiseQuality DEFAULT_QUALITY = NoiseQuality.STANDARD;

    /**
     * The default seed.
     */
    public static final int DEFAULT_SEED = 0;

    private final int octaveCount;
    private final double frequency;
    private final double lacunarity;
    private final double persistence;
    private final NoiseQuality noiseQuality;
    private final int seed;

    /**
     * Constructs a new instance given the billow options.
     *
     * @param octaveCount The octave count
     * @param frequency The frequency
     * @param lacunarity The lacunarity
     * @param persistence The persistence
     * @param noiseQuality The noise quality
     * @param seed The seed
     */
    public Billow(int octaveCount, double frequency, double lacunarity, double persistence, NoiseQuality noiseQuality, int seed) {
        if (octaveCount < 1 || octaveCount > MAX_OCTAVE_COUNT) {
            throw new IllegalArgumentException("octaveCount must be between 1 and BILLOW_MAX_OCTAVE: " + MAX_OCTAVE_COUNT);
        }

        this.octaveCount = octaveCount;
        this.frequency = frequency;
        this.lacunarity = lacunarity;
        this.persistence = persistence;
        this.noiseQuality = noiseQuality;
        this.seed = seed;
    }

    /**
     * Gets the octave count.
     *
     * @return The octave count
     */
    public int getOctaveCount() {
        return octaveCount;
    }

    /**
     * Gets the frequency.
     *
     * @return The frequency
     */
    public double getFrequency() {
        return frequency;
    }

    /**
     * Gets the lacunarity.
     *
     * @return The lacunarity
     */
    public double getLacunarity() {
        return lacunarity;
    }

    /**
     * Gets the persistence.
     *
     * @return The persistence
     */
    public double getPersistence() {
        return persistence;
    }

    /**
     * Gets the noise quality.
     *
     * @return The noise quality
     */
    public NoiseQuality getNoiseQuality() {
        return noiseQuality;
    }

    /**
     * Gets the seed.
     *
     * @return The seed
     */
    public int getSeed() {
        return seed;
    }

    @Override
    public double getValue(double x, double y, double z) {
        double z1 = z;
        double y1 = y;
        double x1 = x;
        double value = 0.0;
        double signal;
        double curPersistence = 1.0;
        double nx, ny, nz;
        int seed;

        x1 *= frequency;
        y1 *= frequency;
        z1 *= frequency;

        for (int curOctave = 0; curOctave < octaveCount; curOctave++) {

            // Make sure that these floating-point values have the same range as a 32-
            // bit integer so that we can pass them to the coherent-noise functions.
            nx = Utils.makeInt32Range(x1);
            ny = Utils.makeInt32Range(y1);
            nz = Utils.makeInt32Range(z1);

            // Get the coherent-noise value from the input value and add it to the
            // final result.
            seed = (this.seed + curOctave);
            signal = Noise.gradientCoherentNoise3D(nx, ny, nz, seed, noiseQuality);
            signal = 2.0 * Math.abs(signal) - 1.0;
            value += signal * curPersistence;

            // Prepare the next octave.
            x1 *= lacunarity;
            y1 *= lacunarity;
            z1 *= lacunarity;
            curPersistence *= persistence;
        }
        value += 0.5;

        return value;
    }

    /**
     * Returns a new builder instance.
     *
     * @return A new builder instance
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Represents a builder of {@link Billow} instances.
     */
    public static class Builder extends Generator.Builder {

        /**
         * The octave count, or how many octaves are involved in generating the noise.
         */
        private final int octaveCount;

        /**
         * The frequency.
         */
        private final double frequency;

        /**
         * The lacunarity.
         */
        private final double lacunarity;

        /**
         * The noise quality.
         */
        private final NoiseQuality noiseQuality;

        /**
         * The persistence.
         */
        private final double persistence;

        /**
         * The seed.
         */
        private final int seed;

        @Override
        protected void checkValues() throws IllegalStateException {

        }

        @Override
        public Billow build() throws IllegalStateException {
            return null;
        }

    }

}
