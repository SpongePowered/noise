package net.royawesome.jlibnoise.module;

import net.royawesome.jlibnoise.Utils;

public class Spheres extends Module {
	/// Default frequency value for the noise::module::Spheres noise module.
	public static final double DEFAULT_SPHERES_FREQUENCY = 1.0;

	/// Frequency of the concentric spheres.
	double frequency = DEFAULT_SPHERES_FREQUENCY;


	public Spheres() {
		super(0);
	}


	public double getFrequency() {
		return frequency;
	}


	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}


	@Override
	public int GetSourceModuleCount() {
		return 0;
	}

	@Override
	public double GetValue(double x, double y, double z) {
		x *= frequency;
		y *= frequency;
		z *= frequency;

		double distFromCenter = Math.sqrt(x * x + y * y + z * z);
		double distFromSmallerSphere = distFromCenter - Math.floor(distFromCenter);
		double distFromLargerSphere = 1.0 - distFromSmallerSphere;
		double nearestDist = Utils.GetMin(distFromSmallerSphere, distFromLargerSphere);
		return 1.0 - (nearestDist * 4.0); // Puts it in the -1.0 to +1.0 range.

	}

}
