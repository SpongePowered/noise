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
    private double power = DEFAULT_TURBULENCE_POWER;
    // Noise module that displaces the @a x coordinate.
    private final Perlin xDistortModule;
    // Noise module that displaces the @a y coordinate.
    private final Perlin yDistortModule;
    // Noise module that displaces the @a z coordinate.
    private final Perlin zDistortModule;

    public Turbulence() {
        super(1);
        xDistortModule = new Perlin();
        yDistortModule = new Perlin();
        zDistortModule = new Perlin();
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public int getRoughnessCount() {
        return xDistortModule.getOctaveCount();
    }

    public double getFrequency() {
        return xDistortModule.getFrequency();
    }

    public int getSeed() {
        return xDistortModule.getSeed();
    }

    public void setSeed(int seed) {
        xDistortModule.setSeed(seed);
        yDistortModule.setSeed(seed + 1);
        zDistortModule.setSeed(seed + 2);
    }

    public void setFrequency(double frequency) {
        xDistortModule.setFrequency(frequency);
        yDistortModule.setFrequency(frequency);
        zDistortModule.setFrequency(frequency);
    }

    public void setRoughness(int roughness) {
        xDistortModule.setOctaveCount(roughness);
        yDistortModule.setOctaveCount(roughness);
        zDistortModule.setOctaveCount(roughness);
    }

    @Override
    public int getSourceModuleCount() {
        return 1;
    }

    @Override
    public double getValue(double x, double y, double z) {
        if (sourceModule[0] == null) {
            throw new NoModuleException();
        }

        // Get the values from the three noise::module::Perlin noise modules and
        // add each value to each coordinate of the input value.  There are also
        // some offsets added to the coordinates of the input values.  This prevents
        // the distortion modules from returning zero if the (x, y, z) coordinates,
        // when multiplied by the frequency, are near an integer boundary.  This is
        // due to a property of gradient coherent noise, which returns zero at
        // integer boundaries.
        double x0, y0, z0;
        double x1, y1, z1;
        double x2, y2, z2;
        x0 = x + (12414.0 / 65536.0);
        y0 = y + (65124.0 / 65536.0);
        z0 = z + (31337.0 / 65536.0);
        x1 = x + (26519.0 / 65536.0);
        y1 = y + (18128.0 / 65536.0);
        z1 = z + (60493.0 / 65536.0);
        x2 = x + (53820.0 / 65536.0);
        y2 = y + (11213.0 / 65536.0);
        z2 = z + (44845.0 / 65536.0);
        double xDistort = x + (xDistortModule.getValue(x0, y0, z0) * power);
        double yDistort = y + (yDistortModule.getValue(x1, y1, z1) * power);
        double zDistort = z + (zDistortModule.getValue(x2, y2, z2) * power);

        // Retrieve the output value at the offset input value instead of the
        // original input value.
        return sourceModule[0].getValue(xDistort, yDistort, zDistort);
    }
}
