/*
 * This file is part of jlibnoise.
 * Original libnoise by Jason Bevins <http://libnoise.sourceforge.net/>
 *
 * Copyright (c) 2011 Garrett Fleenor <http://www.spout.org/>
 * jlibnoise is licensed under the GNU Lesser General Public License.
 *
 * jlibnoise is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jlibnoise is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.royawesome.jlibnoise.model;

import net.royawesome.jlibnoise.MathHelper;
import net.royawesome.jlibnoise.Utils;
import net.royawesome.jlibnoise.exception.NoModuleException;
import net.royawesome.jlibnoise.module.Module;

/**
 * Model that defines the surface of a cylinder.
 */
public class Cylinder {
	Module module;

	/**
	 * @param module The noise module that is used to generate the output values.
	 */
	public Cylinder(Module mod) {
		this.module = mod;
	}

	/**
	 * Returns the noise module that is used to generate the output values.
	 *
	 * @return A reference to the noise module.
	 */
	public Module getModule() {
		return this.module;
	}

	/**
	 * Sets the noise module that is used to generate the output values.
	 *
	 * @param module The noise module that is used to generate the output values.
	 *
	 * This noise module must exist for the lifetime of this object, until you pass a new noise module to this method.
	 */
	public void setModule(Module mod) {
		if (mod == null) {
			throw new IllegalArgumentException("Mod cannot be null");
		}
		this.module = mod;
	}

	/**
	 * Returns the output value from the noise module given the (angle, height) coordinates of the specified input value located on the surface of the cylinder.
	 *
	 * @param angle The angle around the cylinder's center, in degrees.
	 * @param height The height along the @a y axis.
	 * @return The output value from the noise module.
	 */
	double getValue(double angle, double height) {
		if (module == null) {
			throw new NoModuleException();
		}

		double x, y, z;
		x = MathHelper.cos(angle * Utils.DEG_TO_RAD);
		y = height;
		z = MathHelper.sin(angle * Utils.DEG_TO_RAD);
		return module.GetValue(x, y, z);
	}
}
