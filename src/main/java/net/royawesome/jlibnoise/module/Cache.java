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
package net.royawesome.jlibnoise.module;

import net.royawesome.jlibnoise.exception.NoModuleException;

public class Cache extends Module {
	// The cached output value at the cached input value.
	double cachedValue;
	// Determines if a cached output value is stored in this noise
	// module.
	boolean isCached = false;
	// @a x coordinate of the cached input value.
	double xCache;
	// @a y coordinate of the cached input value.
	double yCache;
	// @a z coordinate of the cached input value.
	double zCache;

	public Cache() {
		super(1);
	}

	@Override
	public int GetSourceModuleCount() {
		return 1;
	}

	@Override
	public void SetSourceModule(int index, Module sourceModule) {
		super.SetSourceModule(index, sourceModule);
		isCached = false;
	}

	@Override
	public double GetValue(double x, double y, double z) {
		if (SourceModule[0] == null) {
			throw new NoModuleException();
		}

		if (!(isCached && x == xCache && y == yCache && z == zCache)) {
			cachedValue = SourceModule[0].GetValue(x, y, z);
			xCache = x;
			yCache = y;
			zCache = z;
		}
		isCached = true;
		return cachedValue;
	}
}
