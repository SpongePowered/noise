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
package org.spongepowered.noise.module.modifier;

import org.spongepowered.noise.exception.NoModuleException;
import org.spongepowered.noise.module.NoiseModule;
import org.spongepowered.noise.module.source.Perlin;

/**
 * Noise module that randomly displaces the input value before returning the
 * output value from a source module.
 *
 * <p><em>Turbulence</em> is the pseudo-random displacement of the input value.
 * The {@link #get(double, double, double)} method randomly displaces the
 * {@code (x, y, z)} coordinates of the input value before retrieving the output
 * value from the source module. To control the turbulence, an application can
 * modify its frequency, its power, and its roughness.</p>
 *
 * <p>The frequency of the turbulence determines how rapidly the displacement
 * amount changes. To specify the frequency, call the
 * {@link #setFrequency(double)} method.</p>
 *
 * <p>he power of the turbulence determines the scaling factor that is applied
 * to the displacement amount. To specify the power, call the
 * {@link #setPower(double)} method.</p>
 *
 * <p>The roughness of the turbulence determines the roughness of the changes
 * to the displacement amount. Low values smoothly change the displacement
 * amount. High values roughly change the displacement amount, which produces
 * more "kinky" changes. To specify the roughness, call the
 * {@link #setRoughness(int)} method.</p>
 *
 * <p>Use of this noise module may require some trial and error. Assuming that
 * you are using a generator module as the source module, you should first:</p>
 * <ul>
 *     <li>Set the frequency to the same frequency as the source module.</li>
 *     <li>Set the power to the reciprocal of the frequency.</li>
 * </ul>
 *
 * <p>From these initial frequency and power values, modify these values until
 * this noise module produce the desired changes in your terrain or texture.
 * For example:</p>
 * <ul>
 *     <li>Low frequency (1/8 initial frequency) and low power (1/8 initial
 *     power) produces very minor, almost unnoticeable changes.</li>
 *     <li>Low frequency (1/8 initial frequency) and high power (8 times
 *     initial power) produces "ropey" lava-like terrain or marble-like
 *     textures.</li>
 *     <li>High frequency (8 times initial frequency) and low power (1/8
 *     initial power) produces a noisy version of the initial terrain or
 *     texture.</li>
 *     <li>High frequency (8 times initial frequency) and high power (8 times
 *     initial power) produces nearly pure noise, which isn't entirely
 *     useful.</li>
 * </ul>
 *
 * <p>Displacing the input values result in more realistic terrain and textures.
 * If you are generating elevations for terrain height maps, you can use this
 * noise module to produce more realistic mountain ranges or terrain features
 * that look like flowing lava rock. If you are generating values for textures,
 * you can use this noise module to produce realistic marble-like or
 * "oily" textures.</p>
 *
 * <p>Internally, there are three noise::module::Perlin noise modules
 * that displace the input value; one for the {@code x}, one for the {@code y},
 * and one for the {@code z} coordinate.</p>
 *
 * @sourceModules 1
 */
public class Turbulence extends NoiseModule {

    /**
     * Default power for the {@link Turbulence} noise module.
     */
    public static final double DEFAULT_TURBULENCE_POWER = 1.0;

    // The power (scale) of the displacement.
    private double power = Turbulence.DEFAULT_TURBULENCE_POWER;
    // Noise module that displaces the {@code x} coordinate.
    private final Perlin xDistortModule;
    // Noise module that displaces the {@code y} coordinate.
    private final Perlin yDistortModule;
    // Noise module that displaces the {@code z} coordinate.
    private final Perlin zDistortModule;

    public Turbulence() {
        super(1);
        this.xDistortModule = new Perlin();
        this.yDistortModule = new Perlin();
        this.zDistortModule = new Perlin();
    }

    /**
     * Create a new Turbulence module with the source modules pre-configured.
     *
     * @param source the input module
     */
    public Turbulence(final NoiseModule source) {
        this();
        this.setSourceModule(0, source);
    }

    /**
     * Get the power of the turbulence.
     *
     * <p>The power of the turbulence determines the scaling factor that is
     * applied to the displacement amount.</p>
     *
     * @return the power of the turbulence
     * @see #DEFAULT_TURBULENCE_POWER
     */
    public double power() {
        return this.power;
    }

    /**
     * Set the power of the turbulence.
     *
     * <p>The power of the turbulence determines the scaling factor that is
     * applied to the displacement amount.</p>
     *
     * @param power the power of the turbulence
     */
    public void setPower(final double power) {
        this.power = power;
    }

