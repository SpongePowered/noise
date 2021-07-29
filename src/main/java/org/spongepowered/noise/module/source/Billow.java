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

public class Billow extends Module {
    public static final double DEFAULT_BILLOW_FREQUENCY = 1.0;
    public static final double DEFAULT_BILLOW_LACUNARITY = 2.0;
    public static final int DEFAULT_BILLOW_OCTAVE_COUNT = 6;
    public static final double DEFAULT_BILLOW_PERSISTENCE = 0.5;
    public static final NoiseQuality DEFAULT_BILLOW_QUALITY = NoiseQuality.STANDARD;
    public static final int DEFAULT_BILLOW_SEED = 0;
    public static final int BILLOW_MAX_OCTAVE = 30;
    private double frequency = Billow.DEFAULT_BILLOW_FREQUENCY;
    private double lacunarity = Billow.DEFAULT_BILLOW_LACUNARITY;
    private NoiseQuality quality = Billow.DEFAULT_BILLOW_QUALITY;
    private double persistence = Billow.DEFAULT_BILLOW_PERSISTENCE;
    private int seed = Billow.DEFAULT_BILLOW_SEED;
    private int octaveCount = Billow.DEFAULT_BILLOW_OCTAVE_COUNT;

    public Billow() {
        super(0);
    }

    public int getOctaveCount() {
        return this.octaveCount;
    }

    public void setOctaveCount(final int octaveCount) {
        if (octaveCount < 1 || octaveCount > Billow.BILLOW_MAX_OCTAVE) {
            throw new IllegalArgumentException("octaveCount must be between 1 and BILLOW_MAX_OCTAVE: " + Billow.BILLOW_MAX_OCTAVE);
        }
        this.octaveCount = octaveCount;
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

    public NoiseQuality getQuality() {
        return this.quality;
    }

    public void setQuality(final NoiseQuality quality) {
        this.quality = quality;
    }

    public double getPersistence() {
        return this.persistence;
    }

    public void setPersistence(final double persistence) {
        this.persistence = persistence;
    }

    public int getSeed() {
        return this.seed;
    }

    public void setSeed(final int seed) {
        this.seed = seed;
    }

    @Override
    public int getSourceModuleCount() {
        return 0;
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
