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

/**
 * Noise module that outputs 3-dimensional Perlin noise.
 *
 * <p>Perlin noise is the sum of several coherent-noise functions of
 * ever-increasing frequencies and ever-decreasing amplitudes.</p>
 *
 * <p>An important property of Perlin noise is that a small change in the
 * input value will produce a small change in the output value, while a
 * large change in the input value will produce a random change in the
 * output value.</p>
 *
 * <p>This noise module outputs Perlin-noise values that usually range from
 * -1.0 to +1.0, but there are no guarantees that all output values will
 * exist within that range.</p>
 *
 * <p>For a better description of Perlin noise, see the links in the
 * <a href="#references">References and Acknowledgments</a> section.</p>
 *
 * <h2>Octaves</h2>
 *
 * <p>The number of octaves control the <em>amount of detail</em> of the
 * Perlin noise. Adding more octaves increases the detail of the Perlin
 * noise, but with the drawback of increasing the calculation time.</p>
 *
 * <p>An octave is one of the coherent-noise functions in a series of
 * coherent-noise functions that are added together to form Perlin
 * noise.</p>
 *
 * <p>An application may specify the frequency of the first octave by
 * calling the {@link #setFrequency(double)} method.</p>
 *
 * <p>An application may specify the number of octaves that generate Perlin
 * noise by calling the {@link #setOctaveCount(int)} method.</p>
 *
 * <p>These coherent-noise functions are called octaves because each octave
 * has, by default, double the frequency of the previous octave. Musical
 * tones have this property as well; a musical C tone that is one octave
 * higher than the previous C tone has double its frequency.</p>
 *
 * <h2>Frequency</h2>
 *
 * <p>An application may specify the frequency of the first octave by
 * calling the {@link #setFrequency(double)} method.</p>
 *
 * <h2>Persistence</h2>
 *
 * <p>The persistence value controls the <i>roughness</i> of the Perlin noise.
 * Larger values produce rougher noise.</p>
 *
 * <p>The persistence value determines how quickly the amplitudes diminish
 * for successive octaves. The amplitude of the first octave is 1.0.
 * The amplitude of each subsequent octave is equal to the product of the
 * previous octave's amplitude and the persistence value. So a
 * persistence value of 0.5 sets the amplitude of the first octave to
 * 1.0; the second, 0.5; the third, 0.25; etc.</p>
 *
 * <p>An application may specify the persistence value by calling the
 * {@link #setPersistence(double)} method.</p>
 *
 * <h2>Lacunarity</h2>
 *
 * <p>The lacunarity specifies the frequency multiplier between successive
 * octaves.</p>
 *
 * <p>The effect of modifying the lacunarity is subtle; you may need to play
 * with the lacunarity value to determine the effects. For best results,
 * set the lacunarity to a number between 1.5 and 3.5.</p>
 *
 * <h2 id="references">References &amp; acknowledgments</h2>
 *
 * <p><a href="http://www.noisemachine.com/talk1/">The Noise Machine</a> -
 * From the master, Ken Perlin himself. This page contains a presentation that
 * describes Perlin noise and some of its variants. He won an Oscar for creating
 * the Perlin noise algorithm!</p>
 *
 * <p><a href="http://freespace.virgin.net/hugo.elias/models/m_perlin.htm">
 * Perlin Noise</a> - Hugo Elias's webpage contains a very good description of
 * Perlin noise and describes its many applications. This page gave me the
 * inspiration to create libnoise in the first place. Now that I know how to
 * generate Perlin noise, I will never again use cheesy subdivision algorithms
 * to create terrain (unless I absolutely need the speed).</p>
 *
 * <p><a href=http://www.robo-murito.net/code/perlin-noise-math-faq.html>The
 * Perlin noise math FAQ</a> - A good page that describes Perlin noise in
 * plain English with only a minor amount of math.  During development of
 * libnoise, I noticed that my coherent-noise function generated terrain
 * with some "regularity" to the terrain features.  This page describes a
 * better coherent-noise function called <i>gradient noise</i>. This version of
 * {@link Perlin} uses gradient coherent noise to generate Perlin noise.</p>
 *
 * @sourceModules 0
 */
public class Perlin extends Module {

    /**
     * Default frequency for the {@link Perlin} noise module.
     */
    public static final double DEFAULT_PERLIN_FREQUENCY = 1.0;

    /**
     * Default lacunarity for the {@link Perlin} noise module.
     */
    public static final double DEFAULT_PERLIN_LACUNARITY = 2.0;

    /**
     * Default number of octaves for the {@link Perlin} noise module.
     */
    public static final int DEFAULT_PERLIN_OCTAVE_COUNT = 6;

    /**
     * Default persistence value for the {@link Perlin} noise module.
     */
    public static final double DEFAULT_PERLIN_PERSISTENCE = 0.5;

    /**
     * Default noise quality for the {@link Perlin} noise module.
     */
    public static final NoiseQuality DEFAULT_PERLIN_QUALITY = NoiseQuality.STANDARD;

    /**
     * Default noise seed for the {@link Perlin} noise module.
     */
    public static final int DEFAULT_PERLIN_SEED = 0;

    /**
     * Maximum number of octaves for the {@link Perlin} noise module.
     */
    public static final int PERLIN_MAX_OCTAVE = 30;

