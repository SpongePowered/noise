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
 * Noise module that outputs 3-dimensional ridged-multifractal noise.
 *
 * <p>This noise module, heavily based on the Perlin-noise module, generates
 * ridged-multifractal noise. Ridged-multifractal noise is generated in
 * much of the same way as Perlin noise, except the output of each octave
 * is modified by an absolute-value function. Modifying the octave
 * values in this way produces ridge-like formations.</p>
 *
 * <p>Ridged-multifractal noise does not use a persistence value. This is
 * because the persistence values of the octaves are based on the values
 * generated from previous octaves, creating a feedback loop (or
 * that's what it looks like after reading the code.)</p>
 *
 * <p>This noise module outputs ridged-multifractal-noise values that
 * usually range from -1.0 to +1.0, but there are no guarantees that all
 * output values will exist within that range.</p>
 *
 * <p>For ridged-multifractal noise generated with only one octave,
 * the output value ranges from -1.0 to 0.0.</p>
 *
 * <p>Ridged-multifractal noise is often used to generate craggy mountainous
 * terrain or marble-like textures.</p>
 *
 * <h2>Octaves</h2>
 *
 * <p>The number of octaves control the <i>amount of detail</i> of the
 * ridged-multifractal noise. Adding more octaves increases the detail
 * of the ridged-multifractal noise, but with the drawback of increasing
 * the calculation time.</p>
 *
 * <p>An application may specify the number of octaves that generate ridged
 * -multifractal noise by calling the {@link #setOctaveCount(int)} method.</p>
 *
 * <h2>Frequency</h2>
 *
 * <p>An application may specify the frequency of the first octave by
 * calling the {@link #setFrequency(double)} method.</p>
 *
 * <h2>Lacunarity</h2>
 *
 * <p>The lacunarity specifies the frequency multiplier between successive
 * octaves.</p>
 *
 * <p>The effect of modifying the lacunarity is subtle; you may need to play
 * with the lacunarity value to determine the effects.  For best results,
 * set the lacunarity to a number between 1.5 and 3.5.</p>
 *
 * <h2 id="references">References &amp; Acknowledgments</h2>
 *
 * <p><a href="https://web.archive.org/web/20070822200812/http://www.texturingandmodeling.com/Musgrave.html">F.
 * Kenton "Doc Mojo" Musgrave's texturing page</a> - This page contains
 * links to source code that generates ridged-multfractal noise, among
 * other types of noise. The source file <a href="https://web.archive.org/web/20061119144652/http://www.texturingandmodeling.com/CODE/MUSGRAVE/CLOUD/fractal.c">
 * fractal.c</a> contains the code I used in my ridged-multifractal class
 * (see the {@code RidgedMultifractal())} function.) This code was written by F.
 * Kenton Musgrave, the person who created <a href=http://www.pandromeda.com/>MojoWorld</a>.
 * He is also one of the authors in <em>Texturing and Modeling:
 * A Procedural Approach</em> (Morgan Kaufmann, 2002. ISBN 1-55860-848-6).</p>
 *
 * @sourceModules 0
 */
public class RidgedMulti extends NoiseModule {

    /**
     * Default frequency for the {@link RidgedMulti} noise module.
     */
    public static final double DEFAULT_RIDGED_FREQUENCY = 1.0;

    /**
     * Default lacunarity for the {@link RidgedMulti} noise module.
     */
    public static final double DEFAULT_RIDGED_LACUNARITY = 2.0;

    /**
     * Default number of octaves for the {@link RidgedMulti} noise module.
     */
    public static final int DEFAULT_RIDGED_OCTAVE_COUNT = 6;

    /**
     * Default noise quality for the {@link RidgedMulti} noise module.
     */
    public static final NoiseQuality DEFAULT_RIDGED_QUALITY = NoiseQuality.STANDARD;

    /**
     * Default noise seed for the {@link RidgedMulti} noise module.
     */
    public static final int DEFAULT_RIDGED_SEED = 0;

    /**
     * Maximum number of octaves for the {@link RidgedMulti} noise module.
     */
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
    // Seed value used by the ridged-multifractal-noise function.
    private int seed = RidgedMulti.DEFAULT_RIDGED_SEED;

    public RidgedMulti() {
        super(0);
        this.calcSpectralWeights();
    }

    /**
     * Get the frequency of the first octave.
     *
     * @return the frequency of the first octave
     * @see #DEFAULT_RIDGED_FREQUENCY
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
     * Get the lacunarity of the ridged-multifractal noise.
     *
     * @return the lacunarity of the ridged-multifractal noise.
     * @see #DEFAULT_RIDGED_LACUNARITY
     */
    public double getLacunarity() {
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
     * Get the quality of the ridged-multifractal noise.
     *
     * <p>See {@link NoiseQuality} for definitions of the various
     * coherent-noise qualities.</p>
     *
     * @return the quality of the ridged-multifractal noise
     * @see #DEFAULT_RIDGED_QUALITY
     */
    public NoiseQuality getNoiseQuality() {
        return this.noiseQuality;
    }

    /**
     * Set the quality of the ridged-multifractal noise.
     *
     * <p>See {@link NoiseQuality} for definitions of the various
     * coherent-noise qualities.</p>
     *
     * @param noiseQuality the quality of the ridged-multifractal noise
     */
    public void setNoiseQuality(final NoiseQuality noiseQuality) {
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
    public int getOctaveCount() {
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
    public int getSeed() {
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
        this.spectralWeights = new double[RidgedMulti.RIDGED_MAX_OCTAVE];
        for (int i = 0; i < RidgedMulti.RIDGED_MAX_OCTAVE; i++) {
            // Compute weight for each frequency.
            this.spectralWeights[i] = Math.pow(frequency, -h);
            frequency *= this.lacunarity;
        }
    }

    /**
     * Returns the maximum value the RidgedMulti module can output in its
     * current configuration.
     *
     * @return The maximum possible value for
     *     {@link RidgedMulti#getValue(double, double, double)} to return
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
            final double ny;
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
