/*
 * This file is part of Flow Noise, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2013 Flow Powered <https://flowpowered.com/>
 * Original libnoise C++ library by Jason Bevins <http://libnoise.sourceforge.net/>
 * jlibnoise Java port by Garrett Fleenor <https://github.com/RoyAwesome/jlibnoise>
 * Flow Noise is re-licensed with permission from jlibnoise author.
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
package com.flowpowered.noise.module;

import com.flowpowered.noise.module.combiner.*;
import com.flowpowered.noise.module.misc.Cache;
import com.flowpowered.noise.module.modifier.*;
import com.flowpowered.noise.module.combiner.selector.*;

import java.util.Collection;

/**
 * A Module is basically just a function from R^3 -> R (for the actual function, see
 * {@link Module#getValue(double, double, double)}).
 * In this way, one can consider the Module constructor to be a function that returns a module function.
 * <br/>
 * In addition to the getValue method, Module implements a whole bunch of fluent methods.
 */
public abstract class Module {

    /**
     * The module function, an abstract method that all modules must implement.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return the value at x, y, z
     */
    public abstract double getValue(double x, double y, double z);

    // Combiner methods
    public Add add(Module that) {
        return new Add(this, that);
    }

    public Max max(Module that) {
        return new Max(this, that);
    }

    public Min min(Module that) {
        return new Min(this, that);
    }

    public Multiply multiply(Module that) {
        return new Multiply(this, that);
    }

    public Power power(Module that) {
        return new Power(this, that);
    }

    // Misc methods
    public Cache cache() {
        return new Cache(this);
    }

    // Modifier methods
    public Abs abs() {
        return new Abs(this);
    }

    public Clamp clamp(double lowerBound, double upperBound) {
        return new Clamp(this, lowerBound, upperBound);
    }

    public Curve curve(Curve.ControlPoint... controlPoints) {
        return new Curve(this, controlPoints);
    }

    public Curve curve(Collection<Curve.ControlPoint> controlPoints) {
        return new Curve(this, controlPoints);
    }

    public Exponent exp(double exponent) {
        return new Exponent(this, exponent);
    }

    public Invert invert() {
        return new Invert(this);
    }

    public ScaleBias scaleBias(double bias, double scale) {
        return new ScaleBias(this, scale, bias);
    }

    public Terrace terrace(double[] controlPoints, boolean invertTerraces) {
        return new Terrace(this, controlPoints, invertTerraces);
    }

    // Transformer methods

    // Selector methods

    public Blend blendFrom(Module sourceA, Module sourceB) {
        return new Blend(this, sourceA, sourceB);
    }

    public Select selectFrom(Module sourceA, Module sourceB, double edgeFalloff, double lowerBound, double upperBound) {
        return new Select(this, sourceA, sourceB, edgeFalloff, lowerBound, upperBound);
    }

    public Select.Builder selectFrom(Module sourceA, Module sourceB) {
        return new Select.Builder()
                .setSources(sourceA, sourceB);
    }

    public static abstract class Builder {

        protected abstract void checkValues() throws IllegalStateException;

        public abstract Module build() throws IllegalStateException;

    }

}
