package net.royawesome.jlibnoise.module;

import net.royawesome.jlibnoise.exception.NoModuleException;

public class TranslatePoint extends Module {
	/// Default translation factor applied to the @a x coordinate for the
	/// noise::module::TranslatePoint noise module.
	public static final double DEFAULT_TRANSLATE_POINT_X = 0.0;

	/// Default translation factor applied to the @a y coordinate for the
	/// noise::module::TranslatePoint noise module.
	public static final double DEFAULT_TRANSLATE_POINT_Y = 0.0;

	/// Default translation factor applied to the @a z coordinate for the
	/// noise::module::TranslatePoint noise module.
	public static final double DEFAULT_TRANSLATE_POINT_Z = 0.0;


	/// Translation amount applied to the @a x coordinate of the input
	/// value.
	double xTranslation;

	/// Translation amount applied to the @a y coordinate of the input
	/// value.
	double yTranslation;

	/// Translation amount applied to the @a z coordinate of the input
	/// value.
	double zTranslation;


	public TranslatePoint() {
		super(1);
	}

	public double getXTranslation() {
		return xTranslation;
	}

	public void setXTranslation(double xTranslation) {
		this.xTranslation = xTranslation;
	}

	public double getYTranslation() {
		return yTranslation;
	}

	public void setYTranslation(double yTranslation) {
		this.yTranslation = yTranslation;
	}

	public double getZTranslation() {
		return zTranslation;
	}

	public void setZTranslation(double zTranslation) {
		this.zTranslation = zTranslation;
	}

	public void setTranslations(double x, double y, double z){
		setXTranslation(x);
		setYTranslation(y);
		setZTranslation(z);
	}


	@Override
	public int GetSourceModuleCount() {
		return 1;
	}

	@Override
	public double GetValue(double x, double y, double z) {
		if(SourceModule[0] == null) throw new NoModuleException();

		return SourceModule[0].GetValue (x + xTranslation, y + yTranslation,
				z + zTranslation);
	}

}
