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
import org.spongepowered.noise.module.Module;
import org.spongepowered.noise.module.source.Perlin;

public class Turbulence extends Module {
    // Default power for the noise::module::Turbulence noise module.
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

    public double getPower() {
        return this.power;
    }

    public void setPower(final double power) {
        this.power = power;
    }

    public int getRoughnessCount() {
        return this.xDistortModule.getOctaveCount();
    }

    public double getFrequency() {
        return this.xDistortModule.getFrequency();
    }

    public int getSeed() {
        return this.xDistortModule.getSeed();
    }

    public void setSeed(final int seed) {
        this.xDistortModule.setSeed(seed);
        this.yDistortModule.setSeed(seed + 1);
        this.zDistortModule.setSeed(seed + 2);
    }

    public void setFrequency(final double frequency) {
        this.xDistortModule.setFrequency(frequency);
        this.yDistortModule.setFrequency(frequency);
        this.zDistortModule.setFrequency(frequency);
    }

    public void setRoughness(final int roughness) {
        this.xDistortModule.setOctaveCount(roughness);
        this.yDistortModule.setOctaveCount(roughness);
        this.zDistortModule.setOctaveCount(roughness);
    }

    @Override
    public int getSourceModuleCount() {
        return 1;
    }

    @Override
    public double getValue(final double x, final double y, final double z) {
        if (this.sourceModule[0] == null) {
            throw new NoModuleException();
        }

        // Get the values from the three noise::module::Perlin noise modules and
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
        final double xDistort = x + (this.xDistortModule.getValue(x0, y0, z0) * this.power);
        final double yDistort = y + (this.yDistortModule.getValue(x1, y1, z1) * this.power);
        final double zDistort = z + (this.zDistortModule.getValue(x2, y2, z2) * this.power);

        // Retrieve the output value at the offset input value instead of the
        // original input value.
        return this.sourceModule[0].getValue(xDistort, yDistort, zDistort);
    }
}
