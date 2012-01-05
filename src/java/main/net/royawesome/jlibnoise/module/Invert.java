package net.royawesome.jlibnoise.module;

import net.royawesome.jlibnoise.exception.NoModuleException;

public class Invert extends Module {

	public Invert() {
		super(1);
	}

	@Override
	public int GetSourceModuleCount() {
		return 1;
	}

	@Override
	public double GetValue(double x, double y, double z) {
		if(SourceModule[0] == null) throw new NoModuleException();
		return -(SourceModule[0].GetValue (x, y, z));
	}

}
