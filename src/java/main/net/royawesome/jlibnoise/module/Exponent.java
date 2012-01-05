package net.royawesome.jlibnoise.module;

import net.royawesome.jlibnoise.exception.NoModuleException;

public class Exponent extends Module {
	public static final double DEFAULT_EXPONENT = 1.0;
	protected double exponent = DEFAULT_EXPONENT;

	public Exponent() {
		super(1);
	}

	public double getExponent() {
		return exponent;
	}

	public void setExponent(double exponent) {
		this.exponent = exponent;
	}

	@Override
	public int GetSourceModuleCount() {
		return 1;
	}

	@Override
	public double GetValue(double x, double y, double z) {
		if(SourceModule[0] == null) throw new NoModuleException();
		double value = SourceModule[0].GetValue (x, y, z);
		return (Math.pow(Math.abs((value + 1.0) / 2.0), exponent) * 2.0 - 1.0);
	}

}
