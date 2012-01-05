package net.royawesome.jlibnoise.module;

import net.royawesome.jlibnoise.Utils;
import net.royawesome.jlibnoise.exception.NoModuleException;

public class Max extends Module {

	public Max() {
		super(2);
	}

	@Override
	public int GetSourceModuleCount() {
		return 2;
	}

	@Override
	public double GetValue(double x, double y, double z) {
		if(SourceModule[0] == null) throw new NoModuleException();
		if(SourceModule[1] == null) throw new NoModuleException();
		
		double v0 = SourceModule[0].GetValue (x, y, z);
		double v1 = SourceModule[1].GetValue (x, y, z);
		return Utils.GetMax (v0, v1);
	}

}