    // Frequency of the first octave.
    private double frequency = Perlin.DEFAULT_PERLIN_FREQUENCY;
    // Frequency multiplier between successive octaves.
    private double lacunarity = Perlin.DEFAULT_PERLIN_LACUNARITY;
    // Quality of the Perlin noise.
    private NoiseQuality noiseQuality = Perlin.DEFAULT_PERLIN_QUALITY;
    // Total number of octaves that generate the Perlin noise.
    private int octaveCount = Perlin.DEFAULT_PERLIN_OCTAVE_COUNT;
    // Persistence of the Perlin noise.
    private double persistence = Perlin.DEFAULT_PERLIN_PERSISTENCE;
    // Seed value used by the Perlin-noise function.
    private int seed = Perlin.DEFAULT_PERLIN_SEED;

    public Perlin() {
        super(0);
    }

    /**
     * Get the frequency of the first octave.
     *
     * @return the frequency of the first octave
     * @see #DEFAULT_PERLIN_FREQUENCY
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
     * Get the lacunarity of the Perlin noise.
     *
     * <p>The lacunarity is the frequency multiplier between successive octaves.</p>
     *
     * @return the lacunarity of the Perlin noise
     * @see #DEFAULT_PERLIN_LACUNARITY
     */
    public double getLacunarity() {
        return this.lacunarity;
    }

    /**
     * Sets the lacunarity of the Perlin noise.
     *
     * <p>The lacunarity is the frequency multiplier between successive
     * octaves.</p>
     *
     * <p>For best results, set the lacunarity to a number between 1.5
     * and 3.5.</p>
     *
     * @param lacunarity the lacunarity of the Perlin noise
     */
    public void setLacunarity(final double lacunarity) {
        this.lacunarity = lacunarity;
    }

    /**
     * Get the quality of the Perlin noise.
     *
     * <p>See {@link NoiseQuality} for definitions of the various coherent-noise
     * qualities.</p>
     *
     * @return the quality of the Perlin noise
     * @see #DEFAULT_PERLIN_QUALITY
     */
    public NoiseQuality getNoiseQuality() {
        return this.noiseQuality;
    }

    /**
     * Sets the quality of the Perlin noise.
     *
     * <p>See {@link NoiseQuality} for definitions of the various coherent-noise
     * qualities.</p>
     *
     * @param noiseQuality the quality of the Perlin noise
     */
    public void setNoiseQuality(final NoiseQuality noiseQuality) {
        this.noiseQuality = noiseQuality;
    }

    /**
     * Get the number of octaves that generate the Perlin noise.
     *
     * <p>The number of octaves controls the amount of detail in the
     * Perlin noise.</p>
     *
     * @return the number of octaves that generate the Perlin noise
     * @see #DEFAULT_PERLIN_OCTAVE_COUNT
     */
    public int getOctaveCount() {
        return this.octaveCount;
    }

    /**
     * Set the number of octaves that generate the Perlin noise.
     *
     * <p>The octave count must be between 1 and {@link #PERLIN_MAX_OCTAVE},
     * inclusive.</p>
     *
     * <p>The number of octaves controls the amount of detail in the
     * Perlin noise.</p>
     *
     * <p>The larger the number of octaves, the more time required to calculate
     * the Perlin-noise value.</p>
     *
     * @param octaveCount the number of octaves that generate the Perlin noise
     * @throws IllegalArgumentException if the octave count is out of bounds
     */
    public void setOctaveCount(final int octaveCount) {
        if (octaveCount < 1 || octaveCount > Perlin.PERLIN_MAX_OCTAVE) {
            throw new IllegalArgumentException("octaveCount must be between 1 and MAX OCTAVE: " + Perlin.PERLIN_MAX_OCTAVE);
        }

        this.octaveCount = octaveCount;
    }

    /**
     * Get the persistence value of the Perlin noise.
     *
     * <p>The persistence value controls the roughness of the Perlin noise.</p>
     *
     * @return the persistence value
     * @see #DEFAULT_PERLIN_PERSISTENCE
     */
    public double getPersistence() {
        return this.persistence;
    }

    /**
     * Sets the persistence value of the Perlin noise.
     *
     * <p>The persistence value controls the roughness of the Perlin noise.</p>
     *
     * <p>For best results, set the persistence to a number between
     * 0.0 and 1.0.</p>
     *
     * @param persistence the persistence value of the Perlin noise
     */
    public void setPersistence(final double persistence) {
        this.persistence = persistence;
    }

    /**
     * Get the seed value used by the Perlin noise function.
     *
     * @return the seed value
     * @see #DEFAULT_PERLIN_SEED
     */
    public int getSeed() {
        return this.seed;
    }

    /**
     * Set the seed value used by the Perlin-noise function.
     *
     * @param seed the seed value
     */
    public void setSeed(final int seed) {
        this.seed = seed;
    }
    
    /**
     * Returns the maximum value the perlin module can output in its
     * current configuration.
     *
     * @return The maximum possible value for
     *     {@link Perlin#getValue(double, double, double)} to return
     */
    public double getMaxValue() {
        /*
         * Each successive octave adds persistence ^ current_octaves to max possible output.
         * So (p = persistence, o = octave): Max(perlin) = p + p*p + p*p*p + ... + p^(o-1).
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
            signal = Noise.gradientCoherentNoise3D(nx, ny, nz, seed, this.noiseQuality);
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
