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
package org.spongepowered.noise.module.combiner;

import org.spongepowered.noise.exception.NoModuleException;
import org.spongepowered.noise.module.Module;
import org.spongepowered.noise.module.modifier.Turbulence;
import org.spongepowered.noise.module.source.Perlin;

import java.util.Objects;

/**
 * Noise module that uses three source modules to displace each coordinate of
 * the input value before returning the output value from a source module.
 *
 * <p>Unlike most other noise modules, the index value assigned to a source
 * module determines its role in the displacement operation:</p>
 * <dl>
 *     <dt>Source module 0</dt>
 *     <dd>Outputs a value</dd>
 *     <dt>Source module 1</dt>
 *     <dd>specifies the offset to apply to the {@code x} coordinate of the
 *     input value.</dd>
 *     <dt>Source module 2</dt>
 *     <dd>specifies the offset to apply to the {@code y} coordinate of the
 *     input value.</dd>
 *     <dt>Source module 3</dt>
 *     <dd>specifies the offset to apply to the {@code z} coordinate of the
 *     input value.</dd>
 * </dl>
 *
 * <p>The {@link #getValue(double, double, double)} method modifies the
 * {@code (x, y, z)} coordinates of the input value using the output values from
 * the three displacement modules before retrieving the output value from the
 * source module.</p>
 *
 * <p>The {@link Turbulence} noise module is a special case of the displacement
 * module. Internally, there are three {@link Perlin}-noise modules that perform
 * the displacement operation.</p>
 *
 * @sourceModules 4
 */
public class Displace extends Module {
    public Displace() {
        super(4);
    }

    /**
     * Gets the {@code x} displacement module.
     *
     * <p>The {@link #getValue(double, double, double)} method displaces the
     * input value by adding the output value from this displacement module to
     * the {@code x} coordinate of the input value before returning the output
     * value from the source module.</p>
     *
     * @return the {@code x} displacement module
     * @throws NoModuleException if this displacement module has not yet been set
     */
    public Module getXDisplaceModule() {
        if (this.sourceModule == null || this.sourceModule[1] == null) {
            throw new NoModuleException(1);
        }
        return this.sourceModule[1];
    }

    /**
     * Sets the {@code x} displacement module.
     *
     * <p>The {@link #getValue(double, double, double)} method displaces the
     * input value by adding the output value from this displacement module to
     * the {@code x} coordinate of the input value before returning the output
     * value from the source module.</p>
     *
     * <p>This method assigns an index value of {@code 1} to the {@code x}
     * displacement module. Passing the displacement module to this method
     * produces the same results as passing the displacement module to the
     * {@link #setSourceModule(int, Module)} method while assigning it an index
     * value of {@code 1}.</p>
     *
     * @param x displacement module that displaces the {@code x} coordinate
     */
    public void setXDisplaceModule(final Module x) {
        this.sourceModule[1] = Objects.requireNonNull(x, "x");
    }

    /**
     * Gets the {@code y} displacement module.
     *
     * <p>The {@link #getValue(double, double, double)} method displaces the
     * input value by adding the output value from this displacement module to
     * the {@code y} coordinate of the input value before returning the output
     * value from the source module.</p>
     *
     * @return the {@code y} displacement module
     * @throws NoModuleException if this displacement module has not yet been set
     */
    public Module getYDisplaceModule() {
        if (this.sourceModule == null || this.sourceModule[2] == null) {
            throw new NoModuleException(2);
        }
        return this.sourceModule[2];
    }

    /**
     * Sets the {@code y} displacement module.
     *
     * <p>The {@link #getValue(double, double, double)} method displaces the
     * input value by adding the output value from this displacement module to
     * the {@code y} coordinate of the input value before returning the output
     * value from the source module.</p>
     *
     * <p>This method assigns an index value of {@code 2} to the {@code y}
     * displacement module. Passing the displacement module to this method
     * produces the same results as passing the displacement module to the
     * {@link #setSourceModule(int, Module)} method while assigning it an index
     * value of {@code 2}.</p>
     *
     * @param y displacement module that displaces the {@code y} coordinate
     */
    public void setYDisplaceModule(final Module y) {
        this.sourceModule[2] = Objects.requireNonNull(y, "y");
    }

    /**
     * Gets the {@code z} displacement module.
     *
     * <p>The {@link #getValue(double, double, double)} method displaces the
     * input value by adding the output value from this displacement module to
     * the {@code z} coordinate of the input value before returning the output
     * value from the source module.</p>
     *
     * @return the {@code z} displacement module
     * @throws NoModuleException if this displacement module has not yet been set
     */
    public Module getZDisplaceModule() {
        if (this.sourceModule == null || this.sourceModule[3] == null) {
            throw new NoModuleException(3);
        }
        return this.sourceModule[3];
    }

    /**
     * Sets the {@code z} displacement module.
     *
     * <p>The {@link #getValue(double, double, double)} method displaces the
     * input value by adding the output value from this displacement module to
     * the {@code z} coordinate of the input value before returning the output
     * value from the source module.</p>
     *
     * <p>This method assigns an index value of {@code 3} to the {@code z}
     * displacement module. Passing the displacement module to this method
     * produces the same results as passing the displacement module to the
     * {@link #setSourceModule(int, Module)} method while assigning it an index
     * value of {@code 3}.</p>
     *
     * @param z displacement module that displaces the {@code z} coordinate
     */
    public void setZDisplaceModule(final Module z) {
        this.sourceModule[3] = Objects.requireNonNull(z, "z");
    }

    /**
     * Set the {@code x}, {@code y}, and {@code z} displacement modules.
     *
     * <p>The {@link #getValue(double, double, double)} method displaces the
     * input value by adding the output value from each of the displacement
     * modules to the corresponding coordinates of the input value before
     * returning the output value from the source module.</p>
     *
     * <p>This method assigns an index value of {@code 1} to the {@code x}
     * displacement module, an index value of {@code 2} to the {@code y}
     * displacement module, and an index value of {@code 3} to the {@code z}
     * displacement module.</p>
     *
     * @param x module that displaces the {@code x} coordinate of the input value
     * @param y module that displaces the {@code y} coordinate of the input value
     * @param z module that displaces the {@code z} coordinate of the input value
     */
    public void setDisplaceModules(final Module x, final Module y, final Module z) {
        this.setXDisplaceModule(x);
        this.setYDisplaceModule(y);
        this.setZDisplaceModule(z);
    }

    @Override
    public double getValue(final double x, final double y, final double z) {
        if (this.sourceModule[0] == null) {
            throw new NoModuleException(0);
        }
        if (this.sourceModule[1] == null) {
            throw new NoModuleException(1);
        }
        if (this.sourceModule[2] == null) {
            throw new NoModuleException(2);
        }
        if (this.sourceModule[3] == null) {
            throw new NoModuleException(3);
        }

        // Get the output values from the three displacement modules.  Add each
        // value to the corresponding coordinate in the input value.
        final double xDisplace = x + this.sourceModule[1].getValue(x, y, z);
        final double yDisplace = y + this.sourceModule[2].getValue(x, y, z);
        final double zDisplace = z + this.sourceModule[3].getValue(x, y, z);

        // Retrieve the output value using the offset input value instead of
        // the original input value.
        return this.sourceModule[0].getValue(xDisplace, yDisplace, zDisplace);
    }
}
