package net.royawesome.jlibnoise.module;

import net.royawesome.jlibnoise.Utils;

public class Checkerboard extends Module {

	public Checkerboard() {
		super(0);
		
	}

	@Override
	public int GetSourceModuleCount() {
		
		return 0;
	}

	@Override
	public double GetValue(double x, double y, double z) {
		  int ix = (int)(Math.floor(Utils.MakeInt32Range (x)));
		  int iy = (int)(Math.floor(Utils.MakeInt32Range (y)));
		  int iz = (int)(Math.floor(Utils.MakeInt32Range (z)));
		  return ((ix & 1 ^ iy & 1 ^ iz & 1)!=0)? -1.0: 1.0;
	}

}
