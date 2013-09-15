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

import net.royawesome.jlibnoise.exception.NoModuleException;
import net.royawesome.jlibnoise.module.Module;

/**
 * Model that defines the surface of a plane.
 *
 * This model returns an output value from a noise module given the coordinates of an input value located on the surface of an ( @a x,
 */
public class Plane {
	Module module;

	/**
	 * Constructor
	 *
	 * @param module The noise module that is used to generate the output values.
	 */
	public Plane(Module module) {
		if (module == null) {
			throw new IllegalArgumentException("module cannot be null");
		}
		this.module = module;
	}

	/**
	 * Returns the noise module that is used to generate the output values.
	 */
	public Module getModule() {
		return module;
	}

	/**
	 * Sets the noise module that is used to generate the output values.
	 *
	 * @param module The noise module that is used to generate the output values.
	 *
	 * This noise module must exist for the lifetime of this object, until you pass a new noise module to this method.
	 */
	public void setModule(Module module) {
		if (module == null) {
			throw new IllegalArgumentException("module cannot be null");
		}
		this.module = module;
	}

	/**
	 * Returns the output value from the noise module given the ( @a x, @a z ) coordinates of the specified input value located on the surface of the plane.
	 *
	 * @param x The @a x coordinate of the input value.
	 * @param z The @a z coordinate of the input value.
	 * @return The output value from the noise module.
	 */
	double getValue(double x, double z) {
		if (module == null) {
			throw new NoModuleException();
		}
		return module.GetValue(x, 0, z);
	}
}
