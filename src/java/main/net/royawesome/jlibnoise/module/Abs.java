package net.royawesome.jlibnoise.module;

import net.royawesome.jlibnoise.exception.NoModuleException;

public class Abs extends Module {

	public Abs() {
		super(1);
	}

	@Override
	public int GetSourceModuleCount() {
		return 1;
	}

	@Override
	public double GetValue(double x, double y, double z) {
		if(SourceModule == null) throw new NoModuleException();
		return Math.abs(SourceModule[0].GetValue(x, y, z));
	}

}
