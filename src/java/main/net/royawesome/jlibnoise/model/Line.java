package net.royawesome.jlibnoise.model;

import net.royawesome.jlibnoise.exception.NoModuleException;
import net.royawesome.jlibnoise.module.Module;

public class Line {
	/// A flag that specifies whether the value is to be attenuated
	/// (moved toward 0.0) as the ends of the line segment are approached.
	boolean attenuate;

	/// A pointer to the noise module used to generate the output values.
	Module module;


	/// @a x coordinate of the start of the line segment.
	double x0 = 0;

	/// @a x coordinate of the end of the line segment.
	double x1 = 1;

	/// @a y coordinate of the start of the line segment.
	double y0 = 0;

	/// @a y coordinate of the end of the line segment.
	double y1 = 1;

	/// @a z coordinate of the start of the line segment.
	double z0 = 0;

	/// @a z coordinate of the end of the line segment.
	double z1 = 1;

	public Line(Module module){
		if(module == null) throw new IllegalArgumentException("module cannot be null");
		this.module = module;
	}

	public boolean attenuate(){
		return this.attenuate;
	}
	public void setAttenuate(boolean att){
		this.attenuate = att;
	}

	public void setStartPoint(double x, double y, double z){
		this.x0 = x;
		this.y0 = y;
		this.z0 = z;
	}

	public void setEndPoint(double x, double y, double z){
		this.x1 = x;
		this.y1 = y;
		this.z1 = z;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		if(module == null) throw new IllegalArgumentException("module cannot be null");
		this.module = module;
	}

	public double getValue(double p){
		if(module == null) throw new NoModuleException();
		
		double x = (x1 - x0) * p + x0;
		double y = (y1 - y0) * p + y0;
		double z = (z1 - z0) * p + z0;
		double value = module.GetValue (x, y, z);

		if (attenuate) {
			return p * (1.0 - p) * 4 * value;
		} else {
			return value;
		}

	}

}
