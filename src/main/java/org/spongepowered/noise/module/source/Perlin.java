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

public class Perlin extends Module {
    // Default frequency for the noise::module::Perlin noise module.
    public static final double DEFAULT_PERLIN_FREQUENCY = 1.0;
    // Default lacunarity for the noise::module::Perlin noise module.
    public static final double DEFAULT_PERLIN_LACUNARITY = 2.0;
    // Default number of octaves for the noise::module::Perlin noise module.
    public static final int DEFAULT_PERLIN_OCTAVE_COUNT = 6;
    // Default persistence value for the noise::module::Perlin noise module.
    public static final double DEFAULT_PERLIN_PERSISTENCE = 0.5;
    // Default noise quality for the noise::module::Perlin noise module.
    public static final NoiseQuality DEFAULT_PERLIN_QUALITY = NoiseQuality.STANDARD;
    // Default noise seed for the noise::module::Perlin noise module.
    public static final int DEFAULT_PERLIN_SEED = 0;
    // Maximum number of octaves for the noise::module::Perlin noise module.
    public static final int PERLIN_MAX_OCTAVE = 30;
    // Frequency of the first octave.
    private double frequency = DEFAULT_PERLIN_FREQUENCY;
    // Frequency multiplier between successive octaves.
    private double lacunarity = DEFAULT_PERLIN_LACUNARITY;
    // Quality of the Perlin noise.
    private NoiseQuality noiseQuality = DEFAULT_PERLIN_QUALITY;
    // Total number of octaves that generate the Perlin noise.
    private int octaveCount = DEFAULT_PERLIN_OCTAVE_COUNT;
    // Persistence of the Perlin noise.
    private double persistence = DEFAULT_PERLIN_PERSISTENCE;
    // Seed value used by the Perlin-noise function.
    private int seed = DEFAULT_PERLIN_SEED;

    public Perlin() {
        super(0);
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
        if (octaveCount < 1 || octaveCount > PERLIN_MAX_OCTAVE) {
            throw new IllegalArgumentException("octaveCount must be between 1 and MAX OCTAVE: " + PERLIN_MAX_OCTAVE);
        }

        this.octaveCount = octaveCount;
    }

    public double getPersistence() {
        return persistence;
    }

    public void setPersistence(double persistence) {
        this.persistence = persistence;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }
    
    /**
     * Returns the maximum value the perlin module can output in it's current configuration
     * @return The maximum possible value for {@link Perlin#getValue(double, double, double)} to return
     */
    public double getMaxValue() {
    	/*
    	 * Each successive octave adds persistence ^ current_octaves to max possible output.
    	 * So (p = persistence, o = octave): Max(perlin) = p + p*p + p*p*p + ... + p^(o-1).
    	 * Using geometric series formula we can narrow it down to this:
    	 */
    	return (Math.pow(getPersistence(), getOctaveCount()) - 1) / (getPersistence() - 1);
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
