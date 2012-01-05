package net.royawesome.jlibnoise.module;

public class Const extends Module {
	double value = 0.0;
	public Const() {
		super(0);
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public int GetSourceModuleCount() {
		return 0;
	}

	@Override
	public double GetValue(double x, double y, double z) {
		return value;
	}

}
