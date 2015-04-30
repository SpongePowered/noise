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

public class Perlin extends Module {

    public static final int PERLIN_MAX_OCTAVE = 30;

    // Total number of octaves that generate the Perlin noise.
    private final int octaveCount;
    // Frequency of the first octave.
    private final double frequency;
    // Frequency multiplier between successive octaves.
    private final double lacunarity;
    // Persistence of the Perlin noise.
    private final double persistence;
    // Quality of the Perlin noise.
    private final NoiseQuality noiseQuality;
    // Seed value used by the Perlin-noise function.
    private final int seed;

    public Perlin(int octaveCount, double frequency, double lacunarity, double persistence, NoiseQuality noiseQuality, int seed) {

        if (octaveCount < 1 || octaveCount > PERLIN_MAX_OCTAVE) {
            throw new IllegalArgumentException("octaveCount must be between 1 and MAX OCTAVE: " + PERLIN_MAX_OCTAVE);
        }

        this.octaveCount = octaveCount;
        this.frequency = frequency;
        this.lacunarity = lacunarity;
        this.persistence = persistence;
        this.noiseQuality = noiseQuality;
        this.seed = seed;
    }

    public int getOctaveCount() {
        return octaveCount;
    }

    public double getFrequency() {
        return frequency;
    }

    public double getLacunarity() {
        return lacunarity;
    }

    public double getPersistence() {
        return persistence;
    }

    public NoiseQuality getNoiseQuality() {
        return noiseQuality;
    }

    public int getSeed() {
        return seed;
    }

    @Override
    public double get(double x, double y, double z) {
        double x1 = x;
        double y1 = y;
        double z1 = z;
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
            value += signal * curPersistence;

            // Prepare the next octave.
            x1 *= lacunarity;
            y1 *= lacunarity;
            z1 *= lacunarity;
            curPersistence *= persistence;
        }

        return value;
    }
}
