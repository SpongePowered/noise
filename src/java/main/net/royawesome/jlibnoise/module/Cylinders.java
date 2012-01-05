package net.royawesome.jlibnoise.module;

import net.royawesome.jlibnoise.Utils;

public class Cylinders extends Module {
	public static final double DEFAULT_CYLINDERS_FREQUENCY = 1.0;

	double frequency = DEFAULT_CYLINDERS_FREQUENCY;

	public Cylinders() {
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
		z *= frequency;

		double distFromCenter = Math.sqrt(x * x + z * z);
		double distFromSmallerSphere = distFromCenter - Math.floor (distFromCenter);
		double distFromLargerSphere = 1.0 - distFromSmallerSphere;
		double nearestDist = Utils.GetMin (distFromSmallerSphere, distFromLargerSphere);
		return 1.0 - (nearestDist * 4.0); // Puts it in the -1.0 to +1.0 range.

	}

}
