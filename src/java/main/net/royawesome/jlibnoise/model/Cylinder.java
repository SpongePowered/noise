package net.royawesome.jlibnoise.model;

import net.royawesome.jlibnoise.Utils;
import net.royawesome.jlibnoise.exception.NoModuleException;
import net.royawesome.jlibnoise.module.Module;

public class Cylinder {
	Module module;

	public Cylinder(Module mod){
		this.module = mod;
	}

	public Module getModule(){
		return this.module;
	}

	public void setModule(Module mod){
		if(mod == null) throw new IllegalArgumentException("Mod cannot be null");
		this.module = mod;
	}

	double getValue(double angle, double height){
		if(module == null) throw new NoModuleException();
		
		double x, y, z;
		x = Math.cos(angle * Utils.DEG_TO_RAD);
		y = height;
		z = Math.sin(angle * Utils.DEG_TO_RAD);
		return module.GetValue (x, y, z);

	}
}
