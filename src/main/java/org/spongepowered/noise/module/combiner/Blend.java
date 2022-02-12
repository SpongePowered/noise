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

import org.spongepowered.noise.Utils;
import org.spongepowered.noise.exception.NoModuleException;
import org.spongepowered.noise.module.NoiseModule;

/**
 * Noise module that outputs a weighted blend of the output values of two source
 * modules given the output of a control module.
 *
 * <p>Unlike most other noise modules, the index value assigned to a source
 * module determines its role in the blending operation:</p>
 * <dl>
 *     <dt>Source module 0</dt>
 *     <dd>Outputs one of the values to blend</dd>
 *     <dt>Source module 1</dt>
 *     <dd>Outputs one of the values to blend</dd>
 *     <dt>Source module 2</dt>
 *     <dd>Known as the <i>control module</i>. The control module determines the
 *         weight of the blending operation. Negative values weigh the blend
 *         towards the output value from the source module with an index value
 *         of {@code 0}. Positive values weigh the blend towards the output
 *         value from the source module with an index value of {@code 1}.</dd>
 * </dl>
 *
 * <p>An application can pass the control module to the
 * {@link #setControlModule(NoiseModule)} method instead of the
 * {@link #setSourceModule(int, NoiseModule)} method. This may make the application
 * code easier to read.</p>
 *
 * <p>This noise module uses linear interpolation to perform the
 * blending operation.</p>
 *
 * @sourceModules 3
 */
public class Blend extends NoiseModule {
    public Blend() {
        super(3);
    }

    /**
     * Create a new Blend module with the source modules pre-configured.
     *
     * @param left the first source
     * @param right the second source
     * @param control the control module
     */
    public Blend(final NoiseModule left, final NoiseModule right, final NoiseModule control) {
        this();
        this.setSourceModule(0, left);
        this.setSourceModule(1, right);
        this.setSourceModule(2, control);
    }

    /**
     * Returns the control module.
     *
     * <p>The control module determines the weight of the blending operation.
     * Negative values weigh the blend towards the output value from the source
     * module with an index value of {@code 0}. Positive values weigh the blend
     * towards the output value from the source module with an index value
     * of {@code 1}.</p>
     *
     * @return the control module
     * @throws NoModuleException if no control module has been set yet.
     */
    public NoiseModule controlModule() {
        if (this.sourceModule[2] == null) {
            throw new NoModuleException(2);
        }
        return this.sourceModule[2];
    }

    /**
     * Sets the control module.
     *
     * <p>The control module determines the weight of the blending operation.
     * Negative values weigh the blend towards the output value from the source
     * module with an index value of {@code 0}. Positive values weigh the blend
     * towards the output value from the source module with an index value
     * of {@code 1}.</p>
     *
     * <p>This method assigns the control module an index value of {@code 2}.
     * Passing the control module to this method produces the same results as
     * passing the control module to the {@link #setSourceModule(int, NoiseModule)}
     * method while assigning that noise module an index value of {@code 2}.</p>
     *
     * @param module the control module
     */
    public void setControlModule(final NoiseModule module) {
        if (module == null) {
            throw new IllegalArgumentException("Control Module cannot be null");
        }
        this.sourceModule[2] = module;
    }

    @Override
    public double get(final double x, final double y, final double z) {
        if (this.sourceModule[0] == null) {
            throw new NoModuleException(0);
        }
        if (this.sourceModule[1] == null) {
            throw new NoModuleException(1);
        }
        if (this.sourceModule[2] == null) {
            throw new NoModuleException(2);
        }

        final double v0 = this.sourceModule[0].get(x, y, z);
        final double v1 = this.sourceModule[1].get(x, y, z);
        final double alpha = this.sourceModule[2].get(x, y, z);
        return Utils.linearInterp(v0, v1, alpha);
    }
}