    /**
     * Get the roughness of the turbulence.
     *
     * <p>The roughness of the turbulence determines the roughness of the
     * changes to the displacement amount. Low values smoothly change the
     * displacement amount. High values roughly change the displacement amount,
     * which produces more "kinky" changes.</p>
     *
     * @return the roughness of the turbulence
     */
    public int roughnessCount() {
        return this.xDistortModule.octaveCount();
    }

    /**
     * Set the roughness of the turbulence.
     *
     * <p>The roughness of the turbulence determines the roughness of the
     * changes to the displacement amount. Low values smoothly change the
     * displacement amount. High values roughly change the displacement amount,
     * which produces more "kinky" changes.</p>
     *
     * <p>Internally, there are three {@link Perlin} noise modules that displace
     * the input value: one for the {@code x}, one for the {@code y}, and one
     * for the {@code z} coordinates. The roughness value is equal to the number
     * of octaves used by the {@link Perlin} noise modules.</p>
     *
     * @param roughness the roughness of the turbulence
     */
    public void setRoughness(final int roughness) {
        this.xDistortModule.setOctaveCount(roughness);
        this.yDistortModule.setOctaveCount(roughness);
        this.zDistortModule.setOctaveCount(roughness);
    }

    /**
     * Get the frequency of the turbulence.
     *
     * <p>The frequency of the turbulence determines how rapidly the
     * displacement amount changes.</p>
     *
     * @return the frequency of the turbulence
     */
    public double frequency() {
        return this.xDistortModule.frequency();
    }

    /**
     * Set the frequency of the turbulence.
     *
     * <p>The frequency of the turbulence determines how rapidly the
     * displacement amount changes.</p>
     *
     * @param frequency  the frequency of the turbulence
     */
    public void setFrequency(final double frequency) {
        this.xDistortModule.setFrequency(frequency);
        this.yDistortModule.setFrequency(frequency);
        this.zDistortModule.setFrequency(frequency);
    }

    /**
     * Get the seed of the internal Perlin-noise modules that are used to
     * displace the input values.
     *
     * <p>Internally, there are three {@link Perlin} noise modules that displace
     * the input value: one for the {@code x}, one for the {@code y}, and one
     * for the {@code z} coordinates.</p>
     *
     * @return the seed value
     */
    public int seed() {
        return this.xDistortModule.seed();
    }

    /**
     * Set the seed of the internal Perlin-noise modules that are used to
     * displace the input values.
     *
     * <p>Internally, there are three {@link Perlin} noise modules that displace
     * the input value: one for the {@code x}, one for the {@code y}, and one
     * for the {@code z} coordinates. This noise module assigns the following
     * seed values to the {@link Perlin} noise modules:</p>
     * <ul>
     *     <li>It assigns the seed value {@code seed + 0} to the {@code x}
     *     noise module.</li>
     *     <li>It assigns the seed value {@code seed + 1} to the {@code y}
     *     noise module.</li>
     *     <li>It assigns the seed value {@code seed + 2} to the {@code z}
     *     noise module.</li>
     * </ul>
     *
     * @param seed  the seed value
     */
    public void setSeed(final int seed) {
        this.xDistortModule.setSeed(seed);
        this.yDistortModule.setSeed(seed + 1);
        this.zDistortModule.setSeed(seed + 2);
    }

    @Override
    public double get(final double x, final double y, final double z) {
        if (this.sourceModule[0] == null) {
            throw new NoModuleException(0);
        }

        // Get the values from the three Perlin noise modules and
        // add each value to each coordinate of the input value.  There are also
        // some offsets added to the coordinates of the input values.  This prevents
        // the distortion modules from returning zero if the (x, y, z) coordinates,
        // when multiplied by the frequency, are near an integer boundary.  This is
        // due to a property of gradient coherent noise, which returns zero at
        // integer boundaries.
        final double x0, y0, z0;
        final double x1, y1, z1;
        final double x2, y2, z2;
        x0 = x + (12414.0 / 65536.0);
        y0 = y + (65124.0 / 65536.0);
        z0 = z + (31337.0 / 65536.0);
        x1 = x + (26519.0 / 65536.0);
        y1 = y + (18128.0 / 65536.0);
        z1 = z + (60493.0 / 65536.0);
        x2 = x + (53820.0 / 65536.0);
        y2 = y + (11213.0 / 65536.0);
        z2 = z + (44845.0 / 65536.0);
        final double xDistort = x + (this.xDistortModule.get(x0, y0, z0) * this.power);
        final double yDistort = y + (this.yDistortModule.get(x1, y1, z1) * this.power);
        final double zDistort = z + (this.zDistortModule.get(x2, y2, z2) * this.power);

        // Retrieve the output value at the offset input value instead of the
        // original input value.
        return this.sourceModule[0].get(xDistort, yDistort, zDistort);
    }
}
