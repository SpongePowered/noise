package net.royawesome.jlibnoise.module;

import net.royawesome.jlibnoise.Utils;
import net.royawesome.jlibnoise.exception.NoModuleException;

public class Blend extends Module {

	public Blend() {
		super(3);
	}

	public Module getControlModule(){
		if(SourceModule[2] == null) throw new NoModuleException();
		return SourceModule[2];
	}

	public void setControlModule(Module module){
		if(module == null) throw new IllegalArgumentException("Control Module cannot be null");
		SourceModule[2] = module;
	}

	@Override
	public int GetSourceModuleCount() {
		return 3;
	}

	@Override
	public double GetValue(double x, double y, double z) {
		if(SourceModule[0] == null) throw new NoModuleException();
		if(SourceModule[1] == null) throw new NoModuleException();
		if(SourceModule[2] == null) throw new NoModuleException();
		
		double v0 = SourceModule[0].GetValue (x, y, z);
		double v1 = SourceModule[1].GetValue (x, y, z);
		double alpha = (SourceModule[2].GetValue (x, y, z) + 1.0) / 2.0;
		return Utils.LinearInterp (v0, v1, alpha);

	}

}
