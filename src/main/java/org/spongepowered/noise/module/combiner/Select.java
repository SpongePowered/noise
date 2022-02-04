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
 * Noise module that outputs the value selected from one of two source modules
 * chosen by the output value from a control module.
 *
 * <p>Unlike most other noise modules, the index value assigned to a source
 * module determines its role in the selection operation.</p>
 * <dl>
 *     <dt>Source module 0</dt>
 *     <dd>outputs a value</dd>
 *     <dt>Source module 1</dt>
 *     <dd>outputs a value</dd>
 *     <dt>Source module 2</dt>
 *     <dd>is known as the <em>control module</em>. If the output value from the
 *     control module is with a range of values known as the
 *     <em>selection range</em>, this noise module outputs the value from the
 *     source module with an index value of 1. Otherwise, this noise module
 *     outputs the value from the source module with an index value of 0.</dd>
 * </dl>
 *
 * <p>To specify the bounds of the selection range, call the
 * {@link #setBounds(double, double)} method.</p>
 *
 * <p>An application can pass the control module to the
 * {@link #setControlModule(NoiseModule)} method instead of the
 * {@link #setSourceModule(int, NoiseModule)}. This may make the application code
 * easier to read.</p>
 *
 * <p>by default, there is an abrupt transition between the output values from
 * the two source modules at the selection-range boundary. To smooth the
 * transition, pass a non-zero value to the {@link #setEdgeFalloff(double)}
 * method. Higher values result in a smoother transition.</p>
 *
 * @sourceModules 3
 */
public class Select extends NoiseModule {

    /**
     * Default edge-falloff value for the {@link Select} noise module.
     */
    public static final double DEFAULT_SELECT_EDGE_FALLOFF = 0.0;

    /**
     * Default lower bound of the selection range for the
     * {@link Select} noise module.
     */
    public static final double DEFAULT_SELECT_LOWER_BOUND = -1.0;

    /**
     * Default upper bound of the selection range for the
     * {@link Select} noise module.
     */
    public static final double DEFAULT_SELECT_UPPER_BOUND = 1.0;

    // Edge-falloff value.
    private double edgeFalloff = Select.DEFAULT_SELECT_EDGE_FALLOFF;
    // Lower bound of the selection range.
    private double lowerBound = Select.DEFAULT_SELECT_LOWER_BOUND;
    // Upper bound of the selection range.
    private double upperBound = Select.DEFAULT_SELECT_UPPER_BOUND;

    public Select() {
        super(3);
    }


    /**
     * Create a new Select module with the source modules pre-configured.
     *
     * @param a a source module
     * @param b a source module
     * @param control the module to select between the two inputs
     */
    public Select(final NoiseModule a, final NoiseModule b, final NoiseModule control) {
        this();
        this.setSourceModule(0, a);
        this.setSourceModule(1, b);
        this.setSourceModule(2, control);
    }

    /**
     * Get the control module.
     *
     * <p>The control module determines the output value to select. If the
     * output value from the control module is within a range of values known as
     * the <em>selection range</em>, the
     * {@link #get(double, double, double)} method outputs the value from
     * the source module with an index value of 1. Otherwise, this method
     * outputs the value from the source module with an index value of 0.</p>
     *
     * @return the control module
     * @throws NoModuleException if no control module has been set yet
     */
    public NoiseModule controlModule() {
        if (this.sourceModule == null || this.sourceModule[2] == null) {
            throw new NoModuleException(2);
        }
        return this.sourceModule[2];
    }

    /**
     * Set the control module.
     *
     * <p>The control module determines the output value to select. If the
     * output value from the control module is within a range of values known as
     * the <em>selection range</em>, the
     * {@link #get(double, double, double)} method outputs the value from
     * the source module with an index value of 1. Otherwise, this method
     * outputs the value from the source module with an index value of 0.</p>
     *
     * <p>This method assigns the control module an index value of 2. Passing
     * the control module to this method produces the same results as passing
     * the control module to the {@link #setSourceModule(int, NoiseModule)} method
     * while assigning that noise module an index value of 2.</p>
     *
     * @param m the control module
     */
    public void setControlModule(final NoiseModule m) {
        if (m == null) {
            throw new IllegalArgumentException("the module cannot be null");
        }
        this.sourceModule[2] = m;
    }

    /**
     * Get the falloff value at the edge transition.
     *
     * <p>The falloff value is the width of the edge transition at either edge
     * of the selection range.</p>
     *
     * <p>By default, there is an abrupt transition between the output values
     * from the two source modules at the selection-range boundary.</p>
     *
     * @return the falloff value at the edge transition
     */
    public double edgeFalloff() {
        return this.edgeFalloff;
    }

    /**
     * Sets the falloff value at the edge transition.
     *
     * <p>The falloff value is the width of the edge transition at either edge
     * of the selection range.</p>
     *
     * <p>By default, there is an abrupt transition between the values from the
     * two source modules at the boundaries of the selection range.</p>
     *
     * <p>For example, if the selection range is 0.5 to 0.8, and the edge
     * falloff value is 0.1, then the {@link #get(double, double, double)}
     * method outputs:</p>
     * <ul>
     *     <li>the output value from the source module with an index value of
     *     {@code 0} if the output value from the control module is less than
     *     0.4 ( = 0.5 - 0.1).</li>
     *     <li>a linear blend between the two output values from the two source
     *     modules if the output from the control module is between
     *     0.4 (= 0.5 - 0.1) and 0.6 (= 0.5 + 0.1).</li>
     *     <li>the output value from the source module with an index value of
     *     {@code 1} if the output value from the control module is between
     *     0.6 (= 0.5 + 0.1) and 0.7 (= 0.8 - 0.1).</li>
     *     <li>a linear blend between the output values from the two source
     *     modules if the output value from the control module is between
     *     0.7 (= 0.8 - 0.1) and 0.9 (= 0.8 - 0.1).</li>
     *     <li>the output value from the source module with an index value of
     *     {@code 0} if the output value from the control module is greater than
     *     0.9 (= 0.8 + 0.1).</li>
     * </ul>
     *
     * @param edgeFalloff the falloff value at the edge transition
     */
    public void setEdgeFalloff(final double edgeFalloff) {
        // Make sure that the edge falloff curves do not overlap.
        final double boundSize = this.upperBound - this.lowerBound;
        this.edgeFalloff = (edgeFalloff > boundSize / 2) ? boundSize / 2 : edgeFalloff;
    }

    /**
     * Get the lower bound of the selection range.
     *
     * <p>If the output value from the control module is within the selection
     * range, the {@link #get(double, double, double)} method outputs the
     * value from the source module with an index value of 1. Otherwise, this
     * method outputs the value from the source module with an index
     * value of 0.</p>
     *
     * @return the lower bound of the selection range
     */
    public double lowerBound() {
        return this.lowerBound;
    }

    /**
     * Get the upper bound of the selection range.
     *
     * <p>If the output value from the control module is within the selection
     * range, the {@link #get(double, double, double)} method outputs the
     * value from the source module with an index value of 1. Otherwise, this
     * method outputs the value from the source module with an index
     * value of 0.</p>
     *
     * @return the upper bound of the selection range
     */
    public double upperBound() {
        return this.upperBound;
    }

    /**
     * Set the lower and upper bounds of the selection range.
     *
     * <p>If the output value from the control module is within the selection
     * range, the {@link #get(double, double, double)} method outputs the
     * value from the source module with an index value of 1. Otherwise, this
     * method outputs the value from the source module with an index
     * value of 0.</p>
     *
     * @param upper the upper bound
     * @param lower the lower bound
     * @throws IllegalArgumentException if the lower bound is not less than or
     *     equal to the upper bound
     */
    public void setBounds(final double upper, final double lower) {
        if (lower > upper) {
            throw new IllegalArgumentException("lower must be less than upper");
        }
        this.lowerBound = lower;
        this.upperBound = upper;

        this.setEdgeFalloff(this.edgeFalloff);
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

        final double controlValue = this.sourceModule[2].get(x, y, z);
        final double alpha;
        if (this.edgeFalloff > 0.0) {
            if (controlValue < (this.lowerBound - this.edgeFalloff)) {
                // The output value from the control module is below the selector
                // threshold; return the output value from the first source module.
                return this.sourceModule[0].get(x, y, z);
            } else if (controlValue < (this.lowerBound + this.edgeFalloff)) {
                // The output value from the control module is near the lower end of the
                // selector threshold and within the smooth curve. Interpolate between
                // the output values from the first and second source modules.
                final double lowerCurve = (this.lowerBound - this.edgeFalloff);
                final double upperCurve = (this.lowerBound + this.edgeFalloff);
                alpha = Utils.sCurve3((controlValue - lowerCurve) / (upperCurve - lowerCurve));
                return Utils.linearInterp(this.sourceModule[0].get(x, y, z), this.sourceModule[1].get(x, y, z), alpha);
            } else if (controlValue < (this.upperBound - this.edgeFalloff)) {
                // The output value from the control module is within the selector
                // threshold; return the output value from the second source module.
                return this.sourceModule[1].get(x, y, z);
            } else if (controlValue < (this.upperBound + this.edgeFalloff)) {
                // The output value from the control module is near the upper end of the
                // selector threshold and within the smooth curve. Interpolate between
                // the output values from the first and second source modules.
                final double lowerCurve = (this.upperBound - this.edgeFalloff);
                final double upperCurve = (this.upperBound + this.edgeFalloff);
                alpha = Utils.sCurve3((controlValue - lowerCurve) / (upperCurve - lowerCurve));
                return Utils.linearInterp(this.sourceModule[1].get(x, y, z), this.sourceModule[0].get(x, y, z), alpha);
            } else {
                // Output value from the control module is above the selector threshold;
                // return the output value from the first source module.
                return this.sourceModule[0].get(x, y, z);
            }
        } else {
            if (controlValue < this.lowerBound || controlValue > this.upperBound) {
                return this.sourceModule[0].get(x, y, z);
            } else {
                return this.sourceModule[1].get(x, y, z);
            }
        }
    }
}
