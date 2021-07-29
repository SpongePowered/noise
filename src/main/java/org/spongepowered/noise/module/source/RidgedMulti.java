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
    private double frequency = RidgedMulti.DEFAULT_RIDGED_FREQUENCY;
    // Frequency multiplier between successive octaves.
    private double lacunarity = RidgedMulti.DEFAULT_RIDGED_LACUNARITY;
    // Quality of the ridged-multifractal noise.
    private NoiseQuality noiseQuality = RidgedMulti.DEFAULT_RIDGED_QUALITY;
    // Total number of octaves that generate the ridged-multifractal noise.
    private int octaveCount = RidgedMulti.DEFAULT_RIDGED_OCTAVE_COUNT;
    // Contains the spectral weights for each octave.
    private double[] spectralWeights;
    // Seed value used by the ridged-multfractal-noise function.
    private int seed = RidgedMulti.DEFAULT_RIDGED_SEED;

    public RidgedMulti() {
        super(0);
        this.calcSpectralWeights();
    }

    public double getFrequency() {
        return this.frequency;
    }

    public void setFrequency(final double frequency) {
        this.frequency = frequency;
    }

    public double getLacunarity() {
        return this.lacunarity;
    }

    public void setLacunarity(final double lacunarity) {
        this.lacunarity = lacunarity;
    }

    public NoiseQuality getNoiseQuality() {
        return this.noiseQuality;
    }

    public void setNoiseQuality(final NoiseQuality noiseQuality) {
        this.noiseQuality = noiseQuality;
    }

    public int getOctaveCount() {
        return this.octaveCount;
    }

    public void setOctaveCount(final int octaveCount) {
        this.octaveCount = Math.min(octaveCount, RidgedMulti.RIDGED_MAX_OCTAVE);
    }

    public int getSeed() {
        return this.seed;
    }

    public void setSeed(final int seed) {
        this.seed = seed;
    }

    private void calcSpectralWeights() {
        // This exponent parameter should be user-defined; it may be exposed in a
        // future version of libnoise.
        final double h = 1.0;

        double frequency = 1.0;
        this.spectralWeights = new double[RidgedMulti.RIDGED_MAX_OCTAVE];
        for (int i = 0; i < RidgedMulti.RIDGED_MAX_OCTAVE; i++) {
            // Compute weight for each frequency.
            this.spectralWeights[i] = Math.pow(frequency, -h);
            frequency *= this.lacunarity;
        }
    }

    /**
     * Returns the maximum value the RidgedMulti module can output in its current configuration
     * @return The maximum possible value for {@link RidgedMulti#getValue(double, double, double)} to return
     */
    public double getMaxValue() {
    	/*
    	 * Each successive octave adds (1/lacunarity) ^ current_octaves to max possible output.
    	 * So (r = lacunarity, o = octave): Max(ridged) = 1 + 1/r + 1/(r*r) + 1/(r*r*r) + ... + (1/r^(o-1))
    	 * See https://www.wolframalpha.com/input/?i=sum+from+k%3D0+to+n-1+of+1%2Fx%5Ek
    	 */
        return (this.getLacunarity() - Math.pow(this.getLacunarity(), 1 - this.getOctaveCount())) / (this.getLacunarity() - 1) / 1.6;
    }

    @Override
    public int getSourceModuleCount() {
        return 0;
    }

    @Override
    public double getValue(final double x, final double y, final double z) {
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
            final double nx;
            double ny;
            final double nz;
            nx = Utils.makeInt32Range(x1);
            ny = Utils.makeInt32Range(y1);
            nz = Utils.makeInt32Range(z1);

            // Get the coherent-noise value.
            final int seed = (this.seed + curOctave) & 0x7fffffff;
            signal = Noise.gradientCoherentNoise3D(nx, ny, nz, seed, this.noiseQuality) * 2 - 1;

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
