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
import org.spongepowered.noise.module.Module;

public class RidgedMulti extends Module {
    // Default frequency for the noise::module::RidgedMulti noise module.
    public static final double DEFAULT_RIDGED_FREQUENCY = 1.0;
    // Default lacunarity for the noise::module::RidgedMulti noise module.
    public static final double DEFAULT_RIDGED_LACUNARITY = 2.0;
    // Default number of octaves for the noise::module::RidgedMulti noise module.
    public static final int DEFAULT_RIDGED_OCTAVE_COUNT = 6;
    // Default noise quality for the noise::module::RidgedMulti noise module.
    public static final NoiseQuality DEFAULT_RIDGED_QUALITY = NoiseQuality.STANDARD;
    // Default noise seed for the noise::module::RidgedMulti noise module.
    public static final int DEFAULT_RIDGED_SEED = 0;
    // Maximum number of octaves for the noise::module::RidgedMulti noise module.
    public static final int RIDGED_MAX_OCTAVE = 30;
    private double frequency = DEFAULT_RIDGED_FREQUENCY;
    // Frequency multiplier between successive octaves.
    private double lacunarity = DEFAULT_RIDGED_LACUNARITY;
    // Quality of the ridged-multifractal noise.
    private NoiseQuality noiseQuality = DEFAULT_RIDGED_QUALITY;
    // Total number of octaves that generate the ridged-multifractal noise.
    private int octaveCount = DEFAULT_RIDGED_OCTAVE_COUNT;
    // Contains the spectral weights for each octave.
    private double[] spectralWeights;
    // Seed value used by the ridged-multfractal-noise function.
    private int seed = DEFAULT_RIDGED_SEED;

    public RidgedMulti() {
        super(0);
        calcSpectralWeights();
    }

    public double getFrequency() {
        return frequency;
    }

    public void setFrequency(double frequency) {
        this.frequency = frequency;
    }

    public double getLacunarity() {
        return lacunarity;
    }

    public void setLacunarity(double lacunarity) {
        this.lacunarity = lacunarity;
    }

    public NoiseQuality getNoiseQuality() {
        return noiseQuality;
    }

    public void setNoiseQuality(NoiseQuality noiseQuality) {
        this.noiseQuality = noiseQuality;
    }

    public int getOctaveCount() {
        return octaveCount;
    }

    public void setOctaveCount(int octaveCount) {
        this.octaveCount = Math.min(octaveCount, RIDGED_MAX_OCTAVE);
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    private void calcSpectralWeights() {
        // This exponent parameter should be user-defined; it may be exposed in a
        // future version of libnoise.
        double h = 1.0;

        double frequency = 1.0;
        spectralWeights = new double[RIDGED_MAX_OCTAVE];
        for (int i = 0; i < RIDGED_MAX_OCTAVE; i++) {
            // Compute weight for each frequency.
            spectralWeights[i] = Math.pow(frequency, -h);
            frequency *= lacunarity;
        }
    }

    @Override
    public int getSourceModuleCount() {
        return 0;
    }

    @Override
    public double getValue(double x, double y, double z) {
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
            signal = Noise.gradientCoherentNoise3D(nx, ny, nz, seed, noiseQuality) * 2 - 1;

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
            value += (signal * spectralWeights[curOctave]);

            // Go to the next octave.
            x1 *= lacunarity;
            y1 *= lacunarity;
            z1 *= lacunarity;
        }

        return value / 1.6;
    }
}
