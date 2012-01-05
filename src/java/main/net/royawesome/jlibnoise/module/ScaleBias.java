package net.royawesome.jlibnoise.module;

import net.royawesome.jlibnoise.exception.NoModuleException;

public class ScaleBias extends Module {
	/// Default bias for the noise::module::ScaleBias noise module.
	public static final double DEFAULT_BIAS = 0.0;

	/// Default scale for the noise::module::ScaleBias noise module.
	public static final double DEFAULT_SCALE = 1.0;

	/// Bias to apply to the scaled output value from the source module.
	double bias = DEFAULT_BIAS;

	/// Scaling factor to apply to the output value from the source
	/// module.
	double scale = DEFAULT_SCALE;


	public ScaleBias() {
		super(1);
	}

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
	}

	@Override
	public int GetSourceModuleCount() {
		return 1;
	}

	@Override
	public double GetValue(double x, double y, double z) {
		if(SourceModule[0] == null) throw new NoModuleException();

		return SourceModule[0].GetValue (x, y, z) * scale + bias;
	}

}
