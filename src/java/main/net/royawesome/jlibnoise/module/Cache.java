package net.royawesome.jlibnoise.module;

import net.royawesome.jlibnoise.exception.NoModuleException;

public class Cache extends Module {
	/// The cached output value at the cached input value.
	double cachedValue;

	/// Determines if a cached output value is stored in this noise
	/// module.
	boolean isCached = false;

	// @a x coordinate of the cached input value.
	double xCache;

	/// @a y coordinate of the cached input value.
	double yCache;

	/// @a z coordinate of the cached input value.
	double zCache;



	public Cache() {
		super(1);
	}

	@Override
	public int GetSourceModuleCount() {
		return 1;
	}

	@Override
	public void SetSourceModule(int index, Module sourceModule){
		super.SetSourceModule(index, sourceModule);
		isCached = false;
	}

	@Override
	public double GetValue(double x, double y, double z) {
		if(SourceModule[0] == null) throw new NoModuleException();
		
		if (!(isCached && x == xCache && y == yCache && z == zCache)) {
			cachedValue = SourceModule[0].GetValue (x, y, z);
			xCache = x;
			yCache = y;
			zCache = z;
		}
		isCached = true;
		return cachedValue;
	}

}
