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

public class RidgedMulti extends Module {

    // Maximum number of octaves for the noise::module::RidgedMulti noise module.
    public static final int RIDGED_MAX_OCTAVE = 30;

    // Total number of octaves that generate the ridged-multifractal noise.
    private final int octaveCount;
    // Frequency of the first octave.
    private final double frequency;
    // Frequency multiplier between successive octaves.
    private final double lacunarity;
    // Quality of the ridged-multifractal noise.
    private final NoiseQuality noiseQuality;
    // Seed value used by the ridged-multfractal-noise function.
    private final int seed;
    // Contains the spectral weights for each octave.
    private final double[] spectralWeights;

    public RidgedMulti(int octaveCount, double frequency, double lacunarity, NoiseQuality noiseQuality, int seed) {

        if (octaveCount < 1 || octaveCount > RIDGED_MAX_OCTAVE) {
            throw new IllegalArgumentException("octaveCount must be between 1 and MAX OCTAVE: " + RIDGED_MAX_OCTAVE);
        }

        this.octaveCount = octaveCount;
        this.frequency = frequency;
        this.lacunarity = lacunarity;
        this.noiseQuality = noiseQuality;
        this.seed = seed;

        // Calculae spectral weights
        // This exponent parameter should be user-defined; it may be exposed in a
        // future version of libnoise.
        double h = 1.0;

        double freq = 1.0;
        spectralWeights = new double[octaveCount];
        for (int i = 0; i < RIDGED_MAX_OCTAVE; i++) {
            // Compute weight for each frequency.
            spectralWeights[i] = Math.pow(freq, -h);
            freq *= lacunarity;
        }
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
        x1 *= frequency;
        y1 *= frequency;
        z1 *= frequency;

        double signal;
        double value = 0.0;
        double weight = 1.0;

        // These parameters should be user-defined; they may be exposed in a
        // future version of libnoise.
        double offset = 1.0;
        double gain = 2.0;

        for (int curOctave = 0; curOctave < octaveCount; curOctave++) {

            // Make sure that these floating-point values have the same range as a 32-
            // bit integer so that we can pass them to the coherent-noise functions.
            double nx, ny, nz;
            nx = Utils.makeInt32Range(x1);
            ny = Utils.makeInt32Range(y1);
            nz = Utils.makeInt32Range(z1);

            // Get the coherent-noise value.
            int seed = (this.seed + curOctave) & 0x7fffffff;
            signal = Noise.gradientCoherentNoise3D(nx, ny, nz, seed, noiseQuality);

            // Make the ridges.
            signal = Math.abs(signal);
            signal = offset - signal;

            // Square the signal to increase the sharpness of the ridges.
            //noinspection UnusedAssignment
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
            value += (signal * spectralWeights[curOctave]);

            // Go to the next octave.
            x1 *= lacunarity;
            y1 *= lacunarity;
            z1 *= lacunarity;
        }

        return (value * 1.25) - 1.0;
    }

}
