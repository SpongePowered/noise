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

import net.royawesome.jlibnoise.Utils;
import net.royawesome.jlibnoise.exception.NoModuleException;
import net.royawesome.jlibnoise.module.Module;

/**
 * Model that defines the surface of a sphere.
 */
public class Sphere {
	Module module;

	/**
	 * Constructor
	 *
	 * @param module The noise module that is used to generate the output values.
	 */
	public Sphere(Module module) {
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
	 * Returns the output value from the noise module given the (latitude, longitude) coordinates of the specified input value located on the surface of the sphere.
	 *
	 * @param lat The latitude of the input value, in degrees.
	 * @param lon The longitude of the input value, in degrees.
	 * @return The output value from the noise module.
	 */
	public double getValue(double lat, double log) {
		if (module == null) {
			throw new NoModuleException();
		}
		double[] vec = Utils.LatLonToXYZ(lat, log);
		return module.GetValue(vec[0], vec[1], vec[2]);
	}
}
